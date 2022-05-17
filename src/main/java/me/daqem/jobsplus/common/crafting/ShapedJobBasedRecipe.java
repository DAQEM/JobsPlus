package me.daqem.jobsplus.common.crafting;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.Config;
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
import net.minecraft.world.item.Item;
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
            final Item item = getResultItem().getItem();
            if (JobGetters.getJobLevel(player, Jobs.ENCHANTER) >= Config.REQUIRED_LEVEL_EXP_JAR.get()) {
                if (item == ModItems.EXP_JAR.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.ENCHANTER) >= Config.REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_1.get()) {
                if (item == ModItems.EXPERIENCE_BOTTLE.get()) {
                    getResultItem().getOrCreateTag().putInt("tier", 1);
                    return super.assemble(container);
                }
            }
            if (JobGetters.getJobLevel(player, Jobs.ENCHANTER) >= Config.REQUIRED_LEVEL_CURSE_BREAKER.get()) {
                if (item == ModItems.CURSE_BREAKER.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_IRON_HELMET.get()) {
                if (item == ModItems.REINFORCED_IRON_HELMET.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_IRON_BOOTS.get()) {
                if (item == ModItems.REINFORCED_IRON_BOOTS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_OBSIDIAN_HELMET.get()) {
                if (item == ModItems.OBSIDIAN_HELMET.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_OBSIDIAN_BOOTS.get()) {
                if (item == ModItems.OBSIDIAN_BOOTS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_IRON_LEGGINGS.get()) {
                if (item == ModItems.REINFORCED_IRON_LEGGINGS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_OBSIDIAN_LEGGINGS.get()) {
                if (item == ModItems.OBSIDIAN_LEGGINGS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_IRON_CHESTPLATE.get()) {
                if (item == ModItems.REINFORCED_IRON_CHESTPLATE.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_HELMET.get()) {
                if (item == ModItems.REINFORCED_DIAMOND_HELMET.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_BOOTS.get()) {
                if (item == ModItems.REINFORCED_DIAMOND_BOOTS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_OBSIDIAN_CHESTPLATE.get()) {
                if (item == ModItems.OBSIDIAN_CHESTPLATE.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_LEGGINGS.get()) {
                if (item == ModItems.REINFORCED_DIAMOND_LEGGINGS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_CHESTPLATE.get()) {
                if (item == ModItems.REINFORCED_DIAMOND_CHESTPLATE.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_HELMET.get()) {
                if (item == ModItems.REINFORCED_NETHERITE_HELMET.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_BOOTS.get()) {
                if (item == ModItems.REINFORCED_NETHERITE_BOOTS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_LEGGINGS.get()) {
                if (item == ModItems.REINFORCED_NETHERITE_LEGGINGS.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.SMITH) >= Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_CHESTPLATE.get()) {
                if (item == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.MINER) >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_1.get()) {
                if (item == ModItems.MINERS_HAMMER_LEVEL_1.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.DIGGER) >= Config.REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_1.get()) {
                if (item == ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.BUILDER) >= Config.REQUIRED_LEVEL_SMALL_BACKPACK.get()) {
                if (item == ModItems.SMALL_BACKPACK.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.BUILDER) >= Config.REQUIRED_LEVEL_ENDER_BACKPACK.get()) {
                if (item == ModItems.ENDER_BACKPACK.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.LUMBERJACK) >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_1.get()) {
                if (item == ModItems.LUMBERJACK_AXE_LEVEL_1.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.FARMER) >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_1.get()) {
                if (item == ModItems.FARMERS_HOE_LEVEL_1.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.HUNTER) >= Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_1.get()) {
                if (item == ModItems.HUNTERS_SWORD_LEVEL_1.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.HUNTER) >= Config.REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_1.get()) {
                if (item == ModItems.HUNTERS_BOW_LEVEL_1.get()) return super.assemble(container);
            }
            if (JobGetters.getJobLevel(player, Jobs.FISHERMAN) >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_1.get()) {
                if (item == ModItems.FISHERMANS_ROD_LEVEL_1.get()) return super.assemble(container);
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
                JobsPlus.LOGGER.error("Error reading ShapedJobBasedRecipe Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ShapedJobBasedRecipe recipe) {
            try {
                RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            } catch (Exception exception) {
                JobsPlus.LOGGER.error("Error writing ShapedJobBasedRecipe Recipe to packet: ", exception);
                throw exception;
            }
        }
    }

}
