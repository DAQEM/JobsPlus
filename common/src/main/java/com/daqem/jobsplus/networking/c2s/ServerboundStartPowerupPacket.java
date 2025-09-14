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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerboundStartPowerupPacket implements CustomPacketPayload {

    private final ResourceLocation jobLocation;
    private final ResourceLocation powerupLocation;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundStartPowerupPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundStartPowerupPacket decode(RegistryFriendlyByteBuf buf) {
            return new ServerboundStartPowerupPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ServerboundStartPowerupPacket packet) {
            buf.writeResourceLocation(packet.jobLocation);
            buf.writeResourceLocation(packet.powerupLocation);
        }
    };

    public ServerboundStartPowerupPacket(ResourceLocation jobLocation, ResourceLocation powerupLocation) {
        this.jobLocation = jobLocation;
        this.powerupLocation = powerupLocation;
    }

    public ServerboundStartPowerupPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.jobLocation = friendlyByteBuf.readResourceLocation();
        this.powerupLocation = friendlyByteBuf.readResourceLocation();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.SERVERBOUND_START_POWERUP;
    }

    public static void handleServerSide(ServerboundStartPowerupPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof JobsServerPlayer serverPlayer) {
            Job job = serverPlayer.jobsplus$getJob(packet.jobLocation);
            PowerupInstance powerupInstance = PowerupInstance.of(packet.powerupLocation);

            if (job == null) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.translatable("error.job_not_found", packet.jobLocation.toString()));
                return;
            }
            if (powerupInstance == null) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.translatable("error.powerup_not_found", packet.powerupLocation.toString()));
                return;
            }
            if (serverPlayer.jobsplus$getCoins() < powerupInstance.getPrice()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.translatable("error.not_enough_coins"));
                return;
            }
            if (job.getLevel() < powerupInstance.getRequiredLevel()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.translatable("error.not_high_enough_level"));
                return;
            }
            if (job.getPowerupManager().getPowerup(powerupInstance).isPresent()) {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.translatable("error.powerup_already_owned", powerupInstance.getName()));
                return;
            }

            if (job.getPowerupManager().addPowerup(serverPlayer, job, powerupInstance)) {
                serverPlayer.jobsplus$setCoins(serverPlayer.jobsplus$getCoins() - powerupInstance.getPrice());
            } else {
                serverPlayer.jobsplus$getServerPlayer().sendSystemMessage(JobsPlus.translatable("error.could_not_add_powerup", powerupInstance.getName()));
            }
        }
    }
}
