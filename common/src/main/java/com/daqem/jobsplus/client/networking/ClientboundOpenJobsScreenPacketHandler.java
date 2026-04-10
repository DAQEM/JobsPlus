package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientboundOpenJobsScreenPacketHandler {

    public static void handle(@NotNull ClientboundOpenJobsScreenPacket packet, ClientboundContext context) {
        @Nullable Screen previousScreen = null;
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            previousScreen = jobsScreen.getPreviousScreen();
        }
        Minecraft.getInstance().setScreen(new JobsScreen(new JobsScreenState(
                packet.jobs(),
                packet.coins()
        ), previousScreen));
    }
}
