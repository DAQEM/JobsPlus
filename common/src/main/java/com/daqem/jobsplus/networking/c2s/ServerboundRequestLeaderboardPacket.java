package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.s2c.ClientboundLeaderboardPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ServerboundRequestLeaderboardPacket implements CustomPacketPayload {

    private final ResourceLocation jobLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestLeaderboardPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundRequestLeaderboardPacket decode(RegistryFriendlyByteBuf friendlyByteBuf) {
            return new ServerboundRequestLeaderboardPacket(friendlyByteBuf.readResourceLocation());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf friendlyByteBuf, ServerboundRequestLeaderboardPacket packet) {
            friendlyByteBuf.writeResourceLocation(packet.jobLocation);
        }
    };

    public ServerboundRequestLeaderboardPacket(ResourceLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_REQUEST_LEADERBOARD;
    }

    public static void handleServerSide(ServerboundRequestLeaderboardPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer sender) {
            List<LeaderboardPlayer> leaderboard = sender.jobsplus$getLevelData().jobsplus$getSortedLeaderboard(packet.jobLocation);
            NetworkManager.sendToPlayer((ServerPlayer) sender, new ClientboundLeaderboardPacket(leaderboard));
        }
    }
}