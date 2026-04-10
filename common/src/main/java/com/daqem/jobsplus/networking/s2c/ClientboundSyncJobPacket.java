package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ClientboundSyncJobPacket(Job job) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundSyncJobPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_sync_job_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncJobPacket> STREAM_CODEC = StreamCodec.composite(
            Job.Serializer.STREAM_CODEC,
            ClientboundSyncJobPacket::job,
            ClientboundSyncJobPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}