package com.daqem.jobsplus.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.daqem.jobsplus.config.JobsPlusClientConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
    public void handleSystemChat(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        Component content = packet.content();
        if (content.getContents() instanceof TranslatableContents translatableContents) {
            if (translatableContents.getKey().equals("jobsplus.job.level_up")) {
                Object[] args = translatableContents.getArgs();
                if (args.length > 0 && args[0] instanceof Component nameComponent) {
                    String playerName = nameComponent.getString();
                    Minecraft minecraft = Minecraft.getInstance();
                    if (minecraft.player != null) {
                        boolean isMe = minecraft.player.getName().getString().equals(playerName);
                        if (isMe) {
                            if (!JobsPlusClientConfig.showYourLevelUpChatMessages.get()) {
                                ci.cancel();
                            }
                        } else {
                            if (!JobsPlusClientConfig.showPlayersLevelUpChatMessages.get()) {
                                ci.cancel();
                            }
                        }
                    }
                }
            }

            if (translatableContents.getKey().equals("jobsplus.job.exp.gain")) {
                if (!JobsPlusClientConfig.showExpAboveHotbar.get()) {
                    ci.cancel();
                }
            }
        }
    }
}
