package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ClientboundOpenHudEditorPacket() implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundOpenHudEditorPacket> TYPE = new Type<>(JobsPlus.API.getId("clientbound_open_hud_editor_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenHudEditorPacket> STREAM_CODEC =
            StreamCodec.unit(new ClientboundOpenHudEditorPacket());

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}