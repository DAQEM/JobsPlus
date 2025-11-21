package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;

public class ClientboundSyncJobPacketHandler {

    public static void handleClientSide(ClientboundSyncJobPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsPlayer jobsPlayer) {
            Job job = jobsPlayer.jobsplus$getJob(packet.getJobLocation());
            if (job != null) {
                job.setLevel(packet.getLevel());
                job.setExperience(packet.getExperience(), false, false);
            } else {
                JobInstance jobInstance = JobInstance.of(packet.getJobLocation());
                if (jobInstance != null) {
                    Job newJob = jobsPlayer.jobsplus$addNewJob(jobInstance);
                    if (newJob != null) {
                        newJob.setLevel(packet.getLevel());
                        newJob.setExperience(packet.getExperience(), false, false);
                    }
                }
            }
        }
    }
}
