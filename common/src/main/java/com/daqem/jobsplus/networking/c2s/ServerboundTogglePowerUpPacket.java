package com.daqem.jobsplus.networking.c2s;

import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ServerboundTogglePowerUpPacket implements CustomPacketPayload {

    private final ResourceLocation jobLocation;
    private final ResourceLocation powerupLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTogglePowerUpPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundTogglePowerUpPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundTogglePowerUpPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundTogglePowerUpPacket packet) {
            buf.writeResourceLocation(packet.jobLocation);
            buf.writeResourceLocation(packet.powerupLocation);
        }
    };

    public ServerboundTogglePowerUpPacket(ResourceLocation jobLocation, ResourceLocation powerupLocation) {
        this.jobLocation = jobLocation;
        this.powerupLocation = powerupLocation;
    }

    public ServerboundTogglePowerUpPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobLocation = friendlyByteBuf.readResourceLocation();
        this.powerupLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_TOGGLE_POWERUP;
    }

    public static void handleServerSide(ServerboundTogglePowerUpPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer serverPlayer) {
            Job job = serverPlayer.jobsplus$getJob(packet.jobLocation);
            if (job != null) {
                job.getPowerupManager().getPowerup(packet.powerupLocation).ifPresent(powerup -> {
                    powerup.toggle();
                    serverPlayer.jobsplus$updateJob(job);
                });
            }
        }
    }
}
