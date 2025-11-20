package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ClientboundPlayerJobsPacket implements CustomPacketPayload {

    private final UUID playerUUID;
    private final List<Job> jobs;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerJobsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundPlayerJobsPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundPlayerJobsPacket(
                    buf.readUUID(),
                    buf.readList(buf1 -> Job.Serializer.fromNetwork(buf1, null))
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundPlayerJobsPacket packet) {
            buf.writeUUID(packet.playerUUID);
            buf.writeCollection(packet.jobs, Job.Serializer::toNetwork);
        }
    };

    public ClientboundPlayerJobsPacket(UUID playerUUID, List<Job> jobs) {
        this.playerUUID = playerUUID;
        this.jobs = jobs;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_PLAYER_JOBS;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public List<Job> getJobs() {
        return jobs;
    }
}