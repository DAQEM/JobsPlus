package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
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
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobInfo(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> info(context.getSource())))
                .then(Commands.literal("start")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobStart(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> start(context.getSource())))
                .then(Commands.literal("stop")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobStop(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> stop(context.getSource())))
                .then(Commands.literal("powerups")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobPowerUps(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> powerups(context.getSource())))
                .then(Commands.literal("crafting")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobCrafting(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> crafting(context.getSource())))
                .then(Commands.literal("display")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobDisplay(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> display(context.getSource())))
                .executes(context -> help(context.getSource())));

    }

    private static int help(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("HELP"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Available commands: \n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/jobs\n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "about\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "coins\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "crafting [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "display [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "info [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "powerups [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "start [job]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job " + ChatFormatting.GREEN + "stop [job]");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int about(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("ABOUT"));
            ChatHandler.sendMessage(player, ChatFormatting.GREEN + "Jobs+ is a mod that adds 12 jobs " +
                    "to the game. These jobs each have their own power-ups, superpower and tasks. You can level-up " +
                    "your job by doing tasks that fit the job. Think of mining, building, killing, etc. Every time " +
                    "you level-up your job, you get a job-coin. With this coin you can buy new jobs and power-ups." +
                    "Once you reach job-level 100, your job will obtain its super power. This superpower will give " +
                    "you a massive benefit that fits the job. Each job also has its own craftable items that will be " +
                    "unlocked once you reach a certain job-level.");
            ChatHandler.sendMessage(player, ChatHandler.footer(5));
        }
        return 1;
    }

    private static int coins(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                ChatHandler.sendMessage(player, ChatHandler.header("COINS"));
                ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Your job coins: " +
                        ChatFormatting.GREEN + handler.getCoins());
                ChatHandler.sendMessage(player, ChatHandler.footer(5));
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
                    ChatHandler.sendMessage(player, ChatHandler.header("COINS"));
                    ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + target.getScoreboardName() +
                            "s job coins: " + ChatFormatting.GREEN + handler.getCoins());
                    ChatHandler.sendMessage(player, ChatHandler.footer(5));
                });
            }

        }
        return 1;
    }

    private static int info(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("INFO"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job info [job]\n\n This command will show you some useful information " +
                    "about a certain job.");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int start(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("START"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job start [job]\n\n This command will start a certain job.");
            ChatHandler.sendMessage(player, ChatHandler.footer(5));
        }
        return 1;
    }

    private static int stop(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("STOP"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job stop [job]\n\n This command will stop a certain job if your job level " +
                    "is still level 1 or by paying 10 job coins.");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int powerups(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("POWER-UPS"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job powerups [job]\n\n This command will show you some useful information " +
                    "about the power-ups a certain job has.");
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
        }
        return 1;
    }

    private static int crafting(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("CRAFTING"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job crafting [job]\n\n This command will show you some useful information " +
                    "about the custom crafting recipes a certain job has.");
            ChatHandler.sendMessage(player, ChatHandler.footer(8));
        }
        return 1;
    }

    private static int display(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("DISPLAY"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job display [job]\n\n This command will change the job that is shown in " +
                    "front of your name in chat. By default, this is the first jon you chose.");
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
        }
        return 1;
    }

    private static int jobInfo(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, "name: " + player.getScoreboardName() + " job argument: " + job);
            //TODO JOB INFO
        }
        return 1;
    }

    private static int jobStart(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                switch (job) {
                    case ALCHEMIST -> {
                        if (JobGetters.getJobIsEnabled(player, job)) {
                            ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You have already started this job.");
                        } else {

                        }
                    }
                    case BUILDER -> {}
                    case BUTCHER -> {}
                    case CRAFTSMAN -> {}
                    case DIGGER -> {}
                    case ENCHANTER -> {}
                    case FARMER -> {}
                    case FISHERMAN -> {}
                    case HUNTER -> {}
                    case LUMBERJACK -> {}
                    case MINER -> {}
                    case SMITH -> {}
                }
            });
            //TODO JOB START
        }
        return 1;
    }

    private static int jobStop(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, "name: " + player.getScoreboardName() + " job argument: " + job);
            //TODO JOB START
        }
        return 1;
    }

    private static int jobPowerUps(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, "name: " + player.getScoreboardName() + " job argument: " + job);
            //TODO JOB POWERUPS
        }
        return 1;
    }

    private static int jobCrafting(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, "name: " + player.getScoreboardName() + " job argument: " + job);
            //TODO JOB CRAFTING
        }
        return 1;
    }

    private static int jobDisplay(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, "name: " + player.getScoreboardName() + " job argument: " + job);
            //TODO JOB CRAFTING
        }
        return 1;
    }

//    switch (job) {
//        case ALCHEMIST -> {}
//        case BUILDER -> {}
//        case BUTCHER -> {}
//        case CRAFTSMAN -> {}
//        case DIGGER -> {}
//        case ENCHANTER -> {}
//        case FARMER -> {}
//        case FISHERMAN -> {}
//        case HUNTER -> {}
//        case LUMBERJACK -> {}
//        case MINER -> {}
//        case SMITH -> {}
//    }
}
