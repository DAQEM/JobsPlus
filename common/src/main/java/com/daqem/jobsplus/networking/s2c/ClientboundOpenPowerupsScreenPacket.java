package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundOpenPowerupsScreenPacket(List<Job> jobs, double coins,
                                                  Identifier jobLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundOpenPowerupsScreenPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_open_powerups_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenPowerupsScreenPacket> STREAM_CODEC = StreamCodec.composite(Job.Serializer.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundOpenPowerupsScreenPacket::jobs, ByteBufCodecs.DOUBLE, ClientboundOpenPowerupsScreenPacket::coins, Identifier.STREAM_CODEC, ClientboundOpenPowerupsScreenPacket::jobLocation, ClientboundOpenPowerupsScreenPacket::new);

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
