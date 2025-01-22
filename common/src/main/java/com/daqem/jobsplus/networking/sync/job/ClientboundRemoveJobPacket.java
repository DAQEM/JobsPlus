package com.daqem.jobsplus.networking.sync.job;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.player.JobsClientPlayer;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundRemoveJobPacket implements CustomPacketPayload {

    private final JobInstance jobInstance;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRemoveJobPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundRemoveJobPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundRemoveJobPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundRemoveJobPacket packet) {
            boolean jobInstanceIsNotNull = packet.jobInstance != null;
            buf.writeBoolean(jobInstanceIsNotNull);
            if (jobInstanceIsNotNull) {
                buf.writeResourceLocation(packet.jobInstance.getLocation());
            }
        }
    };

    public ClientboundRemoveJobPacket(JobInstance jobInstance) {
        this.jobInstance = jobInstance;
    }

    public ClientboundRemoveJobPacket(FriendlyByteBuf friendlyByteBuf) {
        boolean jobInstanceIsNotNull = friendlyByteBuf.readBoolean();
        this.jobInstance = jobInstanceIsNotNull ? JobInstance.of(friendlyByteBuf.readResourceLocation()) : null;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_REMOVE_JOB;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundRemoveJobPacket packet, NetworkManager.PacketContext context) {
        if (packet.jobInstance == null) return;
        if (context.getPlayer() instanceof JobsClientPlayer player) {
            player.jobsplus$removeJob(packet.jobInstance);
        }
    }
}
