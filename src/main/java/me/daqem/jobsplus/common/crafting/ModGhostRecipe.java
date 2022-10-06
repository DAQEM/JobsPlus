package me.daqem.jobsplus.common.crafting;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.GhostRecipe;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ModGhostRecipe extends GhostRecipe {

    private final List<GhostIngredient> ingredients = Lists.newArrayList();
    float time;
    private Recipe<?> recipe;

    public void clear() {
        this.recipe = null;
        this.ingredients.clear();
        this.time = 0.0F;
    }

    public void addIngredient(@NotNull Ingredient p_100144_, int p_100145_, int p_100146_) {
        this.ingredients.add(new ModGhostRecipe.GhostIngredient(p_100144_, p_100145_, p_100146_));
    }

    public ModGhostRecipe.@NotNull GhostIngredient get(int p_100142_) {
        return this.ingredients.get(p_100142_);
    }

    public int size() {
        return this.ingredients.size();
    }

    @Nullable
    public Recipe<?> getRecipe() {
        return this.recipe;
    }

    public void setRecipe(@NotNull Recipe<?> p_100148_) {
        this.recipe = p_100148_;
    }

    public void render(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, int leftPos, int topPos, float partialTicks) {
        if (!Screen.hasControlDown()) {
            this.time += partialTicks;
        }

        for (int i = 0; i < this.ingredients.size(); ++i) {
            ModGhostRecipe.GhostIngredient ghostIngredient = this.ingredients.get(i);
            int j = ghostIngredient.getX() + leftPos;
            int k = ghostIngredient.getY() + topPos;

            ItemStack itemstack = ghostIngredient.getItem();

            if (minecraft.player != null) {
                for (Slot containerSlot : minecraft.player.containerMenu.slots) {
                    if (containerSlot.x == ghostIngredient.getX() && containerSlot.y == ghostIngredient.getY()) {
                        if (!itemstack.getDescriptionId().equals(containerSlot.getItem().getDescriptionId())) {
                            if (i == 0) {
                                GuiComponent.fill(poseStack, j - 4, k - 4, j + 20, k + 20, 822018048);
                            } else {
                                GuiComponent.fill(poseStack, j, k, j + 16, k + 16, 822018048);
                            }

                            ItemRenderer itemrenderer = minecraft.getItemRenderer();
                            itemrenderer.renderAndDecorateFakeItem(itemstack, j, k);
                            RenderSystem.depthFunc(516);
                            GuiComponent.fill(poseStack, j, k, j + 16, k + 16, 822083583);
                            RenderSystem.depthFunc(515);
                            if (i == 0) {
                                itemrenderer.renderGuiItemDecorations(minecraft.font, itemstack, j, k);
                            }
                        }
                    }
                }
            }
        }
    }
}
