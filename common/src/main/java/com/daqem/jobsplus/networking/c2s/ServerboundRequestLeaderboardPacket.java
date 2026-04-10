package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.s2c.ClientboundLeaderboardPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.knot.Knot;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record ServerboundRequestLeaderboardPacket(Identifier jobLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundRequestLeaderboardPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_request_leaderboard_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestLeaderboardPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ServerboundRequestLeaderboardPacket::jobLocation,
            ServerboundRequestLeaderboardPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer sender) {
            List<LeaderboardPlayer> leaderboard = sender.jobsplus$getLevelData().jobsplus$getSortedLeaderboard(this.jobLocation);
            Knot.NETWORKING.sendToPlayer((ServerPlayer) sender, new ClientboundLeaderboardPacket(leaderboard));
        }
    }
}