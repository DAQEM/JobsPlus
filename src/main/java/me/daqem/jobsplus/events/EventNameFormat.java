package me.daqem.jobsplus.events;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventNameFormat {

    private static String createTabName(Player player) {
        for (Jobs job : Jobs.values()) {
            if (job.get() + 1 == JobGetters.getDisplay(player)) {
                return ChatHandler.ColorizedJobName(job) + player.getScoreboardName();
            }
        }
        return player.getScoreboardName();
    }

    @SubscribeEvent
    public void onTabNameChange(PlayerEvent.TabListNameFormat event) {
        Player player = event.getPlayer();
        if (!Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()) return;
        if (player.isSpectator()) return;
        event.setDisplayName(new KeybindComponent(createTabName(player)));
    }

    @SubscribeEvent
    public void onNameChange(PlayerEvent.NameFormat event) {
        Player player = event.getPlayer();
        if (!Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()) return;
        if (player.isSpectator()) return;
        event.setDisplayname(new KeybindComponent(createTabName(player)));
    }
}
