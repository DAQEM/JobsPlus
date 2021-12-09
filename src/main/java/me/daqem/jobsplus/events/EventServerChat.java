package me.daqem.jobsplus.events;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class EventServerChat {

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        if (event.getPlayer() != null) {
            Player player = event.getPlayer();

            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                /* Job start verification */
                if (handler.getVerification()[CapType.START_VERIFICATION_FREE.get()] == 1 ||
                        handler.getVerification()[CapType.START_VERIFICATION_PAID.get()] == 1) {
                    Jobs job = null;
                    for (int i = 0; i < 10; i++) {
                        if (handler.getSelector()[i] == 1) {
                            job = Jobs.values()[i];
                        }
                    }
                    if (event.getMessage().equalsIgnoreCase("yes") && job != null) {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() + "[JOBS+] " +
                                ChatColor.green() + "You have become " + ChatHandler.getAna(job) + " " +
                                ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");

                        if (handler.getVerification()[CapType.START_VERIFICATION_PAID.get()] == 1)
                            JobSetters.removeCoins(player, 10);
                        JobSetters.setLevel(job, player, 1);
                        if (JobGetters.getAmountOfEnabledJobs(player) == 1) JobSetters.setDisplay(player, job.get());
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You didn't say yes. Canceling job start command.");
                    }
                    reset(event, player);
                }

                /* Job stop free verification */
                if (handler.getVerification()[CapType.STOP_VERIFICATION_FREE.get()] == 1 ||
                        handler.getVerification()[CapType.STOP_VERIFICATION_PAID.get()] == 1) {
                    Jobs job = null;
                    for (int i = 0; i < 10; i++) {
                        if (handler.getSelector()[i] == 1) {
                            job = Jobs.values()[i];
                        }
                    }
                    if (event.getMessage().equalsIgnoreCase("yes") && job != null) {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkGreen() + "[JOBS+] "
                                + ChatColor.green() + "You have stopped being " + ChatHandler.getAna(job) +
                                " " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        if (JobGetters.getDisplay(player) == job.get()+1) JobSetters.setDisplay(player, -1);
                        if (handler.getVerification()[CapType.STOP_VERIFICATION_PAID.get()] == 1) JobSetters.removeCoins(player, 5);
                        JobSetters.setLevel(job, player, 0);
                        JobSetters.setEXP(job, player, 0);
                    } else {
                        ChatHandler.sendMessage(player, ChatColor.boldDarkRed() +
                                "[JOBS+] " + ChatColor.red() + "You didn't say yes. Canceling job stop command.");
                    }
                    reset(event, player);
                    if (JobGetters.getAmountOfEnabledJobs(player) == 0) {
                        JobSetters.setDisplay(player, 0);
                    }
                }

                /* Job power-up buy verification */
                if (handler.getVerification()[CapType.POWER_UP_VERIFICATION.get()] == 1) {
                    reset(event, player);
                }
                JobSetters.setVerification(player, -1);
            });
            if (JobGetters.getDisplay(player) == 0) {
                event.setComponent(new KeybindComponent(ChatColor.gray() + player.getScoreboardName() +
                        ChatColor.darkGray() + " > " + ChatColor.white() + event.getMessage()));
            } else {
                for (Jobs job : Jobs.values()) {
                    if (job.get() == JobGetters.getDisplay(player) - 1) {
                        event.setComponent(new KeybindComponent(ChatHandler.ColorizedJobName(job) +
                                ChatColor.gray() + player.getScoreboardName() + ChatColor.darkGray() + " > " +
                                ChatColor.white() + event.getMessage()));
                        return;
                    }
                }
            }
        }
    }

    private void reset(ServerChatEvent event, Player player) {
        JobSetters.setVerification(player, -1);
        JobSetters.setSelector(player, -1);
        event.setCanceled(true);
    }
}
