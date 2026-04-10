package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ClientboundUnlockItemRestrictionPacket(
        Identifier itemRestrictionLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundUnlockItemRestrictionPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_unlock_item_restriction_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUnlockItemRestrictionPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ClientboundUnlockItemRestrictionPacket::itemRestrictionLocation,
            ClientboundUnlockItemRestrictionPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
