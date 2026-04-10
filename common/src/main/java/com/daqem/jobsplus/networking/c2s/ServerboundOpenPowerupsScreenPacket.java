package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenPowerupsScreenPacket;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.knot.Knot;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public record ServerboundOpenPowerupsScreenPacket(Identifier jobLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundOpenPowerupsScreenPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_open_powerups_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenPowerupsScreenPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ServerboundOpenPowerupsScreenPacket::jobLocation,
            ServerboundOpenPowerupsScreenPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer jobsServerPlayer) {
            Knot.NETWORKING.sendToPlayer(jobsServerPlayer.jobsplus$getServerPlayer(), new ClientboundOpenPowerupsScreenPacket(
                    Stream.concat(jobsServerPlayer.jobsplus$getJobs().stream(), jobsServerPlayer.jobsplus$getInactiveJobs().stream()).toList(),
                    jobsServerPlayer.jobsplus$getCoins(),
                    this.jobLocation
            ));
        }
    }
}
