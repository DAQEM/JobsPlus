package com.daqem.jobsplus.integration.arc.condition.conditions.job.powerup;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.condition.type.JobsPlusConditionType;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class PowerupNotActiveCondition extends AbstractCondition {

    private final Identifier powerupThatShouldNotBeActiveLocation;

    public PowerupNotActiveCondition(boolean inverted, Identifier powerupThatShouldNotBeActiveLocation) {
        super(inverted);
        this.powerupThatShouldNotBeActiveLocation = powerupThatShouldNotBeActiveLocation;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        if (actionData.getPlayer() instanceof JobsPlayer player) {
            PowerupInstance powerupInstance = PowerupInstance.of(powerupThatShouldNotBeActiveLocation);
            if (powerupInstance == null) {
                return false;
            }
            Optional<Powerup> powerup = player.jobsplus$getJobs().stream()
                    .map(job -> job.getPowerupManager().getPowerup(powerupInstance))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();

            return powerup.isEmpty() ||
                    (powerup.get().getPowerupInstance().getIdentifier().equals(this.powerupThatShouldNotBeActiveLocation)
                            && powerup.get().getState() != PowerupState.ACTIVE);
        }
        return false;
    }

    @Override
    public IConditionType<? extends ICondition> getType() {
        return JobsPlusConditionType.POWERUP_NOT_ACTIVE;
    }

    @Override
    public Component getDescription() {
        PowerupInstance powerupInstance = PowerupInstance.of(powerupThatShouldNotBeActiveLocation);
        if (powerupInstance == null) {
            return JobsPlus.API.literal("ERROR: Powerup not found: '" + powerupThatShouldNotBeActiveLocation.toString() + "'");
        }
        return getDescription(powerupInstance.getName());
    }

    public static class Serializer implements IConditionSerializer<PowerupNotActiveCondition> {


        @Override
        public PowerupNotActiveCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new PowerupNotActiveCondition(
                    inverted,
                    getIdentifier(jsonObject, "powerup"));
        }

        @Override
        public PowerupNotActiveCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new PowerupNotActiveCondition(
                    inverted,
                    friendlyByteBuf.readIdentifier());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PowerupNotActiveCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeIdentifier(type.powerupThatShouldNotBeActiveLocation);
        }
    }
}
