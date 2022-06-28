package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;

public class BossBarHandler {

    public static void updateBossBar(Player player) {
        if (player.getServer() == null) return;

        CustomBossEvents customBossEvents = player.getServer().getCustomBossEvents();

        for (Jobs job : Jobs.values()) {
            CustomBossEvent customBossEvent = customBossEvents.get(JobsPlus.getId(player.getUUID() + "-" + job.get()));
            final int jobLevel = JobGetters.getJobLevel(player, job);
            if (customBossEvent != null) {
                if (jobLevel == 0) {
                    customBossEvent.removeAllPlayers();
                    customBossEvents.remove(customBossEvent);
                    return;
                }
                customBossEvent.setName(Component.literal("(Level: " + jobLevel + ")  " + ChatHandler.ColorizedJobName(job) + " (EXP: " + getEXPPercentage(player, job) + "%)"));
                customBossEvent.setValue(JobGetters.getJobEXP(player, job));
                customBossEvent.setMax(LevelHandler.calcExp(JobGetters.getJobLevel(player, job)));
                if (jobLevel == 100) customBossEvent.setProgress(1F);
            }
        }
    }

    public static void createBossBar(Player player, Jobs job) {
        if (player.getServer() == null) return;

        final CustomBossEvents customBossEvents = player.getServer().getCustomBossEvents();
        final ResourceLocation id = JobsPlus.getId(player.getUUID() + "-" + job.get());
        CustomBossEvent customBossEvent = customBossEvents.get(id);
        removeAllActiveBossBars(player, customBossEvents);

        if (customBossEvent == null) {
            final int jobLevel = JobGetters.getJobLevel(player, job);
            customBossEvent = customBossEvents.create(id, Component.literal("(Level: " + jobLevel + ")  " + ChatHandler.ColorizedJobName(job) + " (EXP: " + getEXPPercentage(player, job) + "%)"));
            customBossEvent.addPlayer((ServerPlayer) player);
            customBossEvent.setMax(LevelHandler.calcExp(jobLevel));
            customBossEvent.setColor(getBossBarColor(job));
            customBossEvent.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
            customBossEvent.setValue(JobGetters.getJobEXP(player, job));
            if (jobLevel == 100) customBossEvent.setProgress(1F);
        }
    }

    public static void removeAllActiveBossBars(Player player, CustomBossEvents customBossEvents) {
        for (Jobs jobs : Jobs.values()) {
            CustomBossEvent customBossEvent = customBossEvents.get(JobsPlus.getId(player.getUUID() + "-" + jobs.get()));
            if (customBossEvent != null) {
                customBossEvent.removeAllPlayers();
                customBossEvents.remove(customBossEvent);
            }
        }
    }

    public static BossEvent.BossBarColor getBossBarColor(Jobs job) {
        return job == Jobs.ALCHEMIST ? BossEvent.BossBarColor.PURPLE : job == Jobs.FARMER || job == Jobs.BUILDER ? BossEvent.BossBarColor.GREEN :
                job == Jobs.DIGGER || job == Jobs.LUMBERJACK ? BossEvent.BossBarColor.YELLOW : job == Jobs.ENCHANTER ? BossEvent.BossBarColor.PINK :
                        job == Jobs.FISHERMAN ? BossEvent.BossBarColor.BLUE : job == Jobs.HUNTER ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.WHITE;
    }

    public static String getEXPPercentage(Player player, Jobs job) {
        int jobLevel = JobGetters.getJobLevel(player, job);

        if (jobLevel == 100) return "100";
        return String.format("%.2f", (double) JobGetters.getJobEXP(player, job) / LevelHandler.calcExp(jobLevel) * 100);
    }

    public static void removeBossBar(Player player, Jobs job) {
        if (player.getServer() == null) return;
        CustomBossEvents customBossEvents = player.getServer().getCustomBossEvents();

        final CustomBossEvent customBossEvent = customBossEvents.get(JobsPlus.getId(player.getUUID() + "-" + job.get()));
        if (customBossEvent != null) {
            customBossEvent.removeAllPlayers();
            customBossEvents.remove(customBossEvent);
        }

    }
}
