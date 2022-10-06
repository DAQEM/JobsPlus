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
        AdvancementHandler.grand(player, job.name().toLowerCase() + str);
    }

    public static void revokeAdvancement(ServerPlayer player, Jobs job) {
        AdvancementHandler.revoke(player, job.name().toLowerCase());
    }
}
