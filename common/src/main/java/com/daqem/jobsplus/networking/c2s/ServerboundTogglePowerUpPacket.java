package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ServerboundTogglePowerUpPacket(Identifier jobLocation, Identifier powerupLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundTogglePowerUpPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_toggle_powerup_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTogglePowerUpPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ServerboundTogglePowerUpPacket::jobLocation,
            Identifier.STREAM_CODEC,
            ServerboundTogglePowerUpPacket::powerupLocation,
            ServerboundTogglePowerUpPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer serverPlayer) {
            Job job = serverPlayer.jobsplus$getJob(this.jobLocation);
            if (job != null) {
                job.getPowerupManager().getPowerup(this.powerupLocation).ifPresent(powerup -> {
                    job.getPowerupManager().togglePowerup(powerup);
                });
            }
        }
    }
}
