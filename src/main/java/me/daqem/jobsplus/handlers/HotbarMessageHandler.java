package me.daqem.jobsplus.handlers;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;

public class HotbarMessageHandler {

    public static void sendHotbarMessage(ServerPlayer serverPlayer, String message) {
        serverPlayer.sendMessage(new KeybindComponent(message), ChatType.GAME_INFO, serverPlayer.getUUID());

    }
}
