package com.daqem.jobsplus.networking.c2s;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.knot.Knot;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.NotNull;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.networking.s2c.ClientboundPlayerJobsPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundRequestPlayerJobsPacket(UUID playerUUID) implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundRequestPlayerJobsPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_request_player_jobs_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestPlayerJobsPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ServerboundRequestPlayerJobsPacket::playerUUID,
            ServerboundRequestPlayerJobsPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer sender) {
            Map<Identifier, LeaderboardPlayer> jobsMap = new HashMap<>(sender.jobsplus$getLevelData().jobsplus$getPlayerJobEntries().getOrDefault(this.playerUUID, new HashMap<>()));

            JobManager.getInstance().getJobs().forEach((resourceLocation, jobInstance) ->
                    jobsMap.putIfAbsent(resourceLocation, new LeaderboardPlayer(this.playerUUID, resourceLocation))
            );

            List<Job> jobs = jobsMap.entrySet().stream()
                    .map(entry -> {
                                JobInstance jobInstance = JobInstance.of(entry.getKey());
                                if (jobInstance == null) {
                                    return null;
                                }
                                return new Job(
                                        null,
                                        jobInstance,
                                        entry.getValue().getLevel(),
                                        entry.getValue().getExperience());
                            }
                    )
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(Job::getLevel).thenComparingDouble(Job::getExperience).reversed())
                    .toList();

            Knot.NETWORKING.sendToPlayer((ServerPlayer) sender, new ClientboundPlayerJobsPacket(this.playerUUID, jobs));
        }
    }
}