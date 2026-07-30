package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.networking.s2c.ClientboundPlayerJobsPacket;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundPlayerJobsPacketHandler {

    public static void handle(@NotNull ClientboundPlayerJobsPacket packet, ClientboundContext context) {
        if (Minecraft.getInstance().gui.screen() instanceof JobsScreen jobsScreen) {
            LeaderboardPlayer viewingPlayer = jobsScreen.getState().getViewingPlayer();
            if (viewingPlayer != null && viewingPlayer.getUuid().equals(packet.playerUUID())) {
                jobsScreen.getState().setViewingPlayerJobs(packet.jobs());
            }
        }
    }
}
