package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundLeaderboardPacket(List<LeaderboardPlayer> leaderboard) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundLeaderboardPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_leaderboard_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLeaderboardPacket> STREAM_CODEC = StreamCodec.composite(
            LeaderboardPlayer.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientboundLeaderboardPacket::leaderboard,
            ClientboundLeaderboardPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}