package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.events.DoubleJumpEvents;
import me.daqem.jobsplus.handlers.VeinMinerHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketPowerUps(String str) {

    public static void encode(PacketPowerUps msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.str);
    }

    public static PacketPowerUps decode(FriendlyByteBuf buf) {
        return new PacketPowerUps(buf.readUtf());
    }

    public static void handle(PacketPowerUps msg, Supplier<NetworkEvent.Context> context) {
        String str = msg.str();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                switch (str) {
                    case "enable_veinminer" -> VeinMinerHandler.enableVeinMining(player);
                    case "disable_veinminer" -> VeinMinerHandler.disableVeinMining(player);
                    case "doublejump" ->
                            DoubleJumpEvents.attemptPlayerJump(player, new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER, true) ? 1 : 0, JobGetters.hasPowerupEnabled(player, Jobs.BUILDER, CapType.POWER_UP3.get(), true) ? 1 : 0});
                }
            }
            context.get().setPacketHandled(true);
        }
    }
}
