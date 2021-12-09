package me.daqem.jobsplus.common.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

public class BlockItemBase extends BlockItem {


    public BlockItemBase(Block p_40565_) {
        super(p_40565_, new Properties().tab(CreativeModeTab.TAB_FOOD)); //TODO Temporary because of error.
//        super(p_40565_, new Item.Properties().tab(JobsPlus.TAB));
    }
}
