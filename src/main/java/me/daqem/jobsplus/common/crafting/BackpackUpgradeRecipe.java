package me.daqem.jobsplus.common.crafting;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.item.BackpackItem;
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

public class BackpackUpgradeRecipe extends ShapedRecipe {

    public BackpackUpgradeRecipe(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container) {
        if (container.menu instanceof CraftingMenu craftingMenu) {
            Player player = craftingMenu.player;
            final ItemStack backpackItem = super.assemble(container);
            var backpackPresent = false;
            for (int index = 0; index < container.getContainerSize(); ++index) {
                final var slotStack = container.getItem(index);
                if (!slotStack.isEmpty()) {
                    final var item = slotStack.getItem();
                    if (item instanceof BackpackItem) {
                        if (backpackPresent) {
                            return ItemStack.EMPTY;
                        }
                        backpackPresent = true;
                        if (slotStack.hasTag()) {
                            backpackItem.setTag(Objects.requireNonNull(slotStack.getTag()).copy());
                        }
                    }
                }
            }
            final Jobs builder = Jobs.BUILDER;
            if (JobGetters.jobIsEnabled(player, builder)) {
                if (JobGetters.getJobLevel(player, builder) >= 5 && backpackItem.getItem() == ModItems.SMALL_BACKPACK.get()) {
                    return backpackItem;
                }
                if (JobGetters.getJobLevel(player, builder) >= 10 && backpackItem.getItem() == ModItems.MEDIUM_BACKPACK.get()) {
                    return backpackItem;
                }
                if (JobGetters.getJobLevel(player, builder) >= 20 && backpackItem.getItem() == ModItems.LARGE_BACKPACK.get()) {
                    return backpackItem;
                }
                if (JobGetters.getJobLevel(player, builder) >= 35 && backpackItem.getItem() == ModItems.HUGE_BACKPACK.get()) {
                    return backpackItem;
                }
                if (JobGetters.getJobLevel(player, builder) >= 10 && backpackItem.getItem() == ModItems.ENDER_BACKPACK.get()) {
                    return backpackItem;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BACKPACK_UPGRADE_RECIPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BackpackUpgradeRecipe> {
        @Nullable
        @Override
        public BackpackUpgradeRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new BackpackUpgradeRecipe(Objects.requireNonNull(RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer)));
        }

        @Override
        @Nonnull
        public BackpackUpgradeRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new BackpackUpgradeRecipe(RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error reading CopyBackpack Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull BackpackUpgradeRecipe recipe) {
            try {
                RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error writing CopyBackpack Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
