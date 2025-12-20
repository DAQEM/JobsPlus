package com.daqem.jobsplus.networking.c2s;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.s2c.ClientboundPlayerJobsPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public class ServerboundRequestPlayerJobsPacket implements CustomPacketPayload {

    private final UUID playerUUID;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestPlayerJobsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundRequestPlayerJobsPacket decode(RegistryFriendlyByteBuf friendlyByteBuf) {
            return new ServerboundRequestPlayerJobsPacket(friendlyByteBuf.readUUID());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf friendlyByteBuf, ServerboundRequestPlayerJobsPacket packet) {
            friendlyByteBuf.writeUUID(packet.playerUUID);
        }
    };

    public ServerboundRequestPlayerJobsPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_REQUEST_PLAYER_JOBS;
    }

    public static void handleServerSide(ServerboundRequestPlayerJobsPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer sender) {
            Map<Identifier, LeaderboardPlayer> jobsMap = new HashMap<>(sender.jobsplus$getLevelData().jobsplus$getPlayerJobEntries().getOrDefault(packet.playerUUID, new HashMap<>()));

            JobManager.getInstance().getJobs().forEach((resourceLocation, jobInstance) ->
                    jobsMap.putIfAbsent(resourceLocation, new LeaderboardPlayer(packet.playerUUID, resourceLocation))
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

            NetworkManager.sendToPlayer((ServerPlayer) sender, new ClientboundPlayerJobsPacket(packet.playerUUID, jobs));
        }
    }
}