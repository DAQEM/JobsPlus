package com.daqem.jobsplus.networking.sync.job;

import com.daqem.jobsplus.client.player.JobsClientPlayer;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundUpdateJobPacket implements CustomPacketPayload {

    private final Job job;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateJobPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundUpdateJobPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundUpdateJobPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundUpdateJobPacket packet) {
            Job.Serializer.toNetwork(buf, packet.job);
        }
    };

    public ClientboundUpdateJobPacket(Job job) {
        this.job = job;
    }

    public ClientboundUpdateJobPacket(FriendlyByteBuf friendlyByteBuf) {
        JobsPlayer player = null;
        if (Minecraft.getInstance().player instanceof JobsPlayer jobsPlayer) {
            player = jobsPlayer;
        }
        this.job = Job.Serializer.fromNetwork(friendlyByteBuf, player);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_UPDATE_JOB;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundUpdateJobPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsClientPlayer player) {
            player.jobsplus$replaceJob(packet.job);
        }
    }
}
