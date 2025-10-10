package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreen;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClientboundOpenPowerupsScreenPacket implements CustomPacketPayload {

    private final List<Job> jobs;
    private final int coins;
    private final ResourceLocation jobLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenPowerupsScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundOpenPowerupsScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundOpenPowerupsScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundOpenPowerupsScreenPacket packet) {
            buf.writeCollection(packet.jobs, Job.Serializer::toNetwork);
            buf.writeInt(packet.coins);
            buf.writeResourceLocation(packet.jobLocation);
        }
    };

    public ClientboundOpenPowerupsScreenPacket(List<Job> jobs, int coins, ResourceLocation jobLocation) {
        this.jobs = jobs;
        this.coins = coins;
        this.jobLocation = jobLocation;
    }

    public ClientboundOpenPowerupsScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobs = friendlyByteBuf.readList(friendlyByteBuf1 -> Job.Serializer.fromNetwork(friendlyByteBuf1, null));
        this.coins = friendlyByteBuf.readInt();
        this.jobLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_OPEN_POWERUPS_SCREEN;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public int getCoins() {
        return coins;
    }

    public ResourceLocation getJobLocation() {
        return jobLocation;
    }
}
