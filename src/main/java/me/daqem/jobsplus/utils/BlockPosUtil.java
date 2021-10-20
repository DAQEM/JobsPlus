package me.daqem.jobsplus.utils;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;

public class BlockPosUtil {

    public static boolean testAllSides(ArrayList<BlockPos> timeoutList, BlockPos pos) {
        if (timeoutList.contains(pos)) {
            return false;
        } else if (timeoutList.contains(pos.north())) {
            return false;
        } else if (timeoutList.contains(pos.east())) {
            return false;
        } else if (timeoutList.contains(pos.south())) {
            return false;
        } else if (timeoutList.contains(pos.west())) {
            return false;
        } else if (timeoutList.contains(pos.above())) {
            return false;
        } else return !timeoutList.contains(pos.below());
    }
}
