package com.daqem.jobsplus.client.event;

import com.daqem.jobsplus.client.JobsPlusClient;
import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.networking.c2s.ServerboundOpenJobsScreenPacket;
import com.daqem.knot.Knot;
import com.daqem.knot.events.EventResult;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public class EventKeyPressed {

    public static void registerEvent() {
        Knot.Events.Client.RAW_INPUT_KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            Screen screen = client.gui.screen();
            KeyEvent keyEvent = new KeyEvent(keyCode, scanCode, modifiers);
            if (JobsPlusClient.OPEN_MENU.matches(keyEvent) && action == 1) {
                if (screen instanceof JobsScreen) screen.onClose();
                else if (screen == null) Knot.NETWORKING.sendToServer(new ServerboundOpenJobsScreenPacket());
            }
            return EventResult.PASS;
        });
    }
}
