package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.BossBarHandler;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
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

            //True means the player successfully started or stopped a job.
            boolean noError = false;

            if (player != null) {
                //Run when the player is starting a job.
                if (isStart) {
                    //Run when the job is free to start.
                    if (JobGetters.getAmountOfEnabledJobs(player) < Config.AMOUNT_OF_FREE_JOBS.get()) {
                        noError = true;
                        //Checks if this is the players first job and sets the display tyo that job.
                        if (JobGetters.getAmountOfEnabledJobs(player) == 0
                                && Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()
                                && Config.SET_FIRST_JOB_AS_DISPLAY.get()) {
                            JobSetters.setDisplay(player, job.get());
                        }
                    } else {
                        //Run when the player has enough coins to buy the job.
                        if (JobGetters.getCoins(player) >= Config.JOB_START_COST.get()) {
                            noError = true;
                            JobSetters.removeCoins(player, Config.JOB_START_COST.get());
                        } else {
                            ChatHandler.sendMessage(player, new TranslatableComponent("error.coins.not_enough.need_10", ChatColor.boldDarkRed(), ChatColor.red()).getString());
                        }
                    }
                    //Run then the player successfully started the job.
                    if (noError) {
                        JobSetters.setLevel(job, player, 1);
                    }
                }
                //Run when the player is stopping a job.
                else {
                    //Run when the job is free to stop.
                    if (JobGetters.getJobLevel(player, job) == Config.JOB_LEVEL_TO_STOP_JOB_FOR_FREE.get()) {
                        noError = true;
                    } else {
                        //Does the player have enough coins to stop this job?
                        if (JobGetters.getCoins(player) >= Config.JOB_STOP_COST.get()) {
                            noError = true;
                            JobSetters.removeCoins(player, Config.JOB_STOP_COST.get());
                        } else {
                            ChatHandler.sendMessage(player, new TranslatableComponent("error.coins.need_5_to_stop_job", ChatColor.boldDarkRed(), ChatColor.red()).getString());
                        }
                    }
                    //Run then the player successfully stopped the job.
                    if (noError) {
                        JobSetters.set(job, player, 0, 0, 0, 0, 0);
                        BossBarHandler.removeBossBar(player, job);
                    }
                    //Check is the players display is the stopped job and if true removing it.
                    if ((job.get() + 1) == JobGetters.getDisplay(player)) {
                        JobSetters.setDisplay(player, -1);
                    }
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
