package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundOpenHudEditorPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenHudEditorPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundOpenHudEditorPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundOpenHudEditorPacket();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundOpenHudEditorPacket packet) {
        }
    };

    public ClientboundOpenHudEditorPacket() {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_OPEN_HUD_EDITOR;
    }
}