package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClientboundSyncJobPacket implements CustomPacketPayload {

    private final Job job;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncJobPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundSyncJobPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundSyncJobPacket(Job.Serializer.fromNetwork(buf, null));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundSyncJobPacket packet) {
            Job.Serializer.toNetwork(buf, packet.job);
        }
    };

    public ClientboundSyncJobPacket(Job job) {
        this.job = job;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_SYNC_JOB;
    }

    public Job getJob() {
        return job;
    }
}