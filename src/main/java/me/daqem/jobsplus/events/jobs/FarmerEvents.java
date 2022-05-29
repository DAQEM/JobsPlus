package me.daqem.jobsplus.events.jobs;

import com.google.common.collect.Maps;
import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmerEvents {

    public static final ArrayList<BlockPos> blockPosArrayList = new ArrayList<>();
    private static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (map) -> {
        map.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        map.put(DyeColor.LIME, Blocks.LIME_WOOL);
        map.put(DyeColor.PINK, Blocks.PINK_WOOL);
        map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        map.put(DyeColor.RED, Blocks.RED_WOOL);
        map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });
    private final Jobs job = Jobs.FARMER;

    private static void doubleCropDrop(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return;
        final List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, player.getMainHandItem());
        for (ItemStack drop : drops) {
            ItemHandler.addFreshItemEntity(level, pos, drop);
        }
    }

    @SubscribeEvent
    public void onCropBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        final Level level = player.getLevel();
        if (level.isClientSide) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;
        Block block = event.getState().getBlock();
        BlockState state = event.getState();
        final BlockPos pos = event.getPos();
        if (block instanceof CropBlock cropBlock) {
            if (cropBlock.isMaxAge(event.getState())) {
                ExpHandler.addEXPLow(player, job);
            }
            if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get())) {
                if (Math.random() * 100 < 10) doubleCropDrop(state, level, pos, player);
            }
            if (JobGetters.hasSuperPowerEnabled(player, job)) doubleCropDrop(state, level, pos, player);
        } else {
            if (block == Blocks.CACTUS || block == Blocks.SUGAR_CANE) {
                if (!blockPosArrayList.contains(pos)) {
                    if (event.getWorld().getBlockState(pos.above()).getBlock() == Blocks.SUGAR_CANE ||
                            event.getWorld().getBlockState(pos.above()).getBlock() == Blocks.CACTUS) {
                        if (!blockPosArrayList.contains(pos.above())) {
                            ExpHandler.addEXPMid(player, job);
                        } else {
                            ExpHandler.addEXPLow(player, job);
                        }
                    } else {
                        ExpHandler.addEXPLow(player, job);
                    }
                }
            } else if (block.getDescriptionId().equals("block.farmersdelight.tomatoes")) {
                if (CropHandler.stateToAge(event.getState()) == 7) {
                    ExpHandler.addEXPLow(player, job);
                }
            } else if (block == Blocks.MELON || block == Blocks.PUMPKIN) {
                if (!blockPosArrayList.contains(pos)) {
                    ExpHandler.addEXPLow(player, job);
                    if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get())) {
                        if (block == Blocks.MELON) {
                            if (Math.random() * 100 < 10) {
                                ItemHandler.addFreshItemEntity(level, pos, Items.GLISTERING_MELON_SLICE);
                            }
                        } else {
                            if (Math.random() * 100 < 5) {
                                ItemHandler.addFreshItemEntity(level, pos, Items.PUMPKIN_PIE);
                            } else if (Math.random() * 100 < 10) {
                                ItemHandler.addFreshItemEntity(level, pos, Items.CARVED_PUMPKIN);
                            }
                        }
                    }
                }
            } else if (block == Blocks.COCOA) {
                if (CropHandler.stateToAgeString(event.getState()).startsWith("2")) {
                    ExpHandler.addEXPLow(player, job);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreed(BabyEntitySpawnEvent event) {
        Player player = event.getCausedByPlayer();
        if (player != null) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (!player.isCreative()) {
                    ExpHandler.addEXPHigh(player, job);
                }
            }
        }
    }

    @SubscribeEvent
    public void onSheepShear(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        if (JobGetters.jobIsEnabled(player, job)) {
            if (event.getTarget() instanceof Sheep sheep) {
                if (sheep.readyForShearing()) {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.SHEARS) {
                        ExpHandler.addEXPLow(player, job);
                        Random random = new Random();
                        if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP3.get())) {
                            dropWool(sheep, random, DyeColor.byId(new Random().nextInt(16)));
                        }
                        if (JobGetters.hasSuperPowerEnabled(player, job)) {
                            int i = 1 + random.nextInt(3);
                            for (int j = 0; j < i; ++j) {
                                dropWool(sheep, random, sheep.getColor());
                            }
                        }
                    }
                }
            }
        }
    }

    public void dropWool(Sheep sheep, Random random, DyeColor color) {
        ItemEntity itementity = sheep.spawnAtLocation(ITEM_BY_DYE.get(color), 1);
        if (itementity != null) {
            itementity.setDeltaMovement(itementity.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getPlayer();
        if (JobGetters.jobIsEnabled(player, job)) {
            if (!player.isCreative()) {
                if (event.getCrafting().getDescriptionId().equals("item.minecraft.bread")) {
                    ExpHandler.addEXPLowest(player, job);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCropPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                Block block = event.getPlacedBlock().getBlock();
                if (block == Blocks.SUGAR_CANE || block == Blocks.CACTUS || block == Blocks.PUMPKIN || block == Blocks.MELON) {
                    blockPosArrayList.add(event.getPos());
                }
            }
        }
    }
}
