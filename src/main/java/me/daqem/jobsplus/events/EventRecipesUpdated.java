package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class EventRecipesUpdated {

    @SubscribeEvent
    public void onServerStarted(RecipesUpdatedEvent event) {
        if (Minecraft.getInstance().level == null) return;

        ModRecipeManager.requiredLevelsClient.clear();
        for (ConstructionCraftingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ConstructionRecipeType.INSTANCE)) {
            ModRecipeManager.requiredLevelsClient.put(recipe.getResultItem(), Map.of(recipe.getJob(), recipe.getRequiredLevel()));
        }
    }
}
