package com.daqem.jobsplus.player.job.powerup;

import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public class Powerup {

    public static final Codec<Powerup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("powerup").forGetter(powerup -> powerup.powerupInstance.getLocation()),
            PowerupState.CODEC.fieldOf("state").forGetter(Powerup::getState)

    ).apply(instance, Powerup::new));

    private final PowerupInstance powerupInstance;
    private PowerupState powerupState;

    public Powerup(ResourceLocation powerupLocation, PowerupState powerupState) {
        this.powerupInstance = PowerupInstance.of(powerupLocation);
        this.powerupState = powerupState;
    }

    public Powerup(PowerupInstance powerupInstance, PowerupState powerupState) {
        this.powerupInstance = powerupInstance;
        this.powerupState = powerupState;
    }

    public PowerupInstance getPowerupInstance() {
        return powerupInstance;
    }

    public PowerupState getState() {
        return powerupState;
    }

    public void setState(PowerupState powerupState) {
        this.powerupState = powerupState;
    }

    public void toggle() {
        if (powerupState == PowerupState.ACTIVE) {
            powerupState = PowerupState.INACTIVE;
        } else if (powerupState == PowerupState.INACTIVE) {
            powerupState = PowerupState.ACTIVE;
        }
    }
}
