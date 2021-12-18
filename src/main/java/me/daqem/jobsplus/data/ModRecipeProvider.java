package me.daqem.jobsplus.data;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(ModItems.SMALL_BACKPACK.get())
                .pattern("ILI")
                .pattern("LSL")
                .pattern("ILI")
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('L', Tags.Items.LEATHER)
                .define('S', Tags.Items.STRING)
                .unlockedBy("iron_block", has(Items.IRON_BLOCK))
                .save(RecipeInjector.Inject(consumer, ModRecipes.BACKPACK_UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.MEDIUM_BACKPACK.get())
                .pattern("GLG")
                .pattern("LBL")
                .pattern("GLG")
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('L', Tags.Items.LEATHER)
                .define('B', ModItems.SMALL_BACKPACK.get())
                .unlockedBy("gold_block", has(Items.GOLD_BLOCK))
                .save(RecipeInjector.Inject(consumer, ModRecipes.BACKPACK_UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.LARGE_BACKPACK.get())
                .pattern("DLD")
                .pattern("LBL")
                .pattern("DLD")
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('L', Tags.Items.LEATHER)
                .define('B', ModItems.MEDIUM_BACKPACK.get())
                .unlockedBy("diamond_block", has(Items.DIAMOND_BLOCK))
                .save(RecipeInjector.Inject(consumer, ModRecipes.BACKPACK_UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUGE_BACKPACK.get())
                .pattern("ELE")
                .pattern("LBL")
                .pattern("ELE")
                .define('E', Tags.Items.STORAGE_BLOCKS_EMERALD)
                .define('L', Tags.Items.LEATHER)
                .define('B', ModItems.LARGE_BACKPACK.get())
                .unlockedBy("emerald_block", has(Items.EMERALD_BLOCK))
                .save(RecipeInjector.Inject(consumer, ModRecipes.BACKPACK_UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.ENDER_BACKPACK.get())
                .pattern("GLG")
                .pattern("LEL")
                .pattern("GLG")
                .define('G', Tags.Items.ENDER_PEARLS)
                .define('L', Tags.Items.LEATHER)
                .define('E', Tags.Items.CHESTS_ENDER)
                .unlockedBy("ender_chest", has(Items.ENDER_CHEST))
                .save(RecipeInjector.Inject(consumer, ModRecipes.BACKPACK_UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.EXP_JAR.get())
                .pattern("PPP")
                .pattern("GEG")
                .pattern("GGG")
                .define('P', ItemTags.PLANKS)
                .define('G', Tags.Items.GLASS)
                .define('E', Tags.Items.GEMS_EMERALD)
                .unlockedBy("emerald", has(Items.EMERALD))
                .save(RecipeInjector.Inject(consumer, ModRecipes.EXP_JAR_RECIPE.get()));
    }

    static class RecipeInjector implements FinishedRecipe {

        final FinishedRecipe inner;
        RecipeSerializer<?> serializerOverride;

        public RecipeInjector(FinishedRecipe innerIn, RecipeSerializer<?> serializerOverrideIn) {
            this.inner = innerIn;
            this.serializerOverride = serializerOverrideIn;
        }

        public static Consumer<FinishedRecipe> Inject(Consumer<FinishedRecipe> consumer, RecipeSerializer<?> serializer) {
            return iFinishedRecipe -> consumer.accept(new RecipeInjector(iFinishedRecipe, serializer));
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject jsonObject) {
            this.inner.serializeRecipeData(jsonObject);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.inner.getId();
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return this.inner.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.inner.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.inner.getAdvancementId();
        }

        @Override
        public @NotNull JsonObject serializeRecipe() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", Objects.requireNonNull(Objects.requireNonNullElseGet(this.serializerOverride, this.inner::getType).getRegistryName()).toString());
            serializeRecipeData(jsonObject);
            return jsonObject;
        }
    }
}
