package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.knot.Knot;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public record ServerboundStartJobPacket(Identifier jobLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundStartJobPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_start_job_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundStartJobPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ServerboundStartJobPacket::jobLocation,
            ServerboundStartJobPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer serverPlayer) {
            JobInstance jobInstance = JobInstance.of(this.jobLocation);

            if (jobInstance == null) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.job_not_found", this.jobLocation.toString()));
                return;
            }
            if (serverPlayer.jobsplus$getJobs().size() >= JobsPlusConfig.maxJobs.get()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.max_jobs_reached"));
                return;
            }

            if (serverPlayer.jobsplus$getJobs().size() >= JobsPlusConfig.amountOfFreeJobs.get()) {
                if (serverPlayer.jobsplus$getCoins() < jobInstance.getPrice()) {
                    serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.not_enough_coins"));
                    return;
                }
                serverPlayer.jobsplus$setCoins(serverPlayer.jobsplus$getCoins() - jobInstance.getPrice());
            }

            serverPlayer.jobsplus$addNewJob(jobInstance);
            Knot.NETWORKING.sendToPlayer((ServerPlayer) serverPlayer, new ClientboundOpenJobsScreenPacket(
                    Stream.concat(serverPlayer.jobsplus$getJobs().stream(), serverPlayer.jobsplus$getInactiveJobs().stream()).toList(),
                    serverPlayer.jobsplus$getCoins()
            ));
        }
    }
}
