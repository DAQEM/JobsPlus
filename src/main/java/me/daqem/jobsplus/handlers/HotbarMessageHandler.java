package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HotbarMessageHandler {

    public static void sendHotbarMessageServer(ServerPlayer serverPlayer, String message) {
        serverPlayer.sendSystemMessage(JobsPlus.literal(message), true);
    }

    public static void sendHotbarMessageServer(ServerPlayer serverPlayer, Component message) {
        serverPlayer.sendSystemMessage(message, true);
    }
}
