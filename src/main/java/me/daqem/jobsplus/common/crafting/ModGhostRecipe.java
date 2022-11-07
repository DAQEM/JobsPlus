package me.daqem.jobsplus.common.crafting;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ModGhostRecipe {
    private final List<GhostIngredient> ingredients = Lists.newArrayList();
    float time;
    @Nullable
    private Recipe<?> recipe;

    public void clearIngredients() {
        this.ingredients.clear();
    }

    public void addIngredient(Ingredient ingredient, int x, int y, Slot slot) {
        this.ingredients.add(new GhostIngredient(ingredient, x, y, slot));
    }

    public GhostIngredient get(int index) {
        return this.ingredients.get(index);
    }

    public int size() {
        return this.ingredients.size();
    }

    @Nullable
    public Recipe<?> getRecipe() {
        return this.recipe;
    }

    public void setRecipe(@Nullable Recipe<?> recipe) {
        this.recipe = recipe;
    }

    public void render(PoseStack poseStack, Minecraft minecraft, int leftPos, int topPos, boolean showResultItem, float partialTicks) {
        if (!Screen.hasControlDown()) {
            this.time += partialTicks;
        }

        for (int i = 0; i < this.ingredients.size(); ++i) {
            GhostIngredient ghostIngredient = this.ingredients.get(i);
            Item itemInSlot = ghostIngredient.getSlot().getItem().getItem();
            boolean flag = true;
            if (itemInSlot != Items.AIR) {
                for (ItemStack itemStack : ghostIngredient.getIngredient().getItems()) {
                    if (itemStack.is(itemInSlot)) flag = false;
                }
            }
            if (flag) {
                int j = ghostIngredient.getX() + leftPos;
                int k = ghostIngredient.getY() + topPos;
                if (i == 0 && showResultItem) {
                    GuiComponent.fill(poseStack, j - 4, k - 4, j + 20, k + 20, 822018048);
                } else {
                    GuiComponent.fill(poseStack, j, k, j + 16, k + 16, 822018048);
                }

                ItemStack itemstack = ghostIngredient.getItem();
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

    @OnlyIn(Dist.CLIENT)
    public class GhostIngredient {
        private final Ingredient ingredient;
        private final int x;
        private final int y;
        private final Slot slot;

        public GhostIngredient(Ingredient ingredient, int x, int y, Slot slot) {
            this.ingredient = ingredient;
            this.x = x;
            this.y = y;
            this.slot = slot;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public ItemStack getItem() {
            ItemStack[] itemStacks = this.ingredient.getItems();
            return itemStacks.length == 0 ? ItemStack.EMPTY : itemStacks[Mth.floor(time / 30.0F) % itemStacks.length];
        }

        public Slot getSlot() {
            return slot;
        }

        public Ingredient getIngredient() {
            return ingredient;
        }
    }
}