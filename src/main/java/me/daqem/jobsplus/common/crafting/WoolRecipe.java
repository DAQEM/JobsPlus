package me.daqem.jobsplus.common.crafting;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.JobsPlus;
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
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class WoolRecipe extends ShapelessRecipe {

    public WoolRecipe(ShapelessRecipe shapelessRecipe) {
        super(shapelessRecipe.getId(), shapelessRecipe.getGroup(), shapelessRecipe.getResultItem(), shapelessRecipe.getIngredients());
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container) {
        if (container.menu instanceof CraftingMenu craftingMenu) {
            Player player = craftingMenu.player;
            if (JobGetters.jobIsEnabled(player, Jobs.HUNTER) && JobGetters.getJobLevel(player, Jobs.HUNTER) >= 1) {
                return super.assemble(container);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WOOL_RECIPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<WoolRecipe> {
        @Nullable
        @Override
        public WoolRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new WoolRecipe(Objects.requireNonNull(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer)));
        }

        @Override
        @Nonnull
        public WoolRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new WoolRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromJson(recipeId, json));
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error reading CopyBackpack Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull WoolRecipe recipe) {
            try {
                RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error writing CopyBackpack Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
