package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HotbarMessageHandler {

    public static void sendHotbarMessageServer(ServerPlayer serverPlayer, String message) {
        serverPlayer.sendMessage(JobsPlus.literal(message), ChatType.GAME_INFO, serverPlayer.getUUID());
    }

    public static void sendHotbarMessageServer(ServerPlayer serverPlayer, Component message) {
        serverPlayer.sendMessage(message, ChatType.GAME_INFO, serverPlayer.getUUID());
    }
}
