package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.networking.s2c.ClientboundLeaderboardPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundLeaderboardPacketHandler {

    public static void handle(@NotNull ClientboundLeaderboardPacket packet, ClientboundContext context) {
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            jobsScreen.getState().setLeaderboardPlayers(packet.leaderboard());
        }
    }
}
