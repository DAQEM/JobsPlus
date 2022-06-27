package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.handlers.BossBarHandler;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;

import java.util.Arrays;

public class JobCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
                .literal("job")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("debug")
                        .then(Commands.argument("target", EntityArgument.players())
                                .executes(context -> debug(
                                        context.getSource(),
                                        EntityArgument.getPlayer(context, "target"))))
                        .executes(context -> debug(
                                context.getSource(),
                                null)))
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
                )
        );
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
            if (level == 0) JobSetters.set(job, target, 0, 0, 0, 0, 0);
            ChatHandler.sendMessage(player, ChatHandler.header("SET LEVEL"));
            ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s " +
                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "-level to " + level);
            ChatHandler.sendMessage(player, ChatHandler.footer(9));
            JobSetters.setLevel(job, target, level);
            BossBarHandler.updateBossBar(target);
        }
        return 1;
    }

    private static int setEXP(CommandSourceStack source, Player target, Jobs job, int exp) {
        if (source.getEntity() instanceof Player player) {
            if (JobGetters.getJobLevel(target, job) == 0) {
                ChatHandler.sendMessage(player, new TranslatableComponent("error.exp.level_is_0", ChatColor.boldDarkRed(), ChatColor.red()).getString());
                return 1;
            }
            int maxJobEXP = LevelHandler.calcExp(JobGetters.getJobLevel(target, job)) - 1;
            if (exp > maxJobEXP) {
                ChatHandler.sendMessage(player, new TranslatableComponent("error.exp.cannot_be_higher_than_max", ChatColor.boldDarkRed(), ChatColor.red(), maxJobEXP).getString());
                return 1;
            }
            if (exp < 0) {
                ChatHandler.sendMessage(player, new TranslatableComponent("error.exp.cannot_be_negative", ChatColor.boldDarkRed(), ChatColor.red()).getString());

                return 1;
            }
            ChatHandler.sendMessage(player, ChatHandler.header("SET EXP"));
            ChatHandler.sendMessage(player, ChatColor.green() + "Set " + target.getScoreboardName() + "s " +
                    ChatHandler.capitalizeWord(job.toString().toLowerCase()) + "-EXP to " + exp);
            ChatHandler.sendMessage(player, ChatHandler.footer(7));
            JobSetters.setEXP(job, target, exp);
            BossBarHandler.updateBossBar(target);
        }
        return 1;
    }

    private static int setCoins(CommandSourceStack source, Player target, int coins) {
        if (source.getEntity() instanceof Player player) {
            if (coins < 0) {
                ChatHandler.sendMessage(player, new TranslatableComponent("error.coins.cannot_be_negative", ChatColor.boldDarkRed(), ChatColor.red()).getString());
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
                ChatHandler.sendMessage(player, new TranslatableComponent("error.job.target_not_performing", ChatColor.boldDarkRed(), ChatColor.red(), target.getScoreboardName()).getString());
            }
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
}
