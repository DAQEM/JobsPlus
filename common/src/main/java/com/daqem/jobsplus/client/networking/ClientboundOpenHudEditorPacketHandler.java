package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.hud.HudEditorScreen;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenHudEditorPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

public class ClientboundOpenHudEditorPacketHandler {

    public static void handleClientSide(ClientboundOpenHudEditorPacket packet, NetworkManager.PacketContext context) {
        if (JobsPlusClientConfig.jobStatusBarJobs.get().isEmpty()) {
            context.getPlayer().displayClientMessage(
                    JobsPlus.translatable("command.hud.jobs_required").withStyle(ChatFormatting.RED),
                    false
            );
        } else {
            Minecraft.getInstance().setScreen(new HudEditorScreen());
        }
    }
}