package me.daqem.jobsplus.common.crafting;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModRecipes;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ShapedJobBasedRecipe extends ShapedRecipe {

    public ShapedJobBasedRecipe(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container) {
        if (container.menu instanceof CraftingMenu craftingMenu) {
            Player player = craftingMenu.player;
            if (JobGetters.jobIsEnabled(player, Jobs.ENCHANTER)
                    && JobGetters.getJobLevel(player, Jobs.ENCHANTER) >= 5
                    && Objects.equals(getResultItem().getItem().getRegistryName(), JobsPlus.getId("exp_jar"))) {
                return super.assemble(container);
            }
            if (JobGetters.jobIsEnabled(player, Jobs.SMITH)) {
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 1) {
                    if (getResultItem().getItem() == ModItems.REINFORCED_IRON_HELMET.get() ||
                            getResultItem().getItem() == ModItems.REINFORCED_IRON_BOOTS.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 5) {
                    if (getResultItem().getItem() == ModItems.OBSIDIAN_HELMET.get() ||
                            getResultItem().getItem() == ModItems.OBSIDIAN_BOOTS.get() ||
                            getResultItem().getItem() == ModItems.REINFORCED_IRON_LEGGINGS.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 10) {
                    if (getResultItem().getItem() == ModItems.OBSIDIAN_LEGGINGS.get() ||
                            getResultItem().getItem() == ModItems.REINFORCED_IRON_CHESTPLATE.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 20) {
                    if (getResultItem().getItem() == ModItems.REINFORCED_DIAMOND_HELMET.get() ||
                            getResultItem().getItem() == ModItems.REINFORCED_DIAMOND_BOOTS.get() ||
                            getResultItem().getItem() == ModItems.OBSIDIAN_CHESTPLATE.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 35) {
                    if (getResultItem().getItem() == ModItems.REINFORCED_DIAMOND_LEGGINGS.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 50) {
                    if (getResultItem().getItem() == ModItems.REINFORCED_DIAMOND_CHESTPLATE.get() ||
                            getResultItem().getItem() == ModItems.REINFORCED_NETHERITE_HELMET.get() ||
                            getResultItem().getItem() == ModItems.REINFORCED_NETHERITE_BOOTS.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 75) {
                    if (getResultItem().getItem() == ModItems.REINFORCED_NETHERITE_LEGGINGS.get()) {
                        return super.assemble(container);
                    }
                }
                if (JobGetters.getJobLevel(player, Jobs.SMITH) >= 100) {
                    if (getResultItem().getItem() == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.MINER)) {
                if (JobGetters.getJobLevel(player, Jobs.MINER) >= 5) {
                    if (getResultItem().getItem() == ModItems.MINERS_HAMMER_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.DIGGER)) {
                if (JobGetters.getJobLevel(player, Jobs.DIGGER) >= 5) {
                    if (getResultItem().getItem() == ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.BUILDER)) {
                if (JobGetters.getJobLevel(player, Jobs.BUILDER) >= 5) {
                    if (getResultItem().getItem() == ModItems.SMALL_BACKPACK.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.LUMBERJACK)) {
                if (JobGetters.getJobLevel(player, Jobs.LUMBERJACK) >= 5) {
                    if (getResultItem().getItem() == ModItems.LUMBERJACK_AXE_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.FARMER)) {
                if (JobGetters.getJobLevel(player, Jobs.FARMER) >= 5) {
                    if (getResultItem().getItem() == ModItems.FARMERS_HOE_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.HUNTER)) {
                if (JobGetters.getJobLevel(player, Jobs.HUNTER) >= 5) {
                    if (getResultItem().getItem() == ModItems.HUNTERS_SWORD_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                    if (getResultItem().getItem() == ModItems.HUNTERS_BOW_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                }
            }
            if (JobGetters.jobIsEnabled(player, Jobs.FISHERMAN)) {
                if (JobGetters.getJobLevel(player, Jobs.FISHERMAN) >= 5) {
                    if (getResultItem().getItem() == ModItems.FISHERMANS_ROD_LEVEL_1.get()) {
                        return super.assemble(container);
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SHAPED_JOB_BASED_RECIPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShapedJobBasedRecipe> {
        @Nullable
        @Override
        public ShapedJobBasedRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new ShapedJobBasedRecipe(Objects.requireNonNull(RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer)));
        }

        @Override
        @Nonnull
        public ShapedJobBasedRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new ShapedJobBasedRecipe(RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error reading ShapedJobBasedRecipe Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ShapedJobBasedRecipe recipe) {
            try {
                RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error writing ShapedJobBasedRecipe Recipe to packet: ", exception);
                throw exception;
            }
        }
    }

}
