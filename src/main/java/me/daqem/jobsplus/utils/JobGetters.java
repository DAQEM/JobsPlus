package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class JobGetters {

    public static boolean jobIsEnabled(Player player, Jobs job) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> {
                    if (handler.getAlchemist()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case BUILDER -> {
                    if (handler.getBuilder()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case DIGGER -> {
                    if (handler.getDigger()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case ENCHANTER -> {
                    if (handler.getEnchanter()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case FARMER -> {
                    if (handler.getFarmer()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case FISHERMAN -> {
                    if (handler.getFisherman()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case HUNTER -> {
                    if (handler.getHunter()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case LUMBERJACK -> {
                    if (handler.getLumberjack()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case MINER -> {
                    if (handler.getMiner()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case SMITH -> {
                    if (handler.getSmith()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
            }
        });
        return atomicBoolean.get();
    }

    public static int getJobLevel(Player player, Jobs job) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.LEVEL.get()]);
                case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.LEVEL.get()]);
                case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.LEVEL.get()]);
                case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.LEVEL.get()]);
                case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.LEVEL.get()]);
                case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.LEVEL.get()]);
                case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.LEVEL.get()]);
                case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.LEVEL.get()]);
                case MINER -> atomicInteger.set(handler.getMiner()[CapType.LEVEL.get()]);
                case SMITH -> atomicInteger.set(handler.getSmith()[CapType.LEVEL.get()]);
            }
        });
        return atomicInteger.get();
    }

    public static int getJobEXP(Player player, Jobs job) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.EXP.get()]);
                case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.EXP.get()]);
                case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.EXP.get()]);
                case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.EXP.get()]);
                case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.EXP.get()]);
                case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.EXP.get()]);
                case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.EXP.get()]);
                case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.EXP.get()]);
                case MINER -> atomicInteger.set(handler.getMiner()[CapType.EXP.get()]);
                case SMITH -> atomicInteger.set(handler.getSmith()[CapType.EXP.get()]);
            }
        });
        return atomicInteger.get();
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

    public static int getVerification(Player player, CapType capType) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> atomicInteger.set(handler.getVerification()[capType.get()]));
        return atomicInteger.get();
    }

    public static boolean noVerificationEnabled(Player player) {
        if (getVerification(player, CapType.START_VERIFICATION_FREE) == 1) return false;
        if (getVerification(player, CapType.START_VERIFICATION_PAID) == 1) return false;
        if (getVerification(player, CapType.STOP_VERIFICATION_FREE) == 1) return false;
        if (getVerification(player, CapType.STOP_VERIFICATION_PAID) == 1) return false;
        return getVerification(player, CapType.POWER_UP_VERIFICATION) != 1;
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

    public static boolean hasEnabledPowerup(Player player, Jobs job, int powerUp) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (!JobGetters.jobIsEnabled(player, job)) return false;
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> atomicBoolean.set(handler.getAlchemist()[powerUp] == 1);
                case BUILDER -> atomicBoolean.set(handler.getBuilder()[powerUp] == 1);
                case DIGGER -> atomicBoolean.set(handler.getDigger()[powerUp] == 1);
                case ENCHANTER -> atomicBoolean.set(handler.getEnchanter()[powerUp] == 1);
                case FARMER -> atomicBoolean.set(handler.getFarmer()[powerUp] == 1);
                case FISHERMAN -> atomicBoolean.set(handler.getFisherman()[powerUp] == 1);
                case HUNTER -> atomicBoolean.set(handler.getHunter()[powerUp] == 1);
                case LUMBERJACK -> atomicBoolean.set(handler.getLumberjack()[powerUp] == 1);
                case MINER -> atomicBoolean.set(handler.getMiner()[powerUp] == 1);
                case SMITH -> atomicBoolean.set(handler.getSmith()[powerUp] == 1);
            }
        });
        return atomicBoolean.get();
    }

    public static int getPowerup(Player player, Jobs job, int powerUp) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            if (powerUp == 2) {
                switch (job) {
                    case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.POWERUP1.get()]);
                    case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.POWERUP1.get()]);
                    case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.POWERUP1.get()]);
                    case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.POWERUP1.get()]);
                    case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.POWERUP1.get()]);
                    case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.POWERUP1.get()]);
                    case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.POWERUP1.get()]);
                    case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.POWERUP1.get()]);
                    case MINER -> atomicInteger.set(handler.getMiner()[CapType.POWERUP1.get()]);
                    case SMITH -> atomicInteger.set(handler.getSmith()[CapType.POWERUP1.get()]);
                }
            } else if (powerUp == 3) {
                switch (job) {
                    case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.POWERUP2.get()]);
                    case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.POWERUP2.get()]);
                    case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.POWERUP2.get()]);
                    case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.POWERUP2.get()]);
                    case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.POWERUP2.get()]);
                    case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.POWERUP2.get()]);
                    case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.POWERUP2.get()]);
                    case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.POWERUP2.get()]);
                    case MINER -> atomicInteger.set(handler.getMiner()[CapType.POWERUP2.get()]);
                    case SMITH -> atomicInteger.set(handler.getSmith()[CapType.POWERUP2.get()]);
                }
            } else if (powerUp == 4) {
                switch (job) {
                    case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.POWERUP3.get()]);
                    case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.POWERUP3.get()]);
                    case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.POWERUP3.get()]);
                    case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.POWERUP3.get()]);
                    case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.POWERUP3.get()]);
                    case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.POWERUP3.get()]);
                    case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.POWERUP3.get()]);
                    case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.POWERUP3.get()]);
                    case MINER -> atomicInteger.set(handler.getMiner()[CapType.POWERUP3.get()]);
                    case SMITH -> atomicInteger.set(handler.getSmith()[CapType.POWERUP3.get()]);
                }
            }
        });
        return atomicInteger.get();
    }

    public static int getSuperPower(Player player, Jobs job) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler -> atomicInteger.set(handler.getSuperpower()[Jobs.getJobInt(job)]));
        return atomicInteger.get();
    }

    public static boolean hasSuperPowerEnabled(Player player, Jobs job) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        player.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler -> atomicBoolean.set(getSuperPower(player, job) == 0 && JobGetters.getJobLevel(player, job) == 100));
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
}
