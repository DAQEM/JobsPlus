package com.daqem.jobsplus.command;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenHudEditorPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.knot.Knot;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import java.util.stream.Collectors;

public class JobsPlusCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jobsplus")
                .then(Commands.literal("hud")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Knot.NETWORKING.sendToPlayer(player, new ClientboundOpenHudEditorPacket());
                            return 1;
                        })
                )
                .then(Commands.literal("itemtag")
                        .executes(context -> {
                            ServerPlayer serverPlayer = context.getSource().getPlayer();
                            if (serverPlayer != null) {
                                serverPlayer.sendSystemMessage(JobsPlus.API.literal(
                                        serverPlayer.getMainHandItem().tags().map(itemTagKey -> itemTagKey.location().toString()).collect(Collectors.joining(", "))
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
                                serverPlayer.sendSystemMessage(JobsPlus.API.translatable("command.toggleexp." + (newStatus ? "enabled" : "disabled")));
                            }
                            return 1;
                        })
                )
                .then(Commands.literal("togglecoins")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .executes(context -> {
                            ServerPlayer serverPlayer = context.getSource().getPlayer();
                            if (serverPlayer instanceof JobsServerPlayer jobsServerPlayer) {
                                boolean newStatus = !jobsServerPlayer.jobsplus$isCoinsEnabled();
                                jobsServerPlayer.jobsplus$setCoinsEnabled(newStatus);
                                serverPlayer.sendSystemMessage(JobsPlus.API.translatable("command.togglecoins." + (newStatus ? "enabled" : "disabled")));
                            }
                            return 1;
                        })
                )
                .then(Commands.literal("attributes")
                        .executes(context -> {
                            ServerPlayer serverPlayer = context.getSource().getPlayer();
                            if (serverPlayer != null) {
                                serverPlayer.getAttributes().getSyncableAttributes().forEach(attribute -> {
                                    serverPlayer.sendSystemMessage(JobsPlus.API.literal(
                                            attribute.getAttribute().getRegisteredName() + ": " + attribute.getValue()
                                    ));
                                    attribute.getModifiers().forEach(attributeModifier -> serverPlayer.sendSystemMessage(JobsPlus.API.literal(
                                            attributeModifier.id() + ": " + attributeModifier.amount()
                                    )));
                                    serverPlayer.sendSystemMessage(JobsPlus.API.literal(" "));
                                });
                            }
                            return 0;
                        })
                )
        );
    }
}
