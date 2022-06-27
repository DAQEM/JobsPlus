package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.handlers.BossBarHandler;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record PacketBossBarr(String job) {

    public static void encode(PacketBossBarr msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.job());
    }

    public static PacketBossBarr decode(FriendlyByteBuf buf) {
        return new PacketBossBarr(buf.readUtf());
    }

    public static void handle(PacketBossBarr msg, Supplier<NetworkEvent.Context> context) {
        String job = msg.job();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null && player.getServer() != null) {
                if (!Objects.equals(job, "NONE")) {
                    BossBarHandler.createBossBar(player, Jobs.valueOf(job));
                } else {
                    BossBarHandler.removeAllActiveBossBars(player, player.getServer().getCustomBossEvents());
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
