package com.daqem.jobsplus.command;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenHudEditorPacket;
import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.networking.NetworkManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.stream.Collectors;

public class JobsPlusCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jobsplus")
                .then(Commands.literal("hud")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            NetworkManager.sendToPlayer(player, new ClientboundOpenHudEditorPacket());
                            return 1;
                        })
                )
                .then(Commands.literal("itemtag")
                        .executes(context -> {
                            ServerPlayer serverPlayer = context.getSource().getPlayer();
                            if (serverPlayer != null) {
                                serverPlayer.sendSystemMessage(JobsPlus.literal(
                                        serverPlayer.getMainHandItem().getTags().map(itemTagKey -> itemTagKey.location().toString()).collect(Collectors.joining(", "))
                                ));
                            }
                            return 0;
                        })
                )
                .then(Commands.literal("attributes")
                        .executes(context -> {
                            ServerPlayer serverPlayer = context.getSource().getPlayer();
                            if (serverPlayer != null) {
                                serverPlayer.getAttributes().getSyncableAttributes().forEach(attribute -> {
                                    serverPlayer.sendSystemMessage(JobsPlus.literal(
                                            attribute.getAttribute().getRegisteredName() + ": " + attribute.getValue()
                                    ));
                                    attribute.getModifiers().forEach(attributeModifier -> serverPlayer.sendSystemMessage(JobsPlus.literal(
                                            attributeModifier.id() + ": " + attributeModifier.amount()
                                    )));
                                    serverPlayer.sendSystemMessage(JobsPlus.literal(" "));
                                });
                            }
                            return 0;
                        })
                )
        );
    }
}
