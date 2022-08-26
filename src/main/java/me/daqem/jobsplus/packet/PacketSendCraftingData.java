package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.SideProxy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketSendCraftingData(CompoundTag tag) {

    public static void encode(PacketSendCraftingData msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }

    public static PacketSendCraftingData decode(FriendlyByteBuf buf) {
        return new PacketSendCraftingData(buf.readNbt());
    }

    public static void handle(PacketSendCraftingData msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> clientWork(msg));
            context.get().setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientWork(PacketSendCraftingData msg) {
        CompoundTag tag = msg.tag;
        SideProxy.Client.setEntries(tag);
    }
}
