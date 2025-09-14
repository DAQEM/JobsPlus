package com.daqem.jobsplus.player;

import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface JobsServerPlayer extends JobsPlayer {

    ServerPlayer jobsplus$getServerPlayer();

    void jobsplus$updateJob(Job job);

    void jobsplus$updateActionHolders(Job job);

    @Nullable Powerup jobsplus$getPowerup(PowerupInstance powerupInstance);
}
