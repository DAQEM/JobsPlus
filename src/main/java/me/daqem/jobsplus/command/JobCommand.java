package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
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
                        .then(Commands.argument("target", EntityArgument.players())
                                .executes(context -> coinsPlayer(
                                        context.getSource(),
                                        EntityArgument.getPlayer(context, "target")
                                )))
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
            player.sendMessage(ChatHandler.header("HELP"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Available commands: \n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/jobs\n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "about\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "coins\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "crafting [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "display [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "info [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "powerups [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "start [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "stop [job]"), player.getUUID());
            player.sendMessage(ChatHandler.footer(4), player.getUUID());
            //TODO HELP
        }
        return 1;
    }

    private static int about(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("ABOUT"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.GREEN + "Jobs+ is a mod that adds 12 jobs " +
                    "to the game. These jobs each have their own power-ups, superpower and tasks. You can level-up " +
                    "your job by doing tasks that fit the job. Think of mining, building, killing, etc. Every time " +
                    "you level-up your job, you get a job-coin. With this coin you can buy new jobs and power-ups." +
                    "Once you reach job-level 100, your job will obtain its super power. This superpower will give " +
                    "you a massive benefit that fits the job. Each job also has its own craftable items that will be " +
                    "unlocked once you reach a certain job-level."), player.getUUID());
            player.sendMessage(ChatHandler.footer(5), player.getUUID());
            //TODO ABOUT
        }
        return 1;
    }

    private static int coins(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                player.sendMessage(ChatHandler.header("COINS"), player.getUUID());
                player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Your job coins: " + ChatFormatting.GREEN + handler.getCoins()), player.getUUID());
                player.sendMessage(ChatHandler.footer(5), player.getUUID());
            });
        }
        return 1;
    }

    private static int coinsPlayer(CommandSourceStack source, Player target) {
        if (source.getEntity() instanceof Player player) {
            if (target.getScoreboardName().equals(source.getEntity().getScoreboardName())) {
                coins(source);
            } else {
                target.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                    player.sendMessage(ChatHandler.header("COINS"), player.getUUID());
                    player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + target.getScoreboardName() + "s job coins: " + ChatFormatting.GREEN + handler.getCoins()), player.getUUID());
                    player.sendMessage(ChatHandler.footer(5), player.getUUID());
                });
            }

        }
        return 1;
    }

    private static int info(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("INFO"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job info [job]\n\n This command will show you some useful information about a certain job."), player.getUUID());
            player.sendMessage(ChatHandler.footer(4), player.getUUID());
            //TODO INFO
        }
        return 1;
    }

    private static int start(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("START"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job start [job]\n\n This command will start a certain job."), player.getUUID());
            player.sendMessage(ChatHandler.footer(5), player.getUUID());
            //TODO START
        }
        return 1;
    }

    private static int stop(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("STOP"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job stop [job]\n\n This command will stop a certain job if your job level is still level 1 or by paying 10 job coins."), player.getUUID());
            player.sendMessage(ChatHandler.footer(4), player.getUUID());
            //TODO START
        }
        return 1;
    }

    private static int powerups(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("POWER-UPS"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job powerups [job]\n\n This command will show you some useful information about the power-ups a certain job has."), player.getUUID());
            player.sendMessage(ChatHandler.footer(9), player.getUUID());
            //TODO POWERUPS
        }
        return 1;
    }

    private static int crafting(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("CRAFTING"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job crafting [job]\n\n This command will show you some useful information about the custom crafting recipes a certain job has."), player.getUUID());
            player.sendMessage(ChatHandler.footer(8), player.getUUID());
            //TODO CRAFTING
        }
        return 1;
    }

    private static int display(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.sendMessage(ChatHandler.header("DISPLAY"), player.getUUID());
            player.sendMessage(new KeybindComponent(ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job display [job]\n\n This command will change the job that is shown in front of your name in chat. By default, this is the first jon you chose."), player.getUUID());
            player.sendMessage(ChatHandler.footer(7), player.getUUID());
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
