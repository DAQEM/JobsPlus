package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class ExpHandler {

    public static void addEXPLowest(Player player, Jobs job) {
        addRandomJobEXP(player, job, 0, 2);
    }

    public static void addEXPLow(Player player, Jobs job) {
        addRandomJobEXP(player, job, 2, 4);
    }

    public static void addEXPMid(Player player, Jobs job) {
        addRandomJobEXP(player, job, 4, 6);
    }

    public static void addEXPHigh(Player player, Jobs job) {
        addRandomJobEXP(player, job, 6, 10);
    }

    public static void addEXPBrewing(Player player, Jobs job) {
        addRandomJobEXP(player, job, 30, 50);
    }

    public static void addEXPOneLapis(Player player, Jobs job) {
        addRandomJobEXP(player, job, 30, 50);
    }

    public static void addEXPTwoLapis(Player player, Jobs job) {
        addRandomJobEXP(player, job, 50, 70);
    }

    public static void addEXPTheeLapis(Player player, Jobs job) {
        addRandomJobEXP(player, job, 70, 100);
    }

    public static int getEXPLowest() {
        return getRandomJobEXP(0, 2);
    }

    public static int getEXPLow() {
        return getRandomJobEXP(2, 4);
    }

    public static int getEXPMid() {
        return getRandomJobEXP(4, 6);
    }

    public static int getEXPHigh() {
        return getRandomJobEXP(6, 10);
    }

    public static int getEXPFishing() {
        return getRandomJobEXP(15, 20);
    }

    public static void addRandomJobEXP(Player player, Jobs job, int lowerBound, int upperBound) {
        int randomNumber = new Random().nextInt(upperBound - lowerBound) + lowerBound;
        addJobEXP(player, job, randomNumber);
    }

    public static int getRandomJobEXP(int lowerBound, int upperBound) {
        return new Random().nextInt(upperBound - lowerBound) + lowerBound;
    }

    public static void addJobEXP(Player player, Jobs job, int exp) {
        if (player.isCreative() && JobGetters.getEXPHotBarSetting(player) == 0) {
            HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.boldRed() + "Gaining job-EXP is disabled in Creative.");
            return;
        }
        if (JobGetters.getJobLevel(player, job) >= 100) return;

        //Job-EXP Multipliers.
        final Double global = Config.GLOBAL_EXP_MULTIPLIER.get();
        if (job == Jobs.ALCHEMIST) exp = (int) ((exp * Config.ALCHEMIST_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.BUILDER) exp = (int) ((exp * Config.BUILDER_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.DIGGER) exp = (int) ((exp * Config.DIGGER_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.ENCHANTER) exp = (int) ((exp * Config.ENCHANTER_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.FARMER) exp = (int) ((exp * Config.FARMER_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.FISHERMAN) exp = (int) ((exp * Config.FISHERMAN_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.HUNTER) exp = (int) ((exp * Config.HUNTER_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.LUMBERJACK) exp = (int) ((exp * Config.LUMBERJACK_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.MINER) exp = (int) ((exp * Config.MINER_EXP_MULTIPLIER.get()) * global);
        if (job == Jobs.SMITH) exp = (int) ((exp * Config.SMITH_EXP_MULTIPLIER.get()) * global);

        JobSetters.addEXP(job, player, exp);
        int maxEXP = LevelHandler.calcExp(JobGetters.getJobLevel(player, job));
        int newEXP = JobGetters.getJobEXP(player, job);
        if (newEXP >= maxEXP) {
            JobSetters.setLevel(job, player, -2);
            JobSetters.setEXP(job, player, newEXP - maxEXP);
            LevelUpHandler.handle(player, job, JobGetters.getJobLevel(player, job));
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (exp != 0 && JobGetters.getEXPHotBarSetting(player) == 0) {
                serverPlayer.sendSystemMessage(JobsPlus.literal(ChatHandler.ColorizedJobName(job) + ChatColor.gray() +
                        " +" + exp + " EXP"), true);
            }
        }
        BossBarHandler.updateBossBar(player);
    }
}
