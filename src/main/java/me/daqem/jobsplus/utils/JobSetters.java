package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class JobSetters {

    public static void set(Jobs job, Player player, int level, int exp, int powerUp1, int powerUp2, int powerUp3) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            int[] tempArray = new int[]{level, exp, powerUp1, powerUp2, powerUp3};
            ArrayList<Integer> array = new ArrayList<>();
            for (int i : tempArray) {
                array.add(i);
            }
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(arrayGenerator(array, handler.getAlchemist()));
                case BUILDER -> handler.setBuilder(arrayGenerator(array, handler.getBuilder()));
                case DIGGER -> handler.setDigger(arrayGenerator(array, handler.getDigger()));
                case ENCHANTER -> handler.setEnchanter(arrayGenerator(array, handler.getEnchanter()));
                case FARMER -> handler.setFarmer(arrayGenerator(array, handler.getFarmer()));
                case FISHERMAN -> handler.setFisherman(arrayGenerator(array, handler.getFisherman()));
                case HUNTER -> handler.setHunter(arrayGenerator(array, handler.getHunter()));
                case LUMBERJACK -> handler.setLumberjack(arrayGenerator(array, handler.getLumberjack()));
                case MINER -> handler.setMiner(arrayGenerator(array, handler.getMiner()));
                case SMITH -> handler.setSmith(arrayGenerator(array, handler.getSmith()));
            }
        });
    }

    public static void addEXP(Jobs job, Player player, int exp) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(addEXPGenerator(handler.getAlchemist(), exp));
                case BUILDER -> handler.setBuilder(addEXPGenerator(handler.getBuilder(), exp));
                case DIGGER -> handler.setDigger(addEXPGenerator(handler.getDigger(), exp));
                case ENCHANTER -> handler.setEnchanter(addEXPGenerator(handler.getEnchanter(), exp));
                case FARMER -> handler.setFarmer(addEXPGenerator(handler.getFarmer(), exp));
                case FISHERMAN -> handler.setFisherman(addEXPGenerator(handler.getFisherman(), exp));
                case HUNTER -> handler.setHunter(addEXPGenerator(handler.getHunter(), exp));
                case LUMBERJACK -> handler.setLumberjack(addEXPGenerator(handler.getLumberjack(), exp));
                case MINER -> handler.setMiner(addEXPGenerator(handler.getMiner(), exp));
                case SMITH -> handler.setSmith(addEXPGenerator(handler.getSmith(), exp));
            }
        });
    }

    public static void setEXP(Jobs job, Player player, int exp) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(setEXPGenerator(handler.getAlchemist(), exp));
                case BUILDER -> handler.setBuilder(setEXPGenerator(handler.getBuilder(), exp));
                case DIGGER -> handler.setDigger(setEXPGenerator(handler.getDigger(), exp));
                case ENCHANTER -> handler.setEnchanter(setEXPGenerator(handler.getEnchanter(), exp));
                case FARMER -> handler.setFarmer(setEXPGenerator(handler.getFarmer(), exp));
                case FISHERMAN -> handler.setFisherman(setEXPGenerator(handler.getFisherman(), exp));
                case HUNTER -> handler.setHunter(setEXPGenerator(handler.getHunter(), exp));
                case LUMBERJACK -> handler.setLumberjack(setEXPGenerator(handler.getLumberjack(), exp));
                case MINER -> handler.setMiner(setEXPGenerator(handler.getMiner(), exp));
                case SMITH -> handler.setSmith(setEXPGenerator(handler.getSmith(), exp));
            }
        });
    }

    public static void setLevel(Jobs job, Player player, int level) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {

//            if (level == 0) AdvancementHandler.revoke((ServerPlayer) player, "test");
//            else AdvancementHandler.grand((ServerPlayer) player, "test");

            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(addLevelGenerator(handler.getAlchemist(), level));
                case BUILDER -> handler.setBuilder(addLevelGenerator(handler.getBuilder(), level));
                case DIGGER -> handler.setDigger(addLevelGenerator(handler.getDigger(), level));
                case ENCHANTER -> handler.setEnchanter(addLevelGenerator(handler.getEnchanter(), level));
                case FARMER -> handler.setFarmer(addLevelGenerator(handler.getFarmer(), level));
                case FISHERMAN -> handler.setFisherman(addLevelGenerator(handler.getFisherman(), level));
                case HUNTER -> handler.setHunter(addLevelGenerator(handler.getHunter(), level));
                case LUMBERJACK -> handler.setLumberjack(addLevelGenerator(handler.getLumberjack(), level));
                case MINER -> handler.setMiner(addLevelGenerator(handler.getMiner(), level));
                case SMITH -> handler.setSmith(addLevelGenerator(handler.getSmith(), level));
            }
        });
    }

    public static void addPowerUp(Jobs job, Player player, int powerUp) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(addPowerUpGenerator(handler.getAlchemist(), powerUp));
                case BUILDER -> handler.setBuilder(addPowerUpGenerator(handler.getBuilder(), powerUp));
                case DIGGER -> handler.setDigger(addPowerUpGenerator(handler.getDigger(), powerUp));
                case ENCHANTER -> handler.setEnchanter(addPowerUpGenerator(handler.getEnchanter(), powerUp));
                case FARMER -> handler.setFarmer(addPowerUpGenerator(handler.getFarmer(), powerUp));
                case FISHERMAN -> handler.setFisherman(addPowerUpGenerator(handler.getFisherman(), powerUp));
                case HUNTER -> handler.setHunter(addPowerUpGenerator(handler.getHunter(), powerUp));
                case LUMBERJACK -> handler.setLumberjack(addPowerUpGenerator(handler.getLumberjack(), powerUp));
                case MINER -> handler.setMiner(addPowerUpGenerator(handler.getMiner(), powerUp));
                case SMITH -> handler.setSmith(addPowerUpGenerator(handler.getSmith(), powerUp));
            }
        });
    }

    public static void setPowerUp(Jobs job, Player player, int powerUp, int i) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(setPowerUpGenerator(handler.getAlchemist(), powerUp, i));
                case BUILDER -> handler.setBuilder(setPowerUpGenerator(handler.getBuilder(), powerUp, i));
                case DIGGER -> handler.setDigger(setPowerUpGenerator(handler.getDigger(), powerUp, i));
                case ENCHANTER -> handler.setEnchanter(setPowerUpGenerator(handler.getEnchanter(), powerUp, i));
                case FARMER -> handler.setFarmer(setPowerUpGenerator(handler.getFarmer(), powerUp, i));
                case FISHERMAN -> handler.setFisherman(setPowerUpGenerator(handler.getFisherman(), powerUp, i));
                case HUNTER -> handler.setHunter(setPowerUpGenerator(handler.getHunter(), powerUp, i));
                case LUMBERJACK -> handler.setLumberjack(setPowerUpGenerator(handler.getLumberjack(), powerUp, i));
                case MINER -> handler.setMiner(setPowerUpGenerator(handler.getMiner(), powerUp, i));
                case SMITH -> handler.setSmith(setPowerUpGenerator(handler.getSmith(), powerUp, i));
            }
        });
    }

    public static void setSuperPower(Player player, Jobs job, int superpower) {
        player.getCapability(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY).ifPresent(handler -> {
            int[] array = handler.getSuperpower();
            int alchemist = array[0], builder = array[1], digger = array[2], enchanter = array[3], farmer = array[4],
                    fisherman = array[5], hunter = array[6], lumberjack = array[7], miner = array[8], smith = array[9];
            for (int i = 0; i < 10; ++i) {
                if (i == Jobs.getJobInt(job)) {
                    if (job == Jobs.ALCHEMIST) alchemist = superpower;
                    if (job == Jobs.BUILDER) builder = superpower;
                    if (job == Jobs.DIGGER) digger = superpower;
                    if (job == Jobs.ENCHANTER) enchanter = superpower;
                    if (job == Jobs.FARMER) farmer = superpower;
                    if (job == Jobs.FISHERMAN) fisherman = superpower;
                    if (job == Jobs.HUNTER) hunter = superpower;
                    if (job == Jobs.LUMBERJACK) lumberjack = superpower;
                    if (job == Jobs.MINER) miner = superpower;
                    if (job == Jobs.SMITH) smith = superpower;
                }
            }
            handler.setSuperpower(new int[]{alchemist, builder, digger, enchanter, farmer, fisherman, hunter, lumberjack, miner, smith});
        });
    }

    public static void setVerification(Player player, int type) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            if (type == -1) {
                handler.setVerification(new int[]{0, 0, 0, 0, 0});
                return;
            }
            ArrayList<Integer> array = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                array.add(0);
            }
            array.set(type, 1);
            handler.setVerification(array.stream().mapToInt(i -> i).toArray());
        });
    }

    public static void setSelector(Player player, int type) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            if (type == -1) {
                handler.setSelector(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
                return;
            }
            ArrayList<Integer> array = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                array.add(0);
            }
            array.set(type, 1);
            handler.setSelector(array.stream().mapToInt(i -> i).toArray());
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

    private static int[] arrayGenerator(ArrayList<Integer> arrayList, int[] intArray) {
        for (int j : arrayList) {
            if (arrayList.get(j) == -1) arrayList.set(j, intArray[j]);
            if (arrayList.get(j) == -2) arrayList.set(j, intArray[j] + 1);
        }
        return arrayList.stream().mapToInt(i -> i).toArray();
    }

    private static int[] addEXPGenerator(int[] array, int exp) {
        return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()] + exp, array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
    }

    private static int[] setEXPGenerator(int[] array, int exp) {
        return new int[]{array[CapType.LEVEL.get()], exp, array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
    }

    private static int[] addLevelGenerator(int[] array, int level) {
        if (level == -2) level = array[CapType.LEVEL.get()] + 1;
        return new int[]{level, array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
    }

    private static int[] addPowerUpGenerator(int[] array, int powerUp) {
        if (powerUp == CapType.POWER_UP1.get())
            return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], 1, array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
        if (powerUp == CapType.POWER_UP2.get())
            return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], 1, array[CapType.POWER_UP3.get()]};
        if (powerUp == CapType.POWER_UP3.get())
            return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], 1};
        return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
    }

    private static int[] setPowerUpGenerator(int[] array, int powerUp, int i) {
        if (powerUp == CapType.POWER_UP1.get())
            return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], i, array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
        if (powerUp == CapType.POWER_UP2.get())
            return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], i, array[CapType.POWER_UP3.get()]};
        if (powerUp == CapType.POWER_UP3.get())
            return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], i};
        return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()], array[CapType.POWER_UP1.get()], array[CapType.POWER_UP2.get()], array[CapType.POWER_UP3.get()]};
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
}
