package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipeType;
import me.daqem.jobsplus.config.RequirementsConfig;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventServerAboutToStart {


    @SuppressWarnings("deprecation")
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ModRecipeManager.requiredLevelsServer.clear();
        for (ConstructionCraftingRecipe recipe : event.getServer().getRecipeManager().getAllRecipesFor(ConstructionRecipeType.INSTANCE)) {
            ModRecipeManager.requiredLevelsServer.put(recipe.getResultItem(), Map.of(recipe.getJob(), recipe.getRequiredLevel()));
        }

        //Generate Jobs+ items default entries
        if (RequirementsConfig.ADD_JOBSPLUS_ITEMS.get()) {
            try {
                ArrayList<Item> configItems = new ArrayList<>();
                for (Object entry : RequirementsConfig.REQUIREMENTS.get()) {
                    if (entry instanceof ArrayList<?> list) {
                        String itemLoc = (String) list.get(0);
                        configItems.add(Registry.ITEM.get(new ResourceLocation(itemLoc.split(":")[0], itemLoc.split(":")[1])));
                    }
                }

                @SuppressWarnings("unchecked")
                ArrayList<List<Object>> list = (ArrayList<List<Object>>) RequirementsConfig.REQUIREMENTS.get();

                for (RegistryObject<Item> registryObject : ModItems.ITEMS.getEntries()) {
                    if (registryObject.get() == ModItems.CONSTRUCTION_TABLE.get()) continue;
                    if (configItems.stream().noneMatch(item -> item.getDescriptionId().equals(registryObject.get().getDescriptionId()))) {
                        String key = Registry.ITEM.getKey(registryObject.get()).toString();
                        Map<String, Tuple<String, Integer>> defaultRequirements = RequirementsConfig.getDefaultRequirements();
                        if (defaultRequirements.containsKey(key)) {
                            list.add(List.of(
                                    key,
                                    defaultRequirements.get(key).getA(),
                                    defaultRequirements.get(key).getB()));

                        }
                    }
                }
                RequirementsConfig.REQUIREMENTS.set(list);
                RequirementsConfig.REQUIREMENTS.save();
            } catch (Exception ignored) {
            }
        }
    }
}
