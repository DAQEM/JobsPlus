package com.daqem.jobsplus.networking.s2c;

import com.daqem.itemrestrictions.data.ItemRestrictionManager;
import com.daqem.jobsplus.client.toast.ItemRestrictionUnlockedToast;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ClientboundUnlockItemRestrictionPacket implements CustomPacketPayload {

    private final ResourceLocation itemRestrictionLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUnlockItemRestrictionPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundUnlockItemRestrictionPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundUnlockItemRestrictionPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundUnlockItemRestrictionPacket packet) {
            buf.writeResourceLocation(packet.itemRestrictionLocation);
        }
    };

    public ClientboundUnlockItemRestrictionPacket(ResourceLocation itemRestrictionLocation) {
        this.itemRestrictionLocation = itemRestrictionLocation;

    }

    public ClientboundUnlockItemRestrictionPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.itemRestrictionLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_UNLOCK_ITEM_RESTRICTION;
    }

    public ResourceLocation getItemRestrictionLocation() {
        return itemRestrictionLocation;
    }
}
