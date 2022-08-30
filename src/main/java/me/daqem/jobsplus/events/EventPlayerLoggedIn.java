package me.daqem.jobsplus.events;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.AdvancementHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerLoggedIn {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final Player player = event.getEntity();
        player.refreshDisplayName();

        //Check is display is allowed. If not and player has a display. Clearing display.
        if (JobGetters.getDisplay(player) != 0 && !Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()) {
            JobSetters.setDisplay(player, -1);
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (!AdvancementHandler.isDone(serverPlayer, "root")) {
                player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack(), "/give " + player.getScoreboardName() + " written_book{pages:['[\"\",{\"text\":\"Welcome to Jobs+\",\"bold\":true},{\"text\":\"\\\\n\\\\nPress \\'J\\' to open the Jobs+ menu.\\\\n\\\\nJobs+ is a mod that adds 10 jobs to Minecraft. Each with their own custom items and power-ups. Every job has 100 levels and every level requires a certain amount of job-EXP.\",\"color\":\"reset\"}]','{\"text\":\"You can get these job-EXP points by doing certain tasks that belong to that job. See the \\'How to get EXP\\' page in the Jobs+ menu for more information on this. Every time you level up a job, you will get " + Config.COINS_PER_LEVEL_UP.get() + " job coin. These coins can be used to buy new jobs or power-ups.\"}','[\"\",{\"text\":\"Custom Items.\",\"bold\":true},{\"text\":\"\\\\n\\\\nAs your job-level gets higher, the craftable items get better and better.\\\\n\\\\nIf you want more information about the items, visit the \\'Job Crafting Recipes\\' page in the Jobs+ menu.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Power-ups.\",\"bold\":true},{\"text\":\"\\\\n\\\\nEach job has 3 power-ups, which can be bought using 10 coins and a superpower, which is unlocked after reaching job-level 100. For more information about power-ups. Visit the \\'Power-ups\\' page in the Jobs+ menu. \",\"color\":\"reset\"}]'],title:\"Jobs+ Introduction\",author:\"DAQEM Studios\",display:{Lore:[\"A short introduction of the Jobs+ mod.\"]}}");
                AdvancementHandler.grand(serverPlayer, "root");
            }
        }

        try {
            for (ServerPlayer serverPlayer : Objects.requireNonNull(player.getServer()).getPlayerList().getPlayers()) {
                serverPlayer.refreshDisplayName();
                serverPlayer.refreshTabListName();
            }
        } catch (Exception ignored) {
        }
    }
}
