package me.daqem.jobsplus.events.item;

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
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class FarmersHoeEvents {

    @SubscribeEvent
    public void onRightClickBlockWithFarmersHoe(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        Level level = event.getWorld();
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
                    (JobGetters.getJobLevel(player, job) >= 5 && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_1.get())
                    || (JobGetters.getJobLevel(player, job) >= 25 && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_2.get())
                    || (JobGetters.getJobLevel(player, job) >= 50 && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_3.get())
                    || (JobGetters.getJobLevel(player, job) >= 75 && stack.getItem() == ModItems.FARMERS_HOE_LEVEL_4.get())) {
                if (!stack.getOrCreateTag().contains("mode")) stack.getOrCreateTag().putInt("mode", 0);
                int mode = stack.getOrCreateTag().getInt("mode");
                if (mode == 0 || mode == 1 || mode == 2) {
                    Vec3 eyePosition = player.getEyePosition(1);
                    Vec3 rotation = player.getViewVector(1);
                    for (int x = -mode; x <= mode; x++) {
                        for (int z = -mode; z <= mode; z++) {
                            BlockPos blockPos = level.clip(new ClipContext(eyePosition, eyePosition.add(rotation.x * 5, rotation.y * 5, rotation.z * 5), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos().offset(x, 0, z);
                            BlockState state = level.getBlockState(blockPos);
                            Block block = state.getBlock();
                            if (block instanceof CropBlock cropBlock) {
                                if (cropBlock.isMaxAge(state)) {
                                    exp += 1;
                                    ModItemUtils.damageItem(1, stack, player);
                                    List<ItemStack> drops = state.getDrops(drops(level, blockPos, player, stack));
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
                                exp += 2;
                                ModItemUtils.damageItem(2, stack, player);
                                List<ItemStack> drops = state.getDrops(drops(level, blockPos, player, stack));
                                for (ItemStack drop : drops) {
                                    dropHandler(drop, player, level, blockPos);
                                }
                                level.removeBlock(blockPos, false);
                            }
                            if (block == Blocks.NETHER_WART) {
                                if (CropHandler.stateToAge(state) == 3) {
                                    exp += 2;
                                    ModItemUtils.damageItem(2, stack, player);
                                    List<ItemStack> drops = state.getDrops(drops(level, blockPos, player, stack));
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
                                    List<ItemStack> drops = state.getDrops(drops(level, blockPos, player, stack));
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
                                    List<ItemStack> drops = state.getDrops(drops(level, blockPos, player, stack));
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
                                                ItemHandler.addItemsToInventoryOrDrop(Items.KELP.getDefaultInstance(), player, level, blockPos);
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
                                            ItemHandler.addItemsToInventoryOrDrop(tempBlock.asItem().getDefaultInstance(), player, level, blockPos);
                                            if (!FarmerEvents.blockPosArrayList.contains(blockPos)) {
                                                exp += ExpHandler.getEXPLow();
                                            }
                                        }
                                    }
                                } else {
                                    HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic.tool"));
                                }
                            }
                        }
                    }
                }
            } else {
                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
            }
            if (exp > 0) ExpHandler.addJobEXP(player, job, exp);
        }
    }


    public LootContext.Builder drops(Level level, BlockPos pos, Player player, ItemStack stack) {
        return new LootContext.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()))
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.TOOL, stack);
    }

    public void dropHandler(ItemStack drop, Player player, Level level, BlockPos blockPos) {
        if (player.getMainHandItem().getItem() == ModItems.FARMERS_HOE_LEVEL_4.get())
            ItemHandler.addItemsToInventoryOrDrop(drop, player, level, blockPos);
        else
            level.addFreshEntity(new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, new ItemStack(drop.getItem(), drop.getCount())));

    }
}
