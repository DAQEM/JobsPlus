package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;

public class LevelUpAnnouncer {

    public static void announceLevelUp(Player player, Jobs job, int level) {
        if (player.getServer() != null) {
            String message = player.getScoreboardName() + " has achieved level " + level + " as an " + job.toString().toLowerCase();
            if (job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER) {
                message = player.getScoreboardName() + " has achieved level " + level + " as an " + job.toString().toLowerCase();
            }

            player.getServer().getPlayerList().broadcastMessage(new KeybindComponent(message), ChatType.CHAT, player.getUUID());
        }
    }
}
