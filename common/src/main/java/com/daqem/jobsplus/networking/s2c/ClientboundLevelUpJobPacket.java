package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ClientboundLevelUpJobPacket(Identifier jobLocation, int level) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundLevelUpJobPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_level_up_job_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelUpJobPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ClientboundLevelUpJobPacket::jobLocation,
            ByteBufCodecs.INT,
            ClientboundLevelUpJobPacket::level,
            ClientboundLevelUpJobPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
