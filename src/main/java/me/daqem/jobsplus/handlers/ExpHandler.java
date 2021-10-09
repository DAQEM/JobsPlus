package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;

public class ExpHandler {

    public void addJobExp(Player player, Jobs job, int exp) {
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> expHandler(job, player, handler.getAlchemist(), exp);
                case BUILDER -> expHandler(job, player, handler.getBuilder(), exp);
                case BUTCHER -> expHandler(job, player, handler.getButcher(), exp);
                case CRAFTSMAN -> expHandler(job, player, handler.getCraftsman(), exp);
                case DIGGER -> expHandler(job, player, handler.getDigger(), exp);
                case ENCHANTER -> expHandler(job, player, handler.getEnchanter(), exp);
                case FARMER -> expHandler(job, player, handler.getFarmer(), exp);
                case FISHERMAN -> expHandler(job, player, handler.getFisherman(), exp);
                case HUNTER -> expHandler(job, player, handler.getHunter(), exp);
                case LUMBERJACK -> expHandler(job, player, handler.getLumberjack(), exp);
                case MINER -> expHandler(job, player, handler.getMiner(), exp);
                case SMITH -> expHandler(job, player, handler.getSmith(), exp);
            }
        });
    }

    private static void expHandler(Jobs job, Player player, int[] array, int exp) {
        JobSetters.addEXP(job, player, exp);
        if (array[CapType.EXP.get()] > LevelFormulaHandler.calcExp(array[CapType.LEVEL.get()])) {
            JobSetters.setLevel(job, player, -2);
            LevelUpHandler.handler(player, job, array[CapType.LEVEL.get()]);
        }
    }
}
