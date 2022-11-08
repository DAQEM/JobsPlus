package me.daqem.jobsplus.events.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.item.FarmersHoeItem;
import me.daqem.jobsplus.events.jobs.FarmerEvents;
import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.ModItemUtils;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class FarmersHoeEvents {

    @SubscribeEvent
    public void onRightClickBlockWithFarmersHoe(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack stack = player.getMainHandItem();
        if (!level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND && stack.getItem() instanceof FarmersHoeItem && !player.isCrouching()) {
            int exp = 0;
            Jobs job = Jobs.FARMER;
            Block clicked = level.getBlockState(event.getPos()).getBlock();
            if ((clicked instanceof CropBlock || clicked == Blocks.MELON || clicked == Blocks.PUMPKIN
                    || clicked == Blocks.NETHER_WART || clicked instanceof SweetBerryBushBlock
                    || clicked == Blocks.SUGAR_CANE || clicked == Blocks.CACTUS
                    || clicked == Blocks.BAMBOO || clicked == Blocks.KELP_PLANT
                    || clicked.getDescriptionId().equals("block.farmersdelight.tomatoes"))
                    &&
                    (JobGetters.getJobLevel(player, job) >= ModRecipeManager.getRequiredJobLevelServer(stack))) {
                if (!stack.getOrCreateTag().contains("mode")) stack.getOrCreateTag().putInt("mode", 0);
                int mode = stack.getOrCreateTag().getInt("mode");
                if (mode == 0 || mode == 1 || mode == 2) {
                    Item itemUsed = player.getMainHandItem().getItem();
                    for (int x = -mode; x <= mode; x++) {
                        for (int z = -mode; z <= mode; z++) {
                            BlockPos blockPos = new BlockPos(event.getPos().offset(x, 0, z));
                            BlockState state = level.getBlockState(blockPos);
                            Block block = state.getBlock();
                            if (block instanceof CropBlock cropBlock) {
                                if (cropBlock.isMaxAge(state)) {
                                    if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true) && Math.random() * 100 < 10) {
                                        dropBetterItem(player, level, blockPos, block == Blocks.WHEAT ? Items.HAY_BLOCK : block == Blocks.CARROTS ? Items.GOLDEN_CARROT : block == Blocks.POTATOES ? Items.BAKED_POTATO : block == Blocks.BEETROOTS ? Items.BEETROOT_SOUP : Items.AIR, itemUsed);
                                    }
                                    exp += removeOneDropAndReplant(event, player, level, stack, blockPos, state, block, ExpHandler.getEXPLowest());
                                }
                            }
                            if (block == Blocks.MELON || block == Blocks.PUMPKIN) {
                                if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true) && Math.random() * 100 < 10) {
                                    dropBetterItem(player, level, blockPos, block == Blocks.MELON ? Items.GLISTERING_MELON_SLICE : Items.PUMPKIN_PIE, itemUsed);
                                }
                                if (!FarmerEvents.blockPosArrayList.contains(blockPos)) {
                                    exp += ExpHandler.getEXPLow();
                                }
                                ModItemUtils.damageItem(2, stack, player);
                                List<ItemStack> drops = state.getDrops(ItemHandler.drops(level, blockPos, player, stack));
                                for (ItemStack drop : drops) {
                                    if (!FarmerEvents.blockPosArrayList.contains(blockPos)) {
                                        dropHandler(drop, player, level, blockPos);
                                    } else {
                                        dropItems(drop, player, level, blockPos);
                                    }
                                }
                                level.removeBlock(blockPos, false);
                            }
                            if (block == Blocks.NETHER_WART) {
                                if (CropHandler.stateToAge(state) == 3) {
                                    exp += removeOneDropAndReplant(event, player, level, stack, blockPos, state, block, ExpHandler.getEXPLow());
                                }
                            }
                            if (block instanceof SweetBerryBushBlock) {
                                if (CropHandler.stateToAge(state) >= 2) {
                                    if (CropHandler.stateToAge(state) == 2) {
                                        exp += ExpHandler.getEXPLowest();
                                        ModItemUtils.damageItem(1, stack, player);
                                    } else {
                                        exp += ExpHandler.getEXPLow();
                                        ModItemUtils.damageItem(2, stack, player);
                                    }
                                    event.setCanceled(true);
                                    List<ItemStack> drops = state.getDrops(ItemHandler.drops(level, blockPos, player, stack));
                                    for (ItemStack drop : drops) {
                                        dropHandler(drop, player, level, blockPos);
                                    }
                                    level.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(BlockStateProperties.AGE_3, 1));
                                }
                            }
                            if (block.getDescriptionId().equals("block.farmersdelight.tomatoes")) {
                                if (CropHandler.stateToAge(state) == 7) {
                                    exp += ExpHandler.getEXPLowest();
                                    ModItemUtils.damageItem(1, stack, player);
                                    event.setCanceled(true);
                                    List<ItemStack> drops = state.getDrops(ItemHandler.drops(level, blockPos, player, stack));
                                    for (ItemStack drop : drops) {
                                        dropHandler(drop, player, level, blockPos);
                                    }
                                    level.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(BlockStateProperties.AGE_7, 5));
                                }
                            }
                            if (block == Blocks.SUGAR_CANE || block == Blocks.CACTUS || block == Blocks.BAMBOO || block == Blocks.KELP_PLANT) {
                                if (stack.getItem() != ModItems.FARMERS_HOE_LEVEL_1.get()) {
                                    ArrayList<BlockPos> array = new ArrayList<>();
                                    ModItemUtils.damageItem(2, stack, player);
                                    if (block == Blocks.KELP) {
                                        array.add(blockPos);
                                        BlockPos pos = blockPos.above();
                                        while (level.getBlockState(pos).getBlock() == Blocks.KELP_PLANT
                                                || level.getBlockState(pos).getBlock() == Blocks.KELP) {
                                            array.add(pos);
                                            pos = pos.above();
                                        }
                                        for (int i = array.size() - 1; i >= 0; i--) {
                                            level.setBlock(array.get(i), Blocks.WATER.defaultBlockState(), 19);
                                            boolean giveDrop = true;
                                            if (array.size() == 1) {
                                                if (level.getBlockState(array.get(i).below()).getBlock() != Blocks.KELP_PLANT) {
                                                    level.setBlock(array.get(i), Blocks.KELP.defaultBlockState(), 19);
                                                    giveDrop = false;
                                                } else {
                                                    level.setBlock(array.get(i).below(), Blocks.KELP.defaultBlockState(), 19);
                                                }
                                            }
                                            if (giveDrop) {
                                                ItemHandler.addItemsToInventoryOrDrop(Items.KELP.getDefaultInstance(), player, level, blockPos, 0);
                                            }
                                            array.remove(i);
                                            if (!FarmerEvents.blockPosArrayList.contains(blockPos)) {
                                                exp += ExpHandler.getEXPLow();
                                            }
                                        }
                                    } else {
                                        Block tempBlock = Blocks.SUGAR_CANE;
                                        if (block == Blocks.CACTUS) tempBlock = Blocks.CACTUS;
                                        if (block == Blocks.BAMBOO) tempBlock = Blocks.BAMBOO;
                                        array.add(blockPos);
                                        BlockPos pos = blockPos.above();
                                        while (level.getBlockState(pos).getBlock() == tempBlock) {
                                            array.add(pos);
                                            pos = pos.above();
                                        }
                                        for (int i = array.size() - 1; i >= 0; i--) {
                                            level.removeBlock(array.get(i), false);
                                            ItemHandler.addItemsToInventoryOrDrop(tempBlock.asItem().getDefaultInstance(), player, level, blockPos, 0);
                                            if (!FarmerEvents.blockPosArrayList.contains(blockPos)) {
                                                exp += ExpHandler.getEXPLow();
                                            }
                                        }
                                    }
                                } else {
                                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, JobsPlus.translatable("error.magic.tool").withStyle(ChatFormatting.RED));
                                }
                            }
                        }
                    }
                }
            }
            if (exp > 0) ExpHandler.addJobEXP(player, job, exp);
        }
    }

    private int removeOneDropAndReplant(PlayerInteractEvent.RightClickBlock event, Player player, Level level, ItemStack stack, BlockPos blockPos, BlockState state, Block block, int exp) {
        ModItemUtils.damageItem(1, stack, player);
        List<ItemStack> drops = state.getDrops(ItemHandler.drops(level, blockPos, player, stack));
        for (ItemStack drop : drops) {
            if (drop.getCount() > 1) {
                drop.setCount(drop.getCount() - 1);
            }
            dropHandler(drop, player, level, blockPos);
        }
        player.level.setBlockAndUpdate(blockPos, block.defaultBlockState());
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        return exp;
    }

    public void dropHandler(ItemStack drop, Player player, Level level, BlockPos blockPos) {
        dropItems(drop, player, level, blockPos);
        if (JobGetters.hasSuperPowerEnabled(player, Jobs.FARMER, true)) dropItems(drop, player, level, blockPos);
        if (Math.random() * 100 < 20 && JobGetters.hasPowerupEnabled(player, Jobs.FARMER, CapType.POWER_UP1.get(), true)) {
            dropItems(drop, player, level, blockPos);
        }
    }

    public void dropItems(ItemStack drop, Player player, Level level, BlockPos blockPos) {
        if (player.getMainHandItem().getItem() == ModItems.FARMERS_HOE_LEVEL_4.get())
            ItemHandler.addItemsToInventoryOrDrop(drop, player, level, blockPos, 0);
        else
            ItemHandler.addFreshItemEntity(level, blockPos, new ItemStack(drop.getItem(), drop.getCount()));
    }

    public void dropBetterItem(Player player, Level level, BlockPos blockPos, Item item, Item itemUsed) {
        if (item == Items.AIR) return;
        if (itemUsed == ModItems.FARMERS_HOE_LEVEL_4.get()) {
            ItemHandler.addItemsToInventoryOrDrop(item.getDefaultInstance(), player, level, blockPos, 0);
        } else {
            ItemHandler.addFreshItemEntity(level, blockPos, item);
        }
    }
}
