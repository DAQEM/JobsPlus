package me.daqem.jobsplus.events.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.common.item.FarmersHoeItem;
import me.daqem.jobsplus.events.jobs.FarmerEvents;
import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.ModItemUtils;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
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
                    (JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_1.get() && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_1.get())
                    || (JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_2.get() && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_2.get())
                    || (JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_3.get() && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_3.get())
                    || (JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_4.get() && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_4.get())) {
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
                                    if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get())) {
                                        if (Math.random() * 100 < 10) {
                                            if (block == Blocks.WHEAT) {
                                                dropBetterItem(player, level, blockPos, Items.HAY_BLOCK, itemUsed);
                                            } else if (block == Blocks.CARROTS) {
                                                dropBetterItem(player, level, blockPos, Items.GOLDEN_CARROT, itemUsed);
                                            } else if (block == Blocks.POTATOES) {
                                                dropBetterItem(player, level, blockPos, Items.BAKED_POTATO, itemUsed);
                                            } else if (block == Blocks.BEETROOTS) {
                                                dropBetterItem(player, level, blockPos, Items.BEETROOT_SOUP, itemUsed);
                                            }
                                        }
                                    }
                                    exp += 1;
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
                                }
                            }
                            if (block == Blocks.MELON || block == Blocks.PUMPKIN) {
                                if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get())) {
                                    if (block == Blocks.MELON) {
                                        if (Math.random() * 100 < 10) {
                                            dropBetterItem(player, level, blockPos, Items.GLISTERING_MELON_SLICE, itemUsed);
                                        }
                                    } else {
                                        if (Math.random() * 100 < 5) {
                                            dropBetterItem(player, level, blockPos, Items.PUMPKIN_PIE, itemUsed);
                                        } else if (Math.random() * 100 < 10) {
                                            dropBetterItem(player, level, blockPos, Items.CARVED_PUMPKIN, itemUsed);
                                        }
                                    }
                                }
                                if (!FarmerEvents.blockPosArrayList.contains(blockPos)) {
                                    exp += 2;
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
                                    exp += 2;
                                    ModItemUtils.damageItem(2, stack, player);
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
                                }
                            }
                            if (block instanceof SweetBerryBushBlock) {
                                if (CropHandler.stateToAge(state) >= 2) {
                                    if (CropHandler.stateToAge(state) == 2) {
                                        exp += 1;
                                        ModItemUtils.damageItem(1, stack, player);
                                    } else {
                                        exp += 2;
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
                                    exp += 2;
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
                                ArrayList<BlockPos> array = new ArrayList<>();
                                if (stack.getItem() != ModItems.FARMERS_HOE_LEVEL_1.get()) {
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
                                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, TranslatableString.get("error.magic.tool"));
                                }
                            }
                        }
                    }
                }
            }
            if (exp > 0) ExpHandler.addJobEXP(player, job, exp);
        }
    }

    public void dropHandler(ItemStack drop, Player player, Level level, BlockPos blockPos) {
        dropItems(drop, player, level, blockPos);
        if (JobGetters.hasSuperPowerEnabled(player, Jobs.FARMER)) dropItems(drop, player, level, blockPos);
        if (Math.random() * 100 < 20 && JobGetters.hasEnabledPowerup(player, Jobs.FARMER, CapType.POWER_UP1.get())) {
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
        if (itemUsed == ModItems.FARMERS_HOE_LEVEL_4.get()) {
            ItemHandler.addItemsToInventoryOrDrop(item.getDefaultInstance(), player, level, blockPos, 0);
        } else {
            ItemHandler.addFreshItemEntity(level, blockPos, item);
        }
    }
}
