package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreen;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenPowerupsScreenPacket;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class ClientboundOpenPowerupsScreenPacketHandler {

    public static void handleClientSide(ClientboundOpenPowerupsScreenPacket packet, NetworkManager.PacketContext context) {
        @Nullable Screen previousScreen = null;
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            previousScreen = jobsScreen.getPreviousScreen();
        }
        Job job = packet.getJobs().stream().filter(j -> j.getJobInstance().getLocation().equals(packet.getJobLocation())).findFirst().orElse(null);
        JobsScreen jobsScreen = new JobsScreen(new JobsScreenState(
                packet.getJobs(),
                packet.getCoins(),
                job,
                RightTab.EXPERIENCE
        ), previousScreen);
        if (job != null) {
            Minecraft.getInstance().setScreen(new PowerupsScreen(new PowerupsScreenState(job, packet.getCoins()), jobsScreen));
        }
    }
}
