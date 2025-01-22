package com.daqem.jobsplus.networking.c2s;

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

public class ServerboundTogglePowerUpPacket implements CustomPacketPayload {

    private final @Nullable JobInstance jobInstance;
    private final @Nullable PowerupInstance powerupInstance;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTogglePowerUpPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundTogglePowerUpPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundTogglePowerUpPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundTogglePowerUpPacket packet) {
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

    public ServerboundTogglePowerUpPacket(@Nullable JobInstance jobInstance, @Nullable PowerupInstance powerupInstance) {
        this.jobInstance = jobInstance;
        this.powerupInstance = powerupInstance;
    }

    public ServerboundTogglePowerUpPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        boolean jobInstanceIsNotNull = friendlyByteBuf.readBoolean();
        this.jobInstance = jobInstanceIsNotNull ? JobInstance.of(friendlyByteBuf.readResourceLocation()) : null;
        boolean powerupInstanceIsNotNull = friendlyByteBuf.readBoolean();
        this.powerupInstance = powerupInstanceIsNotNull ? PowerupInstance.of(friendlyByteBuf.readResourceLocation()) : null;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_TOGGLE_POWERUP;
    }

    public static void handleServerSide(ServerboundTogglePowerUpPacket packet, NetworkManager.PacketContext context) {
        if (packet.jobInstance == null || packet.powerupInstance == null) return;
        if (context.getPlayer() instanceof JobsServerPlayer serverPlayer) {
            Job job = serverPlayer.jobsplus$getJob(packet.jobInstance);
            if (job != null) {
                job.getPowerupManager().getPowerup(packet.powerupInstance).ifPresent(powerup -> {
                    powerup.toggle();
                    serverPlayer.jobsplus$updateJob(job);
                });
            }
        }
    }
}
