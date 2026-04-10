package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.Knot;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ServerboundSyncPlayerJobsPacket() implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundSyncPlayerJobsPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_sync_player_jobs_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSyncPlayerJobsPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerboundSyncPlayerJobsPacket());

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer serverPlayer) {
            List<Job> jobs = serverPlayer.jobsplus$getJobs();
            jobs.forEach(job -> Knot.NETWORKING.sendToPlayer(
                    serverPlayer.jobsplus$getServerPlayer(),
                    new ClientboundSyncJobPacket(job)
            ));
        }
    }
}
