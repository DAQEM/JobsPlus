package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerboundSyncPlayerJobsPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSyncPlayerJobsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundSyncPlayerJobsPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundSyncPlayerJobsPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundSyncPlayerJobsPacket packet) {
        }
    };

    public ServerboundSyncPlayerJobsPacket() {
    }

    public ServerboundSyncPlayerJobsPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_SYNC_PLAYER_JOBS;
    }

    public static void handleServerSide(ServerboundSyncPlayerJobsPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer serverPlayer) {
            List<Job> jobs = serverPlayer.jobsplus$getJobs();
            jobs.forEach(job -> NetworkManager.sendToPlayer(
                    serverPlayer.jobsplus$getServerPlayer(),
                    new ClientboundSyncJobPacket(
                            job.getJobInstance().getIdentifier(),
                            job.getLevel(),
                            job.getExperience()
                    )
            ));
        }
    }
}
