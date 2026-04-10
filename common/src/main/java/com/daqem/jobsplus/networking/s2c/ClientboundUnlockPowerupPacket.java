package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ClientboundUnlockPowerupPacket(Identifier powerupLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundUnlockPowerupPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_unlock_powerup_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUnlockPowerupPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ClientboundUnlockPowerupPacket::powerupLocation,
            ClientboundUnlockPowerupPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
