package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.JobItemEntryHelper;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public record PacketConstructionArrows(boolean left, String job, int selectedButtonRight, boolean bulk) {

    public static void encode(PacketConstructionArrows msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.left());
        buf.writeUtf(msg.job());
        buf.writeInt(msg.selectedButtonRight());
        buf.writeBoolean(msg.bulk());
    }

    public static PacketConstructionArrows decode(FriendlyByteBuf buf) {
        return new PacketConstructionArrows(buf.readBoolean(), buf.readUtf(), buf.readInt(), buf.readBoolean());
    }

    public static void handle(PacketConstructionArrows msg, Supplier<NetworkEvent.Context> context) {
        boolean left = msg.left();
        Jobs job = Jobs.valueOf(msg.job());
        int selectedButtonRight = msg.selectedButtonRight();
        boolean bulk = msg.bulk();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null && player.getServer() != null) {
                if (left) {
                    ArrayList<ItemStack> recipe = JobItemEntryHelper.getRecipe(JobItemEntryHelper.getStacksForJob(job, JobItemEntryHelper.getItemEntriesAsArrayList()).get(selectedButtonRight), JobItemEntryHelper.getItemEntriesAsArrayList());
                    for (int l = 0; l < (bulk ? 64 : 1); ++l) {
                        ArrayList<Item> list = new ArrayList<>();
                        for (ItemStack invItem : player.getInventory().items) {
                            if (!list.contains(invItem.getItem())) {
                                for (int i = 0; i < recipe.size(); ++i) {
                                    ItemStack recipeItem = recipe.get(i);
                                    if (!invItem.is(Items.AIR)) {
                                        if (invItem.is(recipeItem.getItem())) {
                                            ItemStack containerItem = player.containerMenu.getSlot(i + 36).getItem();
                                            if (!containerItem.is(recipeItem.getItem())) {
                                                player.containerMenu.quickMoveStack(player, player.containerMenu.getSlot(i + 36).getContainerSlot() + 36);
                                            }
                                            if (containerItem.is(Items.AIR)) {
                                                if (invItem.getMaxStackSize() == 1) {
                                                    ItemStack copy = invItem.copy();
                                                    invItem.setCount(invItem.getCount() - 1);
                                                    player.containerMenu.setItem(i + 36, player.containerMenu.getStateId(), copy);
                                                    list.add(recipeItem.getItem());
                                                    JobsPlus.LOGGER.debug(copy);
                                                } else {
                                                    invItem.setCount(invItem.getCount() - 1);
                                                    ItemStack copy = invItem.copy();
                                                    copy.setCount(1);
                                                    player.containerMenu.setItem(i + 36, player.containerMenu.getStateId(), copy);
                                                    list.add(recipeItem.getItem());
                                                }
                                            } else if (containerItem.is(recipeItem.getItem()) && containerItem.getMaxStackSize() > containerItem.getCount()) {
                                                invItem.setCount(invItem.getCount() - 1);
                                                containerItem.setCount(containerItem.getCount() + 1);
                                                list.add(recipeItem.getItem());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (Slot slot : player.containerMenu.slots) {
                        if (!(slot.container instanceof Inventory)) {
                            player.containerMenu.quickMoveStack(player, slot.getContainerSlot() + 36);
                        }
                    }
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
