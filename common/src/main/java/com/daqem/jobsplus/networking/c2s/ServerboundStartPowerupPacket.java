package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.networking.ServerboundContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record ServerboundStartPowerupPacket(Identifier jobLocation, Identifier powerupLocation) implements CustomPacketPayload {

    public static final Type<@NotNull ServerboundStartPowerupPacket> TYPE = new Type<>(JobsPlus.API.getId("serverbound_start_powerup_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundStartPowerupPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            ServerboundStartPowerupPacket::jobLocation,
            Identifier.STREAM_CODEC,
            ServerboundStartPowerupPacket::powerupLocation,
            ServerboundStartPowerupPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ServerboundContext context) {
        if (context.player() instanceof JobsServerPlayer serverPlayer) {
            Job job = serverPlayer.jobsplus$getJob(this.jobLocation);
            PowerupInstance powerupInstance = PowerupInstance.of(this.powerupLocation);

            if (job == null) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.job_not_found", this.jobLocation.toString()));
                return;
            }
            if (powerupInstance == null) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.powerup_not_found", this.powerupLocation.toString()));
                return;
            }
            if (serverPlayer.jobsplus$getCoins() < powerupInstance.getPrice()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.not_enough_coins"));
                return;
            }
            if (job.getLevel() < powerupInstance.getRequiredLevel()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.not_high_enough_level"));
                return;
            }
            if (job.getPowerupManager().getPowerup(powerupInstance).isPresent()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.powerup_already_owned", powerupInstance.getName()));
                return;
            }

            if (job.getPowerupManager().addPowerup(powerupInstance)) {
                serverPlayer.jobsplus$setCoins(serverPlayer.jobsplus$getCoins() - powerupInstance.getPrice());
            } else {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.API.translatable("error.could_not_add_powerup", powerupInstance.getName()));
            }
        }
    }
}
