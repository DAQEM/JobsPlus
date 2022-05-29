package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.common.item.ExcavatorItem;
import me.daqem.jobsplus.common.item.HammerItem;
import me.daqem.jobsplus.events.jobs.DiggerEvents;
import me.daqem.jobsplus.events.jobs.MinerEvents;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.MobEffectHandler;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ToolFunctions {

    private ToolFunctions() {
    }

    public static void breakInRadius(Level level, Player player, int mode, IBreakValidator breakValidator, boolean damageTool) {
        int radius = 0;
        if (mode == 0 || mode == 1) radius = 1;
        if (mode == 2 || mode == 3) radius = 2;
        if (!level.isClientSide) {
            List<BlockPos> brokenBlocks = getBreakBlocks(level, player, radius, mode);
            Jobs miner = Jobs.MINER;
            Jobs digger = Jobs.DIGGER;
            int minerExp = 0;
            int diggerExp = 0;
            int toolDamage = -1;
            for (BlockPos pos : brokenBlocks) {
                BlockState state = level.getBlockState(pos);
                if (breakValidator.canBreak(state)) {
                    Block block = state.getBlock();
                    String blockString = block.getDescriptionId().replace("block.minecraft.", "");
                    if (player.getMainHandItem().getItem() instanceof HammerItem) {
                        if (!MinerEvents.timeoutList.contains(pos)) {
                            if (MinerEvents.lowestList.contains(blockString)) {
                                minerExp += ExpHandler.getEXPLowest();
                            } else if (block instanceof OreBlock) {
                                minerExp += ExpHandler.getEXPMid();
                            } else if (MinerEvents.lowList.contains(blockString)
                                    || state.is(BlockTags.TERRACOTTA)
                                    || (player.getMainHandItem().getItem() instanceof PickaxeItem && player.getMainHandItem().isCorrectToolForDrops(state))) {
                                minerExp += ExpHandler.getEXPLow();
                            }
                            player.awardStat(Stats.BLOCK_MINED.get(block));
                        }
                        MinerEvents.timeoutList.remove(pos);
                    }
                    final boolean isExcavator = player.getMainHandItem().getItem() instanceof ExcavatorItem;
                    if (isExcavator) {
                        if (!DiggerEvents.timeoutList.contains(pos)) {
                            if (DiggerEvents.lowestList.contains(blockString)) {
                                diggerExp += ExpHandler.getEXPLowest();
                            } else if (DiggerEvents.lowList.contains(blockString)) {
                                diggerExp += ExpHandler.getEXPLow();
                            }
                            player.awardStat(Stats.BLOCK_MINED.get(block));
                        }
                        DiggerEvents.timeoutList.remove(pos);
                    }
                    level.removeBlock(pos, false);

                    if (!player.isCreative()) {
                        int bonusLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
                        int silkLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
                        final List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, player.getMainHandItem());
                        final List<ItemStack> stacks = ItemHandler.smeltedRawMaterials(player, drops);
                        final int exp = state.getExpDrop(level, pos, bonusLevel, silkLevel);
                        if (JobGetters.hasEnabledPowerup(player, digger, CapType.POWER_UP1.get()) && isExcavator) {
                            for (ItemStack itemStack : stacks) {
                                ItemHandler.addItemsToInventoryOrDrop(itemStack, player, level, pos, exp);
                            }
                        } else {
                            dropItems(level, stacks, pos, exp);
                        }
                        if (isExcavator) {
                            DiggerEvents.dropMinerals(player, level, pos);
                        }
                    }

                    if (damageTool) {
                        toolDamage++;
                    }
                }
            }
            if (minerExp != 0) {
                ExpHandler.addJobEXP(player, miner, minerExp);
                MobEffectHandler.addPlayerPowerUpEffects(player, miner);
            }
            if (diggerExp != 0) {
                ExpHandler.addJobEXP(player, digger, diggerExp);
                MobEffectHandler.addPlayerPowerUpEffects(player, digger);
            }
            if (toolDamage > 0) {
                player.getInventory().getSelected().hurtAndBreak(toolDamage, player, player1 -> {
                });
            }
        }
    }

    public static List<BlockPos> getBreakBlocks(Level level, Player player, int radius, int mode) {
        ArrayList<BlockPos> potentialBrokenBlocks = new ArrayList<>();
        Vec3 eyePosition = player.getEyePosition(1);
        Vec3 rotation = player.getViewVector(1);
        Vec3 combined = eyePosition.add(rotation.x * 5, rotation.y * 5, rotation.z * 5);
        BlockHitResult rayTraceResult = level.clip(new ClipContext(eyePosition, combined, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        if (rayTraceResult.getType() == BlockHitResult.Type.BLOCK) {
            Direction.Axis axis = rayTraceResult.getDirection().getAxis();
            ArrayList<BlockPos> positions = new ArrayList<>();
            BlockPos origin = rayTraceResult.getBlockPos();

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        positions.add(new BlockPos(x, y, z));
                    }
                }
            }
            for (BlockPos pos : positions) {
                if (mode == 0 || mode == 2) {
                    if (axis == Direction.Axis.Y) {
                        if (pos.getY() == 0) {
                            potentialBrokenBlocks.add(origin.offset(pos));
                        }
                    } else if (axis == Direction.Axis.X) {
                        if (pos.getX() == 0) {
                            potentialBrokenBlocks.add(origin.offset(pos));
                        }
                    } else if (axis == Direction.Axis.Z) {
                        if (pos.getZ() == 0) {
                            potentialBrokenBlocks.add(origin.offset(pos));
                        }
                    }
                }
                if (mode == 1 || mode == 3) {
                    potentialBrokenBlocks.add(origin.offset(pos));
                }
            }
        }
        return potentialBrokenBlocks;
    }

    private static void dropItems(Level level, List<ItemStack> stacks, BlockPos pos, int exp) {
        for (ItemStack stack : stacks) {
            if (exp > 0) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp));
            }
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }
}
