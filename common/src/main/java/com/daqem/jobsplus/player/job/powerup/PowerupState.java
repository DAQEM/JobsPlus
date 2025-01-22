package com.daqem.jobsplus.player.job.powerup;

/**
 * Enum representing the state of a Powerup in the game.
 * Each state represents a different condition a Powerup can be in.
 */
public enum PowerupState {
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
    LOCKED
}
