package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.MobEffectHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DiggerEvents {

    public static final ArrayList<Block> lowestList = new ArrayList<>(List.of(Blocks.DIRT, Blocks.SAND));
    public static final ArrayList<Block> lowList = new ArrayList<>(List.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT,
            Blocks.PODZOL, Blocks.ROOTED_DIRT, Blocks.CLAY, Blocks.RED_SAND, Blocks.SOUL_SAND, Blocks.SOUL_SOIL,
            Blocks.MYCELIUM, Blocks.GRAVEL, Blocks.MUD));

    private final Jobs job = Jobs.DIGGER;

    public static void addFreshItemEntity(Player player, Level level, BlockPos pos, Item item) {
        if (JobGetters.hasPowerupEnabled(player, Jobs.DIGGER, CapType.POWER_UP1.get(), true)) {
            ItemHandler.addItemsToInventoryOrDrop(item.getDefaultInstance(), player, level, pos, 1);
        } else {
            ItemHandler.addFreshItemEntity(level, pos, item.getDefaultInstance());
        }
    }

    public static void dropMinerals(Player player, Level level, BlockPos pos) {
        if (JobGetters.hasPowerupEnabled(player, Jobs.DIGGER, CapType.POWER_UP2.get(), true)) {
            if (Math.random() * 100 <= 2) addFreshItemEntity(player, level, pos, Items.GOLD_NUGGET);
            else if (Math.random() * 100 <= 1) addFreshItemEntity(player, level, pos, Items.RAW_GOLD);
            else if (Math.random() * 100 <= 0.05) addFreshItemEntity(player, level, pos, Items.RAW_GOLD_BLOCK);
            if (Math.random() * 100 <= 2) addFreshItemEntity(player, level, pos, Items.IRON_NUGGET);
            else if (Math.random() * 100 <= 1) addFreshItemEntity(player, level, pos, Items.RAW_IRON);
            else if (Math.random() * 100 <= 0.05) addFreshItemEntity(player, level, pos, Items.RAW_IRON_BLOCK);
        }
    }

    @SubscribeEvent
    public void onDiggingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                Block block = event.getState().getBlock();
                boolean didGetEXP = false;
                final BlockPos pos = event.getPos();
                if (lowList.contains(block)) {
                    ExpHandler.addEXPLow(player, job);
                    didGetEXP = true;
                } else if (lowestList.contains(block)) {
                    ExpHandler.addEXPLowest(player, job);
                    didGetEXP = true;
                }
                if (didGetEXP) {
                    MobEffectHandler.addPlayerPowerUpEffects(player, job);
                    handleBreak(player, event.getState(), pos);
                    dropMinerals(player, player.getLevel(), pos);
                }
            }
        }
    }

    public void handleBreak(Player player, BlockState state, BlockPos pos) {
        if (!JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP1.get(), true)) return;
        player.getLevel().removeBlock(pos, false);
        for (ItemStack drop : state.getDrops(ItemHandler.drops(player.getLevel(), pos, player, player.getMainHandItem()))) {
            ItemHandler.addItemsToInventoryOrDrop(drop, player, player.getLevel(), pos, 0);
        }
    }
}
