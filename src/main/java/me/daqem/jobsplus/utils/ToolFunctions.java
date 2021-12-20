package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.events.jobs.MinerEvents;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
            Jobs job = Jobs.MINER;
            int exp = 0;
            for (BlockPos pos : brokenBlocks) {
                BlockState state = level.getBlockState(pos);
                if (breakValidator.canBreak(state)) {
                    Block block = level.getBlockState(pos).getBlock();
                    if (!MinerEvents.timeoutList.contains(pos)) {
                        if (block instanceof OreBlock) {
                            exp += ExpHandler.getEXPMid(player, job);
                        } else if (MinerEvents.lowList.contains(block.getDescriptionId().replace("block.minecraft.", ""))) {
                            exp += ExpHandler.getEXPLow(player, job);
                        }
                        player.awardStat(Stats.BLOCK_MINED.get(block));
                    }
                    MinerEvents.timeoutList.remove(pos);
                    level.removeBlock(pos, false);

                    if (!player.isCreative()) {
                        BlockPos offsetPos = new BlockPos(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
                        dropItems(level, Block.getDrops(state, (ServerLevel) level, pos, null, player, player.getMainHandItem()), offsetPos);
                    }

                    if (damageTool) {
                        player.getInventory().getSelected().hurtAndBreak(1, player, player1 -> {
                        });
                    }
                }
            }
            if (exp != 0) {
                ExpHandler.addJobEXP(player, job, exp);
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

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        positions.add(new BlockPos(x, y, z));
                    }
                }
            }

            BlockPos origin = rayTraceResult.getBlockPos();
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

    private static void dropItems(Level level, List<ItemStack> stacks, BlockPos pos) {
        for (ItemStack stack : stacks) {
            ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            level.addFreshEntity(itemEntity);
        }
    }
}
