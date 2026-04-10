package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record ClientboundPlayerJobsPacket(UUID playerUUID, List<Job> jobs) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundPlayerJobsPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_player_jobs_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerJobsPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ClientboundPlayerJobsPacket::playerUUID,
            Job.Serializer.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientboundPlayerJobsPacket::jobs,
            ClientboundPlayerJobsPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}