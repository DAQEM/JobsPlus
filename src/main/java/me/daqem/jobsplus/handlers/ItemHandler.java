package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
        if (newDrops.isEmpty()) {
            return drops;
        }
        return newDrops;
    }
}
