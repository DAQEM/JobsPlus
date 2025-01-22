package com.daqem.jobsplus.player.job.powerup;

import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;

public class Powerup {

    private final PowerupInstance powerupInstance;
    private PowerupState powerupState;

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
