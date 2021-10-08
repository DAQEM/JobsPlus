package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;

public class JobCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("job")
                        .then(Commands.literal("help")
                                .executes(context -> help(context.getSource())))
                        .then(Commands.literal("about")
                                .executes(context -> about(context.getSource())))
                        .then(Commands.literal("coins")
                                .executes(context -> coins(context.getSource())))
                        .then(Commands.literal("info")
                                .then(Commands.argument("job name", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> jobInfo(
                                                context.getSource(),
                                                context.getArgument("job name", Jobs.class)
                                        )))
                                .executes(context -> info(context.getSource())))
                        .then(Commands.literal("start")
                                .then(Commands.argument("job name", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> jobStart(
                                                context.getSource(),
                                                context.getArgument("job name", Jobs.class)
                                        )))
                                .executes(context -> start(context.getSource())))
                        .then(Commands.literal("stop")
                                .then(Commands.argument("job name", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> jobStop(
                                                context.getSource(),
                                                context.getArgument("job name", Jobs.class)
                                        )))
                                .executes(context -> stop(context.getSource())))
                        .then(Commands.literal("powerups")
                                .then(Commands.argument("job name", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> jobPowerUps(
                                                context.getSource(),
                                                context.getArgument("jobn ame", Jobs.class)
                                        )))
                                .executes(context -> powerups(context.getSource())))
                        .then(Commands.literal("crafting")
                                .then(Commands.argument("job name", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> jobCrafting(
                                                context.getSource(),
                                                context.getArgument("job name", Jobs.class)
                                        )))
                                .executes(context -> crafting(context.getSource())))
                        .then(Commands.literal("display")
                                .then(Commands.argument("job name", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> jobDisplay(
                                                context.getSource(),
                                                context.getArgument("job name", Jobs.class)
                                        )))
                                .executes(context -> display(context.getSource())))
                        .executes(context -> help(context.getSource())));

    }

    private static int help(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("help"), player.getUUID());
            //TODO HELP
        }
        return 1;
    }

    private static int about(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("about"), player.getUUID());
            //TODO ABOUT
        }
        return 1;
    }

    private static int coins(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("coins"), player.getUUID());
            //TODO COINS
        }
        return 1;
    }

    private static int info(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("info"), player.getUUID());
            //TODO INFO
        }
        return 1;
    }

    private static int start(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("start"), player.getUUID());
            //TODO START
        }
        return 1;
    }

    private static int stop(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("start"), player.getUUID());
            //TODO START
        }
        return 1;
    }

    private static int powerups(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("powerups"), player.getUUID());
            //TODO POWERUPS
        }
        return 1;
    }

    private static int crafting(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("crafting"), player.getUUID());
            //TODO CRAFTING
        }
        return 1;
    }

    private static int display(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("crafting"), player.getUUID());
            //TODO CRAFTING
        }
        return 1;
    }

    private static int jobInfo(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("name: " + player.getScoreboardName() + " job argument: " + job), player.getUUID());
            //TODO JOB INFO
        }
        return 1;
    }

    private static int jobStart(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("name: " + player.getScoreboardName() + " job argument: " + job), player.getUUID());
            //TODO JOB START
        }
        return 1;
    }

    private static int jobStop(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("name: " + player.getScoreboardName() + " job argument: " + job), player.getUUID());
            //TODO JOB START
        }
        return 1;
    }

    private static int jobPowerUps(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("name: " + player.getScoreboardName() + " job argument: " + job), player.getUUID());
            //TODO JOB POWERUPS
        }
        return 1;
    }

    private static int jobCrafting(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("name: " + player.getScoreboardName() + " job argument: " + job), player.getUUID());
            //TODO JOB CRAFTING
        }
        return 1;
    }

    private static int jobDisplay(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(new KeybindComponent("name: " + player.getScoreboardName() + " job argument: " + job), player.getUUID());
            //TODO JOB CRAFTING
        }
        return 1;
    }
}
