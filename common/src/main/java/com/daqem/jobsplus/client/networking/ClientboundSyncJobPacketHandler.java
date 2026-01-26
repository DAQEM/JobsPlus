package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;

public class ClientboundSyncJobPacketHandler {

    public static void handleClientSide(ClientboundSyncJobPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsPlayer jobsPlayer) {
            Job syncedJob = packet.getJob();
            Job localJob = jobsPlayer.jobsplus$getJob(syncedJob.getJobInstance().getIdentifier());

            // If the job doesn't exist on the client yet, create it
            if (localJob == null) {
                JobInstance jobInstance = JobInstance.of(syncedJob.getJobInstance().getIdentifier());
                if (jobInstance != null) {
                    localJob = jobsPlayer.jobsplus$addNewJob(jobInstance);
                }
            }

            // Update the local job instance with the server's data
            if (localJob != null) {
                localJob.setLevel(syncedJob.getLevel());
                localJob.setExperience(syncedJob.getExperience(), false, false);

                // Sync the Powerup Manager
                // Clear existing powerups to ensure we have an exact match of the server state
                localJob.getPowerupManager().clearPowerups();
                localJob.getPowerupManager().addPowerups(syncedJob.getPowerupManager().getAllPowerups());
            }
        }
    }
}