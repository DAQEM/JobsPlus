package me.daqem.jobsplus.command;

import com.mojang.brigadier.CommandDispatcher;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.packet.PacketSendMainMenuData;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class JobsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jobs")
                .executes(context -> jobs(context.getSource()))
                .then(Commands.literal("menu")
                        .executes(context -> menu(context.getSource()))));
    }

    public static int jobs(CommandSourceStack source) {
        try {
            if (source.getEntity() instanceof Player player) {
                player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                    List<Jobs> enabledJobs = new ArrayList<>();
                    ChatHandler.sendMessage(player, (ChatHandler.header("JOBS")));
                    for (Jobs job : Jobs.values()) {
                        if (JobGetters.jobIsEnabled(player, job)) {
                            enabledJobs.add(job);
                        }
                    }
                    if (!enabledJobs.isEmpty()) {
                        ChatHandler.sendMessage(player, (ChatColor.green() + "Currently Performing Jobs:"));

                        for (Jobs job : enabledJobs) {
                            switch (job) {
                                case ALCHEMIST -> ChatHandler.sendMessage(player, enabledJobString("Alchemist      ", handler.getAlchemist(), JobGetters.getJobLevel(player, job)));
                                case BUILDER -> ChatHandler.sendMessage(player, enabledJobString("Builder        ", handler.getBuilder(), JobGetters.getJobLevel(player, job)));
                                case DIGGER -> ChatHandler.sendMessage(player, enabledJobString("Digger         ", handler.getDigger(), JobGetters.getJobLevel(player, job)));
                                case ENCHANTER -> ChatHandler.sendMessage(player, enabledJobString("Enchanter    ", handler.getEnchanter(), JobGetters.getJobLevel(player, job)));
                                case FARMER -> ChatHandler.sendMessage(player, enabledJobString("Farmer        ", handler.getFarmer(), JobGetters.getJobLevel(player, job)));
                                case FISHERMAN -> ChatHandler.sendMessage(player, enabledJobString("Fisherman    ", handler.getFisherman(), JobGetters.getJobLevel(player, job)));
                                case HUNTER -> ChatHandler.sendMessage(player, enabledJobString("Hunter        ", handler.getHunter(), JobGetters.getJobLevel(player, job)));
                                case LUMBERJACK -> ChatHandler.sendMessage(player, enabledJobString("Lumberjack  ", handler.getLumberjack(), JobGetters.getJobLevel(player, job)));
                                case MINER -> ChatHandler.sendMessage(player, enabledJobString("Miner          ", handler.getMiner(), JobGetters.getJobLevel(player, job)));
                                case SMITH -> ChatHandler.sendMessage(player, enabledJobString("Smith           ", handler.getSmith(), JobGetters.getJobLevel(player, job)));
                            }
                        }
                    }
                    if (enabledJobs.size() != 10) {
                        ChatHandler.sendMessage(player, ChatColor.green() + "\nAvailable Jobs:");
                        StringBuilder availableJobs = new StringBuilder();
                        for (Jobs job : Jobs.values()) {
                            if (!enabledJobs.contains(job)) {
                                if (availableJobs.isEmpty()) {
                                    availableJobs.append(" ").append(ChatHandler.capitalizeWord(job.toString().toLowerCase()));
                                } else {
                                    availableJobs.append(", ").append(ChatHandler.capitalizeWord(job.toString().toLowerCase()));
                                }
                            }
                        }
                        ChatHandler.sendMessage(player, ChatColor.gray() + availableJobs);
                    }
                });
                ChatHandler.sendMessage(player, ChatHandler.footer(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int menu(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketSendMainMenuData(new int[]{
                    JobGetters.getJobLevel(player, Jobs.ALCHEMIST), JobGetters.getJobEXP(player, Jobs.ALCHEMIST),
                    JobGetters.getJobLevel(player, Jobs.BUILDER), JobGetters.getJobEXP(player, Jobs.BUILDER),
                    JobGetters.getJobLevel(player, Jobs.DIGGER), JobGetters.getJobEXP(player, Jobs.DIGGER),
                    JobGetters.getJobLevel(player, Jobs.FARMER), JobGetters.getJobEXP(player, Jobs.FARMER),
                    JobGetters.getJobLevel(player, Jobs.FISHERMAN), JobGetters.getJobEXP(player, Jobs.FISHERMAN),
                    JobGetters.getJobLevel(player, Jobs.ENCHANTER), JobGetters.getJobEXP(player, Jobs.ENCHANTER),
                    JobGetters.getJobLevel(player, Jobs.HUNTER), JobGetters.getJobEXP(player, Jobs.HUNTER),
                    JobGetters.getJobLevel(player, Jobs.LUMBERJACK), JobGetters.getJobEXP(player, Jobs.LUMBERJACK),
                    JobGetters.getJobLevel(player, Jobs.MINER), JobGetters.getJobEXP(player, Jobs.MINER),
                    JobGetters.getJobLevel(player, Jobs.SMITH), JobGetters.getJobEXP(player, Jobs.SMITH),
                    JobGetters.getCoins(player), JobGetters.getAmountOfEnabledJobs(player)}));
        }
        return 1;
    }

    private static String enabledJobString(String job, int[] jobInfo, int level) {
        if (level == 100) {
            return ChatColor.darkGray() + " - " + ChatColor.darkGreen() + job +
                    ChatColor.darkGray() + "[" + ChatColor.green() + "LVL " + jobInfo[CapType.LEVEL.get()] +
                    ChatColor.darkGray() + "] [" + ChatColor.green() + "MAX LEVEL" +
                    ChatColor.darkGray() + "]";
        }
        return ChatColor.darkGray() + " - " + ChatColor.darkGreen() + job +
                ChatColor.darkGray() + "[" + ChatColor.green() + "LVL " + jobInfo[CapType.LEVEL.get()] +
                ChatColor.darkGray() + "] [" + ChatColor.green() + "EXP " + jobInfo[CapType.EXP.get()] +
                ChatColor.darkGray() + "/" + ChatColor.darkGreen() + LevelHandler.calcExp(jobInfo[CapType.LEVEL.get()]) +
                ChatColor.darkGray() + "]";
    }
}
