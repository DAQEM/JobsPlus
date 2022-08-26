package me.daqem.jobsplus.common.container.construction;

import me.daqem.jobsplus.SideProxy;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModBlocks;
import me.daqem.jobsplus.init.ModMenuTypes;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ConstructionMenu extends AbstractContainerMenu {


    final Container constructingSlots = new SimpleContainer(26) {
        public void setChanged() {
            super.setChanged();
            ConstructionMenu.this.slotsChanged(this);
        }
    };
    private final ContainerLevelAccess access;
    private CompoundTag enabledJobsTag;

    public ConstructionMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
        this.enabledJobsTag = buf.readNbt();
        if (enabledJobsTag == null || enabledJobsTag.isEmpty())
            HotbarMessageHandler.sendHotBarMessageClient(ChatColor.red() + "Press '" + SideProxy.Client.OPEN_GUI_KEYBIND.getKey().getDisplayName().getString().toUpperCase() + "' first, and start a job.");
    }

    public ConstructionMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
        super(ModMenuTypes.CONSTRUCTION.get(), containerId);
        this.access = access;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 170));
        }

        for (int l = 0; l < 5; ++l) {
            for (int m = 0; m < 5; ++m) {
                this.addSlot(new Slot(constructingSlots, m + l * 5, 44 + m * 18, -15 + l * 18));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (i <= 35) {
                if (!this.moveItemStackTo(itemStack1, 36, 61, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (i <= 60) {
                if (!this.moveItemStackTo(itemStack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
            if (i == 0) {
                player.drop(itemStack1, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.CONSTRUCTION_TABLE.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((p_39575_, p_39576_) -> this.clearContainer(player, this.constructingSlots));
    }

    public CompoundTag getDataTag() {
        return enabledJobsTag;
    }

    public Container getConstructingSlots() {
        return constructingSlots;
    }
}
