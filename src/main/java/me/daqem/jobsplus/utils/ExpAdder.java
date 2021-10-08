package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;

public class ExpAdder {

    public void addJobExp(Player player, Jobs job, int exp) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> {
                    handler.setAlchemist(new int[]{1, handler.getAlchemist()[CapType.LEVEL.get()], handler.getAlchemist()[CapType.EXP.get()] + exp});
                    if (handler.getAlchemist()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getAlchemist()[CapType.LEVEL.get()])) {
                        handler.setAlchemist(new int[]{1, handler.getAlchemist()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getAlchemist()[CapType.LEVEL.get()]);
                    }
                }
                case BUILDER -> {
                    handler.setBuilder(new int[]{1, handler.getBuilder()[CapType.LEVEL.get()], handler.getBuilder()[CapType.EXP.get()] + exp});
                    if (handler.getBuilder()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getBuilder()[CapType.LEVEL.get()])) {
                        handler.setBuilder(new int[]{1, handler.getBuilder()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getBuilder()[CapType.LEVEL.get()]);
                    }
                }
                case BUTCHER -> {
                    handler.setButcher(new int[]{1, handler.getButcher()[CapType.LEVEL.get()], handler.getButcher()[CapType.EXP.get()] + exp});
                    if (handler.getButcher()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getButcher()[CapType.LEVEL.get()])) {
                        handler.setButcher(new int[]{1, handler.getButcher()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getButcher()[CapType.LEVEL.get()]);
                    }
                }
                case CRAFTSMAN -> {
                    handler.setCraftsman(new int[]{1, handler.getCraftsman()[CapType.LEVEL.get()], handler.getCraftsman()[CapType.EXP.get()] + exp});
                    if (handler.getCraftsman()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getCraftsman()[CapType.LEVEL.get()])) {
                        handler.setCraftsman(new int[]{1, handler.getCraftsman()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getCraftsman()[CapType.LEVEL.get()]);
                    }
                }
                case DIGGER -> {
                    handler.setDigger(new int[]{1, handler.getDigger()[CapType.LEVEL.get()], handler.getDigger()[CapType.EXP.get()] + exp});
                    if (handler.getDigger()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getDigger()[CapType.LEVEL.get()])) {
                        handler.setDigger(new int[]{1, handler.getDigger()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getDigger()[CapType.LEVEL.get()]);
                    }
                }
                case ENCHANTER -> {
                    handler.setEnchanter(new int[]{1, handler.getEnchanter()[CapType.LEVEL.get()], handler.getEnchanter()[CapType.EXP.get()] + exp});
                    if (handler.getEnchanter()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getEnchanter()[CapType.LEVEL.get()])) {
                        handler.setEnchanter(new int[]{1, handler.getEnchanter()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getEnchanter()[CapType.LEVEL.get()]);
                    }
                }
                case FARMER -> {
                    handler.setFarmer(new int[]{1, handler.getFarmer()[CapType.LEVEL.get()], handler.getFarmer()[CapType.EXP.get()] + exp});
                    if (handler.getFarmer()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getFarmer()[CapType.LEVEL.get()])) {
                        handler.setFarmer(new int[]{1, handler.getFarmer()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getFarmer()[CapType.LEVEL.get()]);
                    }
                }
                case FISHERMAN -> {
                    handler.setFisherman(new int[]{1, handler.getFisherman()[CapType.LEVEL.get()], handler.getFisherman()[CapType.EXP.get()] + exp});
                    if (handler.getFisherman()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getFisherman()[CapType.LEVEL.get()])) {
                        handler.setFisherman(new int[]{1, handler.getFisherman()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getFisherman()[CapType.LEVEL.get()]);
                    }
                }
                case HUNTER -> {
                    handler.setHunter(new int[]{1, handler.getHunter()[CapType.LEVEL.get()], handler.getHunter()[CapType.EXP.get()] + exp});
                    if (handler.getHunter()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getHunter()[CapType.LEVEL.get()])) {
                        handler.setHunter(new int[]{1, handler.getHunter()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getHunter()[CapType.LEVEL.get()]);
                    }
                }
                case LUMBERJACK -> {
                    handler.setLumberjack(new int[]{1, handler.getLumberjack()[CapType.LEVEL.get()], handler.getLumberjack()[CapType.EXP.get()] + exp});
                    if (handler.getLumberjack()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getLumberjack()[CapType.LEVEL.get()])) {
                        handler.setLumberjack(new int[]{1, handler.getLumberjack()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getLumberjack()[CapType.LEVEL.get()]);
                    }
                }
                case MINER -> {
                    handler.setMiner(new int[]{1, handler.getMiner()[CapType.LEVEL.get()], handler.getMiner()[CapType.EXP.get()] + exp});
                    if (handler.getMiner()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getMiner()[CapType.LEVEL.get()])) {
                        handler.setMiner(new int[]{1, handler.getMiner()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getMiner()[CapType.LEVEL.get()]);
                    }
                }
                case SMITH -> {
                    handler.setSmith(new int[]{1, handler.getSmith()[CapType.LEVEL.get()], handler.getSmith()[CapType.EXP.get()] + exp});
                    if (handler.getSmith()[CapType.EXP.get()] > LevelFormula.calcExp(handler.getSmith()[CapType.LEVEL.get()])) {
                        handler.setSmith(new int[]{1, handler.getSmith()[CapType.LEVEL.get()] + 1, 0});
                        LevelUpAnnouncer.announceLevelUp(player, job, handler.getSmith()[CapType.LEVEL.get()]);
                    }
                }
            }
        });
    }
}
