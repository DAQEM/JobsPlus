package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;

import java.util.Arrays;

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
//                .then(Commands.literal("info")
//                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
//                                .executes(context -> jobInfo(
//                                        context.getSource(),
//                                        context.getArgument("job", Jobs.class)
//                                )))
//                        .executes(context -> info(context.getSource())))
                .then(Commands.literal("start")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .then(Commands.literal("force")
                                        .executes(context -> forceJobStart(
                                                context.getSource(),
                                                context.getArgument("job", Jobs.class)
                                        )))
                                .executes(context -> jobStart(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> start(context.getSource())))
                .then(Commands.literal("stop")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .then(Commands.literal("force")
                                        .executes(context -> forceJobStop(
                                                context.getSource(),
                                                context.getArgument("job", Jobs.class)
                                        )))
                                .executes(context -> jobStop(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> stop(context.getSource())))
                .then(Commands.literal("powerups")
                        .then(Commands.literal("buy")
                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                        .then(Commands.argument("powerup", IntegerArgumentType.integer(1, 3))
                                                .executes(context -> jobPowerUps(
                                                        context.getSource(),
                                                        context.getArgument("job", Jobs.class),
                                                        IntegerArgumentType.getInteger(context, "powerup")
                                                )))
                                        .executes(context -> powerups(context.getSource())))
                                .executes(context -> powerups(context.getSource())))
                        .then(Commands.literal("switch")
                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                        .then(Commands.argument("powerup", IntegerArgumentType.integer(1, 3))
                                                .executes(context -> switchJobPowerUps(
                                                        context.getSource(),
                                                        context.getArgument("job", Jobs.class),
                                                        IntegerArgumentType.getInteger(context, "powerup")
                                                )))
                                        .executes(context -> powerups(context.getSource())))
                                .executes(context -> powerups(context.getSource())))
                        .executes(context -> powerups(context.getSource())))
                .then(Commands.literal("superpower")
                        .then(Commands.literal("switch")
                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                        .executes(context -> switchSuperpower(
                                                context.getSource(),
                                                context.getArgument("job", Jobs.class)
                                        )))))
//                .then(Commands.literal("crafting")
//                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
//                                .executes(context -> jobCrafting(
//                                        context.getSource(),
//                                        context.getArgument("job", Jobs.class)
//                                )))
//                        .executes(context -> crafting(context.getSource())))
                .then(Commands.literal("display")
                        .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                .executes(context -> jobDisplay(
                                        context.getSource(),
                                        context.getArgument("job", Jobs.class)
                                )))
                        .executes(context -> display(context.getSource())))
                .then(Commands.literal("admin")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.literal("level")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 100))
                                                                .executes(context -> setLevel(
                                                                        context.getSource(),
                                                                        EntityArgument.getPlayer(context, "target"),
                                                                        context.getArgument("job", Jobs.class),
                                                                        IntegerArgumentType.getInteger(context, "level"))))
                                                        .executes(context -> setLevelHelp(context.getSource())))
                                                .executes(context -> setLevelHelp(context.getSource())))
                                        .executes(context -> setLevelHelp(context.getSource())))
                                .then(Commands.literal("exp")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                                        .then(Commands.argument("exp", IntegerArgumentType.integer())
                                                                .executes(context -> setEXP(
                                                                        context.getSource(),
                                                                        EntityArgument.getPlayer(context, "target"),
                                                                        context.getArgument("job", Jobs.class),
                                                                        IntegerArgumentType.getInteger(context, "exp"))))
                                                        .executes(context -> setEXPHelp(context.getSource())))
                                                .executes(context -> setEXPHelp(context.getSource())))
                                        .executes(context -> setEXPHelp(context.getSource())))
                                .then(Commands.literal("coins")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("coins", IntegerArgumentType.integer())
                                                        .executes(context -> setCoins(
                                                                context.getSource(),
                                                                EntityArgument.getPlayer(context, "target"),
                                                                IntegerArgumentType.getInteger(context, "coins")
                                                        )))
                                                .executes(context -> setCoinsHelp(context.getSource())))
                                        .executes(context -> setCoinsHelp(context.getSource())))
                                .then(Commands.literal("display")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("display", EnumArgument.enumArgument(Jobs.class))
                                                        .executes(context -> setDisplay(
                                                                context.getSource(),
                                                                EntityArgument.getPlayer(context, "target"),
                                                                context.getArgument("display", Jobs.class)
                                                        )))
                                                .executes(context -> setDisplayHelp(context.getSource())))
                                        .executes(context -> setDisplayHelp(context.getSource())))
                                .then(Commands.literal("powerup")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                                        .then(Commands.argument("powerup", IntegerArgumentType.integer(1, 3))
                                                                .then(Commands.argument("int", IntegerArgumentType.integer(0, 2))
                                                                        .executes(context -> setPowerUp(
                                                                                context.getSource(),
                                                                                EntityArgument.getPlayer(context, "target"),
                                                                                context.getArgument("job", Jobs.class),
                                                                                IntegerArgumentType.getInteger(context, "powerup"),
                                                                                IntegerArgumentType.getInteger(context, "int"))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .executes(context -> adminSet(context.getSource())))
                        .then(Commands.literal("debug")
                                .then(Commands.argument("target", EntityArgument.players())
                                        .executes(context -> debug(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "target"))))
                                .executes(context -> debug(
                                        context.getSource(),
                                        null)))
                        .executes(context -> admin(context.getSource())))
                .executes(context -> help(context.getSource())));

    }

    private static int switchJobPowerUps(CommandSourceStack source, Jobs job, int powerUp) {
        if (source.getEntity() instanceof Player player) {
            ++powerUp;
            if (JobGetters.getPowerup(player, job, powerUp) == 1 || JobGetters.getPowerup(player, job, powerUp) == 2) {
                if (JobGetters.getPowerup(player, job, powerUp) == 1) {
                    JobSetters.setPowerUp(job, player, powerUp, 2);
                } else {
                    JobSetters.setPowerUp(job, player, powerUp, 1);
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You have not bought this power-up yet.");
            }
        }
        return 1;
    }

    private static int switchSuperpower(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.getJobLevel(player, job) == 100) {
                if (JobGetters.getSuperPower(player, job) == 0) {
                    JobSetters.setSuperPower(player, job, 1);
                } else {
                    JobSetters.setSuperPower(player, job, 0);
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You must be level 100 to perform this action.");
            }
        }
        return 1;
    }

    private static int setPowerUp(CommandSourceStack source, Player target, Jobs job, int powerUp, int i) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET POWER-UP"));
            if (powerUp == 1) {
                ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s first " +
                        ChatHandler.capitalizeWord(job.toString().toLowerCase()) + " power-up to " + i);
            } else if (powerUp == 2) {
                ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s second " +
                        ChatHandler.capitalizeWord(job.toString().toLowerCase()) + " power-up to " + i);
            } else if (powerUp == 3) {
                ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s third " +
                        ChatHandler.capitalizeWord(job.toString().toLowerCase()) + " power-up to " + i);
            }
            ChatHandler.sendMessage(player, ChatHandler.footer(13));
        } else {
            if (powerUp == 1) {
                JobsPlus.LOGGER.info("Set " + target.getScoreboardName() + "s first " +
                        ChatHandler.capitalizeWord(job.toString().toLowerCase()) + " power-up to " + i);
            } else if (powerUp == 2) {
                JobsPlus.LOGGER.info("Set " + target.getScoreboardName() + "s second " +
                        ChatHandler.capitalizeWord(job.toString().toLowerCase()) + " power-up to " + i);
            } else if (powerUp == 3) {
                JobsPlus.LOGGER.info("Set " + target.getScoreboardName() + "s third " +
                        ChatHandler.capitalizeWord(job.toString().toLowerCase()) + " power-up to " + i);
            }
        }
        JobSetters.setPowerUp(job, target, powerUp + 1, i);
        return 1;
    }

    private static int help(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("HELP"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Available commands: \n\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/jobs\n\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "about\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "coins\n" +
//                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "crafting [job]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "display [job]\n" +
//                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "info [job]\n" +
//                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "powerups [job]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "start [job]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job " + ChatColor.green() + "stop [job]");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int about(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("ABOUT"));
            ChatHandler.sendMessage(player, ChatColor.green() + "Jobs+ is a mod that adds 10 jobs " +
                    "to the game. These jobs each have their own power-ups, superpower and tasks. You can level-up " +
                    "your job by doing tasks that fit the job. Think of mining, building, killing, etc. Every time " +
                    "you level-up your job, you get a job-coin. With this coin you can buy new jobs and power-ups." +
                    " Once you reach job-level 100, your job will obtain its superpower. This superpower will give " +
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
                ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Your job coins: " +
                        ChatColor.green() + handler.getCoins());
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
                    ChatHandler.sendMessage(player, ChatColor.darkGreen() + target.getScoreboardName() +
                            "s job coins: " + ChatColor.green() + handler.getCoins());
                    ChatHandler.sendMessage(player, ChatHandler.footer(5));
                });
            }

        }
        return 1;
    }

    private static int info(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("INFO"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job info [job]\n\n This command will show you some useful information " +
                    "about a certain job.");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int start(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("START"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job start [job]\n\n This command will start a certain job.");
            ChatHandler.sendMessage(player, ChatHandler.footer(5));
        }
        return 1;
    }

    private static int stop(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("STOP"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job stop [job]\n\n This command will stop a certain job if your job level " +
                    "is still level 1 or by paying 10 job coins.");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int powerups(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("POWER-UPS"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job powerups buy [job]\n\n This command will buy one of the available power-ups.");
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
        }
        return 1;
    }

    private static int crafting(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("CRAFTING"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job crafting [job]\n\n This command will show you some useful information " +
                    "about the custom crafting recipes a certain job has.");
            ChatHandler.sendMessage(player, ChatHandler.footer(8));
        }
        return 1;
    }

    private static int display(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("DISPLAY"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job display [job]\n\n This command will change the job that is shown in " +
                    "front of your name in tab and chat. By default, this is the first job you chose.");
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
        }
        return 1;
    }

    private static int jobInfo(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                    "[JOBS+] " + ChatColor.red() + "This command is not done yet.");
            //TODO JOB INFO
        }
        return 1;
    }

    private static int jobStart(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                switch (job) {
                    case ALCHEMIST -> jobStartMethod(player, job, CapType.SELCTOR_ALCHEMIST, handler.getCoins());
                    case BUILDER -> jobStartMethod(player, job, CapType.SELCTOR_BUILDER, handler.getCoins());
                    case DIGGER -> jobStartMethod(player, job, CapType.SELCTOR_DIGGER, handler.getCoins());
                    case ENCHANTER -> jobStartMethod(player, job, CapType.SELCTOR_ENCHANTER, handler.getCoins());
                    case FARMER -> jobStartMethod(player, job, CapType.SELCTOR_FARMER, handler.getCoins());
                    case FISHERMAN -> jobStartMethod(player, job, CapType.SELCTOR_FISHERMAN, handler.getCoins());
                    case HUNTER -> jobStartMethod(player, job, CapType.SELCTOR_HUNTER, handler.getCoins());
                    case LUMBERJACK -> jobStartMethod(player, job, CapType.SELCTOR_LUMBERJACK, handler.getCoins());
                    case MINER -> jobStartMethod(player, job, CapType.SELCTOR_MINER, handler.getCoins());
                    case SMITH -> jobStartMethod(player, job, CapType.SELCTOR_SMITH, handler.getCoins());
                }
            });
        }
        return 1;
    }

    private static int forceJobStart(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            if (!JobGetters.jobIsEnabled(player, job)) {
                if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                    JobSetters.setLevel(job, player, 1);
                    if (JobGetters.getAmountOfEnabledJobs(player) == 1) JobSetters.setDisplay(player, job.get());
                } else {
                    if (JobGetters.getCoins(player) < 10) {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                    } else {
                        JobSetters.setLevel(job, player, 1);
                        JobSetters.removeCoins(player, 10);
                    }
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You are already performing this job.");
            }
        }
        return 1;
    }

    private static int jobStop(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                switch (job) {
                    case ALCHEMIST -> jobStopMethod(player, job, CapType.SELCTOR_ALCHEMIST, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case BUILDER -> jobStopMethod(player, job, CapType.SELCTOR_BUILDER, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case DIGGER -> jobStopMethod(player, job, CapType.SELCTOR_DIGGER, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case ENCHANTER -> jobStopMethod(player, job, CapType.SELCTOR_ENCHANTER, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case FARMER -> jobStopMethod(player, job, CapType.SELCTOR_FARMER, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case FISHERMAN -> jobStopMethod(player, job, CapType.SELCTOR_FISHERMAN, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case HUNTER -> jobStopMethod(player, job, CapType.SELCTOR_HUNTER, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case LUMBERJACK -> jobStopMethod(player, job, CapType.SELCTOR_LUMBERJACK, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case MINER -> jobStopMethod(player, job, CapType.SELCTOR_MINER, handler.getCoins(), JobGetters.getJobLevel(player, job));
                    case SMITH -> jobStopMethod(player, job, CapType.SELCTOR_SMITH, handler.getCoins(), JobGetters.getJobLevel(player, job));
                }
            });
        }
        return 1;
    }

    private static int forceJobStop(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (JobGetters.getJobLevel(player, job) == 1) {
                    JobSetters.setLevel(job, player, 0);
                    if (JobGetters.getAmountOfEnabledJobs(player) == 0) {
                        JobSetters.setDisplay(player, -1);
                    }
                } else {
                    if (JobGetters.getCoins(player) >= 5) {
                        JobSetters.setLevel(job, player, 0);
                        JobSetters.removeCoins(player, 5);
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You need 5 job-coins to stop a job.");
                    }
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You are not performing this job.");
            }
        }
        return 1;
    }

    private static int jobPowerUps(CommandSourceStack source, Jobs job, int powerUp) {
        if (source.getEntity() instanceof Player player) {
            powerUp = powerUp + 1;
            if (JobGetters.jobIsEnabled(player, job)) {
                if (!JobGetters.hasEnabledPowerup(player, job, powerUp)) {
                    if (!(JobGetters.getCoins(player) < 10)) {
                        JobSetters.addPowerUp(job, player, powerUp);
                        JobSetters.removeCoins(player, 10);
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You need 10 job-coins to buy a power-up.");
                    }
                } else {
                    ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                            "[JOBS+] " + ChatColor.red() + "You have already bought this power-up.");
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You are not performing this job.");
            }
        }
        return 1;
    }

    private static int jobCrafting(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                    "[JOBS+] " + ChatColor.red() + "This command is not done yet.");
            //TODO JOB CRAFTING
        }
        return 1;
    }

    private static int jobDisplay(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                        "[JOBS+] " + ChatColor.green() + "Your chat and tab display has been changed to " + ChatHandler.capitalizeWord(job.toString()));
                JobSetters.setDisplay(player, job.get());
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You are not performing this job.");
            }
        }
        return 1;
    }

    private static int setLevelHelp(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET LEVEL"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job admin set level [player] [job] [level]\n\n " +
                    "This command will set the level of a certain job.\n\n" +
                    "Note: This does not add job coins automatically!");
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
        }
        return 1;
    }

    private static int setEXPHelp(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET EXP"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job admin set exp [player] [job] [exp]\n\n " +
                    "This command will set the exp of a certain job.\n\n" +
                    "Note: You cannot go higher than the max EXP of the job-level!");
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
        }
        return 1;
    }

    private static int setCoinsHelp(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET COINS"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job admin set coins [player] [coins]\n\n " +
                    "This command will set the job coins of a player.");
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
        }
        return 1;
    }

    private static int setDisplayHelp(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET DISPLAY"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Usage: " +
                    ChatColor.green() + "/job admin set display [player] [job]\n\n " +
                    "This command will set the custom job prefix in tab and chat.");
            ChatHandler.sendMessage(player, ChatHandler.footer(11));
        }
        return 1;
    }

    private static int setLevel(CommandSourceStack source, Player target, Jobs job, int level) {
        if (source.getEntity() instanceof Player player) {
            if (level == 0) JobSetters.set(job, player, 0, 0, 0, 0, 0);
            ChatHandler.sendMessage(player, ChatHandler.header("SET LEVEL"));
            ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s " +
                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "-level to " + level);
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
            JobSetters.setLevel(job, target, level);

        }
        return 1;
    }

    private static int setEXP(CommandSourceStack source, Player target, Jobs job, int exp) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.getJobLevel(player, job) == 0) {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "Cannot set job-EXP because the job-level is 0.");
                return 1;
            }
            int maxJobEXP = LevelHandler.calcExp(JobGetters.getJobLevel(target, job)) - 1;
            if (exp > maxJobEXP) {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "The exp cannot be higher than the " +
                        "max job-exp for this level. Max: " + maxJobEXP + ".");
                return 1;
            }
            if (exp < 0) {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "The exp cannot be negative.");
                return 1;
            }
            ChatHandler.sendMessage(player, ChatHandler.header("SET EXP"));
            ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s " +
                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "-EXP to " + exp);
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
            JobSetters.setEXP(job, target, exp);
        }
        return 1;
    }

    private static int setCoins(CommandSourceStack source, Player target, int coins) {
        if (source.getEntity() instanceof Player player) {
            if (coins < 0) {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "The coins cannot be negative.");
                return 1;
            }
            target.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                ChatHandler.sendMessage(player, ChatHandler.header("SET COINS"));
                ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() +
                        "s job-coins to " + coins);
                ChatHandler.sendMessage(player, ChatHandler.footer(9));
                handler.setCoins(coins);
            });
        }
        return 1;
    }

    private static int setDisplay(CommandSourceStack source, Player target, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(target, job)) {
                ChatHandler.sendMessage(player, ChatHandler.header("SET DISPLAY"));
                ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() +
                        "s display prefix to " + ChatHandler.capitalizeWord(job.toString()));
                ChatHandler.sendMessage(player, ChatHandler.footer(11));
                JobSetters.setDisplay(target, job.get());
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + target.getScoreboardName() + " is not performing this job.");
            }
        }
        return 1;
    }

    private static int adminSet(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("HELP"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Available commands: \n\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin set " + ChatColor.green() + "coins [player] [coins]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin set " + ChatColor.green() + "exp [player] [job] [exp]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin set " + ChatColor.green() + "level [player] [job] [level]\n");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int admin(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("HELP"));
            ChatHandler.sendMessage(player, ChatColor.darkGreen() + "Available commands: \n\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin " + ChatColor.green() + "debug [player]\n\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin set " + ChatColor.green() + "coins [player] [coins]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin set " + ChatColor.green() + "exp [player] [job] [exp]\n" +
                    ChatColor.darkGray() + " - " + ChatColor.darkGreen() + "/job admin set " + ChatColor.green() + "level [player] [job] [level]\n");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int debug(CommandSourceStack source, Player target) {
        if (source.getEntity() instanceof Player player) {
            if (target == null) target = player;
            Player finalTarget = target;
            target.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> finalTarget.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler2 -> {
                ChatHandler.sendMessage(player, ChatHandler.header("DEBUG"));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Alchemist      " + ChatColor.green() + Arrays.toString(handler.getAlchemist()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Builder        " + ChatColor.green() + Arrays.toString(handler.getBuilder()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Digger         " + ChatColor.green() + Arrays.toString(handler.getDigger()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Enchanter    " + ChatColor.green() + Arrays.toString(handler.getEnchanter()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Farmer        " + ChatColor.green() + Arrays.toString(handler.getFarmer()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Fisherman    " + ChatColor.green() + Arrays.toString(handler.getFisherman()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Hunter        " + ChatColor.green() + Arrays.toString(handler.getHunter()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Lumberjack  " + ChatColor.green() + Arrays.toString(handler.getLumberjack()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Miner          " + ChatColor.green() + Arrays.toString(handler.getMiner()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Smith           " + ChatColor.green() + Arrays.toString(handler.getSmith()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Coins          " + ChatColor.green() + handler.getCoins());
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Verification  " + ChatColor.green() + Arrays.toString(handler.getVerification()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Selector      " + ChatColor.green() + Arrays.toString(handler.getSelector()));
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Display        " + ChatColor.green() + handler.getDisplay());
                ChatHandler.sendMessage(player, ChatColor.darkGray() + " - " + ChatColor.darkGreen() +
                        "Superpower  " + ChatColor.green() + Arrays.toString(handler2.getSuperpower()));
                ChatHandler.sendMessage(player, ChatHandler.footer(5));
            }));
        }
        return 1;
    }

    private static void jobStartMethod(Player player, Jobs job, CapType capType, int coins) {
        if (!JobGetters.jobIsEnabled(player, job)) {
            if (JobGetters.noVerificationEnabled(player)) {
                if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                    /* Free job */
                    JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                    JobSetters.setSelector(player, capType.get());
                    if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                "[JOBS+] " + ChatColor.green() + "Are you sure you want to become an " +
                                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? Say yes in chat.");
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                "[JOBS+] " + ChatColor.green() + "Are you sure you want to become a " +
                                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? Say yes in chat.");
                    }
                    /* Has to pay for job */
                } else {
                    if (coins < 10) {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                    } else {
                        JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                        JobSetters.setSelector(player, capType.get());
                        if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                            ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                    "[JOBS+] " + ChatColor.green() + "Are you sure you want to become an " +
                                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? This will cost 10 job-coins. Say yes in chat.");
                        } else {
                            ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                    "[JOBS+] " + ChatColor.green() + "Are you sure you want to become a " +
                                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? This will cost 10 job-coins. Say yes in chat.");
                        }
                    }
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You are already performing another operation.");
            }
        } else {
            ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                    "[JOBS+] " + ChatColor.red() + "You are already performing this job.");
        }
    }

    private static void jobStopMethod(Player player, Jobs job, CapType capType, int coins, int level) {
        if (JobGetters.jobIsEnabled(player, job)) {
            if (JobGetters.noVerificationEnabled(player)) {
                if (level == 1) {
                    JobSetters.setVerification(player, CapType.STOP_VERIFICATION_FREE.get());
                    JobSetters.setSelector(player, capType.get());
                    if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                "[JOBS+] " + ChatColor.green() + "Are you sure you want to  stop being an " +
                                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? Say yes in chat.");
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                "[JOBS+] " + ChatColor.green() + "Are you sure you want to  stop being a " +
                                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? Say yes in chat.");
                    }
                } else {
                    if (coins >= 5) {
                        JobSetters.setVerification(player, CapType.STOP_VERIFICATION_PAID.get());
                        JobSetters.setSelector(player, capType.get());
                        if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                            ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                    "[JOBS+] " + ChatColor.green() + "Are you sure you want to  stop being an " +
                                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? This will cost 5 job-coins. Say yes in chat.");
                        } else {
                            ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() +
                                    "[JOBS+] " + ChatColor.green() + "Are you sure you want to  stop being a " +
                                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? This will cost 5 job-coins. Say yes in chat.");
                        }
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You need 5 job-coins to stop a job.");
                    }
                }
            } else {
                ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                        "[JOBS+] " + ChatColor.red() + "You are already performing another operation.");
            }
        } else {
            ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                    "[JOBS+] " + ChatColor.red() + "You are not performing this job.");
        }
    }
}
