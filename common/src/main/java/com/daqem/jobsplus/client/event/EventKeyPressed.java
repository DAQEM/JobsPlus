package com.daqem.jobsplus.client.event;

import com.daqem.jobsplus.client.JobsPlusClient;
import com.daqem.jobsplus.client.screen.job.JobsScreen;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.networking.c2s.ServerboundOpenJobsScreenPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class EventKeyPressed {

    public static void registerEvent() {
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            Screen screen = client.screen;
            if (JobsPlusClient.OPEN_MENU.matches(keyCode, scanCode) && action == 1) {
                if (screen instanceof JobsScreen) screen.onClose();
                else if (screen == null) NetworkManager.sendToServer(new ServerboundOpenJobsScreenPacket());
            }
            return EventResult.pass();
        });
    }
}
