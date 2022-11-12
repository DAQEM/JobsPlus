package me.daqem.jobsplus.common.crafting.construction;

import me.daqem.jobsplus.common.inventory.construction.container.ConstructionCraftingContainer;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public interface ConstructionCraftingRecipe extends Recipe<ConstructionCraftingContainer> {
    default @NotNull RecipeType<?> getType() {
        return ConstructionRecipeType.INSTANCE;
    }

    int getWidth();

    int getHeight();

    Jobs getJob();

    int getRequiredLevel();

}