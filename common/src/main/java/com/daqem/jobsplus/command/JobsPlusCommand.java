package com.daqem.jobsplus.command;

import java.util.stream.Collectors;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenHudEditorPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.mojang.brigadier.CommandDispatcher;

import dev.architectury.networking.NetworkManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

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
                .then(Commands.literal("toggleexp")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .executes(context -> {
                            ServerPlayer serverPlayer = context.getSource().getPlayer();
                            if (serverPlayer instanceof JobsServerPlayer jobsServerPlayer) {
                                boolean newStatus = !jobsServerPlayer.jobsplus$isExpEnabled();
                                jobsServerPlayer.jobsplus$setExpEnabled(newStatus);
                                serverPlayer.sendSystemMessage(JobsPlus.translatable("command.toggleexp." + (newStatus ? "enabled" : "disabled")));
                            }
                            return 1;
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
