package me.daqem.jobsplus.handlers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

public class VeinMinerHandler {

    public static final String IS_VEIN_MINING = "IsVeinMining";

    public static void enableVeinMining(ServerPlayer player) {
        player.getPersistentData().putBoolean(IS_VEIN_MINING, true);
    }

    public static void disableVeinMining(ServerPlayer player) {
        player.getPersistentData().putBoolean(IS_VEIN_MINING, false);
    }

    public static boolean isNotVeinMining(ServerPlayer player) {
        return !player.getPersistentData().getBoolean(IS_VEIN_MINING);
    }

    public static void disableVeinMiningIfNotOre(ServerPlayer player, Block minedBlock) {
        if (!BlockHandler.isOre(minedBlock)) disableVeinMining(player);
    }
}
