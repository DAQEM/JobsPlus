package me.daqem.jobsplus.common.crafting;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.item.BackpackItem;
import me.daqem.jobsplus.common.item.ExcavatorItem;
import me.daqem.jobsplus.common.item.HammerItem;
import me.daqem.jobsplus.common.item.LumberAxeItem;
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

public class UpgradeRecipe extends ShapedRecipe {

    public UpgradeRecipe(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container) {
        if (container.menu instanceof CraftingMenu craftingMenu) {
            Player player = craftingMenu.player;
            final ItemStack upgradedStack = super.assemble(container);
            var backpackPresent = false;
            var hammerPresent = false;
            var excavatorPresent = false;
            var lumberAxePresent = false;
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
                            upgradedStack.setTag(Objects.requireNonNull(slotStack.getTag()).copy());
                        }
                    }
                    if (item instanceof HammerItem) {
                        if (hammerPresent) {
                            return ItemStack.EMPTY;
                        }
                        hammerPresent = true;
                        if (slotStack.hasTag()) {
                            upgradedStack.setTag(Objects.requireNonNull(slotStack.getTag()).copy());
                        }
                    }
                    if (item instanceof ExcavatorItem) {
                        if (excavatorPresent) {
                            return ItemStack.EMPTY;
                        }
                        excavatorPresent = true;
                        if (slotStack.hasTag()) {
                            upgradedStack.setTag(Objects.requireNonNull(slotStack.getTag()).copy());
                        }
                    }
                    if (item instanceof LumberAxeItem) {
                        if (lumberAxePresent) {
                            return ItemStack.EMPTY;
                        }
                        lumberAxePresent = true;
                        if (slotStack.hasTag()) {
                            upgradedStack.setTag(Objects.requireNonNull(slotStack.getTag()).copy());
                        }
                    }
                }
            }
            Jobs builder = Jobs.BUILDER;
            if (JobGetters.jobIsEnabled(player, builder) && backpackPresent) {
                if (JobGetters.getJobLevel(player, builder) >= 10 && upgradedStack.getItem() == ModItems.MEDIUM_BACKPACK.get()) {
                    return upgradedStack;
                }
                if (JobGetters.getJobLevel(player, builder) >= 20 && upgradedStack.getItem() == ModItems.LARGE_BACKPACK.get()) {
                    return upgradedStack;
                }
                if (JobGetters.getJobLevel(player, builder) >= 35 && upgradedStack.getItem() == ModItems.HUGE_BACKPACK.get()) {
                    return upgradedStack;
                }
                if (JobGetters.getJobLevel(player, builder) >= 10 && upgradedStack.getItem() == ModItems.ENDER_BACKPACK.get()) {
                    return upgradedStack;
                }
            }
            Jobs miner = Jobs.MINER;
            if (JobGetters.jobIsEnabled(player, miner) && hammerPresent) {
                if (JobGetters.getJobLevel(player, miner) >= 25) {
                    if (getResultItem().getItem() == ModItems.MINERS_HAMMER_LEVEL_2.get()) {
                        return upgradedStack;
                    }
                }
                if (JobGetters.getJobLevel(player, miner) >= 50) {
                    if (getResultItem().getItem() == ModItems.MINERS_HAMMER_LEVEL_3.get()) {
                        return upgradedStack;
                    }
                }
                if (JobGetters.getJobLevel(player, miner) >= 75) {
                    if (getResultItem().getItem() == ModItems.MINERS_HAMMER_LEVEL_4.get()) {
                        return upgradedStack;
                    }
                }
            }
            Jobs digger = Jobs.DIGGER;
            if (JobGetters.jobIsEnabled(player, digger) && excavatorPresent) {
                if (JobGetters.getJobLevel(player, digger) >= 25) {
                    if (getResultItem().getItem() == ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get()) {
                        return upgradedStack;
                    }
                }
                if (JobGetters.getJobLevel(player, digger) >= 50) {
                    if (getResultItem().getItem() == ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get()) {
                        return upgradedStack;
                    }
                }
                if (JobGetters.getJobLevel(player, digger) >= 75) {
                    if (getResultItem().getItem() == ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get()) {
                        return upgradedStack;
                    }
                }
            }
            Jobs lumberjack = Jobs.LUMBERJACK;
            if (JobGetters.jobIsEnabled(player, lumberjack) && lumberAxePresent) {
                if (JobGetters.getJobLevel(player, lumberjack) >= 25) {
                    if (getResultItem().getItem() == ModItems.LUMBERJACK_AXE_LEVEL_2.get()) {
                        return upgradedStack;
                    }
                }
                if (JobGetters.getJobLevel(player, lumberjack) >= 50) {
                    if (getResultItem().getItem() == ModItems.LUMBERJACK_AXE_LEVEL_3.get()) {
                        return upgradedStack;
                    }
                }
                if (JobGetters.getJobLevel(player, lumberjack) >= 75) {
                    if (getResultItem().getItem() == ModItems.LUMBERJACK_AXE_LEVEL_4.get()) {
                        return upgradedStack;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.UPGRADE_RECIPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<UpgradeRecipe> {
        @Nullable
        @Override
        public UpgradeRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new UpgradeRecipe(Objects.requireNonNull(RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer)));
        }

        @Override
        @Nonnull
        public UpgradeRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new UpgradeRecipe(RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error reading CopyBackpack Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull UpgradeRecipe recipe) {
            try {
                RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            } catch (Exception exception) {
                JobsPlus.LOGGER.info("Error writing CopyBackpack Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
