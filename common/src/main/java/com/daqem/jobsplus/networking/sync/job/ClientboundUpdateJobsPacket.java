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

import java.util.List;

public class ClientboundUpdateJobsPacket implements CustomPacketPayload {

    private final List<Job> jobs;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateJobsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundUpdateJobsPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundUpdateJobsPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundUpdateJobsPacket packet) {
            buf.writeCollection(packet.jobs, Job.Serializer::toNetwork);
        }
    };

    public ClientboundUpdateJobsPacket(List<Job> jobs) {
        this.jobs = jobs;
    }

    public ClientboundUpdateJobsPacket(FriendlyByteBuf friendlyByteBuf) {
        JobsPlayer player;
        if (Minecraft.getInstance().player instanceof JobsPlayer jobsPlayer) {
            player = jobsPlayer;
        } else {
            player = null;
        }
        this.jobs = friendlyByteBuf.readList(friendlyByteBuf1 -> Job.Serializer.fromNetwork(friendlyByteBuf1, player));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_UPDATE_JOBS;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundUpdateJobsPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsClientPlayer player) {
            player.jobsplus$replaceJobs(packet.jobs);
        }
    }
}
