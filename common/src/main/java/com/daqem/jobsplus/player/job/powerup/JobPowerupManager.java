package com.daqem.jobsplus.player.job.powerup;

import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JobPowerupManager {

    private final Job job;
    private final Map<Identifier, Powerup> powerups;

    public JobPowerupManager(Job job, @NotNull List<Powerup> powerupsList) {
        this.job = job;
        this.powerups = new HashMap<>();
        for (Powerup powerup : powerupsList) {
            PowerupInstance powerupInstance = powerup.getPowerupInstance();
            if (powerupInstance != null) {
                Identifier id = powerupInstance.getIdentifier();
                if (id != null) {
                    this.powerups.put(id, powerup);
                }
            }
        }
    }

    public Optional<Powerup> getPowerup(PowerupInstance powerupInstance) {
        if (powerupInstance == null || powerupInstance.getIdentifier() == null) {
            return Optional.empty();
        }
        return getPowerup(powerupInstance.getIdentifier());
    }

    public Optional<Powerup> getPowerup(Identifier powerupLocation) {
        if (powerupLocation == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.powerups.get(powerupLocation));
    }

    public List<Powerup> getAllPowerups() {
        return new ArrayList<>(this.powerups.values());
    }

    public List<Powerup> getAllActivePowerups() {
        List<Powerup> activePowerups = new ArrayList<>();
        for (Powerup powerup : this.powerups.values()) {
            if (powerup.getState() == PowerupState.ACTIVE) {
                activePowerups.add(powerup);
            }
        }
        return activePowerups;
    }

    public boolean addPowerup(PowerupInstance powerupInstance) {
        return addPowerup(powerupInstance, PowerupState.ACTIVE);
    }

    public boolean addPowerup(PowerupInstance powerupInstance, PowerupState powerupState) {
        if (canAddPowerup(powerupInstance)) {
            Identifier id = powerupInstance.getIdentifier();
            if (id != null) {
                this.powerups.put(id, new Powerup(powerupInstance, powerupState));
                this.job.markPowerupsDirty();
                return true;
            }
        }
        return false;
    }

    public boolean canAddPowerup(PowerupInstance powerupInstance) {
        if (powerupInstance == null || powerupInstance.getIdentifier() == null) return false;

        if (this.powerups.containsKey(powerupInstance.getIdentifier())) return false;

        if (powerupInstance.getParent() == null) return true;

        return getPowerup(powerupInstance.getParent()).isPresent();
    }

    public void forceAddPowerup(PowerupInstance powerupInstance, PowerupState powerupState) {
        Identifier id = powerupInstance.getIdentifier();
        if (id != null) {
            this.powerups.put(id, new Powerup(powerupInstance, powerupState));
            this.job.markPowerupsDirty();
        }
    }

    public void clearPowerups() {
        this.powerups.clear();
        this.job.markPowerupsDirty();
    }

    public void addPowerups(List<Powerup> powerups) {
        for (Powerup powerup : powerups) {
            PowerupInstance powerupInstance = powerup.getPowerupInstance();
            if (powerupInstance != null) {
                Identifier id = powerupInstance.getIdentifier();
                if (id != null) {
                    this.powerups.put(id, powerup);
                }
            }
        }
    }

    public void togglePowerup(Powerup powerup) {
        powerup.toggle();
        this.job.markPowerupsDirty();
    }
}