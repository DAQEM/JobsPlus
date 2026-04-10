package com.daqem.jobsplus.client.event;

import com.daqem.jobsplus.client.gui.statusbar.JobsStatusBarsContainerComponent;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.knot.Knot;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;

public class EventRenderHud {

    public static void registerEvent() {
        AtomicReference<JobsStatusBarsContainerComponent> component = new AtomicReference<>(null);
        Knot.Events.Client.HUD_RENDER.register((graphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player instanceof JobsPlayer jobsPlayer) {
                if (component.get() == null) {
                    component.set(new JobsStatusBarsContainerComponent());
                } else {
                    component.get().extractRenderState(graphics, 0, 0, 1.0f, jobsPlayer);
                }
            }
        });
    }
}
