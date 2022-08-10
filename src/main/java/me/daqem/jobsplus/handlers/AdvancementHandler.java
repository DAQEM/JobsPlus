package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdvancementHandler {

    public static boolean isDone(ServerPlayer serverPlayer, String id) {
        final Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(JobsPlus.getId("jobsplus/" + id));
        return serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
    }

    public static void grand(ServerPlayer serverPlayer, String id) {
        final Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(JobsPlus.getId("jobsplus/" + id));
        AdvancementProgress advancementProgress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        for (String s : advancementProgress.getRemainingCriteria()) {
            serverPlayer.getAdvancements().award(advancement, s);
        }
    }

    public static void revoke(ServerPlayer serverPlayer, String id) {
        Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(JobsPlus.getId("jobsplus/" + id));
        AdvancementProgress advancementProgress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        for (String s : advancementProgress.getCompletedCriteria()) {
            serverPlayer.getAdvancements().revoke(advancement, s);
        }

        List<Advancement> advancements = new ArrayList<>();
        addChildren(advancement, advancements);

        for (Advancement child : advancements) {
            AdvancementProgress advancementProgressChild = serverPlayer.getAdvancements().getOrStartProgress(child);
            if (advancementProgressChild.isDone()) {
                for (String s : advancementProgressChild.getCompletedCriteria()) {
                    serverPlayer.getAdvancements().revoke(child, s);
                }
            }
        }
    }

    private static void addChildren(Advancement advancement, List<Advancement> advancements) {
        for (Advancement child : advancement.getChildren()) {
            advancements.add(child);
            addChildren(child, advancements);
        }

    }

    public static void grandJobAdvancement(ServerPlayer player, Jobs job, String str) {
        if (job == Jobs.ALCHEMIST) AdvancementHandler.grand(player, "alchemist" + str);
        else if (job == Jobs.BUILDER) AdvancementHandler.grand(player, "builder" + str);
        else if (job == Jobs.DIGGER) AdvancementHandler.grand(player, "digger" + str);
        else if (job == Jobs.ENCHANTER) AdvancementHandler.grand(player, "enchanter" + str);
        else if (job == Jobs.FARMER) AdvancementHandler.grand(player, "farmer" + str);
        else if (job == Jobs.FISHERMAN) AdvancementHandler.grand(player, "fisherman" + str);
        else if (job == Jobs.HUNTER) AdvancementHandler.grand(player, "hunter" + str);
        else if (job == Jobs.LUMBERJACK) AdvancementHandler.grand(player, "lumberjack" + str);
        else if (job == Jobs.MINER) AdvancementHandler.grand(player, "miner" + str);
        else if (job == Jobs.SMITH) AdvancementHandler.grand(player, "smith" + str);
    }

    public static void revokeAdvancement(ServerPlayer player, Jobs job) {
        if (job == Jobs.ALCHEMIST) AdvancementHandler.revoke(player, "alchemist");
        else if (job == Jobs.BUILDER) AdvancementHandler.revoke(player, "builder");
        else if (job == Jobs.DIGGER) AdvancementHandler.revoke(player, "digger");
        else if (job == Jobs.ENCHANTER) AdvancementHandler.revoke(player, "enchanter");
        else if (job == Jobs.FARMER) AdvancementHandler.revoke(player, "farmer");
        else if (job == Jobs.FISHERMAN) AdvancementHandler.revoke(player, "fisherman");
        else if (job == Jobs.HUNTER) AdvancementHandler.revoke(player, "hunter");
        else if (job == Jobs.LUMBERJACK) AdvancementHandler.revoke(player, "lumberjack");
        else if (job == Jobs.MINER) AdvancementHandler.revoke(player, "miner");
        else if (job == Jobs.SMITH) AdvancementHandler.revoke(player, "smith");
    }
}
