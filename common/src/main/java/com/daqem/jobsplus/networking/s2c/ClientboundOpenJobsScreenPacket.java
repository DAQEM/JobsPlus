package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClientboundOpenJobsScreenPacket implements CustomPacketPayload {

    private final List<Job> jobs;
    private final int coins;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenJobsScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundOpenJobsScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundOpenJobsScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundOpenJobsScreenPacket packet) {
            buf.writeCollection(packet.jobs, Job.Serializer::toNetwork);
            buf.writeInt(packet.coins);
        }
    };

    public ClientboundOpenJobsScreenPacket(List<Job> jobs, int coins) {
        this.jobs = jobs;
        this.coins = coins;
    }

    public ClientboundOpenJobsScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobs = friendlyByteBuf.readList(friendlyByteBuf1 -> Job.Serializer.fromNetwork(friendlyByteBuf1, null));
        this.coins = friendlyByteBuf.readInt();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_OPEN_JOBS_SCREEN;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public int getCoins() {
        return coins;
    }
}
