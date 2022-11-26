package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.item.ExcavatorItem;
import me.daqem.jobsplus.common.item.HammerItem;
import me.daqem.jobsplus.events.jobs.DiggerEvents;
import me.daqem.jobsplus.events.jobs.MinerEvents;
import me.daqem.jobsplus.handlers.*;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
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
                if (breakValidator.canBreak(state) && !(state.getBlock() instanceof ShulkerBoxBlock)) {
                    Block block = state.getBlock();
                    ItemStack stack = player.getMainHandItem();
                    if (stack.getItem() instanceof HammerItem) {
                        if (MinerEvents.lowestList.contains(block)) {
                            minerExp += ExpHandler.getEXPLowest();
                        } else if (block instanceof DropExperienceBlock && block != Blocks.SCULK) {
                            minerExp += ExpHandler.getEXPMid();
                        } else if (MinerEvents.lowList.contains(block)
                                || state.is(BlockTags.TERRACOTTA)
                                || (stack.getItem() instanceof PickaxeItem && stack.isCorrectToolForDrops(state))) {
                            minerExp += ExpHandler.getEXPLow();
                        }
                        player.awardStat(Stats.BLOCK_MINED.get(block));
                    }
                    final boolean isExcavator = stack.getItem() instanceof ExcavatorItem;
                    if (isExcavator) {
                        if (DiggerEvents.lowestList.contains(block)) {
                            diggerExp += ExpHandler.getEXPLowest();
                        } else if (DiggerEvents.lowList.contains(block)) {
                            diggerExp += ExpHandler.getEXPLow();
                        }
                        player.awardStat(Stats.BLOCK_MINED.get(block));
                    }
                    level.removeBlock(pos, false);

                    if (!player.isCreative()) {
                        int bonusLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                        int silkLevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
                        final List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, stack);
                        final List<ItemStack> stacks = ItemHandler.smeltedRawMaterials(player, drops);
                        final int exp = state.getExpDrop(level, level.random, pos, bonusLevel, silkLevel);

                        //Add drops to inventory for Digger powerup
                        if (JobGetters.hasPowerupEnabled(player, digger, CapType.POWER_UP1.get(), true) && isExcavator) {
                            for (ItemStack itemStack : stacks) {
                                ItemHandler.addItemsToInventoryOrDrop(itemStack, player, level, pos, exp);
                            }
                        }
                        //Drop items on the ground.
                        else {
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
                ModItemUtils.damageItem(toolDamage, player.getInventory().getSelected(), player);
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
            Item item = stack.getItem();
            // Add EXP drops to autosmelt powerup for hammer.
            if (item == Items.GOLD_INGOT || item == Items.COPPER_INGOT || item == Items.IRON_INGOT) {
                exp = 1;
            } else if (item == Items.NETHERITE_INGOT) {
                exp = 2;
            }

            // Drop EXP
            if (exp > 0) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp));
            }

            //Drop Item
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }

    @SuppressWarnings("all")
    public static boolean breakInRadius(BlockState state, Level level, BlockPos pos, Player player, Jobs job, int requiredLevel) {
        if (player.isCrouching() || level.isClientSide || state.getBlock() instanceof ShulkerBoxBlock) return true;
        if (JobGetters.jobIsEnabled(player, job) && JobGetters.getJobLevel(player, job) >= requiredLevel) {
            CompoundTag tag = player.getMainHandItem().getOrCreateTag();
            if (VeinMinerHandler.isNotVeinMining((ServerPlayer) player)) {
                float destroySpeed = level.getBlockState(pos).getDestroySpeed(null, null);
                ToolFunctions.breakInRadius(level, player, tag.contains("mode") ? tag.getInt("mode") : 0, (breakState) -> {
                    double hardness = breakState.getDestroySpeed(null, null);
                    boolean isEffective = player.getMainHandItem().isCorrectToolForDrops(breakState);
                    boolean verifyHardness = hardness < destroySpeed * 5 && hardness > 0;
                    return isEffective && verifyHardness;
                }, true);
            }
        } else {
            HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, JobsPlus.translatable("error.magic").withStyle(ChatFormatting.RED));
            return false;
        }
        return true;
    }
}
