package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.client.screen.job.JobsScreen;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientboundOpenJobsScreenPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenJobsScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundOpenJobsScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundOpenJobsScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundOpenJobsScreenPacket packet) {
        }
    };

    public ClientboundOpenJobsScreenPacket() {
    }

    public ClientboundOpenJobsScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_OPEN_JOBS_SCREEN;
    }

    public static void handleClientSide(ClientboundOpenJobsScreenPacket packet, NetworkManager.PacketContext context) {
        @Nullable Screen previousScreen = null;
        if (Minecraft.getInstance().screen instanceof JobsScreen jobsScreen) {
            previousScreen = jobsScreen.getPreviousScreen();
        }
        JobsScreen.open(previousScreen);
    }
}
