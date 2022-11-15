package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.config.Config;
import me.daqem.jobsplus.init.ModPackets;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.nbt.CompoundTag;
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
                CompoundTag dataTag = new CompoundTag();

                for (Jobs job : Jobs.values()) {
                    CompoundTag jobTag = new CompoundTag();
                    jobTag.putInt("Level", JobGetters.getJobLevel(player, job));
                    jobTag.putInt("Exp", JobGetters.getJobEXP(player, job));
                    jobTag.putInt("Powerup1", JobGetters.getPowerup(player, job, CapType.POWER_UP1.get()));
                    jobTag.putInt("Powerup2", JobGetters.getPowerup(player, job, CapType.POWER_UP2.get()));
                    jobTag.putInt("Powerup3", JobGetters.getPowerup(player, job, CapType.POWER_UP3.get()));
                    jobTag.putInt("Superpower", JobGetters.getSuperPower(player, job));
                    dataTag.put(job.name(), jobTag);
                }

                dataTag.putInt("JobsEnabled", JobGetters.getAmountOfEnabledJobs(player));
                dataTag.putInt("Coins", JobGetters.getCoins(player));
                dataTag.putInt("Display", JobGetters.getDisplay(player));
                dataTag.putInt("ActiveBossBar", JobGetters.getActiveBossBar(player));

                dataTag.putBoolean("DisplayJobConfig", Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get());
                dataTag.putInt("AmountOfFreeJobs", Config.AMOUNT_OF_FREE_JOBS.get());
                dataTag.putInt("JobStartCost", Config.JOB_START_COST.get());
                dataTag.putInt("JobStopCost", Config.JOB_STOP_COST.get());
                dataTag.putInt("JobLevelToStopJobForFree", Config.JOB_LEVEL_TO_STOP_JOB_FOR_FREE.get());
                dataTag.putInt("PowerupCost", Config.POWERUP_COST.get());

                dataTag.putInt("JobID", msg.jobId);
                dataTag.putInt("ActiveLeftButton", msg.activeLeftButton);
                dataTag.putInt("ActiveRightButton", msg.activeRightButton);
                dataTag.putInt("SelectedButton", msg.selectedButton);
                dataTag.putFloat("ScrollOffs", msg.scrollOffs);
                dataTag.putInt("StartIndex", msg.startIndex);

                ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketSendMainMenuData(dataTag));
            }
            context.get().setPacketHandled(true);
        }
    }
}
