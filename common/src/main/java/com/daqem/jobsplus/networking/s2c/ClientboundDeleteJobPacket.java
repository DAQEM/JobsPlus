package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClientboundDeleteJobPacket implements CustomPacketPayload {

    private final Identifier jobLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDeleteJobPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundDeleteJobPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundDeleteJobPacket(buf.readIdentifier());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundDeleteJobPacket packet) {
            buf.writeIdentifier(packet.jobLocation);
        }
    };

    public ClientboundDeleteJobPacket(JobInstance jobInstance) {
        this(jobInstance.getIdentifier());
    }

    public ClientboundDeleteJobPacket(Identifier jobLocation) {
        this.jobLocation = jobLocation;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_DELETE_JOB;
    }

    public Identifier getJobLocation() {
        return jobLocation;
    }
}