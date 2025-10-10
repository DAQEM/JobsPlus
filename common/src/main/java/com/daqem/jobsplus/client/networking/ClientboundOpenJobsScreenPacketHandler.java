package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class ClientboundOpenJobsScreenPacketHandler {

    public static void handleClientSide(ClientboundOpenJobsScreenPacket packet, NetworkManager.PacketContext context) {
        @Nullable Screen previousScreen = null;
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            previousScreen = jobsScreen.getPreviousScreen();
        }
        Minecraft.getInstance().setScreen(new JobsScreen(new JobsScreenState(
                packet.getJobs(),
                packet.getCoins()
        ), previousScreen));
    }
}
