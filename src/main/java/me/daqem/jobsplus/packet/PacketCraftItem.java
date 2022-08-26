package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobItemEntryHelper;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public record PacketCraftItem(ItemStack stack, boolean bulk, String job) {

    public static void encode(PacketCraftItem msg, FriendlyByteBuf buf) {
        buf.writeItemStack(msg.stack(), false);
        buf.writeBoolean(msg.bulk());
        buf.writeUtf(msg.job);
    }

    public static PacketCraftItem decode(FriendlyByteBuf buf) {
        return new PacketCraftItem(buf.readItem(), buf.readBoolean(), buf.readUtf());
    }

    public static void handle(PacketCraftItem msg, Supplier<NetworkEvent.Context> context) {
        ItemStack stack = msg.stack();
        boolean bulk = msg.bulk();
        Jobs job = Jobs.valueOf(msg.job());
        int amount = 65;
        int freeSlots = 0;
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null && player.getServer() != null) {
                if (JobGetters.getJobLevel(player, job) >= JobItemEntryHelper.getRequiredLevelForStack(stack)) {
                    AbstractContainerMenu containerMenu = player.containerMenu;
                    if (bulk) {
                        for (ItemStack item : player.getInventory().items) {
                            if (item.getItem() == Items.AIR) ++freeSlots;
                        }

                        for (int i = 36; i < 36 + 25; i++) {
                            if (containerMenu.getSlot(i).getItem().getItem() != Items.AIR) {
                                final int count = containerMenu.getSlot(i).getItem().getCount();
                                if (count < amount) {
                                    amount = count;
                                }
                            }
                        }
                        if (stack.getMaxStackSize() == 1) {
                            if (freeSlots < amount) amount = freeSlots;
                        } else if (stack.getMaxStackSize() == 16) {
                            amount = freeSlots == 1 ? 16 : freeSlots == 2 ? 32 : freeSlots == 3 ? 48 : 64;
                        }
                    }

                    if (amount == 65) amount = 1;

                    final ArrayList<ItemStack> recipe = JobItemEntryHelper.getRecipe(stack, JobItemEntryHelper.getItemEntriesAsArrayList());
                    int correct = 0;
                    for (int i = 36; i < 36 + 25; i++) {
                        final ItemStack item = containerMenu.getSlot(i).getItem();
                        if (recipe.get(i - 36).equals(item.getItem().getDefaultInstance(), true)
                                || (recipe.get(i - 36).getTag() == null && item.getTag() == null)
                                || recipe.get(i - 36).equals(item, true)) {
                            ++correct;
                        }
                    }

                    if (correct == 25) {
                        for (int a = 0; a < amount; ++a) {
                            for (int i = 36; i < 36 + 25; i++) {
                                final ItemStack item = containerMenu.getSlot(i).getItem();
                                if (item.getItem() != Items.AIR) {
                                    if (recipe.get(i - 36).equals(item.getItem().getDefaultInstance(), true)
                                            || (recipe.get(i - 36).getTag() == null && item.getTag() == null)
                                            || recipe.get(i - 36).equals(item, true)) {
                                        if ((stack.getItem().getClass() == item.getItem().getClass())
                                                && recipe.get(i - 36).equals(item.getItem().getDefaultInstance(), true)) {
                                            stack.setTag(item.getTag());
                                        }
                                        if (item.getCount() == 1) {
                                            containerMenu.setItem(i, containerMenu.getStateId(), ItemStack.EMPTY);
                                        } else {
                                            item.setCount(item.getCount() - 1);
                                            containerMenu.setItem(i, containerMenu.getStateId(), item);
                                        }
                                    }
                                }
                            }
                            stack.setCount(1);
                            player.getInventory().add(stack);
                            if (player.getInventory().getFreeSlot() == -1) return;
                        }
                    }
                }
            }
        }
        //TODO Check if items are correct
        context.get().setPacketHandled(true);
    }
}
