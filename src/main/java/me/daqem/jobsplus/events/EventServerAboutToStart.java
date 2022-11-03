package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipeType;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

import java.util.Map;

public class EventServerAboutToStart {

    public static void onServerStarted(ServerAboutToStartEvent event) {
        for (ConstructionCraftingRecipe recipe : event.getServer().getRecipeManager().getAllRecipesFor(ConstructionRecipeType.INSTANCE)) {
            ModRecipeManager.requiredLevels.put(recipe.getResultItem(), Map.of(recipe.getJob(), recipe.getRequiredLevel()));
        }
    }
}
