package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ClientboundDeleteJobPacket(Identifier jobLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundDeleteJobPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_delete_job_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDeleteJobPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ClientboundDeleteJobPacket::jobLocation,
            ClientboundDeleteJobPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}