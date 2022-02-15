package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    public static List<ItemStack> smeltedRawMaterials(Player player, List<ItemStack> drops) {
        List<ItemStack> newDrops = new ArrayList<>();
        if (JobGetters.hasEnabledPowerup(player, Jobs.MINER, CapType.POWERUP2.get())) {
            for (ItemStack drop : drops) {
                if (drop.getItem() == Items.RAW_COPPER) {
                    newDrops.add(new ItemStack(Items.COPPER_INGOT, drop.getCount()));
                } else if (drop.getItem() == Items.RAW_IRON) {
                    newDrops.add(new ItemStack(Items.IRON_INGOT, drop.getCount()));
                } else if (drop.getItem() == Items.RAW_GOLD) {
                    newDrops.add(new ItemStack(Items.GOLD_INGOT, drop.getCount()));
                } else {
                    newDrops.add(drop);
                }
            }
        }
        if (newDrops.isEmpty()) return drops;
        return newDrops;
    }

    public static void addItemsToInventoryOrDrop(ItemStack drop, Player player, Level level, BlockPos blockPos) {
        int dropCount = drop.getCount();
        int oldDropCount = drop.getCount();
        if (player.getInventory().getFreeSlot() != -1) {
            player.addItem(drop);
        }
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem().getDescriptionId().equals(drop.getItem().getDescriptionId()) && item.getCount() <= 64 && item.getMaxStackSize() == 64) {
                if (item.getCount() + dropCount > 64) {
                    dropCount = item.getCount() + dropCount - 64;
                    player.addItem(new ItemStack(drop.getItem(), oldDropCount - dropCount));
                    oldDropCount = dropCount;
                } else {
                    player.addItem(drop);
                }
            }
        }
        if (dropCount != 0)
            level.addFreshEntity(new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, new ItemStack(drop.getItem(), dropCount)));

    }
}
