package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClientboundUnlockItemRestrictionPacket implements CustomPacketPayload {

    private final Identifier itemRestrictionLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUnlockItemRestrictionPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundUnlockItemRestrictionPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundUnlockItemRestrictionPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundUnlockItemRestrictionPacket packet) {
            buf.writeIdentifier(packet.itemRestrictionLocation);
        }
    };

    public ClientboundUnlockItemRestrictionPacket(Identifier itemRestrictionLocation) {
        this.itemRestrictionLocation = itemRestrictionLocation;

    }

    public ClientboundUnlockItemRestrictionPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.itemRestrictionLocation = friendlyByteBuf.readIdentifier();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_UNLOCK_ITEM_RESTRICTION;
    }

    public Identifier getItemRestrictionLocation() {
        return itemRestrictionLocation;
    }
}
