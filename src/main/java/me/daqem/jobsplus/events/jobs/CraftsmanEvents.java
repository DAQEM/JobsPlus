package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class CraftsmanEvents {

    private final static ArrayList<String> bannedItems = new ArrayList<>(List.of("stick"));
    private final Jobs job = Jobs.CRAFTSMAN;

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getPlayer();
        if (JobGetters.jobIsEnabled(player, job)) {
            if (!player.isCreative()) {
                if (!bannedItems.contains(event.getCrafting().getDescriptionId().replace("item.minecraft.", ""))) {
                    ExpHandler.addEXPLow(player, job);
                }
            }
        }
    }
}
