package me.daqem.jobsplus.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.init.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ConstructionCategory implements IRecipeCategory<ConstructionCraftingRecipe> {

    public static final ResourceLocation BACKGROUND = JobsPlus.getId("textures/gui/jei/grid.png");
    public static final ResourceLocation UID = JobsPlus.getId("construction");
    public final IGuiHelper guiHelper;
    public final IDrawable background;
    public final IDrawable icon;

    public ConstructionCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.background = guiHelper.createDrawable(BACKGROUND, 0, 9, 162, 106);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.CONSTRUCTION_TABLE.get()));
    }

    @Override
    public @NotNull RecipeType<ConstructionCraftingRecipe> getRecipeType() {
        return JobsPlusJeiPlugin.CONSTRUCTION_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return JobsPlus.literal("Construction Table");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ConstructionCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        int width = recipe.getWidth();
        int height = recipe.getHeight();
        int count = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int i = width == 1 ? 36 : width == 2 || width == 3 ? 18 : 0;
                int xOffset = 19 + i + x % width * 18;
                int yOffset = 10 + i + y * 18;
                builder.addSlot(RecipeIngredientRole.INPUT, xOffset, yOffset).addIngredients(recipe.getIngredients().get(count));
                count++;
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 46).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(@NotNull ConstructionCraftingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
        guiHelper.createDrawable(BACKGROUND, 0, 0, 18, 18).draw(stack, 106, 36);
    }
}
