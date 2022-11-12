package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventRecipesUpdated {

    @SubscribeEvent
    public void onServerStarted(RecipesUpdatedEvent event) {
        ModRecipeManager.fillRequiredLevelsClient();
    }
}
