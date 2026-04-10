package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.Knot;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public record ServerboundOpenJobsScreenPacket() implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundOpenJobsScreenPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_open_jobs_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenJobsScreenPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerboundOpenJobsScreenPacket());

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer jobsServerPlayer) {
            List<Job> inactiveJobs = jobsServerPlayer.jobsplus$getInactiveJobs();
            Knot.NETWORKING.sendToPlayer(jobsServerPlayer.jobsplus$getServerPlayer(), new ClientboundOpenJobsScreenPacket(
                    Stream.concat(jobsServerPlayer.jobsplus$getJobs().stream(), inactiveJobs.stream()).toList(),
                    jobsServerPlayer.jobsplus$getCoins()
            ));
        }
    }
}
