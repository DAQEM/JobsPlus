package com.daqem.jobsplus.client.event;

import com.daqem.jobsplus.networking.c2s.ServerboundSyncPlayerJobsPacket;
import com.daqem.knot.Knot;

public class EventPlayerJoin {

    public static void registerEvent() {
        Knot.Events.Client.PLAYER_JOIN.register(player ->
                Knot.NETWORKING.sendToServer(new ServerboundSyncPlayerJobsPacket()));
    }
}
