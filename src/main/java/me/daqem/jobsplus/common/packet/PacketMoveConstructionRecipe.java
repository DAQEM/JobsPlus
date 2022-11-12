package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.common.inventory.construction.ConstructionMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketMoveConstructionRecipe(boolean hasShiftDown, CompoundTag recipeData) {

    public static void encode(PacketMoveConstructionRecipe msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.hasShiftDown);
        buf.writeNbt(msg.recipeData);
    }

    public static PacketMoveConstructionRecipe decode(FriendlyByteBuf buf) {
        return new PacketMoveConstructionRecipe(buf.readBoolean(), buf.readNbt());
    }

    public static void handle(PacketMoveConstructionRecipe msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (player.containerMenu instanceof ConstructionMenu menu) {
                    for (int amount = 0; amount < (msg.hasShiftDown() ? 64 : 1); amount++) {
                        for (int craftSlotIndex = 1; craftSlotIndex < 26; craftSlotIndex++) {
                            String craftingSlotItemName = msg.recipeData().getString(String.valueOf(craftSlotIndex));
                            for (int invSlotIndex = 26; invSlotIndex < 62; invSlotIndex++) {
                                ItemStack inventorySlotItem = menu.getSlot(invSlotIndex).getItem();
                                String inventorySlotItemName = inventorySlotItem.getDescriptionId();
                                if (craftingSlotItemName.equals(inventorySlotItemName)) {
                                    if (!inventorySlotItem.hasTag()) {
                                        if (!menu.getSlot(craftSlotIndex).hasItem() ||
                                                menu.getSlot(craftSlotIndex).getItem().is(menu.getSlot(invSlotIndex).getItem().getItem())) {
                                            ItemStack copy = inventorySlotItem.copy();
                                            copy.setCount(1);
                                            menu.getSlot(invSlotIndex).remove(1);
                                            menu.getSlot(craftSlotIndex).safeInsert(copy);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            context.get().setPacketHandled(true);
        }
    }
}
