package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.toast.PowerupUnlockedToast;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
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

public class ClientboundUnlockPowerupPacket implements CustomPacketPayload {

    private final ResourceLocation powerupLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUnlockPowerupPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundUnlockPowerupPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundUnlockPowerupPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundUnlockPowerupPacket packet) {
            buf.writeResourceLocation(packet.powerupLocation);
        }
    };

    public ClientboundUnlockPowerupPacket(ResourceLocation powerupLocation) {
        this.powerupLocation = powerupLocation;
    }

    public ClientboundUnlockPowerupPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.powerupLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_UNLOCK_POWERUP;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundUnlockPowerupPacket packet, NetworkManager.PacketContext context) {
        PowerupInstance powerupInstance = PowerupInstance.of(packet.powerupLocation);
        if (powerupInstance == null) return;
        if (JobsPlusClientConfig.showPowerupUnlockToastMessage.get()) {
            PowerupUnlockedToast.add(Minecraft.getInstance().getToasts(), powerupInstance);
        }
    }
}