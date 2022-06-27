package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.events.DoubleJumpEvents;
import me.daqem.jobsplus.events.jobs.MinerEvents;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record PacketPowerUps(String str, UUID uuid) {

    public static void encode(PacketPowerUps msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.str);
        buf.writeUUID(msg.uuid);
    }

    public static PacketPowerUps decode(FriendlyByteBuf buf) {
        return new PacketPowerUps(buf.readUtf(), buf.readUUID());
    }

    public static void handle(PacketPowerUps msg, Supplier<NetworkEvent.Context> context) {
        String str = msg.str();
        UUID uuid = msg.uuid();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                switch (str) {
                    case "enable_veinminer":
                        if (!MinerEvents.veinMinerArray.contains(uuid)) {
                            MinerEvents.veinMinerArray.add(uuid);
                        }
                        break;
                    case "disable_veinminer":
                        MinerEvents.veinMinerArray.remove(uuid);
                        break;
                    case "doublejump":
                        DoubleJumpEvents.attemptPlayerJump(player, new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER) ? 1 : 0, JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWER_UP3.get()) ? 1 : 0});
                        break;
                }
            }
            context.get().setPacketHandled(true);
        }
    }
}
