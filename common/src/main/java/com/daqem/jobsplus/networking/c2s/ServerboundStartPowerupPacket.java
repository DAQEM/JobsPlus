package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerboundStartPowerupPacket implements CustomPacketPayload {

    private final @Nullable JobInstance jobInstance;
    private final @Nullable PowerupInstance powerupInstance;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundStartPowerupPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundStartPowerupPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundStartPowerupPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundStartPowerupPacket packet) {
            boolean jobInstanceIsNotNull = packet.jobInstance != null;
            buf.writeBoolean(jobInstanceIsNotNull);
            if (jobInstanceIsNotNull) {
                buf.writeResourceLocation(packet.jobInstance.getLocation());
            }
            boolean powerupInstanceIsNotNull = packet.powerupInstance != null;
            buf.writeBoolean(powerupInstanceIsNotNull);
            if (powerupInstanceIsNotNull) {
                buf.writeResourceLocation(packet.powerupInstance.getLocation());
            }
        }
    };

    public ServerboundStartPowerupPacket(@Nullable JobInstance jobInstance, @Nullable PowerupInstance powerupInstance) {
        this.jobInstance = jobInstance;
        this.powerupInstance = powerupInstance;
    }

    public ServerboundStartPowerupPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        boolean jobInstanceIsNotNull = friendlyByteBuf.readBoolean();
        this.jobInstance = jobInstanceIsNotNull ? JobInstance.of(friendlyByteBuf.readResourceLocation()) : null;
        boolean powerupInstanceIsNotNull = friendlyByteBuf.readBoolean();
        this.powerupInstance = powerupInstanceIsNotNull ? PowerupInstance.of(friendlyByteBuf.readResourceLocation()) : null;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_START_POWERUP;
    }

    public static void handleServerSide(ServerboundStartPowerupPacket packet, NetworkManager.PacketContext context) {
        if (packet.jobInstance == null || packet.powerupInstance == null) return;
        if (context.getPlayer() instanceof JobsServerPlayer serverPlayer) {
            Job job = serverPlayer.jobsplus$getJob(packet.jobInstance);

            if (job == null) {
                context.getPlayer().sendSystemMessage(JobsPlus.translatable("error.job_not_found", packet.jobInstance.getLocation().toString()));
                return;
            }
            if (serverPlayer.jobsplus$getCoins() < packet.powerupInstance.getPrice()) {
                context.getPlayer().sendSystemMessage(JobsPlus.translatable("error.not_enough_coins"));
                return;
            }
            if (job.getLevel() < packet.powerupInstance.getRequiredLevel()) {
                context.getPlayer().sendSystemMessage(JobsPlus.translatable("error.not_high_enough_level"));
                return;
            }

            serverPlayer.jobsplus$setCoins(serverPlayer.jobsplus$getCoins() - packet.powerupInstance.getPrice());
            job.getPowerupManager().addPowerup(serverPlayer, job, packet.powerupInstance);
        }
    }
}
