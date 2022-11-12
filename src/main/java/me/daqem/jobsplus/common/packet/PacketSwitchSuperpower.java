package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketSwitchSuperpower(Jobs job) {

    public static void encode(PacketSwitchSuperpower msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.job());
    }

    public static PacketSwitchSuperpower decode(FriendlyByteBuf buf) {
        return new PacketSwitchSuperpower(buf.readEnum(Jobs.class));
    }

    public static void handle(PacketSwitchSuperpower msg, Supplier<NetworkEvent.Context> context) {
        Jobs job = msg.job();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (JobGetters.getSuperPower(player, job) == 0) {
                    JobSetters.setSuperPower(player, job, 1);
                } else {
                    JobSetters.setSuperPower(player, job, 0);
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
