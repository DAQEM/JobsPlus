package me.daqem.jobsplus.events;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.JobGetters;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class EventNameFormat {

    @SubscribeEvent
    public void onTabNameChange(PlayerEvent.TabListNameFormat event) {
        Player player = event.getPlayer();

        if (!player.isSpectator()) {
            event.setDisplayName(new KeybindComponent(createTabName(player)));
        }
    }

    @SubscribeEvent
    public void onNameChange(PlayerEvent.NameFormat event) {
        Player player = event.getPlayer();

        if (!player.isSpectator()) {
            event.setDisplayname(new KeybindComponent(createTabName(player)));
        }
    }
    
    private static String createTabName(Player player) {

        if (JobGetters.getDisplay(player) == 0)
            return ChatFormatting.WHITE + "" + ChatFormatting.BOLD + "UNEMPLOYED " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 1)
            return ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD + "ALCHEMIST " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 2)
            return ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "BUILDER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 3)
            return ChatFormatting.RED + "" + ChatFormatting.BOLD + "BUTCHER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 4)
            return ChatFormatting.BLUE + "" + ChatFormatting.BOLD + "CRAFTSMAN " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 5)
            return ChatFormatting.YELLOW + "" + ChatFormatting.BOLD + "DIGGER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 6)
            return ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.BOLD + "ENCHANTER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 7)
            return ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "FARMER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 8)
            return ChatFormatting.AQUA + "" + ChatFormatting.BOLD + "FISHERMAN " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 9)
            return ChatFormatting.DARK_RED + "" + ChatFormatting.BOLD + "HUNTER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 10)
            return ChatFormatting.GOLD + "" + ChatFormatting.BOLD + "LUMBERJACK " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 11)
            return ChatFormatting.GRAY + "" + ChatFormatting.BOLD + "MINER " + ChatFormatting.RESET + player.getScoreboardName();
        if (JobGetters.getDisplay(player) == 12)
            return ChatFormatting.DARK_GRAY + "" + ChatFormatting.BOLD + "SMITH " + ChatFormatting.RESET + player.getScoreboardName();
        return player.getScoreboardName();
    }
}
