package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClientboundSyncJobPacket implements CustomPacketPayload {

    private final Identifier jobLocation;
    private final int level;
    private final double experience;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncJobPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundSyncJobPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundSyncJobPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundSyncJobPacket packet) {
            buf.writeIdentifier(packet.jobLocation);
            buf.writeInt(packet.level);
            buf.writeDouble(packet.experience);
        }
    };

    public ClientboundSyncJobPacket(Identifier jobLocation, int level, double experience) {
        this.jobLocation = jobLocation;
        this.level = level;
        this.experience = experience;

    }

    public ClientboundSyncJobPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobLocation = friendlyByteBuf.readIdentifier();
        this.level = friendlyByteBuf.readInt();
        this.experience = friendlyByteBuf.readDouble();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_SYNC_JOB;
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
