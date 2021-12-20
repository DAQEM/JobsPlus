package me.daqem.jobsplus.utils;

import net.minecraft.world.level.block.state.BlockState;

public interface IBreakValidator {
    boolean canBreak(BlockState state);
}
