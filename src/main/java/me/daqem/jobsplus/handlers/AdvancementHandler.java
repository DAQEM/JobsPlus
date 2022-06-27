package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementHandler {

    public static void grand(ServerPlayer serverPlayer, String id) {
        final Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(JobsPlus.getId(id));
        AdvancementProgress advancementprogress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        for (String s : advancementprogress.getRemainingCriteria()) {
            serverPlayer.getAdvancements().award(advancement, s);
        }
    }

    public static void revoke(ServerPlayer serverPlayer, String id) {
        final Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(JobsPlus.getId(id));
        AdvancementProgress advancementprogress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        for (String s : advancementprogress.getCompletedCriteria()) {
            serverPlayer.getAdvancements().revoke(advancement, s);
        }
    }
}
