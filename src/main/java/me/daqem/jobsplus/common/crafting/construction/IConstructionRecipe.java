package me.daqem.jobsplus.common.crafting.construction;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface IConstructionRecipe<T extends Container> extends Recipe<T> {
    int getRecipeWidth();

    int getRecipeHeight();
}