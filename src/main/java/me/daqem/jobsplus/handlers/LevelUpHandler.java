package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.events.EventPlayerTick;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;

public class LevelUpHandler {

    public static void handler(Player player, Jobs job, int level) {
        if (player.getServer() != null) {
            player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
                handler.setCoins(handler.getCoins() + 1);
                String ana = job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER ? "an" : "a";
                player.getServer().getPlayerList().broadcastMessage(new KeybindComponent(
                        ChatColor.boldDarkGreen() + player.getScoreboardName() + ChatColor.green() +
                                " has achieved level " + ChatColor.boldDarkGreen() + level + ChatColor.green() +
                                " as " + ana + " " + ChatHandler.ColorizedJobName(job)
                ), ChatType.CHAT, player.getUUID());
                EventPlayerTick.levelUpHashMap.put(player, 0);
            });
        }
    }
}
