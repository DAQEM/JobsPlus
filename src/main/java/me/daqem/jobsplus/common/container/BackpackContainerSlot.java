package me.daqem.jobsplus.common.container;

import me.daqem.jobsplus.utils.BackpackUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BackpackContainerSlot extends SlotItemHandler {

    public BackpackContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return super.getMaxStackSize();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return BackpackUtils.filterItem(stack);
    }

}
