package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.MobEffectHandler;
import me.daqem.jobsplus.utils.BlockPosUtil;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DiggerEvents {

    public static final ArrayList<BlockPos> timeoutList = new ArrayList<>();
    public static final ArrayList<String> lowestList = new ArrayList<>(List.of("dirt", "sand"));
    public static final ArrayList<String> lowList = new ArrayList<>(List.of("grass_block", "coarse_dirt", "podzol", "rooted_dirt", "clay",
            "red_sand", "soul_sand", "soul_soil", "mycelium", "gravel"));

    private final Jobs job = Jobs.DIGGER;

    public static void addFreshItemEntity(Player player, Level level, BlockPos pos, Item item) {
        if (JobGetters.hasEnabledPowerup(player, Jobs.DIGGER, CapType.POWERUP1.get())) {
            ItemHandler.addItemsToInventoryOrDrop(item.getDefaultInstance(), player, level, pos, 1);
        } else {
            ItemHandler.addFreshItemEntity(level, pos, item.getDefaultInstance());
        }
    }

    public static void dropMinerals(Player player, Level level, BlockPos pos) {
        if (JobGetters.hasEnabledPowerup(player, Jobs.DIGGER, CapType.POWERUP2.get())) {
            if (Math.random() * 100 <= 4) addFreshItemEntity(player, level, pos, Items.GOLD_NUGGET);
            else if (Math.random() * 100 <= 2) addFreshItemEntity(player, level, pos, Items.GOLD_INGOT);
            else if (Math.random() * 100 <= 0.1) addFreshItemEntity(player, level, pos, Items.GOLD_BLOCK);
            if (Math.random() * 100 <= 4) addFreshItemEntity(player, level, pos, Items.IRON_NUGGET);
            else if (Math.random() * 100 <= 2) addFreshItemEntity(player, level, pos, Items.IRON_INGOT);
            else if (Math.random() * 100 <= 0.1) addFreshItemEntity(player, level, pos, Items.IRON_BLOCK);
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
                if (BlockPosUtil.testAllSides(timeoutList, pos)) {
                    if (lowList.contains(block.getDescriptionId().replace("block.minecraft.", ""))) {
                        ExpHandler.addEXPLow(player, job);
                        didGetEXP = true;
                    } else if (lowestList.contains(block.getDescriptionId().replace("block.minecraft.", ""))) {
                        ExpHandler.addEXPLowest(player, job);
                        didGetEXP = true;
                    }
                    if (didGetEXP) {
                        MobEffectHandler.addPlayerPowerUpEffects(player, job);
                        handleBreak(player, event.getState(), pos);
                        dropMinerals(player, player.getLevel(), pos);
                    }
                } else {
                    timeoutList.remove(pos);
                }
            }
        }
    }

    public void handleBreak(Player player, BlockState state, BlockPos pos) {
        if (!JobGetters.hasEnabledPowerup(player, job, CapType.POWERUP1.get())) return;
        player.getLevel().removeBlock(pos, false);
        for (ItemStack drop : state.getDrops(ItemHandler.drops(player.getLevel(), pos, player, player.getMainHandItem()))) {
            ItemHandler.addItemsToInventoryOrDrop(drop, player, player.getLevel(), pos, 0);
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (!(timeoutList.size() < 10000)) {
                    timeoutList.remove(0);
                }
                timeoutList.add(event.getPos());
            }
        }
    }
}
