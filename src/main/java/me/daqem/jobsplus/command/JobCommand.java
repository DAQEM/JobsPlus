package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.CapType;
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
                .then(Commands.literal("admin")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.literal("level")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("coins", IntegerArgumentType.integer())
                                                        .executes(context -> setLevel()))
                                                .executes(context -> setLevelHelp()))
                                        .executes(context -> setLevelHelp()))
                                .then(Commands.literal("exp")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("coins", IntegerArgumentType.integer())
                                                        .executes(context -> setEXP()))
                                                .executes(context -> setEXPHelp()))
                                        .executes(context -> setEXPHelp()))
                                .then(Commands.literal("coins")
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .then(Commands.argument("coins", IntegerArgumentType.integer())
                                                        .executes(context -> setCoins()))
                                                .executes(context -> setCoinsHelp()))
                                        .executes(context -> setCoinsHelp()))
                                .executes(context -> adminSet()))
                        .then(Commands.literal("debug")
                                .then(Commands.argument("target", EntityArgument.players())
                                        .executes(context -> debug(context.getSource(), EntityArgument.getPlayer(context, "target"))))
                                .executes(context -> debug(context.getSource(), null)))
                        .executes(context -> admin(context.getSource())))
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
                         if (getJobIsNotEnabled(player, job)) {
                             /* Free job */
                             if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                 JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                 JobSetters.setSelector(player, CapType.SELCTOR_ALCHEMIST.get());
                                 ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                         "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become an Alchemist? Say yes in chat.");
                             /* Has to pay for job */
                             } else {
                                 if (handler.getCoins() < 10) {
                                     ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                             "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                 } else {
                                     JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                     JobSetters.setSelector(player, CapType.SELCTOR_ALCHEMIST.get());
                                     ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                             "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become an Alchemist? This will cost 10 job-coins. Say yes in chat.");
                                 }
                             }
                         }
                    }
                    case BUILDER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_BUILDER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Builder? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_BUILDER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Builder? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case BUTCHER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_BUTCHER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Butcher? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_BUTCHER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Butcher? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case CRAFTSMAN -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_CRAFTSMAN.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Craftsman? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_CRAFTSMAN.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Craftsman? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case DIGGER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_DIGGER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Digger? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_DIGGER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Digger? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case ENCHANTER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_ENCHANTER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become an Enchanter? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_ENCHANTER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become an Enchanter? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case FARMER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_FARMER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Farmer? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_FARMER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Farmer? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case FISHERMAN -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_FISHERMAN.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Fisherman? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_FISHERMAN.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Fisherman? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case HUNTER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_HUNTER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Hunter? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_HUNTER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Hunter? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case LUMBERJACK -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_LUMBERJACK.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Lumberjack? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_LUMBERJACK.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Lumberjack? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case MINER -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_MINER.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Miner? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_MINER.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Miner? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
                    case SMITH -> {
                        if (getJobIsNotEnabled(player, job)) {
                            /* Free job */
                            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                                JobSetters.setSelector(player, CapType.SELCTOR_SMITH.get());
                                ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Smith? Say yes in chat.");
                                /* Has to pay for job */
                            } else {
                                if (handler.getCoins() < 10) {
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                                } else {
                                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                                    JobSetters.setSelector(player, CapType.SELCTOR_SMITH.get());
                                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD +
                                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become a Smith? This will cost 10 job-coins. Say yes in chat.");
                                }
                            }
                        }
                    }
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

    private static int setLevelHelp(CommandSourceStack source) {
        return 1;
    }

    private static int setEXPHelp(CommandSourceStack source) {
        return 1;
    }

    private static int setCoinsHelp(CommandSourceStack source) {
        return 1;
    }

    private static int setLevel(CommandSourceStack source, Player target, int level) {
        return 1;
    }

    private static int setEXP(CommandSourceStack source, Player target, int exp) {
        return 1;
    }

    private static int setCoins(CommandSourceStack source, Player target, int coins) {
        return 1;
    }

    private static int adminSet(CommandSourceStack source) {
        return 1;
    }

    private static int admin(CommandSourceStack source) {
        return 1;
    }

    private static int debug(CommandSourceStack source, Player target) {
        if (source.getEntity() instanceof Player player) {
            if (target == null) {
                ChatHandler.sendMessage(player, ChatHandler.header("DEBUG"));
                ChatHandler.sendMessage(player, );
                ChatHandler.sendMessage(player, ChatHandler.footer(5));
            } else {
                ChatHandler.sendMessage(player, ChatHandler.header("DEBUG"));
                ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting);
                ChatHandler.sendMessage(player, ChatHandler.footer(5));
            }
        }
        return 1;
    }

    private static boolean getJobIsNotEnabled(Player player, Jobs job) {
        if (JobGetters.getJobIsEnabled(player, job)) {
            ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You have already started this job.");
            return false;
        } else {
            return true;
        }
    }

    private static void jobStartMessageFree(Player player, Jobs job) {
        ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_GREEN +
                "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become an " +
                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? Say yes in chat.");
    }

    private static void jobStartMessagePaid(Player player, Jobs job) {
        ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_GREEN +
                "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "Are you sure you want to become an " +
                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "? This will cost 10 job-coins. Say yes in chat.");
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
