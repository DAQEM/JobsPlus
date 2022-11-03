package me.daqem.jobsplus.common.crafting;

import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Iterator;

public interface ModPlaceRecipe {
    default void placeRecipe(Recipe<?> recipe, int width, int height, int resultId, NonNullList<Ingredient> ingredients) {
        int count = 0;
        int offset = 0;
        int recipeWidth = width;

        if (recipe instanceof ConstructionRecipe constructionRecipe) {
            recipeWidth = constructionRecipe.getWidth();
        }

        Iterator<Ingredient> iterator = ingredients.iterator();

        switch (ingredients.size()) {
            case 1 -> offset = 12;
            case 2, 3 -> offset = recipeWidth == 1 ? 7 : 11;
            case 4 -> offset = recipeWidth == 1 ? 2 : recipeWidth == 2 ? 6 : 10;
            case 5 -> offset = recipeWidth == 1 ? 2 : 10;
            case 6, 9, 12 -> offset = 6;
            case 8 -> offset = recipeWidth == 2 ? 4 : 5;
            case 10 -> offset = recipeWidth == 2 ? 1 : 5;
            case 15 -> offset = recipeWidth == 3 ? 1 : 5;
        }

        for (int forHeight = 0; forHeight < height; ++forHeight) {
            if (count == resultId) {
                ++count;
            }

            for (int forWidth = 0; forWidth < width; ++forWidth) {
                if (!iterator.hasNext()) {
                    return;
                }

                if (count - 1 != 0) offset += ((count - 1) % recipeWidth == 0 ? -recipeWidth + 5 : 0);

                this.addItemToSlot(iterator, count + offset);
                ++count;
            }
        }

    }

    void addItemToSlot(Iterator<Ingredient> p_135415_, int p_135416_);
}
