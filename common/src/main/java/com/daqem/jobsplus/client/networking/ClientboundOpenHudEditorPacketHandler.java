package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.hud.HudEditorScreen;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenHudEditorPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundOpenHudEditorPacketHandler {

    public static void handle(@NotNull ClientboundOpenHudEditorPacket packet, ClientboundContext context) {
        if (JobsPlusClientConfig.jobStatusBarJobs.get().isEmpty()) {
            context.player().sendSystemMessage(
                    JobsPlus.API.translatable("command.hud.jobs_required").withStyle(ChatFormatting.RED)
            );
        } else {
            Minecraft.getInstance().setScreen(new HudEditorScreen());
        }
    }
}