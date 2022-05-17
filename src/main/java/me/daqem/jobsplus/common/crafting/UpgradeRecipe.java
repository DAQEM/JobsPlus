package me.daqem.jobsplus.common.crafting;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.item.*;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModRecipes;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.nbt.CompoundTag;
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

public class UpgradeRecipe extends ShapedRecipe {

    public UpgradeRecipe(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getId(), shapedRecipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), shapedRecipe.getIngredients(), shapedRecipe.getResultItem());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container) {
        if (container.menu instanceof CraftingMenu craftingMenu) {
            Player player = craftingMenu.player;
            final ItemStack upgradedStack = super.assemble(container);
            final int tier = upgradedStack.getOrCreateTag().getInt("tier");
            boolean backpackPresent = false, hammerPresent = false, excavatorPresent = false,
                    lumberAxePresent = false, hoePresent = false, swordPresent = false,
                    bowPresent = false, rodPresent = false, expBottlePresent = false, expJarPresent = false;
            for (int index = 0; index < container.getContainerSize(); ++index) {
                final var slotStack = container.getItem(index);
                if (!slotStack.isEmpty()) {
                    final var item = slotStack.getItem();
                    final CompoundTag copy = Objects.requireNonNull(slotStack.getTag()).copy();
                    if (item instanceof BackpackItem) {
                        if (backpackPresent) return ItemStack.EMPTY;
                        backpackPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof HammerItem) {
                        if (hammerPresent) return ItemStack.EMPTY;
                        hammerPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof ExcavatorItem) {
                        if (excavatorPresent) return ItemStack.EMPTY;
                        excavatorPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof LumberAxeItem) {
                        if (lumberAxePresent) return ItemStack.EMPTY;
                        lumberAxePresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof FarmersHoeItem) {
                        if (hoePresent) return ItemStack.EMPTY;
                        hoePresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof HunterSwordItem) {
                        if (swordPresent) return ItemStack.EMPTY;
                        swordPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof HunterBowItem) {
                        if (bowPresent) return ItemStack.EMPTY;
                        bowPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof RodItem) {
                        if (rodPresent) return ItemStack.EMPTY;
                        rodPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof EXPJarItem) {
                        if (expJarPresent) return ItemStack.EMPTY;
                        expJarPresent = true;
                        if (slotStack.hasTag()) upgradedStack.setTag(copy);
                    }
                    if (item instanceof ModExperienceBottleItem) {
                        if (expBottlePresent) return ItemStack.EMPTY;
                        expBottlePresent = true;
                        if (slotStack.hasTag()) {
                            upgradedStack.setTag(copy);
                            if (tier < 6) upgradedStack.getOrCreateTag().putInt("tier", tier + 1);
                        }
                    }
                }
            }
            final Item item = getResultItem().getItem();

            if (backpackPresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.BUILDER);
                if (level >= Config.REQUIRED_LEVEL_MEDIUM_BACKPACK.get()) {
                    if (upgradedStack.getItem() == ModItems.MEDIUM_BACKPACK.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_LARGE_BACKPACK.get()) {
                    if (upgradedStack.getItem() == ModItems.LARGE_BACKPACK.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_HUGE_BACKPACK.get()) {
                    if (upgradedStack.getItem() == ModItems.HUGE_BACKPACK.get()) return upgradedStack;
                }
            }
            if (hammerPresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.MINER);
                if (level >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_2.get()) {
                    if (item == ModItems.MINERS_HAMMER_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_3.get()) {
                    if (item == ModItems.MINERS_HAMMER_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_4.get()) {
                    if (item == ModItems.MINERS_HAMMER_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (excavatorPresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.DIGGER);
                if (level >= Config.REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_2.get()) {
                    if (item == ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_3.get()) {
                    if (item == ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_4.get()) {
                    if (item == ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (lumberAxePresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.LUMBERJACK);
                if (level >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_2.get()) {
                    if (item == ModItems.LUMBERJACK_AXE_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_3.get()) {
                    if (item == ModItems.LUMBERJACK_AXE_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_4.get()) {
                    if (item == ModItems.LUMBERJACK_AXE_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (hoePresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.FARMER);
                if (level >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_2.get()) {
                    if (item == ModItems.FARMERS_HOE_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_3.get()) {
                    if (item == ModItems.FARMERS_HOE_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_4.get()) {
                    if (item == ModItems.FARMERS_HOE_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (swordPresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.HUNTER);
                if (level >= Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_2.get()) {
                    if (item == ModItems.HUNTERS_SWORD_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_3.get()) {
                    if (item == ModItems.HUNTERS_SWORD_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_4.get()) {
                    if (item == ModItems.HUNTERS_SWORD_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (bowPresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.HUNTER);
                if (level >= Config.REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_2.get()) {
                    if (item == ModItems.HUNTERS_BOW_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_3.get()) {
                    if (item == ModItems.HUNTERS_BOW_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_4.get()) {
                    if (item == ModItems.HUNTERS_BOW_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (rodPresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.FISHERMAN);
                if (level >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_2.get()) {
                    if (item == ModItems.FISHERMANS_ROD_LEVEL_2.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_3.get()) {
                    if (item == ModItems.FISHERMANS_ROD_LEVEL_3.get()) return upgradedStack;
                }
                if (level >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_4.get()) {
                    if (item == ModItems.FISHERMANS_ROD_LEVEL_4.get()) return upgradedStack;
                }
            }
            if (expBottlePresent) {
                final int level = JobGetters.getJobLevel(player, Jobs.ENCHANTER);
                if (item == ModItems.EXPERIENCE_BOTTLE.get()) {
                    if (tier == 2 && level >= Config.REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_2.get())
                        return upgradedStack;
                    if (tier == 3 && level >= Config.REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_3.get())
                        return upgradedStack;
                    if (tier == 4 && level >= Config.REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_4.get())
                        return upgradedStack;
                    if (tier == 5 && level >= Config.REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_5.get())
                        return upgradedStack;
                    if (tier == 6 && level >= Config.REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_6.get())
                        return upgradedStack;
                }
            }
            if (expJarPresent) {
                if (JobGetters.getJobLevel(player, Jobs.ENCHANTER) >= Config.REQUIRED_LEVEL_EXP_JAR_TO_EXPERIENCE_BOTTLE.get()) {
                    if (upgradedStack.getOrCreateTag().getInt("EXP") != 0) {
                        if (item == ModItems.EXPERIENCE_BOTTLE.get()) return upgradedStack;
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
                JobsPlus.LOGGER.error("Error reading UpgradeRecipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull UpgradeRecipe recipe) {
            try {
                RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
            } catch (Exception exception) {
                JobsPlus.LOGGER.error("Error writing UpgradeRecipe Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
