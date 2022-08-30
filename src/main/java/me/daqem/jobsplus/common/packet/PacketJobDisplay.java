package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record PacketJobDisplay(String job) {

    public static void encode(PacketJobDisplay msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.job());
    }

    public static PacketJobDisplay decode(FriendlyByteBuf buf) {
        return new PacketJobDisplay(buf.readUtf());
    }

    public static void handle(PacketJobDisplay msg, Supplier<NetworkEvent.Context> context) {
        String job = msg.job();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (Objects.equals(job, "NONE")) {
                    JobSetters.setDisplay(player, -1);
                } else {
                    JobSetters.setDisplay(player, Jobs.getJobInt(Jobs.valueOf(job)));
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
