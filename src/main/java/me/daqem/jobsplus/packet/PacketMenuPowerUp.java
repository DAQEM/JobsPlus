package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketMenuPowerUp(boolean hasPowerUp, Jobs job, int powerUp) {

    public static void encode(PacketMenuPowerUp msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.hasPowerUp());
        buf.writeEnum(msg.job());
        buf.writeInt(msg.powerUp());
    }

    public static PacketMenuPowerUp decode(FriendlyByteBuf buf) {
        return new PacketMenuPowerUp(buf.readBoolean(), buf.readEnum(Jobs.class), buf.readInt());
    }

    public static void handle(PacketMenuPowerUp msg, Supplier<NetworkEvent.Context> context) {
        boolean hasPowerUp = msg.hasPowerUp();
        Jobs job = msg.job();
        int powerUp = msg.powerUp();

        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {

                //Switch Power-up
                if (hasPowerUp) {
                    if (JobGetters.getPowerup(player, job, powerUp) == 1) {
                        JobSetters.setPowerUp(job, player, powerUp, 2);
                    } else {
                        JobSetters.setPowerUp(job, player, powerUp, 1);
                    }
                }

                //Buy Power-up
                else {
                    JobSetters.addPowerUp(job, player, powerUp);
                    JobSetters.removeCoins(player, 10);
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
