package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.networking.s2c.ClientboundLeaderboardPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundLeaderboardPacketHandler {

    public static void handleClientSide(ClientboundLeaderboardPacket packet, NetworkManager.PacketContext context) {
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            jobsScreen.getState().setLeaderboardPlayers(packet.getLeaderboard());
        }
    }
}
