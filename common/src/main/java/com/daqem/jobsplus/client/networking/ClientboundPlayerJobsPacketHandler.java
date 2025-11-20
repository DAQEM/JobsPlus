package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.networking.s2c.ClientboundPlayerJobsPacket;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundPlayerJobsPacketHandler {

    public static void handleClientSide(ClientboundPlayerJobsPacket packet, NetworkManager.PacketContext context) {
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            LeaderboardPlayer viewingPlayer = jobsScreen.getState().getViewingPlayer();
            if (viewingPlayer != null && viewingPlayer.getUuid().equals(packet.getPlayerUUID())) {
                jobsScreen.getState().setViewingPlayerJobs(packet.getJobs());
            }
        }
    }
}
