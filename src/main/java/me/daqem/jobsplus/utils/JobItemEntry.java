package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class JobItemEntry {

    private final ResourceLocation recipeID;
    private final ItemStack stack;
    private final ArrayList<ItemStack> patternList;
    private final Jobs job;
    private int requiredLevel;

    public JobItemEntry(ResourceLocation recipeID, ItemStack stack,
                        ArrayList<ItemStack> patternList, Jobs job, int requiredLevel) {
        this.recipeID = recipeID;
        this.stack = stack;
        this.patternList = patternList;
        this.job = job;
        this.requiredLevel = requiredLevel;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Jobs getJob() {
        return job;
    }

    public ItemStack getStack() {
        return stack;
    }

    public ArrayList<ItemStack> getPatternList() {
        return patternList;
    }

    public ResourceLocation getRecipeID() {
        return recipeID;
    }
}
