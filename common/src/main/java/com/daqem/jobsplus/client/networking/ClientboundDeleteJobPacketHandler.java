package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundDeleteJobPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.knot.networking.ClientboundContext;
import org.jetbrains.annotations.NotNull;

public class ClientboundDeleteJobPacketHandler {

    public static void handle(@NotNull ClientboundDeleteJobPacket packet, ClientboundContext context) {
        if (context.player() instanceof JobsPlayer jobsPlayer) {
            JobInstance jobInstance = JobInstance.of(packet.jobLocation());
            if (jobInstance != null) {
                jobsPlayer.jobsplus$removeJob(jobInstance);
            }
        }
    }
}