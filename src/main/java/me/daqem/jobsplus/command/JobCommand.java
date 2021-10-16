package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.LevelFormulaHandler;
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

import java.util.Arrays;
import java.util.Objects;

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
                                                .then(Commands.argument("job", EnumArgument.enumArgument(Jobs.class))
                                                        .then(Commands.argument("level", IntegerArgumentType.integer())
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
                    case ALCHEMIST -> jobStartMethod(player, job, CapType.SELCTOR_ALCHEMIST, handler.getCoins());
                    case BUILDER -> jobStartMethod(player, job, CapType.SELCTOR_BUILDER, handler.getCoins());
                    case BUTCHER -> jobStartMethod(player, job, CapType.SELCTOR_BUTCHER, handler.getCoins());
                    case CRAFTSMAN -> jobStartMethod(player, job, CapType.SELCTOR_CRAFTSMAN, handler.getCoins());
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

    private static int jobStop(CommandSourceStack source, Jobs job) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, "name: " + player.getScoreboardName() + " job argument: " + job);
            //TODO JOB STOP
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
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET LEVEL"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job admin set level [player] [job] [level]\n\n " +
                    "This command will set the level of a certain job.\n\n" +
                    "Note: This does not add job coins automatically!");
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
        }
        return 1;
    }

    private static int setEXPHelp(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET EXP"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job admin set exp [player] [job] [exp]\n\n " +
                    "This command will set the exp of a certain job.\n\n" +
                    "Note: You cannot go higher than the max EXP of the job-level!");
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
        }
        return 1;
    }

    private static int setCoinsHelp(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("SET COINS"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Usage: " +
                    ChatFormatting.GREEN + "/job admin set coins [player] [coins]\n\n " +
                    "This command will set the job coins of a player.");
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
        }
        return 1;
    }

    private static int setLevel(CommandSourceStack source, Player target, Jobs job, int level) {
        if (source.getEntity() instanceof Player player) {
            if (level > 100 || level < 0) {
                if (level > 100) {
                    ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "A job-level cannot be higher than 100.");
                } else {
                    ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "A job-level cannot be negative.");
                }
                return 1;
            }
            if (level == 0) JobSetters.set(job, player, 0, 0, 0, 0, 0);
            ChatHandler.sendMessage(player, ChatHandler.header("SET LEVEL"));
            ChatHandler.sendMessage(player, ChatFormatting.GREEN + "Set " + target.getScoreboardName() + "s " +
                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "-level to " + level);
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
            JobSetters.setLevel(job, player, level);

        }
        return 1;
    }

    private static int setEXP(CommandSourceStack source, Player target, Jobs job, int exp) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.getJobLevel(player, job) == 0) {
                ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "Cannot set job-EXP because the job-level is 0.");
                return 1;
            }
            int maxJobEXP = LevelFormulaHandler.calcExp(JobGetters.getJobLevel(target, job)) - 1;
            if (exp > maxJobEXP) {
                ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "The exp cannot be higher than the " +
                        "max job-exp for this level. Max: " + maxJobEXP + ".");
                return 1;
            }
            if (exp < 0) {
                ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "The exp cannot be negative.");
                return 1;
            }
            ChatHandler.sendMessage(player, ChatHandler.header("SET EXP"));
            ChatHandler.sendMessage(player, ChatFormatting.GREEN + "Set " + target.getScoreboardName() + "s " +
                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "-EXP to " + exp);
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
            JobSetters.setEXP(job, player, exp);
        }
        return 1;
    }

    private static int setCoins(CommandSourceStack source, Player target, int coins) {
        if (source.getEntity() instanceof Player player) {
            if (coins < 0) {
                ChatHandler.sendMessage(player, ChatFormatting.BOLD + "" + ChatFormatting.DARK_RED +
                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "The coins cannot be negative.");
                return 1;
            }
            target.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                ChatHandler.sendMessage(player, ChatHandler.header("SET COINS"));
                ChatHandler.sendMessage(player, ChatFormatting.GREEN + "Set " + target.getScoreboardName() +
                        "s job-coins to " + coins);
                ChatHandler.sendMessage(player, ChatHandler.footer(9));
                handler.setCoins(coins);
            });
        }
        return 1;
    }

    private static int adminSet(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("HELP"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Available commands: \n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin set " + ChatFormatting.GREEN + "coins [player] [coins]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin set " + ChatFormatting.GREEN + "exp [player] [job] [exp]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin set " + ChatFormatting.GREEN + "level [player] [job] [level]\n");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int admin(CommandSourceStack source) {
        if (source.getEntity() instanceof Player player) {
            ChatHandler.sendMessage(player, ChatHandler.header("HELP"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "Available commands: \n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin " + ChatFormatting.GREEN + "debug [player]\n\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin set " + ChatFormatting.GREEN + "coins [player] [coins]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin set " + ChatFormatting.GREEN + "exp [player] [job] [exp]\n" +
                    ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + "/job admin set " + ChatFormatting.GREEN + "level [player] [job] [level]\n");
            ChatHandler.sendMessage(player, ChatHandler.footer(4));
        }
        return 1;
    }

    private static int debug(CommandSourceStack source, Player target) {
        if (source.getEntity() instanceof Player player) {
            debugText(player, Objects.requireNonNullElse(target, player));
        }
        return 1;
    }

    private static void jobStartMethod(Player player, Jobs job, CapType capType, int coins) {
        if (getJobIsNotEnabled(player, job)) {
            /* Free job */
            if (JobGetters.getAmountOfEnabledJobs(player) < 2) {
                JobSetters.setVerification(player, CapType.START_VERIFICATION_FREE.get());
                JobSetters.setSelector(player, capType.get());
                jobStartMessageFree(player, job);
                /* Has to pay for job */
            } else {
                if (coins < 10) {
                    ChatHandler.sendMessage(player, "" + ChatFormatting.DARK_RED + ChatFormatting.BOLD +
                            "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You do not have enough job-coins. You need 10 job-coins to start another job.");
                } else {
                    JobSetters.setVerification(player, CapType.START_VERIFICATION_PAID.get());
                    JobSetters.setSelector(player, capType.get());
                    jobStartMessagePaid(player, job);
                }
            }
        }
    }

    public static void debugText(Player player, Player target) {
        target.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            ChatHandler.sendMessage(player, ChatHandler.header("DEBUG"));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Alchemist      " + ChatFormatting.GREEN + Arrays.toString(handler.getAlchemist()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Builder        " + ChatFormatting.GREEN + Arrays.toString(handler.getBuilder()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Butcher       " + ChatFormatting.GREEN + Arrays.toString(handler.getButcher()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Craftsman    " + ChatFormatting.GREEN + Arrays.toString(handler.getCraftsman()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Digger         " + ChatFormatting.GREEN + Arrays.toString(handler.getDigger()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Enchanter    " + ChatFormatting.GREEN + Arrays.toString(handler.getEnchanter()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Farmer        " + ChatFormatting.GREEN + Arrays.toString(handler.getFarmer()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Fisherman    " + ChatFormatting.GREEN + Arrays.toString(handler.getFisherman()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Hunter        " + ChatFormatting.GREEN + Arrays.toString(handler.getHunter()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Lumberjack  " + ChatFormatting.GREEN + Arrays.toString(handler.getLumberjack()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Miner          " + ChatFormatting.GREEN + Arrays.toString(handler.getMiner()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Smith           " + ChatFormatting.GREEN + Arrays.toString(handler.getSmith()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Coins          " + ChatFormatting.GREEN + handler.getCoins());
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Verification  " + ChatFormatting.GREEN + Arrays.toString(handler.getVerification()));
            ChatHandler.sendMessage(player, ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN +
                    "Selector      " + ChatFormatting.GREEN + Arrays.toString(handler.getSelector()));
            ChatHandler.sendMessage(player, ChatHandler.footer(5));
        });
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
}
