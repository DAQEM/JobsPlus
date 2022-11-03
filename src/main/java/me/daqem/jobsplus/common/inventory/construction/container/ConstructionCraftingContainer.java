package me.daqem.jobsplus.common.inventory.construction.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;

public class ConstructionCraftingContainer implements Container, StackedContentsCompatible {
    public final AbstractContainerMenu menu;
    private final NonNullList<ItemStack> items;
    private final int width;
    private final int height;

    public ConstructionCraftingContainer(AbstractContainerMenu menu, int width, int height) {
        this.items = NonNullList.withSize(width * height, ItemStack.EMPTY);
        this.menu = menu;
        this.width = width;
        this.height = height;
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for (ItemStack itemstack : this.items) {
            stackedContents.accountSimpleStack(itemstack);
        }
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(i);
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, p_18942_, p_18943_);
        if (!itemstack.isEmpty()) {
            this.menu.slotsChanged(this);
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.items, i);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        this.items.set(p_18944_, p_18945_);
        this.menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
