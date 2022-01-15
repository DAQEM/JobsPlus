package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.client.gui.JobsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketSendMainMenuData(int[] array) {

    public static void encode(PacketSendMainMenuData msg, FriendlyByteBuf buf) {
        buf.writeVarIntArray(msg.array);
    }

    public static PacketSendMainMenuData decode(FriendlyByteBuf buf) {
        return new PacketSendMainMenuData(buf.readVarIntArray());
    }

    public static void handle(PacketSendMainMenuData msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> clientWork(msg));
            context.get().setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientWork(PacketSendMainMenuData msg) {
        int[] array = msg.array;
        Minecraft.getInstance().setScreen(new JobsScreen(array));
    }
}
