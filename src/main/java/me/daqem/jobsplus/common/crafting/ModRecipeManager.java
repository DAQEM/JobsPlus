package me.daqem.jobsplus.common.crafting;

import me.daqem.jobsplus.common.item.ModExperienceBottleItem;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ModRecipeManager {

    public static Map<ItemStack, Map<Jobs, Integer>> requiredLevelsServer = new HashMap<>();
    public static Map<ItemStack, Map<Jobs, Integer>> requiredLevelsClient = new HashMap<>();

    public static int getRequiredJobLevelServer(ItemStack itemStack) {
        return getRequiredJobLevel(itemStack, requiredLevelsServer);
    }

    public static int getRequiredJobLevelClient(ItemStack itemStack) {
        return getRequiredJobLevel(itemStack, requiredLevelsClient);
    }

    public static int getRequiredJobLevel(ItemStack itemStack, Map<ItemStack, Map<Jobs, Integer>> pRequiredLevels) {
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
        return getJob(itemStack, requiredLevelsClient);
    }

    public static Jobs getJob(ItemStack itemStack, Map<ItemStack, Map<Jobs, Integer>> pRequiredLevels) {
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
}
