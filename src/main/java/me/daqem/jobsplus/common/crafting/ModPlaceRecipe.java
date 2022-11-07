package me.daqem.jobsplus.common.crafting;

import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipe;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Iterator;

public interface ModPlaceRecipe<T> {

    default void placeRecipe(int gridWidth, int gridHeight, int resultSlotIndex, Recipe<?> recipe, Iterator<T> ingredientIterator, int idk_zero) {
        int i = gridWidth;
        int j = gridHeight;
        if (recipe instanceof ConstructionRecipe constructionRecipe) {
            i = constructionRecipe.getRecipeWidth();
            j = constructionRecipe.getRecipeHeight();
        }

        int k1 = 0;

        for (int k = 0; k < gridHeight; ++k) {
            if (k1 == resultSlotIndex) {
                ++k1;
            }

            boolean flag = (float) j < (float) gridHeight / 2.0F;
            int l = Mth.floor((float) gridHeight / 2.0F - (float) j / 2.0F);
            if (flag && l > k) {
                k1 += gridWidth;
                ++k;
            }

            for (int i1 = 0; i1 < gridWidth; ++i1) {
                if (!ingredientIterator.hasNext()) {
                    return;
                }

                flag = (float) i < (float) gridWidth / 2.0F;
                l = Mth.floor((float) gridWidth / 2.0F - (float) i / 2.0F);
                int j1 = i;
                boolean flag1 = i1 < i;
                if (flag) {
                    j1 = l + i;
                    flag1 = l <= i1 && i1 < l + i;
                }

                if (flag1) {
                    this.addItemToSlot(ingredientIterator, k1, idk_zero, k, i1);
                } else if (j1 == i1) {
                    k1 += gridWidth - i1;
                    break;
                }

                ++k1;
            }
        }

    }

    void addItemToSlot(Iterator<T> p_135415_, int p_135416_, int p_135417_, int p_135418_, int p_135419_);

}
