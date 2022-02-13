package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.client.gui.JobsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketSendMainMenuData(int[] array, int jobId, int activeLeftButton, int activeRightButton,
                                     int selectedButton, float scrollOffs, int startIndex) {

    public static void encode(PacketSendMainMenuData msg, FriendlyByteBuf buf) {
        buf.writeVarIntArray(msg.array);
        buf.writeInt(msg.jobId);
        buf.writeInt(msg.activeLeftButton);
        buf.writeInt(msg.activeRightButton);
        buf.writeInt(msg.selectedButton);
        buf.writeFloat(msg.scrollOffs);
        buf.writeInt(msg.startIndex);
    }

    public static PacketSendMainMenuData decode(FriendlyByteBuf buf) {
        return new PacketSendMainMenuData(buf.readVarIntArray(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readInt());
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
        int jobId = msg.jobId;
        int activeLeftButton = msg.activeLeftButton;
        int activeRightButton = msg.activeRightButton;
        int selectedButton = msg.selectedButton;
        float scrollOffs = msg.scrollOffs;
        int startIndex = msg.startIndex;
        Minecraft.getInstance().setScreen(new JobsScreen(array, jobId, activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
    }
}
