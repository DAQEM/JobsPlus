package me.daqem.jobsplus.handlers;

import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class CropHandler {

    public static int stateToAge(BlockState blockState) {
        try {
            return Integer.parseInt(Arrays.toString(blockState.getValues().values().toArray()).replace("[", "").replace("]", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String stateToAgeString(BlockState blockState) {
        try {
            return Arrays.toString(blockState.getValues().values().toArray()).replace("[", "").replace("]", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
