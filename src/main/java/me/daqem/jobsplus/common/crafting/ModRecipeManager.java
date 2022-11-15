package me.daqem.jobsplus.common.crafting;

import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipeType;
import me.daqem.jobsplus.common.item.ModExperienceBottleItem;
import me.daqem.jobsplus.config.RequirementsConfig;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModRecipeManager {

    public static Map<ItemStack, Map<Jobs, Integer>> requiredLevelsServer = new HashMap<>();
    public static Map<ItemStack, Map<Jobs, Integer>> requiredLevelsClient = new HashMap<>();

    public static int getRequiredJobLevelServer(ItemStack itemStack) {
        return getRequiredJobLevel(itemStack, requiredLevelsServer);
    }

    public static int getRequiredJobLevelClient(ItemStack itemStack) {
        int requiredJobLevel = getRequiredJobLevel(itemStack, requiredLevelsClient);
        if (requiredJobLevel == 101) {
            fillRequiredLevelsClient();
            requiredJobLevel = getRequiredJobLevel(itemStack, requiredLevelsClient);
        }
        return requiredJobLevel;
    }

    public static int getRequiredJobLevel(ItemStack itemStack, Map<ItemStack, Map<Jobs, Integer>> pRequiredLevels) {
//        int requiredLevelConfig = getRequiredJobLevelConfig(itemStack);
//        if (requiredLevelConfig != 101) {
//            return requiredLevelConfig;
//        }
        if (!(itemStack.getItem() instanceof ModExperienceBottleItem))
            itemStack = itemStack.getItem().getDefaultInstance();
        for (Map.Entry<ItemStack, Map<Jobs, Integer>> entry : pRequiredLevels.entrySet()) {
            ItemStack entryItemStack = entry.getKey();
            if (entryItemStack.is(itemStack.getItem())) {
                if (entryItemStack.equals(itemStack, false)) {
                    for (Map.Entry<Jobs, Integer> jobEntry : entry.getValue().entrySet()) {
                        return jobEntry.getValue();
                    }
                }
            }
        }
        return 101;
    }

    public static Jobs getJobServer(ItemStack itemStack) {
        return getJob(itemStack, requiredLevelsServer);
    }

    public static Jobs getJobClient(ItemStack itemStack) {
        Jobs job = getJob(itemStack, requiredLevelsClient);
        if (job == null) {
            fillRequiredLevelsClient();
            job = getJob(itemStack, requiredLevelsClient);
        }
        return job;
    }

    public static Jobs getJob(ItemStack itemStack, Map<ItemStack, Map<Jobs, Integer>> pRequiredLevels) {
//        Jobs job = getJobConfig(itemStack);
//        if (job != null) {
//            return job;
//        }
        if (!(itemStack.getItem() instanceof ModExperienceBottleItem))
            itemStack = itemStack.getItem().getDefaultInstance();
        for (Map.Entry<ItemStack, Map<Jobs, Integer>> entry : pRequiredLevels.entrySet()) {
            ItemStack entryItemStack = entry.getKey();
            if (entryItemStack.is(itemStack.getItem())) {
                if (entryItemStack.equals(itemStack, false)) {
                    for (Map.Entry<Jobs, Integer> jobEntry : entry.getValue().entrySet()) {
                        return jobEntry.getKey();
                    }
                }
            }
        }
        return null;
    }

    public static void fillRequiredLevelsClient() {
        if (Minecraft.getInstance().level == null) return;
        ModRecipeManager.requiredLevelsClient.clear();
        for (ConstructionCraftingRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ConstructionRecipeType.INSTANCE)) {
            ModRecipeManager.requiredLevelsClient.put(recipe.getResultItem(), Map.of(recipe.getJob(), recipe.getRequiredLevel()));
        }
    }

    @SuppressWarnings("all")
    private static int getRequiredJobLevelConfig(ItemStack itemStack) {
        for (Object o : RequirementsConfig.REQUIREMENTS.get()) {
            if (o instanceof ArrayList<?> list) {
                if (Registry.ITEM.get(new ResourceLocation(list.get(0).toString().split(":")[0], list.get(0).toString().split(":")[1])) == itemStack.getItem()) {
                    if (list.get(2) instanceof Integer requiredLevel) {
                        return requiredLevel;
                    }
                }
            }
        }
        return 101;
    }

    @SuppressWarnings("all")
    private static Jobs getJobConfig(ItemStack itemStack) {
        for (Object o : RequirementsConfig.REQUIREMENTS.get()) {
            if (o instanceof ArrayList<?> list) {
                if (Registry.ITEM.get(new ResourceLocation(list.get(0).toString().split(":")[0], list.get(0).toString().split(":")[1])) == itemStack.getItem()) {
                    if (list.get(1) instanceof String jobString) {
                        return Jobs.getFromString(jobString);
                    }
                }
            }
        }
        return null;
    }
}
