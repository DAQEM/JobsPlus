package com.daqem.jobsplus.networking.sync.coin;

import com.daqem.jobsplus.client.player.JobsClientPlayer;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientBoundUpdateCoinsPacket implements CustomPacketPayload {

    private final double coins;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundUpdateCoinsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientBoundUpdateCoinsPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientBoundUpdateCoinsPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientBoundUpdateCoinsPacket packet) {
            buf.writeDouble(packet.coins);
        }
    };

    public ClientBoundUpdateCoinsPacket(double coins) {
        this.coins = coins;
    }

    public ClientBoundUpdateCoinsPacket(RegistryFriendlyByteBuf buf) {
        this.coins = buf.readDouble();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_UPDATE_COINS;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientBoundUpdateCoinsPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsClientPlayer player) {
            player.jobsplus$setCoins(packet.coins);
        }
    }
}
