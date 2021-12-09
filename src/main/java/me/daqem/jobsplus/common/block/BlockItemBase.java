package me.daqem.jobsplus.common.block;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockItemBase extends BlockItem {


    public BlockItemBase(Block p_40565_) {
        super(p_40565_, new Item.Properties().tab(JobsPlus.TAB));
    }
}
