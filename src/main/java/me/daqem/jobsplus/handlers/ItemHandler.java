package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    public static List<ItemStack> smeltedRawMaterials(Player player, List<ItemStack> drops) {
        List<ItemStack> newDrops = new ArrayList<>();
        if (!JobGetters.hasEnabledPowerup(player, Jobs.MINER, CapType.POWER_UP2.get())) return drops;
        for (ItemStack drop : drops) {
            final Item item = drop.getItem();
            final int count = drop.getCount();
            newDrops.add(item == Items.RAW_COPPER ? new ItemStack(Items.COPPER_INGOT, count) : item == Items.RAW_IRON ? new ItemStack(Items.IRON_INGOT, count) : item == Items.RAW_GOLD ? new ItemStack(Items.GOLD_INGOT, count) : item == Items.ANCIENT_DEBRIS ? new ItemStack(Items.NETHERITE_SCRAP, count) : drop);
        }
        if (newDrops.isEmpty()) return drops;
        return newDrops;
    }

    public static void addItemsToInventoryOrDrop(ItemStack drop, Player player, Level level, BlockPos blockPos, int exp) {
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
            addFreshItemEntity(level, blockPos, new ItemStack(drop.getItem(), dropCount));
        if (exp > 0) {
            level.addFreshEntity(new ExperienceOrb(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, exp));
        }

    }

    public static LootContext.Builder drops(Level level, BlockPos pos, Player player, ItemStack stack) {
        return new LootContext.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()))
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.TOOL, stack);
    }

    public static void addFreshItemEntity(Level level, BlockPos pos, ItemStack stack) {
        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
    }

    public static void addFreshItemEntity(Level level, BlockPos pos, Item item) {
        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item.getDefaultInstance()));
    }
}
