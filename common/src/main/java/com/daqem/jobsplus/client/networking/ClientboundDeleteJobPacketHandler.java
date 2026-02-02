package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundDeleteJobPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import dev.architectury.networking.NetworkManager;

public class ClientboundDeleteJobPacketHandler {

    public static void handleClientSide(ClientboundDeleteJobPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsPlayer jobsPlayer) {
            JobInstance jobInstance = JobInstance.of(packet.getJobLocation());
            if (jobInstance != null) {
                jobsPlayer.jobsplus$removeJob(jobInstance);
            }
        }
    }
}