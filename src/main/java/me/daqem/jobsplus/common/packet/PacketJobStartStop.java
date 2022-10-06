package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.AdvancementHandler;
import me.daqem.jobsplus.handlers.BossBarHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketJobStartStop(boolean isStart, Jobs job) {

    public static void encode(PacketJobStartStop msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isStart());
        buf.writeEnum(msg.job());
    }

    public static PacketJobStartStop decode(FriendlyByteBuf buf) {
        return new PacketJobStartStop(buf.readBoolean(), buf.readEnum(Jobs.class));
    }

    public static void handle(PacketJobStartStop msg, Supplier<NetworkEvent.Context> context) {
        boolean isStart = msg.isStart();
        Jobs job = msg.job();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                //Run when the player is starting a job.
                if (isStart) {
                    //Checks if this is the players first job and sets the display to that job.
                    if (JobGetters.getAmountOfEnabledJobs(player) == 0
                            && Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()
                            && Config.SET_FIRST_JOB_AS_DISPLAY.get()) {
                        JobSetters.setDisplay(player, job.get());
                    }

                    //Run when the job is NOT free to start.
                    if (!(JobGetters.getAmountOfEnabledJobs(player) < Config.AMOUNT_OF_FREE_JOBS.get())) {
                        JobSetters.removeCoins(player, Config.JOB_START_COST.get());
                    }
                    JobSetters.setLevel(job, player, 1);
                    AdvancementHandler.grandJobAdvancement(player, job, "");
                }
                //Run when the player is stopping a job.
                else {
                    //Check is the players display is the stopped job and if true removing it.
                    if ((job.get() + 1) == JobGetters.getDisplay(player)) {
                        JobSetters.setDisplay(player, -1);
                    }

                    //Run when the job is NOT free to stop.
                    if (JobGetters.getJobLevel(player, job) > Config.JOB_LEVEL_TO_STOP_JOB_FOR_FREE.get()) {
                        JobSetters.removeCoins(player, Config.JOB_STOP_COST.get());
                    }
                    JobSetters.resetJob(player, job);
                    BossBarHandler.removeBossBar(player, job);
                    AdvancementHandler.revokeAdvancement(player, job);
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
