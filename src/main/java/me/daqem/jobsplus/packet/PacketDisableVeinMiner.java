package me.daqem.jobsplus.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketDisableVeinMiner() {

    public static void encode(PacketDisableVeinMiner msg, FriendlyByteBuf buf) {
    }

    public static PacketDisableVeinMiner decode(FriendlyByteBuf buf) {
        return new PacketDisableVeinMiner();
    }

    public static void handle(PacketDisableVeinMiner msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(PacketDisableVeinMiner::clientWork);
            context.get().setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientWork() {
        Minecraft.getInstance().player.chat("/veinminer disable");
    }
}
