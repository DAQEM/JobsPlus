package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;

public class LevelUpHandler {

    public static void handler(Player player, Jobs job, int level) {
        if (player.getServer() != null) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {

                /* Create chat level up message */
                String message = player.getScoreboardName() + " has achieved level " + level + " as an " + job.toString().toLowerCase();
                if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                    message = player.getScoreboardName() + " has achieved level " + level + " as an " + job.toString().toLowerCase();
                }

                /* Add 1 job coin on level up. */
                handler.setCoins(handler.getCoins() + 1);

                /* Send level up message in global chat. */
                player.getServer().getPlayerList().broadcastMessage(new KeybindComponent(message), ChatType.CHAT, player.getUUID());
            });
        }

    }
}
