package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientboundLeaderboardPacket implements CustomPacketPayload {

    private final List<LeaderboardPlayer> leaderboard;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLeaderboardPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundLeaderboardPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundLeaderboardPacket(buf.readList(LeaderboardPlayer::fromNetwork));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundLeaderboardPacket packet) {
            buf.writeCollection(packet.leaderboard, (b, p) -> p.toNetwork(b));
        }
    };

    public ClientboundLeaderboardPacket(List<LeaderboardPlayer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_LEADERBOARD;
    }

    public List<LeaderboardPlayer> getLeaderboard() {
        return leaderboard;
    }
}