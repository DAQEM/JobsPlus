package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.sync.coin.ClientBoundUpdateCoinsPacket;
import com.daqem.jobsplus.networking.sync.job.ClientboundUpdateJobsPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ServerboundRequestJobsPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestJobsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundRequestJobsPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundRequestJobsPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundRequestJobsPacket packet) {
        }
    };

    public ServerboundRequestJobsPacket() {
    }

    public ServerboundRequestJobsPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_REQUEST_JOBS;
    }

    public static void handleServerSide(ServerboundRequestJobsPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer jobsServerPlayer) {
            NetworkManager.sendToPlayer(jobsServerPlayer.jobsplus$getServerPlayer(), new ClientboundUpdateJobsPacket(jobsServerPlayer.jobsplus$getJobs()));
            NetworkManager.sendToPlayer(jobsServerPlayer.jobsplus$getServerPlayer(), new ClientBoundUpdateCoinsPacket(jobsServerPlayer.jobsplus$getCoins()));
        }
    }
}
