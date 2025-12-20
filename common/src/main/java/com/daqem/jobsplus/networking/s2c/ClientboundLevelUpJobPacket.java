package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClientboundLevelUpJobPacket implements CustomPacketPayload {

    private final Identifier jobLocation;
    private final int level;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelUpJobPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundLevelUpJobPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundLevelUpJobPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundLevelUpJobPacket packet) {
            buf.writeIdentifier(packet.jobLocation);
            buf.writeInt(packet.level);
        }
    };

    public ClientboundLevelUpJobPacket(Identifier jobLocation, int level) {
        this.jobLocation = jobLocation;
        this.level = level;

    }

    public ClientboundLevelUpJobPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobLocation = friendlyByteBuf.readIdentifier();
        this.level = friendlyByteBuf.readInt();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_LEVEL_UP_JOB;
    }

    public Identifier getJobLocation() {
        return jobLocation;
    }

    public int getLevel() {
        return level;
    }
}
