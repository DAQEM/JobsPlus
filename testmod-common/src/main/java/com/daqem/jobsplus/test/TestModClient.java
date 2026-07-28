package com.daqem.jobsplus.test;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.knot.events.EventsService;
import com.daqem.uilib.gui.component.text.TextComponent;
import net.minecraft.client.Minecraft;

public class TestModClient {

    public static void init() {
        EventsService.Client.HUD_RENDER.register((graphics, _) -> {
            if (Minecraft.getInstance().player instanceof JobsPlayer jobsPlayer) {
                TextComponent debugText = new TextComponent(
                        0, 0, JobsPlus.API.literal("Jobs: " + jobsPlayer.jobsplus$getJobs().size() + ", Power-ups: " + jobsPlayer.jobsplus$getJobs().stream().mapToLong(job -> job.getPowerupManager().getAllActivePowerups().size()).sum() + "/" + jobsPlayer.jobsplus$getJobs().stream().mapToLong(job -> job.getPowerupManager().getAllPowerups().size()).sum())
                );
                debugText.extractRenderState(graphics, 0, 0, 0);
            }
        });
    }
}
