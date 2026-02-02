package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobLevelPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;

public class ClientboundSyncJobLevelPacketHandler {

    public static void handleClientSide(ClientboundSyncJobLevelPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsPlayer jobsPlayer) {
            Job localJob = jobsPlayer.jobsplus$getJob(packet.getJobLocation());

            // If the job doesn't exist on the client yet (rare for level updates, but possible), create it
            if (localJob == null) {
                JobInstance jobInstance = JobInstance.of(packet.getJobLocation());
                if (jobInstance != null) {
                    localJob = jobsPlayer.jobsplus$addNewJob(jobInstance);
                }
            }

            if (localJob != null) {
                localJob.setLevel(packet.getLevel());
                localJob.setExperience(packet.getExperience(), false, false);
            }
        }
    }
}