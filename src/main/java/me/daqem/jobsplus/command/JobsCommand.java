package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.Capitalizer;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.LevelFormulaHandler;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class JobsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jobs").executes(context -> jobs(context.getSource())));
    }

    public static int jobs(CommandSourceStack source) {
        try {
            if (source.getEntity() instanceof Player player) {
                player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                    List<Jobs> enabledJobs = new ArrayList<>();
                    player.sendMessage(ChatHandler.header("JOBS"), player.getUUID());
                    for (Jobs job : Jobs.values()) {
                        switch (job) {
                            case ALCHEMIST -> {
                                if (handler.getAlchemist()[0] == 1) enabledJobs.add(job);
                            }
                            case BUILDER -> {
                                if (handler.getBuilder()[0] == 1) enabledJobs.add(job);
                            }
                            case BUTCHER -> {
                                if (handler.getButcher()[0] == 1) enabledJobs.add(job);
                            }
                            case CRAFTSMAN -> {
                                if (handler.getCraftsman()[0] == 1) enabledJobs.add(job);
                            }
                            case DIGGER -> {
                                if (handler.getDigger()[0] == 1) enabledJobs.add(job);
                            }
                            case ENCHANTER -> {
                                if (handler.getEnchanter()[0] == 1) enabledJobs.add(job);
                            }
                            case FARMER -> {
                                if (handler.getFarmer()[0] == 1) enabledJobs.add(job);
                            }
                            case FISHERMAN -> {
                                if (handler.getFisherman()[0] == 1) enabledJobs.add(job);
                            }
                            case HUNTER -> {
                                if (handler.getHunter()[0] == 1) enabledJobs.add(job);
                            }
                            case LUMBERJACK -> {
                                if (handler.getLumberjack()[0] == 1) enabledJobs.add(job);
                            }
                            case MINER -> {
                                if (handler.getMiner()[0] == 1) enabledJobs.add(job);
                            }
                            case SMITH -> {
                                if (handler.getSmith()[0] == 1) enabledJobs.add(job);
                            }
                        }
                    }
                    if (!enabledJobs.isEmpty()) {
                        player.sendMessage(new KeybindComponent(ChatFormatting.GREEN + "Currently Performing Jobs:"), player.getUUID());

                        for (Jobs job : enabledJobs) {
                            switch (job) {
                                case ALCHEMIST -> player.sendMessage(enabledJobString("Alchemist      ", handler.getAlchemist()), player.getUUID());
                                case BUILDER -> player.sendMessage(enabledJobString("Builder        ", handler.getBuilder()), player.getUUID());
                                case BUTCHER -> player.sendMessage(enabledJobString("Butcher       ", handler.getButcher()), player.getUUID());
                                case CRAFTSMAN -> player.sendMessage(enabledJobString("Craftsman    ", handler.getCraftsman()), player.getUUID());
                                case DIGGER -> player.sendMessage(enabledJobString("Digger         ", handler.getDigger()), player.getUUID());
                                case ENCHANTER -> player.sendMessage(enabledJobString("Enchanter    ", handler.getEnchanter()), player.getUUID());
                                case FARMER -> player.sendMessage(enabledJobString("Farmer        ", handler.getFarmer()), player.getUUID());
                                case FISHERMAN -> player.sendMessage(enabledJobString("Fisherman    ", handler.getFisherman()), player.getUUID());
                                case HUNTER -> player.sendMessage(enabledJobString("Hunter        ", handler.getHunter()), player.getUUID());
                                case LUMBERJACK -> player.sendMessage(enabledJobString("Lumberjack  ", handler.getLumberjack()), player.getUUID());
                                case MINER -> player.sendMessage(enabledJobString("Miner          ", handler.getMiner()), player.getUUID());
                                case SMITH -> player.sendMessage(enabledJobString("Smith           ", handler.getSmith()), player.getUUID());
                            }
                        }
                        player.sendMessage(new KeybindComponent(""), player.getUUID());
                    }
                    if (enabledJobs.size() != 12) {
                        player.sendMessage(new KeybindComponent(ChatFormatting.GREEN + "Available Jobs:"), player.getUUID());
                        StringBuilder availableJobs = new StringBuilder();
                        for (Jobs job : Jobs.values()) {
                            if (!enabledJobs.contains(job)) {
                                if (availableJobs.isEmpty()) {
                                    availableJobs.append(" ").append(Capitalizer.capitalizeWord(job.toString().toLowerCase()));
                                } else {
                                    availableJobs.append(", ").append(Capitalizer.capitalizeWord(job.toString().toLowerCase()));
                                }
                            }
                        }
                        player.sendMessage(new KeybindComponent(ChatFormatting.GRAY + availableJobs.toString() + "\n"), player.getUUID());
                    }
                });
                player.sendMessage(ChatHandler.footer(4), player.getUUID());
            }
            //TODO JOBS
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static Component enabledJobString(String job, int[] jobInfo) {
        return new KeybindComponent(ChatFormatting.DARK_GRAY + " - " + ChatFormatting.DARK_GREEN + job +
                ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GREEN + "LVL " + jobInfo[1] +
                ChatFormatting.DARK_GRAY + "] [" + ChatFormatting.GREEN + "EXP " + jobInfo[2] +
                ChatFormatting.DARK_GRAY + "/" + ChatFormatting.DARK_GREEN + LevelFormulaHandler.calcExp(jobInfo[1]) +
                ChatFormatting.DARK_GRAY + "]");
    }
}
