package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.events.EventPlayerTick;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class LevelUpHandler {

    public static void handler(Player player, Jobs job, int level) {
        if (player.getServer() != null) {
            JobSetters.addCoins(player, 1);
            final TranslatableComponent translatableComponent = new TranslatableComponent("jobsplus.level_up." + job.name().toLowerCase(), ChatColor.green(), ChatColor.boldDarkGreen(), player.getScoreboardName(), ChatColor.green(), ChatColor.boldDarkGreen(), level, ChatColor.green(), ChatHandler.ColorizedJobName(job));
            if (JobGetters.getLevelUpChatSetting(player) == 0) {
                for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
                    if (JobGetters.getLevelUpChatSetting(serverPlayer) != 2)
                        ChatHandler.sendLevelUpMessage(serverPlayer, translatableComponent.getString());
                }
            } else if (JobGetters.getLevelUpChatSetting(player) == 1) {
                ChatHandler.sendLevelUpMessage(player, translatableComponent.getString());
            }
            if (JobGetters.getLevelUpSoundSetting(player) == 0) EventPlayerTick.levelUpHashMap.put(player, 0);
        }
    }
}
