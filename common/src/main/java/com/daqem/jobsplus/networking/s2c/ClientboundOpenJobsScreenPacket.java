package com.daqem.jobsplus.networking.s2c;

import java.util.List;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.network.codec.ByteBufCodecs;
import org.jetbrains.annotations.NotNull;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.job.Job;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundOpenJobsScreenPacket(List<Job> jobs, double coins) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundOpenJobsScreenPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_open_jobs_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenJobsScreenPacket> STREAM_CODEC = StreamCodec.composite(
            Job.Serializer.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientboundOpenJobsScreenPacket::jobs,
            ByteBufCodecs.DOUBLE,
            ClientboundOpenJobsScreenPacket::coins,
            ClientboundOpenJobsScreenPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
