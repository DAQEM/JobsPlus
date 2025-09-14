package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenPowerupsScreenPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class ServerboundOpenPowerupsScreenPacket implements CustomPacketPayload {

    private final ResourceLocation jobLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenPowerupsScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundOpenPowerupsScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundOpenPowerupsScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundOpenPowerupsScreenPacket packet) {
            buf.writeResourceLocation(packet.jobLocation);
        }
    };

    public ServerboundOpenPowerupsScreenPacket(ResourceLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public ServerboundOpenPowerupsScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_OPEN_POWERUPS_SCREEN;
    }

    public static void handleServerSide(ServerboundOpenPowerupsScreenPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer jobsServerPlayer) {
            NetworkManager.sendToPlayer(jobsServerPlayer.jobsplus$getServerPlayer(), new ClientboundOpenPowerupsScreenPacket(
                    Stream.concat(jobsServerPlayer.jobsplus$getJobs().stream(), jobsServerPlayer.jobsplus$getInactiveJobs().stream()).toList(),
                    jobsServerPlayer.jobsplus$getCoins(),
                    packet.jobLocation
            ));
        }
    }
}
