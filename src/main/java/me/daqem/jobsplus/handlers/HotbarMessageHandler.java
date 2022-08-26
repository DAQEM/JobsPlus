package me.daqem.jobsplus.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HotbarMessageHandler {

    public static void sendHotbarMessageServer(ServerPlayer serverPlayer, String message) {
        serverPlayer.sendSystemMessage(Component.literal(message), true);
    }

    public static void sendHotBarMessageClient(String message) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(message), true);
        }
    }
}
