package com.daqem.jobsplus.networking.s2c;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.job.Job;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ClientboundOpenPowerupsScreenPacket implements CustomPacketPayload {

    private final List<Job> jobs;
    private final double coins;
    private final ResourceLocation jobLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenPowerupsScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundOpenPowerupsScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundOpenPowerupsScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundOpenPowerupsScreenPacket packet) {
            buf.writeCollection(packet.jobs, Job.Serializer::toNetwork);
            buf.writeDouble(packet.coins);
            buf.writeResourceLocation(packet.jobLocation);
        }
    };

    public ClientboundOpenPowerupsScreenPacket(List<Job> jobs, double coins, ResourceLocation jobLocation) {
        this.jobs = jobs;
        this.coins = coins;
        this.jobLocation = jobLocation;
    }

    public ClientboundOpenPowerupsScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobs = friendlyByteBuf.readList(friendlyByteBuf1 -> Job.Serializer.fromNetwork(friendlyByteBuf1, null));
        this.coins = friendlyByteBuf.readDouble();
        this.jobLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_OPEN_POWERUPS_SCREEN;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public double getCoins() {
        return coins;
    }

    public ResourceLocation getJobLocation() {
        return jobLocation;
    }
}
