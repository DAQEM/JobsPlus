package me.daqem.jobsplus.events.jobs;

import com.google.common.collect.Maps;
import me.daqem.jobsplus.common.item.FarmersHoeItem;
import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
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
        Level level = player.getLevel();

        if (level.isClientSide) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        Block block = event.getState().getBlock();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        if (block instanceof CropBlock cropBlock) {
            if (cropBlock.isMaxAge(event.getState())) ExpHandler.addEXPLow(player, job);
            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true)) {
                if (Math.random() * 100 < 10) doubleCropDrop(state, level, pos, player);
            }
            if (JobGetters.hasSuperPowerEnabled(player, job, true)) doubleCropDrop(state, level, pos, player);
        } else {
            if (block == Blocks.CACTUS || block == Blocks.SUGAR_CANE) {
                if (!blockPosArrayList.contains(pos)) {
                    if (event.getLevel().getBlockState(pos.above()).getBlock() == Blocks.SUGAR_CANE ||
                            event.getLevel().getBlockState(pos.above()).getBlock() == Blocks.CACTUS) {
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
                    if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true)) {
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
            if (!JobGetters.jobIsEnabled(player, job)) return;
            ExpHandler.addEXPHigh(player, job);
        }
    }

    @SubscribeEvent
    public void onSheepShear(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (!JobGetters.jobIsEnabled(player, job)) return;
        if (!(event.getTarget() instanceof Sheep sheep)) return;
        if (!sheep.readyForShearing()) return;
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.SHEARS) return;

        ExpHandler.addEXPLow(player, job);
        Random random = new Random();

        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP3.get(), true)) {
            dropWool(sheep, random, DyeColor.byId(new Random().nextInt(16)));
        }

        if (JobGetters.hasSuperPowerEnabled(player, job, true)) {
            int i = 1 + random.nextInt(3);
            for (int j = 0; j < i; ++j) {
                dropWool(sheep, random, sheep.getColor());
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
    public void onCraftingBread(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        if (!JobGetters.jobIsEnabled(player, job)) return;

        if (event.getCrafting().is(Items.BREAD)) {
            ExpHandler.addEXPLow(player, job);
        }
    }

    @SubscribeEvent
    public void onCropPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        Block block = event.getPlacedBlock().getBlock();

        if (block == Blocks.SUGAR_CANE || block == Blocks.CACTUS || block == Blocks.PUMPKIN || block == Blocks.MELON || block == Blocks.BAMBOO || block == Blocks.KELP_PLANT) {
            blockPosArrayList.add(event.getPos());
        }

        if (block instanceof BonemealableBlock) {
            ExpHandler.addEXPLowest(player, job);
        }
    }

    @SubscribeEvent
    public void onTillLand(BlockEvent.BlockToolModificationEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer serverPlayer)) return;
        if (!JobGetters.jobIsEnabled(serverPlayer, job)) return;
        if (event.getToolAction() != ToolActions.HOE_TILL) return;
        if (!(event.getHeldItemStack().getItem() instanceof FarmersHoeItem)) return;

        final ItemStack stack = event.getHeldItemStack();
        int mode = stack.getOrCreateTag().getInt("mode");

        for (int x = -mode; x <= mode; x++) {
            for (int z = -mode; z <= mode; z++) {

                BlockPos blockPos = new BlockPos(event.getPos().offset(x, 0, z));
                BlockState state = event.getLevel().getBlockState(blockPos);

                if (blockPos != event.getPos() && (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)
                        || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.ROOTED_DIRT) || state.is(Blocks.PODZOL))) {
                    if (event.getLevel().getBlockState(blockPos.above()).is(Blocks.AIR)) {
                        event.getLevel().setBlock(blockPos, Blocks.FARMLAND.defaultBlockState(), 3);
                        serverPlayer.awardStat(Stats.ITEM_USED.get(event.getHeldItemStack().getItem()));
                    }
                }
            }
        }
    }
}
