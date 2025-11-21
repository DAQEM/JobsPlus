package com.daqem.jobsplus.client.event;

import com.daqem.jobsplus.client.gui.statusbar.JobsStatusBarsContainerComponent;
import com.daqem.jobsplus.player.JobsPlayer;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;

public class EventRenderHud {

    public static void registerEvent() {
        AtomicReference<JobsStatusBarsContainerComponent> component = new AtomicReference<>(null);
        ClientGuiEvent.RENDER_HUD.register((graphics, tickDelta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player instanceof JobsPlayer jobsPlayer) {
                if (component.get() == null) {
                    component.set(new JobsStatusBarsContainerComponent());
                } else {
                    component.get().render(graphics, 0, 0, 1.0f, jobsPlayer);
                }
            }
        });
    }
}
