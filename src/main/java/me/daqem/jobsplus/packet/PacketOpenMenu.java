package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record PacketOpenMenu(int jobId, int activeLeftButton, int activeRightButton, int selectedButton,
                             float scrollOffs,
                             int startIndex) {

    public static void encode(PacketOpenMenu msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.jobId);
        buf.writeInt(msg.activeLeftButton);
        buf.writeInt(msg.activeRightButton);
        buf.writeInt(msg.selectedButton);
        buf.writeFloat(msg.scrollOffs);
        buf.writeInt(msg.startIndex);
    }

    public static PacketOpenMenu decode(FriendlyByteBuf buf) {
        return new PacketOpenMenu(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readInt());
    }

    public static void handle(PacketOpenMenu msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketSendMainMenuData(new int[]{
                        JobGetters.getJobLevel(player, Jobs.ALCHEMIST), JobGetters.getJobEXP(player, Jobs.ALCHEMIST),
                        JobGetters.getJobLevel(player, Jobs.BUILDER), JobGetters.getJobEXP(player, Jobs.BUILDER),
                        JobGetters.getJobLevel(player, Jobs.DIGGER), JobGetters.getJobEXP(player, Jobs.DIGGER),
                        JobGetters.getJobLevel(player, Jobs.ENCHANTER), JobGetters.getJobEXP(player, Jobs.ENCHANTER),
                        JobGetters.getJobLevel(player, Jobs.FARMER), JobGetters.getJobEXP(player, Jobs.FARMER),
                        JobGetters.getJobLevel(player, Jobs.FISHERMAN), JobGetters.getJobEXP(player, Jobs.FISHERMAN),
                        JobGetters.getJobLevel(player, Jobs.HUNTER), JobGetters.getJobEXP(player, Jobs.HUNTER),
                        JobGetters.getJobLevel(player, Jobs.LUMBERJACK), JobGetters.getJobEXP(player, Jobs.LUMBERJACK),
                        JobGetters.getJobLevel(player, Jobs.MINER), JobGetters.getJobEXP(player, Jobs.MINER),
                        JobGetters.getJobLevel(player, Jobs.SMITH), JobGetters.getJobEXP(player, Jobs.SMITH),
                        JobGetters.getCoins(player), JobGetters.getAmountOfEnabledJobs(player),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWER_UP3.get()),
                        JobGetters.getSuperPower(player, Jobs.ALCHEMIST), JobGetters.getSuperPower(player, Jobs.BUILDER),
                        JobGetters.getSuperPower(player, Jobs.DIGGER), JobGetters.getSuperPower(player, Jobs.ENCHANTER),
                        JobGetters.getSuperPower(player, Jobs.FARMER), JobGetters.getSuperPower(player, Jobs.FISHERMAN),
                        JobGetters.getSuperPower(player, Jobs.HUNTER), JobGetters.getSuperPower(player, Jobs.LUMBERJACK),
                        JobGetters.getSuperPower(player, Jobs.MINER), JobGetters.getSuperPower(player, Jobs.SMITH),
                        JobGetters.getDisplay(player), JobGetters.getActiveBossBar(player),
                        Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get() ? 1 : 0
                }, msg.jobId, msg.activeLeftButton, msg.activeRightButton, msg.selectedButton, msg.scrollOffs, msg.startIndex));
            }
            context.get().setPacketHandled(true);
        }
    }
}
