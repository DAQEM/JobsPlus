package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.events.EventPlayerTick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public record PacketOpenMenu(UUID uuid, int jobId, int activeLeftButton, int activeRightButton, int selectedButton,
                             float scrollOffs,
                             int startIndex) {

    public static void encode(PacketOpenMenu msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.uuid);
        buf.writeInt(msg.jobId);
        buf.writeInt(msg.activeLeftButton);
        buf.writeInt(msg.activeRightButton);
        buf.writeInt(msg.selectedButton);
        buf.writeFloat(msg.scrollOffs);
        buf.writeInt(msg.startIndex);
    }

    public static PacketOpenMenu decode(FriendlyByteBuf buf) {
        return new PacketOpenMenu(buf.readUUID(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readInt());
    }

    public static void handle(PacketOpenMenu msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            Map<Float, int[]> data = new HashMap<>();
            data.put(msg.scrollOffs, new int[]{msg.jobId(), msg.activeLeftButton(), msg.activeRightButton(), msg.selectedButton(), msg.startIndex()});
            EventPlayerTick.openMenuMap.put(msg.uuid(), data);
            context.get().setPacketHandled(true);
        }
    }
}
