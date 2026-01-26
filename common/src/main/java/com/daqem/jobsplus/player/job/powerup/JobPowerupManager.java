package com.daqem.jobsplus.player.job.powerup;

import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JobPowerupManager {

    private final Map<Identifier, Powerup> powerups;

    public JobPowerupManager(@NotNull List<Powerup> powerupsList) {
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
        return Optional.ofNullable(powerups.get(powerupLocation));
    }

    public List<Powerup> getAllPowerups() {
        return new ArrayList<>(powerups.values());
    }

    public boolean addPowerup(JobsPlayer player, Job job, PowerupInstance powerupInstance) {
        return addPowerup(player, job, powerupInstance, PowerupState.ACTIVE);
    }

    public boolean addPowerup(JobsPlayer player, Job job, PowerupInstance powerupInstance, PowerupState powerupState) {
        if (canAddPowerup(powerupInstance)) {
            Identifier id = powerupInstance.getIdentifier();
            if (id != null) {
                powerups.put(id, new Powerup(powerupInstance, powerupState));
                this.sendJobUpdatePacket(job, player);
                return true;
            }
        }
        return false;
    }

    private void sendJobUpdatePacket(Job job, JobsPlayer player) {
        if (player instanceof JobsServerPlayer jobsServerPlayer) {
            jobsServerPlayer.jobsplus$updateJob(job);
        }
    }

    public boolean canAddPowerup(PowerupInstance powerupInstance) {
        if (powerupInstance == null || powerupInstance.getIdentifier() == null) return false;

        if (powerups.containsKey(powerupInstance.getIdentifier())) return false;

        if (powerupInstance.getParent() == null) return true;

        return getPowerup(powerupInstance.getParent()).isPresent();
    }

    public void forceAddPowerup(JobsPlayer player, Job job, PowerupInstance powerupInstance, PowerupState powerupState) {
        Identifier id = powerupInstance.getIdentifier();
        if (id != null) {
            powerups.put(id, new Powerup(powerupInstance, powerupState));
            this.sendJobUpdatePacket(job, player);
        }
    }

    public void clearPowerups() {
        powerups.clear();
    }

    public Optional<Powerup> getParent(PowerupInstance powerupInstance) {
        PowerupInstance parentPowerupInstance = powerupInstance.getParent();
        if (parentPowerupInstance == null) return Optional.empty();
        return getPowerup(parentPowerupInstance);
    }

    public Optional<Powerup> getParent(Powerup powerup) {
        PowerupInstance parentPowerupInstance = powerup.getPowerupInstance().getParent();
        if (parentPowerupInstance == null) return Optional.empty();
        return getPowerup(parentPowerupInstance);
    }

    public List<Powerup> getChildren(PowerupInstance powerupInstance) {
        return getChildren(powerupInstance, new ArrayList<>(powerups.values()));
    }

    public List<Powerup> getChildren(Powerup powerup) {
        return getChildren(powerup, new ArrayList<>(powerups.values()));
    }

    public static List<Powerup> getChildren(PowerupInstance powerupInstance, List<Powerup> powerups) {
        return powerups.stream()
                .filter(powerup -> powerup.getPowerupInstance().getParentLocation() != null
                        && powerup.getPowerupInstance().getParentLocation().equals(powerupInstance.getIdentifier()))
                .toList();
    }

    public static List<Powerup> getChildren(Powerup powerup, List<Powerup> powerups) {
        return getChildren(powerup.getPowerupInstance(), powerups);
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

    public void togglePowerup(JobsPlayer player, Job job, Powerup powerup) {
        powerup.toggle();
        sendJobUpdatePacket(job, player);
    }
}