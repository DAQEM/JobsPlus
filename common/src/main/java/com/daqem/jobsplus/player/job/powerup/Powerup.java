package com.daqem.jobsplus.player.job.powerup;

import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.knot.api.codec.KnotStreamCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class Powerup {

    public static final Codec<Powerup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("powerup").forGetter(powerup -> powerup.powerupInstance.getIdentifier()),
            PowerupState.CODEC.fieldOf("state").forGetter(Powerup::getState)

    ).apply(instance, Powerup::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Powerup> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            powerup -> powerup.getPowerupInstance().getIdentifier(),
            KnotStreamCodecs.enumCodec(PowerupState.class),
            Powerup::getState,
            Powerup::new
    );

    private final PowerupInstance powerupInstance;
    private PowerupState powerupState;

    public Powerup(Identifier powerupLocation, PowerupState powerupState) {
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
