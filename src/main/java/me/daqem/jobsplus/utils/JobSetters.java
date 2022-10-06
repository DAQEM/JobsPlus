package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.common.capability.ModCapabilityImpl;
import me.daqem.jobsplus.common.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class JobSetters {

    public static void set(Jobs job, Player player, int level, int exp, int powerUp1, int powerUp2, int powerUp3) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case BUILDER -> handler.setBuilder(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case DIGGER -> handler.setDigger(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case ENCHANTER -> handler.setEnchanter(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case FARMER -> handler.setFarmer(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case FISHERMAN -> handler.setFisherman(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case HUNTER -> handler.setHunter(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case LUMBERJACK -> handler.setLumberjack(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case MINER -> handler.setMiner(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
                case SMITH -> handler.setSmith(new int[]{level, exp, powerUp1, powerUp2, powerUp3});
            }
        });
    }

    public static void addEXP(Jobs job, Player player, int exp) {
        set(job, player, JobGetters.getJobLevel(player, job), JobGetters.getJobEXP(player, job) + exp, JobGetters.getPowerup(player, job, CapType.POWER_UP1.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP2.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP3.get()));
    }

    public static void setEXP(Jobs job, Player player, int exp) {
        set(job, player, JobGetters.getJobLevel(player, job), exp, JobGetters.getPowerup(player, job, CapType.POWER_UP1.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP2.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP3.get()));
    }

    public static void setLevel(Jobs job, Player player, int level) {
        set(job, player, level == -2 ? JobGetters.getJobLevel(player, job) + 1 : level, JobGetters.getJobEXP(player, job), JobGetters.getPowerup(player, job, CapType.POWER_UP1.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP2.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP3.get()));
    }

    public static void addPowerUp(Jobs job, Player player, int powerUp) {
        setPowerUp(job, player, powerUp, 1);
    }

    public static void setPowerUp(Jobs job, Player player, int powerUp, int i) {
        if (powerUp == CapType.POWER_UP1.get()) {
            set(job, player, JobGetters.getJobLevel(player, job), JobGetters.getJobEXP(player, job), i, JobGetters.getPowerup(player, job, CapType.POWER_UP2.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP3.get()));
        } else if (powerUp == CapType.POWER_UP2.get()) {
            set(job, player, JobGetters.getJobLevel(player, job), JobGetters.getJobEXP(player, job), JobGetters.getPowerup(player, job, CapType.POWER_UP1.get()), i, JobGetters.getPowerup(player, job, CapType.POWER_UP3.get()));
        } else if (powerUp == CapType.POWER_UP3.get()) {
            set(job, player, JobGetters.getJobLevel(player, job), JobGetters.getJobEXP(player, job), JobGetters.getPowerup(player, job, CapType.POWER_UP1.get()), JobGetters.getPowerup(player, job, CapType.POWER_UP2.get()), i);
        }
    }

    public static void setSuperPower(Player player, Jobs job, int superpower) {
        player.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler -> {
            int[] array = handler.getSuperpower().clone();
            array[job.get()] = superpower;
            handler.setSuperpower(array);
        });
    }

    public static void setDisplay(Player player, int value) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setDisplay(value + 1));
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.refreshTabListName();
        }
        player.refreshDisplayName();
    }

    public static void removeCoins(Player player, int amount) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setCoins(handler.getCoins() - amount));
    }

    public static void addCoins(Player player, int amount) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setCoins(handler.getCoins() + amount));
    }

    public static void setEXPHotBarSetting(Player player, int value) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setSettings(new int[]{value, handler.getSettings()[1], handler.getSettings()[2], handler.getSettings()[3], handler.getSettings()[4]}));
    }

    public static void setLevelUpSoundSetting(Player player, int value) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setSettings(new int[]{handler.getSettings()[0], value, handler.getSettings()[2], handler.getSettings()[3], handler.getSettings()[4]}));
    }

    public static void setLevelUpChatSetting(Player player, int value) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setSettings(new int[]{handler.getSettings()[0], handler.getSettings()[1], value, handler.getSettings()[3], handler.getSettings()[4]}));
    }

    public static void resetJob(Player player, Jobs job) {
        set(job, player, 0, 0, 0, 0, 0);
    }
}