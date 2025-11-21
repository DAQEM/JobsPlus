package com.daqem.jobsplus.client.event;

import com.daqem.jobsplus.networking.c2s.ServerboundSyncPlayerJobsPacket;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.networking.NetworkManager;

public class EventPlayerJoin {

    public static void registerEvent() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player ->
                NetworkManager.sendToServer(new ServerboundSyncPlayerJobsPacket()));
    }
}
