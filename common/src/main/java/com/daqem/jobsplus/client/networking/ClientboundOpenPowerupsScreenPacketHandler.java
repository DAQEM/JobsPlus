package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreen;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenPowerupsScreenPacket;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientboundOpenPowerupsScreenPacketHandler {

    public static void handle(@NotNull ClientboundOpenPowerupsScreenPacket packet, ClientboundContext context) {
        @Nullable Screen previousScreen = null;
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            previousScreen = jobsScreen.getPreviousScreen();
        }
        Job job = packet.jobs().stream().filter(j -> j.getJobInstance().getIdentifier().equals(packet.jobLocation())).findFirst().orElse(null);
        JobsScreen jobsScreen = new JobsScreen(new JobsScreenState(
                packet.jobs(),
                packet.coins(),
                job,
                RightTab.EXPERIENCE
        ), previousScreen);
        if (job != null) {
            Minecraft.getInstance().setScreen(new PowerupsScreen(new PowerupsScreenState(job, packet.coins()), jobsScreen));
        }
    }
}
