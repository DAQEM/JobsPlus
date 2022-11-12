package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.capability.ModCapabilityImpl;
import me.daqem.jobsplus.common.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.handlers.AdvancementHandler;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

public class JobGetters {

    public static boolean jobIsEnabled(Player player, Jobs job) {
        return getJobLevel(player, job) != 0;
    }

    public static int getJobLevel(Player player, Jobs job) {
        int[] array = getJobArray(player, job);
        return array.length == 0 ? 0 : array[CapType.LEVEL.get()];
    }

    public static int getJobEXP(Player player, Jobs job) {
        return getJobArray(player, job)[CapType.EXP.get()];
    }

    public static int getAmountOfEnabledJobs(Player player) {
        int count = 0;
        for (Jobs job : Jobs.values()) {
            if (jobIsEnabled(player, job)) {
                count++;
            }
        }
        return count;
    }

    public static int getDisplay(Player player) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> atomicInteger.set(handler.getDisplay()));
        return atomicInteger.get();
    }

    public static int getCoins(Player player) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> atomicInteger.set(handler.getCoins()));
        return atomicInteger.get();
    }

    public static boolean hasPowerupEnabled(Player player, Jobs job, int powerUp, boolean giveAchievement) {
        if (player.level.isClientSide) return false;
        if (!JobGetters.jobIsEnabled(player, job)) return false;

        boolean hasPowerupEnabled = getJobArray(player, job)[powerUp] == 1;

        if (giveAchievement && hasPowerupEnabled) {
            AdvancementHandler.grandJobAdvancement((ServerPlayer) player, job, "_powerup_" + (powerUp - 1));
        }

        return hasPowerupEnabled;
    }

    public static int getPowerup(Player player, Jobs job, int powerUp) {
        if (powerUp != CapType.POWER_UP1.get() && powerUp != CapType.POWER_UP2.get() && powerUp != CapType.POWER_UP3.get())
            return 0;
        return getJobArray(player, job)[powerUp];
    }

    public static int getSuperPower(Player player, Jobs job) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler -> atomicInteger.set(handler.getSuperpower()[Jobs.getJobInt(job)]));
        return atomicInteger.get();
    }

    public static boolean hasSuperPowerEnabled(Player player, Jobs job, boolean giveAchievement) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        player.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler -> atomicBoolean.set(getSuperPower(player, job) == 0 && JobGetters.getJobLevel(player, job) == 100));

        if (giveAchievement && atomicBoolean.get()) {
            AdvancementHandler.grandJobAdvancement((ServerPlayer) player, job, "_superpower");
        }

        return atomicBoolean.get();
    }

    public static int getActiveBossBar(Player player) {
        if (player.getServer() != null) {
            for (Jobs job : Jobs.values()) {
                if (player.getServer().getCustomBossEvents().get(JobsPlus.getId(player.getUUID() + "-" + job.get())) != null)
                    return job.get();
            }
        }
        return -1;
    }

    public static int[] getAllSettings(Player player) {
        AtomicInteger expHotbarSetting = new AtomicInteger(), levelUpSoundSetting = new AtomicInteger(), levelUpChatSetting = new AtomicInteger(), four = new AtomicInteger(), five = new AtomicInteger();
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            expHotbarSetting.set(handler.getSettings()[0]);
            levelUpSoundSetting.set(handler.getSettings()[1]);
            levelUpChatSetting.set(handler.getSettings()[2]);
            four.set(handler.getSettings()[3]);
            five.set(handler.getSettings()[4]);
        });
        return new int[]{expHotbarSetting.get(), levelUpSoundSetting.get(), levelUpChatSetting.get(), four.get(), five.get()};
    }

    public static int getEXPHotBarSetting(Player player) {
        return getAllSettings(player)[0];
    }

    public static int getLevelUpSoundSetting(Player player) {
        return getAllSettings(player)[1];
    }

    public static int getLevelUpChatSetting(Player player) {
        return getAllSettings(player)[2];
    }

    private static int[] getJobArray(Player player, Jobs job) {
        AtomicReference<AtomicIntegerArray> atomicIntegerArray = new AtomicReference<>(new AtomicIntegerArray(new int[]{}));
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getAlchemist()));
                case BUILDER -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getBuilder()));
                case DIGGER -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getDigger()));
                case ENCHANTER -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getEnchanter()));
                case FARMER -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getFarmer()));
                case FISHERMAN -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getFisherman()));
                case HUNTER -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getHunter()));
                case LUMBERJACK -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getLumberjack()));
                case MINER -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getMiner()));
                case SMITH -> atomicIntegerArray.set(new AtomicIntegerArray(handler.getSmith()));
            }
        });

        int[] array = new int[atomicIntegerArray.get().length()];

        for (int i = 0; i < atomicIntegerArray.get().length(); i++) {
            array[i] = atomicIntegerArray.get().get(i);
        }

        return array;
    }
}
