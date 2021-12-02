package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.events.EventPlayerTick;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;

public class LevelUpHandler {

    public static void handler(Player player, Jobs job, int level) {
        if (player.getServer() != null) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {

                /* Create chat level up message */
                String message = ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + player.getScoreboardName() +
                        ChatFormatting.RESET + ChatFormatting.GREEN + " has achieved level " + ChatFormatting.DARK_GREEN
                        + ChatFormatting.BOLD + level + ChatFormatting.RESET + ChatFormatting.GREEN + " as a " + ChatHandler.ColorizedJobName(job);
                if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                    message = ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + player.getScoreboardName() +
                            ChatFormatting.RESET + ChatFormatting.GREEN + " has achieved level " + ChatFormatting.DARK_GREEN
                            + ChatFormatting.BOLD + level + ChatFormatting.RESET + ChatFormatting.GREEN + " as an " + ChatHandler.ColorizedJobName(job);
                }

                /* Add 1 job coin on level up. */
                handler.setCoins(handler.getCoins() + 1);

                /* Send level up message in global chat. */
                player.getServer().getPlayerList().broadcastMessage(new KeybindComponent(message), ChatType.CHAT, player.getUUID());

                /* Play level up sound and how LEVEL UP! in action bar */
                EventPlayerTick.levelUpHashMap.put(player, 0);
            });
        }

    }
}
