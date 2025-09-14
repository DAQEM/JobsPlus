package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class ServerboundOpenJobsScreenPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenJobsScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundOpenJobsScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundOpenJobsScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundOpenJobsScreenPacket packet) {
        }
    };

    public ServerboundOpenJobsScreenPacket() {
    }

    public ServerboundOpenJobsScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_OPEN_JOBS_SCREEN;
    }

    public static void handleServerSide(ServerboundOpenJobsScreenPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer jobsServerPlayer) {
            NetworkManager.sendToPlayer(jobsServerPlayer.jobsplus$getServerPlayer(), new ClientboundOpenJobsScreenPacket(
                    Stream.concat(jobsServerPlayer.jobsplus$getJobs().stream(), jobsServerPlayer.jobsplus$getInactiveJobs().stream()).toList(),
                    jobsServerPlayer.jobsplus$getCoins()
            ));
        }
    }
}
