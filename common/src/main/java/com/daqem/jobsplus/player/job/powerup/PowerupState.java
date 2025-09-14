package com.daqem.jobsplus.player.job.powerup;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing the state of a Powerup in the game.
 * Each state represents a different condition a Powerup can be in.
 */
public enum PowerupState implements StringRepresentable {
    /**
     * The Powerup is currently active and in use.
     */
    ACTIVE,

    /**
     * The Powerup has been turned off by the player.
     */
    INACTIVE,

    /**
     * The Powerup is available for purchase but has not been bought by the player yet.
     */
    NOT_OWNED,

    /**
     * The Powerup is not yet available for purchase by the player.
     */
    LOCKED;

    public static final StringRepresentable.EnumCodec<PowerupState> CODEC = StringRepresentable.fromEnum(PowerupState::values);

    @Override
    public @NotNull String getSerializedName() {
        return this.name();
    }
}
