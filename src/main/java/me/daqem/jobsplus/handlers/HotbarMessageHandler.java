package me.daqem.jobsplus.handlers;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HotbarMessageHandler {

    public static void sendHotbarMessage(ServerPlayer serverPlayer, String message) {
        serverPlayer.sendSystemMessage(Component.literal(message), ChatType.GAME_INFO);
    }
}
