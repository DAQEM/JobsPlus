package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class JobSetters {

    public static void set(Jobs job, Player player, int level, int exp, int powerUp1, int powerUp2, int powerUp3) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            int[] tempArray = new int[]{level, exp, powerUp1, powerUp2, powerUp3};
            ArrayList<Integer> array = new ArrayList<>();
            for(int i : tempArray) { array.add(i); }
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(arrayGenerator(array, handler.getAlchemist()));
                case BUILDER -> handler.setBuilder(arrayGenerator(array, handler.getBuilder()));
                case BUTCHER -> handler.setButcher(arrayGenerator(array, handler.getButcher()));
                case CRAFTSMAN -> handler.setCraftsman(arrayGenerator(array, handler.getCraftsman()));
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
                case BUTCHER -> handler.setButcher(addEXPGenerator(handler.getButcher(), exp));
                case CRAFTSMAN -> handler.setCraftsman(addEXPGenerator(handler.getCraftsman(), exp));
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
                case BUTCHER -> handler.setButcher(setEXPGenerator(handler.getButcher(), exp));
                case CRAFTSMAN -> handler.setCraftsman(setEXPGenerator(handler.getCraftsman(), exp));
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
            switch (job) {
                case ALCHEMIST -> handler.setAlchemist(addLevelGenerator(handler.getAlchemist(), level));
                case BUILDER -> handler.setBuilder(addLevelGenerator(handler.getBuilder(), level));
                case BUTCHER -> handler.setButcher(addLevelGenerator(handler.getButcher(), level));
                case CRAFTSMAN -> handler.setCraftsman(addLevelGenerator(handler.getCraftsman(), level));
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

    public static void setVerification(Player player, int type) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            if (type == -1) {
                handler.setVerification(new int[]{0, 0, 0, 0, 0});
                return;
            }
            ArrayList<Integer> array = new ArrayList<>();
            for(int i = 0; i < 4; i++) { array.add(0); }
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
            for(int i = 0; i < 12; i++) { array.add(0); }
            array.set(type, 1);
            handler.setSelector(array.stream().mapToInt(i -> i).toArray());
        });
    }

    public static void removeCoins(Player player, int amount) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> handler.setCoins(handler.getCoins() - amount));
    }

    private static int[] arrayGenerator(ArrayList<Integer> arrayList, int[] intArray) {
        for(int j : arrayList) {
            if (arrayList.get(j) == -1) arrayList.set(j, intArray[j]);
            if (arrayList.get(j) == -2) arrayList.set(j, intArray[j] + 1);
        }
        return arrayList.stream().mapToInt(i -> i).toArray();
    }

    private static int[] addEXPGenerator(int[] array, int exp) {
        return new int[]{array[CapType.LEVEL.get()], array[CapType.EXP.get()] + exp, array[CapType.POWERUP1.get()], array[CapType.POWERUP2.get()], array[CapType.POWERUP3.get()]};
    }

    private static int[] setEXPGenerator(int[] array, int exp) {
        return new int[]{array[CapType.LEVEL.get()], exp, array[CapType.POWERUP1.get()], array[CapType.POWERUP2.get()], array[CapType.POWERUP3.get()]};
    }

    private static int[] addLevelGenerator(int[] array, int level) {
        if (level == -2) level = array[CapType.LEVEL.get()] + 1;
        return new int[]{level, array[CapType.EXP.get()], array[CapType.POWERUP1.get()], array[CapType.POWERUP2.get()], array[CapType.POWERUP3.get()]};
    }
}
