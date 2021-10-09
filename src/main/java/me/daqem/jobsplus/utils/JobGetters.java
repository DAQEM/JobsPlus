package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;

public class JobGetters {

    private boolean isEnabled = false;
    public boolean getIsEnabled() { return this.isEnabled; }
    public void setIsEnabled(boolean value) { this.isEnabled = value; }

    private int jobLevel = 0;
    public int getJobLevel() { return this.jobLevel; }
    public void setJobLevel(int value) { this.jobLevel = value; }

    private int jobEXP = 0;
    public int getJobEXP() { return this.jobEXP; }
    public void setJobEXP(int value) { this.jobEXP = value; }
    
    public boolean getJobEnabled(Player player, Jobs job) {
        setIsEnabled(false);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> {
                    if(handler.getAlchemist()[0] == 1) setIsEnabled(true);
                }
                case BUILDER -> {
                    if(handler.getBuilder()[0] == 1) setIsEnabled(true);
                }
                case BUTCHER -> {
                    if(handler.getButcher()[0] == 1) setIsEnabled(true);
                }
                case CRAFTSMAN -> {
                    if(handler.getCraftsman()[0] == 1) setIsEnabled(true);
                }
                case DIGGER -> {
                    if(handler.getDigger()[0] == 1) setIsEnabled(true);
                }
                case ENCHANTER -> {
                    if(handler.getEnchanter()[0] == 1) setIsEnabled(true);
                }
                case FARMER -> {
                    if(handler.getFarmer()[0] == 1) setIsEnabled(true);
                }
                case FISHERMAN -> {
                    if(handler.getFisherman()[0] == 1) setIsEnabled(true);
                }
                case HUNTER -> {
                    if(handler.getHunter()[0] == 1) setIsEnabled(true);
                }
                case LUMBERJACK -> {
                    if(handler.getLumberjack()[0] == 1) setIsEnabled(true);
                }
                case MINER -> {
                    if(handler.getMiner()[0] == 1) setIsEnabled(true);
                }
                case SMITH -> {
                    if(handler.getSmith()[0] == 1) setIsEnabled(true);
                }
            }
        });
        return getIsEnabled();
    }

    public int getJobLevel(Player player, Jobs job) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> setJobLevel(handler.getAlchemist()[1]);
                case BUILDER -> setJobLevel(handler.getBuilder()[1]);
                case BUTCHER -> setJobLevel(handler.getButcher()[1]);
                case CRAFTSMAN -> setJobLevel(handler.getCraftsman()[1]);
                case DIGGER -> setJobLevel(handler.getDigger()[1]);
                case ENCHANTER -> setJobLevel(handler.getEnchanter()[1]);
                case FARMER -> setJobLevel(handler.getFarmer()[1]);
                case FISHERMAN -> setJobLevel(handler.getFisherman()[1]);
                case HUNTER -> setJobLevel(handler.getHunter()[1]);
                case LUMBERJACK -> setJobLevel(handler.getLumberjack()[1]);
                case MINER -> setJobLevel(handler.getMiner()[1]);
                case SMITH -> setJobLevel(handler.getSmith()[1]);
            }
        });
        return getJobLevel();
    }

    public int getJobEXP(Player player, Jobs job) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> setJobEXP(handler.getAlchemist()[2]);
                case BUILDER -> setJobEXP(handler.getBuilder()[2]);
                case BUTCHER -> setJobEXP(handler.getButcher()[2]);
                case CRAFTSMAN -> setJobEXP(handler.getCraftsman()[2]);
                case DIGGER -> setJobEXP(handler.getDigger()[2]);
                case ENCHANTER -> setJobEXP(handler.getEnchanter()[2]);
                case FARMER -> setJobEXP(handler.getFarmer()[2]);
                case FISHERMAN -> setJobEXP(handler.getFisherman()[2]);
                case HUNTER -> setJobEXP(handler.getHunter()[2]);
                case LUMBERJACK -> setJobEXP(handler.getLumberjack()[2]);
                case MINER -> setJobEXP(handler.getMiner()[2]);
                case SMITH -> setJobEXP(handler.getSmith()[2]);
            }
        });
        return getJobEXP();
    }
}
