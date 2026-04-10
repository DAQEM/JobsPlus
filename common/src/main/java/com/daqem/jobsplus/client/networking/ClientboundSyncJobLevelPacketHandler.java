package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobLevelPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.networking.ClientboundContext;
import org.jetbrains.annotations.NotNull;

public class ClientboundSyncJobLevelPacketHandler {

    public static void handle(@NotNull ClientboundSyncJobLevelPacket packet, ClientboundContext context) {
        if (context.player() instanceof JobsPlayer jobsPlayer) {
            Job localJob = jobsPlayer.jobsplus$getJob(packet.jobLocation());

            // If the job doesn't exist on the client yet (rare for level updates, but possible), create it
            if (localJob == null) {
                JobInstance jobInstance = JobInstance.of(packet.jobLocation());
                if (jobInstance != null) {
                    localJob = jobsPlayer.jobsplus$addNewJob(jobInstance);
                }
            }

            if (localJob != null) {
                localJob.setLevel(packet.level());
                localJob.setExperience(packet.experience(), false, false);
            }
        }
    }
}