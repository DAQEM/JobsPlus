package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.events.EventPlayerTick;
import me.daqem.jobsplus.events.jobs.MinerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record PacketPowerups(String str, UUID uuid) {

    public static void encode(PacketPowerups msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.str);
        buf.writeUUID(msg.uuid);
    }

    public static PacketPowerups decode(FriendlyByteBuf buf) {
        return new PacketPowerups(buf.readUtf(), buf.readUUID());
    }

    public static void handle(PacketPowerups msg, Supplier<NetworkEvent.Context> context) {
        String str = msg.str();
        UUID uuid = msg.uuid();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            if (str.equals("enable_veinminer")) {
                if (!MinerEvents.veinMinerArray.contains(uuid)) {
                    MinerEvents.veinMinerArray.add(uuid);
                }
            } else if (str.equals("disable_veinminer")) {
                MinerEvents.veinMinerArray.remove(uuid);
            } else if (str.equals("doublejump")) {
                if (!EventPlayerTick.doublejumpList.contains(uuid)) {
                    EventPlayerTick.doublejumpList.add(uuid);
                }
            }
            context.get().setPacketHandled(true);
        }
    }


}
