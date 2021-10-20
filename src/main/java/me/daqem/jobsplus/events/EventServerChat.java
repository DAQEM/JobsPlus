package me.daqem.jobsplus.events;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
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

            MinecraftForge.EVENT_BUS.post(new PlayerEvent.NameFormat(player, new KeybindComponent("LMAO")));

            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                /* Job start free verification */
                if (handler.getVerification()[CapType.START_VERIFICATION_FREE.get()] == 1) {
                    Jobs job = null;
                    for (int i = 0; i < 12; i++) {
                        if (handler.getSelector()[i] == 1) {
                            job = Jobs.values()[i];
                        }
                    }
                    if (job != null) {
                        if (event.getMessage().equalsIgnoreCase("yes")) {
                            if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                                ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have become an " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                            } else {
                                ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                        "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have become a " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                            }
                            JobSetters.setLevel(job, player, 1);
                        } else {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_RED + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You didn't say yes. Canceling job start command.");
                        }
                        reset(event, player);
                        if (JobGetters.getAmountOfEnabledJobs(player) == 1) {
                            JobSetters.setDisplay(player, job.get());
                        }
                    }
                }

                /* Job start paid verification */
                if (handler.getVerification()[CapType.START_VERIFICATION_PAID.get()] == 1) {
                    Jobs job = null;
                    for (int i = 0; i < 12; i++) {
                        if (handler.getSelector()[i] == 1) {
                            job = Jobs.values()[i];
                        }
                    }
                    if (event.getMessage().equalsIgnoreCase("yes") && job != null) {
                        if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have become an " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        } else {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have become a " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        }
                        JobSetters.removeCoins(player, 10);
                        JobSetters.setLevel(job, player,1);
                    } else {
                        ChatHandler.sendMessage(player, ChatFormatting.DARK_RED + "" + ChatFormatting.BOLD +
                                "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You didn't say yes. Canceling job start command.");
                    }
                    reset(event, player);
                }

                /* Job stop free verification */
                if (handler.getVerification()[CapType.STOP_VERIFICATION_FREE.get()] == 1) {
                    Jobs job = null;
                    for (int i = 0; i < 12; i++) {
                        if (handler.getSelector()[i] == 1) {
                            job = Jobs.values()[i];
                        }
                    }
                    if (event.getMessage().equalsIgnoreCase("yes") && job != null) {
                        if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have stopped being an " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        } else {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have stopped being a " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        }
                        if (JobGetters.getDisplay(player) == job.get()) {
                            JobSetters.setDisplay(player, -1);
                        }
                        JobSetters.setLevel(job, player,0);
                        JobSetters.setEXP(job, player,0);
                    } else {
                        ChatHandler.sendMessage(player, ChatFormatting.DARK_RED + "" + ChatFormatting.BOLD +
                                "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You didn't say yes. Canceling job stop command.");
                    }
                    reset(event, player);
                    if (JobGetters.getAmountOfEnabledJobs(player) == 0) {
                        JobSetters.setDisplay(player, 0);
                    }
                }

                /* Job stop free verification */
                if (handler.getVerification()[CapType.STOP_VERIFICATION_PAID.get()] == 1) {
                    Jobs job = null;
                    for (int i = 0; i < 12; i++) {
                        if (handler.getSelector()[i] == 1) {
                            job = Jobs.values()[i];
                        }
                    }
                    if (event.getMessage().equalsIgnoreCase("yes") && job != null) {
                        if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have stopped being an " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        } else {
                            ChatHandler.sendMessage(player, ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD +
                                    "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.GREEN + "You have stopped being a " + ChatHandler.capitalizeWord(job.toString().toLowerCase()) + ".");
                        }
                        JobSetters.removeCoins(player, 5);
                        JobSetters.setLevel(job, player,0);
                        JobSetters.setEXP(job, player,0);
                    } else {
                        ChatHandler.sendMessage(player, ChatFormatting.DARK_RED + "" + ChatFormatting.BOLD +
                                "[JOBS+] " + ChatFormatting.RESET + ChatFormatting.RED + "You didn't say yes. Canceling job stop command.");
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
                handler.setVerification(new int[]{0, 0, 0, 0, 0});
            });
        }
    }

    private void reset(ServerChatEvent event, Player player) {
        JobSetters.setVerification(player, -1);
        JobSetters.setSelector(player, -1);
        event.setCanceled(true);
    }
}
