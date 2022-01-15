package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.common.container.backpack.BackpackHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackItemHandler extends ItemStackHandler {

    public BackpackItemHandler(int size) {
        super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
        BackpackHandler.get().setDirty();
    }

    public void upgrade(int slots) {
        if (slots <= this.stacks.size())
            return;
        NonNullList<ItemStack> oldStacks = this.stacks;
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
        for (int i = 0; i < oldStacks.size(); i++) {
            this.stacks.set(i, oldStacks.get(i));
        }
    }
}
