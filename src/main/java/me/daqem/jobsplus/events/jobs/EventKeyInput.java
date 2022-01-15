package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.SideProxy.Client;
import me.daqem.jobsplus.client.gui.JobsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventKeyInput {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (event.getKey() == Client.OPEN_GUI_KEYBIND.getKey().getValue()) {
            if (event.getAction() == 1) {
                Screen screen = Minecraft.getInstance().screen;
                if (screen instanceof JobsScreen) {
                    screen.onClose();
                }
                if (screen == null) {
                    if (Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.chat("/jobs menu");
                    }
                }
            }
        }
    }
}
