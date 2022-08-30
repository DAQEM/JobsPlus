package me.daqem.jobsplus.common.inventory;

import me.daqem.jobsplus.common.inventory.slot.BackpackContainerSlot;
import me.daqem.jobsplus.common.inventory.slot.LockableSlot;
import me.daqem.jobsplus.init.ModMenuTypes;
import me.daqem.jobsplus.utils.BackpackUtils;
import me.daqem.jobsplus.utils.enums.Backpack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class BackpackMenu extends AbstractContainerMenu {

    public final IItemHandler handler;
    private final Backpack tier;
    private final UUID uuid;


    public BackpackMenu(final int windowId, final Inventory playerInventory, UUID uuidIn, Backpack tierIn, IItemHandler handler) {
        super(ModMenuTypes.BACKPACK.get(), windowId);

        this.uuid = uuidIn;
        this.handler = handler;
        this.tier = tierIn;

        addPlayerSlots(playerInventory);
        addMySlots();
    }

    public static BackpackMenu fromNetwork(final int windowId, final Inventory playerInventory, FriendlyByteBuf data) {
        UUID uuidIn = data.readUUID();
        Backpack tier = Backpack.values()[data.readInt()];
        return new BackpackMenu(windowId, playerInventory, uuidIn, tier, new ItemStackHandler(tier.slots));
    }

    public Backpack getTier() {
        return this.tier;
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }


    @Override
    public void clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull Player player) {
        if (clickTypeIn == ClickType.SWAP)
            return;
        if (slot >= 0) getSlot(slot).container.setChanged();
        super.clicked(slot, dragType, clickTypeIn, player);
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            int bagSlotCount = this.slots.size();
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index < playerIn.getInventory().items.size()) {
                if (!this.moveItemStackTo(slotStack, playerIn.getInventory().items.size(), bagSlotCount, false))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(slotStack, 0, playerIn.getInventory().items.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return itemstack;
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int originX = this.tier.slotXOffset;
        int originY = this.tier.slotYOffset;

        //Hotbar
        for (int col = 0; col < 9; col++) {
            int x = originX + col * 18;
            int y = originY + 58;
            addSlot(playerInventory, col, x, y);
        }

        //Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = originX + col * 18;
                int y = originY + row * 18;
                int index = (col + row * 9) + 9;
                addSlot(playerInventory, index, x, y);
            }
        }
    }

    private void addSlot(Inventory playerInventory, int col, int x, int y) {
        Optional<UUID> uuidOptional = BackpackUtils.getUUID(playerInventory.items.get(col));
        boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
        this.addSlot(new LockableSlot(playerInventory, col, x + 1, y + 1, lockMe));
    }

    private void addMySlots() {
        if (this.handler == null) return;

        int cols = this.tier.slotCols;
        int rows = this.tier.slotRows;

        int slotIndex = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = 7 + col * 18;
                int y = 17 + row * 18;

                if (row > 7 && col > 2 && col < 13 && this.tier == Backpack.HUGE)
                    continue;

                this.addSlot(new BackpackContainerSlot(this.handler, slotIndex, x + 1, y + 1));
                slotIndex++;
                if (slotIndex >= this.tier.slots)
                    break;
            }
        }

    }
}
