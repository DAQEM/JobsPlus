package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClientboundSyncJobLevelPacket implements CustomPacketPayload {

    private final Identifier jobLocation;
    private final int level;
    private final double experience;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncJobLevelPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundSyncJobLevelPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundSyncJobLevelPacket(
                    buf.readIdentifier(),
                    buf.readInt(),
                    buf.readDouble()
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundSyncJobLevelPacket packet) {
            buf.writeIdentifier(packet.jobLocation);
            buf.writeInt(packet.level);
            buf.writeDouble(packet.experience);
        }
    };

    public ClientboundSyncJobLevelPacket(Job job) {
        this.jobLocation = job.getJobInstance().getIdentifier();
        this.level = job.getLevel();
        this.experience = job.getExperience();
    }

    public ClientboundSyncJobLevelPacket(Identifier jobLocation, int level, double experience) {
        this.jobLocation = jobLocation;
        this.level = level;
        this.experience = experience;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_SYNC_JOB_LEVEL;
    }

    public Identifier getJobLocation() {
        return jobLocation;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }
}