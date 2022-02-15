package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import me.daqem.jobsplus.events.DoubleJumpEvents;
import me.daqem.jobsplus.events.jobs.MinerEvents;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class SendJobsPacketCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("sendjobspacket")
                .then(Commands.literal("veinminer")
                        .then(Commands.literal("enable")
                                .executes(context -> enableVeinMiner(
                                        context.getSource()
                                )))
                        .then(Commands.literal("disable")
                                .executes(context -> disableVeinMiner(
                                        context.getSource()
                                ))))
                .then(Commands.literal("doublejump")
                        .executes(context -> doubleJump(
                                context.getSource()
                        ))));
    }

    private static int enableVeinMiner(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            if (!MinerEvents.veinMinerArray.contains(player.getUUID())) {
                MinerEvents.veinMinerArray.add(player.getUUID());
            }
        }
        return 1;
    }

    private static int disableVeinMiner(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            MinerEvents.veinMinerArray.remove(player.getUUID());
        }
        return 1;
    }

    private static int doubleJump(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            DoubleJumpEvents.attemptPlayerJump(player, new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER) ? 1 : 0, JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWERUP3.get()) ? 1 : 0});
//            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketSendBuilderInfo(
//                    new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER) ? 1 : 0, JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWERUP3.get()) ? 1 : 0}));
        }
        return 1;
    }
}
