package me.daqem.jobsplus.common.crafting.construction;

import net.minecraft.world.item.crafting.RecipeType;

public class ConstructionRecipeType implements RecipeType<ConstructionCraftingRecipe> {

    public static final ConstructionRecipeType INSTANCE = new ConstructionRecipeType();

    private ConstructionRecipeType() {
    }
}
