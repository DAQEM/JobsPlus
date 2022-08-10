package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.events.EventPlayerTick;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class LevelUpHandler {

    public static void handle(Player player, Jobs job, int level) {
        if (player.getServer() == null) return;
        if (player.level.isClientSide) return;

        JobSetters.addCoins(player, Config.COINS_PER_LEVEL_UP.get());
        final Component translatableComponent = Component.translatable("jobsplus.level_up." + job.name().toLowerCase(), ChatColor.green(), ChatColor.boldDarkGreen(), player.getScoreboardName(), ChatColor.green(), ChatColor.boldDarkGreen(), level, ChatColor.green(), ChatHandler.ColorizedJobName(job));
        if (JobGetters.getLevelUpChatSetting(player) == 0) {
            for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
                if (JobGetters.getLevelUpChatSetting(serverPlayer) != 2)
                    ChatHandler.sendLevelUpMessage(serverPlayer, translatableComponent.getString()); //TODO players only see their own levels. And if they click on it it does /tell [their name]
            }
        } else if (JobGetters.getLevelUpChatSetting(player) == 1) {
            ChatHandler.sendLevelUpMessage(player, translatableComponent.getString());
        }
        if (JobGetters.getLevelUpSoundSetting(player) == 0)
            EventPlayerTick.levelUpHashMap.put((ServerPlayer) player, 0);

        if (level == 5 || level == 25 || level == 50 || level == 75 || level == 100) {
            AdvancementHandler.grandJobAdvancement((ServerPlayer) player, job, "_lvl_" + level);
        }
    }


}
