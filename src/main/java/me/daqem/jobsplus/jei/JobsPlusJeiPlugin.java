package me.daqem.jobsplus.jei;

import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModPotions;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class JobsPlusJeiPlugin implements IModPlugin {

    private static IJeiRuntime jeiRuntime;

    public static Optional<IJeiRuntime> getJeiRuntime() {
        return Optional.of(jeiRuntime);
    }

    public static void addPotionRecipes(@NotNull IRecipeRegistration registration, IVanillaRecipeFactory factory) {
        final ItemStack HASTE = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.HASTE.get());
        final ItemStack LONG_HASTE = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_HASTE.get());
        final ItemStack STRONG_HASTE = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.STRONG_HASTE.get());
        final ItemStack LONG_STRONG_HASTE = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_STRONG_HASTE.get());

        final ItemStack STRONG_REGENERATION = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_REGENERATION);
        final ItemStack LONG_STRONG_REGENERATION = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_STRONG_REGENERATION.get());
        final ItemStack EXTRA_LONG_STRONG_REGENERATION = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.EXTRA_LONG_STRONG_REGENERATION.get());
        final ItemStack EXTRA_STRONG_REGENERATION = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.EXTRA_STRONG_REGENERATION.get());

        final ItemStack STRONG_SWIFTNESS = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS);
        final ItemStack EXTRA_STRONG_SWIFTNESS = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.EXTRA_STRONG_SWIFTNESS.get());
        final ItemStack LONG_EXTRA_STRONG_SWIFTNESS = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_EXTRA_STRONG_SWIFTNESS.get());
        final ItemStack SUPER_STRONG_SWIFTNESS = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SUPER_STRONG_SWIFTNESS.get());
        final ItemStack LONG_SUPER_STRONG_SWIFTNESS = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_SUPER_STRONG_SWIFTNESS.get());

        final ItemStack STRONG_STRENGTH = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_STRENGTH);
        final ItemStack EXTRA_STRONG_STRENGTH = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.EXTRA_STRONG_STRENGTH.get());
        final ItemStack LONG_EXTRA_STRONG_STRENGTH = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_EXTRA_STRONG_STRENGTH.get());
        final ItemStack SUPER_STRONG_STRENGTH = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SUPER_STRONG_STRENGTH.get());
        final ItemStack LONG_SUPER_STRONG_STRENGTH = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_SUPER_STRONG_STRENGTH.get());

        final ItemStack LUCK = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LUCK.get());
        final ItemStack LONG_LUCK = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_LUCK.get());
        final ItemStack STRONG_LUCK = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.STRONG_LUCK.get());
        final ItemStack LONG_STRONG_LUCK = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_STRONG_LUCK.get());

        final ItemStack JESUS = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.JESUS.get());
        final ItemStack LONG_JESUS = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_JESUS.get());

        final ItemStack FLYING = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.FLYING.get());
        final ItemStack LONG_FLYING = PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_FLYING.get());

        final ItemStack AWKWARD = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);

        final List<ItemStack> REDSTONE = List.of(new ItemStack(Items.REDSTONE));
        final List<ItemStack> GLOWSTONE_DUST = List.of(new ItemStack(Items.GLOWSTONE_DUST));
        final List<ItemStack> STRONG_HASTE_STACK_POTIONS = List.of(HASTE, LONG_HASTE);
        final List<ItemStack> EXTRA_STRONG_REGENERATION_POTIONS = List.of(STRONG_REGENERATION, LONG_STRONG_REGENERATION, EXTRA_LONG_STRONG_REGENERATION);
        final List<ItemStack> SUPER_STRONG_SWIFTNESS_POTIONS = List.of(EXTRA_STRONG_SWIFTNESS, LONG_EXTRA_STRONG_SWIFTNESS);
        final List<ItemStack> SUPER_STRONG_STRENGTH_POTIONS = List.of(EXTRA_STRONG_STRENGTH, LONG_EXTRA_STRONG_STRENGTH);
        final List<ItemStack> STRONG_LUCK_POTIONS = List.of(LUCK, LONG_LUCK);
        final List<ItemStack> HONEY_BOTTLE = List.of(new ItemStack(Items.HONEY_BOTTLE));
        final List<ItemStack> GLOW_INK_SAC = List.of(new ItemStack(Items.GLOW_INK_SAC));
        final List<ItemStack> FEATHER = List.of(new ItemStack(Items.FEATHER));
        final List<ItemStack> NETHER_STAR = List.of(new ItemStack(Items.NETHER_STAR));

        IJeiBrewingRecipe haste = factory.createBrewingRecipe(HONEY_BOTTLE, AWKWARD, HASTE);
        IJeiBrewingRecipe longHaste = factory.createBrewingRecipe(REDSTONE, HASTE, LONG_HASTE);
        IJeiBrewingRecipe strongHaste = factory.createBrewingRecipe(GLOWSTONE_DUST, STRONG_HASTE_STACK_POTIONS, STRONG_HASTE);
        IJeiBrewingRecipe longStrongHaste = factory.createBrewingRecipe(REDSTONE, STRONG_HASTE, LONG_STRONG_HASTE);

        IJeiBrewingRecipe longStrongRegeneration = factory.createBrewingRecipe(REDSTONE, STRONG_REGENERATION, LONG_STRONG_REGENERATION);
        IJeiBrewingRecipe extraLongStrongRegeneration = factory.createBrewingRecipe(REDSTONE, LONG_STRONG_REGENERATION, EXTRA_LONG_STRONG_REGENERATION);
        IJeiBrewingRecipe extraStrongRegeneration = factory.createBrewingRecipe(GLOWSTONE_DUST, EXTRA_STRONG_REGENERATION_POTIONS, EXTRA_STRONG_REGENERATION);

        IJeiBrewingRecipe extraStrongSwiftness = factory.createBrewingRecipe(GLOWSTONE_DUST, STRONG_SWIFTNESS, EXTRA_STRONG_SWIFTNESS);
        IJeiBrewingRecipe longExtraStrongSwiftness = factory.createBrewingRecipe(REDSTONE, EXTRA_STRONG_SWIFTNESS, LONG_EXTRA_STRONG_SWIFTNESS);
        IJeiBrewingRecipe superStrongSwiftness = factory.createBrewingRecipe(GLOWSTONE_DUST, SUPER_STRONG_SWIFTNESS_POTIONS, SUPER_STRONG_SWIFTNESS);
        IJeiBrewingRecipe longSuperStrongSwiftness = factory.createBrewingRecipe(REDSTONE, SUPER_STRONG_SWIFTNESS, LONG_SUPER_STRONG_SWIFTNESS);

        IJeiBrewingRecipe extraStrongStrength = factory.createBrewingRecipe(GLOWSTONE_DUST, STRONG_STRENGTH, EXTRA_STRONG_STRENGTH);
        IJeiBrewingRecipe longExtraStrongStrength = factory.createBrewingRecipe(REDSTONE, EXTRA_STRONG_STRENGTH, LONG_EXTRA_STRONG_STRENGTH);
        IJeiBrewingRecipe superStrongStrength = factory.createBrewingRecipe(GLOWSTONE_DUST, SUPER_STRONG_STRENGTH_POTIONS, SUPER_STRONG_STRENGTH);
        IJeiBrewingRecipe longSuperStrongStrength = factory.createBrewingRecipe(REDSTONE, SUPER_STRONG_STRENGTH, LONG_SUPER_STRONG_STRENGTH);

        IJeiBrewingRecipe luck = factory.createBrewingRecipe(GLOW_INK_SAC, AWKWARD, LUCK);
        IJeiBrewingRecipe longLuck = factory.createBrewingRecipe(REDSTONE, LUCK, LONG_LUCK);
        IJeiBrewingRecipe strongLuck = factory.createBrewingRecipe(GLOWSTONE_DUST, STRONG_LUCK_POTIONS, STRONG_LUCK);
        IJeiBrewingRecipe longStrongLuck = factory.createBrewingRecipe(REDSTONE, STRONG_LUCK, LONG_STRONG_LUCK);

        IJeiBrewingRecipe jesus = factory.createBrewingRecipe(FEATHER, AWKWARD, JESUS);
        IJeiBrewingRecipe longJesus = factory.createBrewingRecipe(REDSTONE, JESUS, LONG_JESUS);

        IJeiBrewingRecipe flying = factory.createBrewingRecipe(NETHER_STAR, AWKWARD, FLYING);
        IJeiBrewingRecipe longFlying = factory.createBrewingRecipe(REDSTONE, FLYING, LONG_FLYING);

        List<IJeiBrewingRecipe> brewingRecipes = List.of(haste, longHaste, strongHaste, longStrongHaste, longStrongRegeneration,
                extraLongStrongRegeneration, extraStrongRegeneration, extraStrongSwiftness, longExtraStrongSwiftness, superStrongSwiftness,
                longSuperStrongSwiftness, extraStrongStrength, longExtraStrongStrength, superStrongStrength, longSuperStrongStrength,
                luck, longLuck, strongLuck, longStrongLuck, jesus, longJesus, flying, longFlying);

        for (IJeiBrewingRecipe brewingRecipe : brewingRecipes) {
            registration.addRecipes(RecipeTypes.BREWING, Collections.singletonList(brewingRecipe));
        }

    }

    public static void addAnvilRecipes(@NotNull IRecipeRegistration registration, IVanillaRecipeFactory factory) {
        final List<ItemStack> IRON_BLOCK = List.of(Items.IRON_BLOCK.getDefaultInstance());
        final List<ItemStack> GOLD_BLOCK = List.of(Items.GOLD_BLOCK.getDefaultInstance());
        final List<ItemStack> DIAMOND_BLOCK = List.of(Items.DIAMOND_BLOCK.getDefaultInstance());
        final List<ItemStack> EMERALD_BLOCK = List.of(Items.EMERALD_BLOCK.getDefaultInstance());
        final List<ItemStack> OBSIDIAN = List.of(Items.OBSIDIAN.getDefaultInstance());
        final List<ItemStack> NETHERITE_BLOCK = List.of(Items.NETHERITE_BLOCK.getDefaultInstance());

        IJeiAnvilRecipe hunters_sword_level_1 = factory.createAnvilRecipe(ModItems.HUNTERS_SWORD_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.HUNTERS_SWORD_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe hunters_sword_level_2 = factory.createAnvilRecipe(ModItems.HUNTERS_SWORD_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.HUNTERS_SWORD_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe hunters_sword_level_3 = factory.createAnvilRecipe(ModItems.HUNTERS_SWORD_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.HUNTERS_SWORD_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe hunters_sword_level_4 = factory.createAnvilRecipe(ModItems.HUNTERS_SWORD_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.HUNTERS_SWORD_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe hunters_bow_level_1 = factory.createAnvilRecipe(ModItems.HUNTERS_BOW_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.HUNTERS_BOW_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe hunters_bow_level_2 = factory.createAnvilRecipe(ModItems.HUNTERS_BOW_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.HUNTERS_BOW_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe hunters_bow_level_3 = factory.createAnvilRecipe(ModItems.HUNTERS_BOW_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.HUNTERS_BOW_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe hunters_bow_level_4 = factory.createAnvilRecipe(ModItems.HUNTERS_BOW_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.HUNTERS_BOW_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe lumberjack_axe_level_1 = factory.createAnvilRecipe(ModItems.LUMBERJACK_AXE_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.LUMBERJACK_AXE_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe lumberjack_axe_level_2 = factory.createAnvilRecipe(ModItems.LUMBERJACK_AXE_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.LUMBERJACK_AXE_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe lumberjack_axe_level_3 = factory.createAnvilRecipe(ModItems.LUMBERJACK_AXE_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.LUMBERJACK_AXE_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe lumberjack_axe_level_4 = factory.createAnvilRecipe(ModItems.LUMBERJACK_AXE_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.LUMBERJACK_AXE_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe miners_hammer_level_1 = factory.createAnvilRecipe(ModItems.MINERS_HAMMER_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.MINERS_HAMMER_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe miners_hammer_level_2 = factory.createAnvilRecipe(ModItems.MINERS_HAMMER_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.MINERS_HAMMER_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe miners_hammer_level_3 = factory.createAnvilRecipe(ModItems.MINERS_HAMMER_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.MINERS_HAMMER_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe miners_hammer_level_4 = factory.createAnvilRecipe(ModItems.MINERS_HAMMER_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.MINERS_HAMMER_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe fishermans_rod_level_1 = factory.createAnvilRecipe(ModItems.FISHERMANS_ROD_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.FISHERMANS_ROD_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe fishermans_rod_level_2 = factory.createAnvilRecipe(ModItems.FISHERMANS_ROD_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.FISHERMANS_ROD_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe fishermans_rod_level_3 = factory.createAnvilRecipe(ModItems.FISHERMANS_ROD_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.FISHERMANS_ROD_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe fishermans_rod_level_4 = factory.createAnvilRecipe(ModItems.FISHERMANS_ROD_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.FISHERMANS_ROD_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe farmers_hoe_level_1 = factory.createAnvilRecipe(ModItems.FARMERS_HOE_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.FARMERS_HOE_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe farmers_hoe_level_2 = factory.createAnvilRecipe(ModItems.FARMERS_HOE_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.FARMERS_HOE_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe farmers_hoe_level_3 = factory.createAnvilRecipe(ModItems.FARMERS_HOE_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.FARMERS_HOE_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe farmers_hoe_level_4 = factory.createAnvilRecipe(ModItems.FARMERS_HOE_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.FARMERS_HOE_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe diggers_excavator_level_1 = factory.createAnvilRecipe(ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get().getDefaultInstance()));
        IJeiAnvilRecipe diggers_excavator_level_2 = factory.createAnvilRecipe(ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get().getDefaultInstance(), GOLD_BLOCK, List.of(ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get().getDefaultInstance()));
        IJeiAnvilRecipe diggers_excavator_level_3 = factory.createAnvilRecipe(ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get().getDefaultInstance()));
        IJeiAnvilRecipe diggers_excavator_level_4 = factory.createAnvilRecipe(ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get().getDefaultInstance(), EMERALD_BLOCK, List.of(ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get().getDefaultInstance()));

        IJeiAnvilRecipe reinforced_iron_helmet = factory.createAnvilRecipe(ModItems.REINFORCED_IRON_HELMET.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.REINFORCED_IRON_HELMET.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_iron_chestplate = factory.createAnvilRecipe(ModItems.REINFORCED_IRON_CHESTPLATE.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.REINFORCED_IRON_CHESTPLATE.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_iron_leggings = factory.createAnvilRecipe(ModItems.REINFORCED_IRON_LEGGINGS.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.REINFORCED_IRON_LEGGINGS.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_iron_boots = factory.createAnvilRecipe(ModItems.REINFORCED_IRON_BOOTS.get().getDefaultInstance(), IRON_BLOCK, List.of(ModItems.REINFORCED_IRON_BOOTS.get().getDefaultInstance()));
        IJeiAnvilRecipe obsidian_helmet = factory.createAnvilRecipe(ModItems.OBSIDIAN_HELMET.get().getDefaultInstance(), OBSIDIAN, List.of(ModItems.OBSIDIAN_HELMET.get().getDefaultInstance()));
        IJeiAnvilRecipe obsidian_chestplate = factory.createAnvilRecipe(ModItems.OBSIDIAN_CHESTPLATE.get().getDefaultInstance(), OBSIDIAN, List.of(ModItems.OBSIDIAN_CHESTPLATE.get().getDefaultInstance()));
        IJeiAnvilRecipe obsidian_leggings = factory.createAnvilRecipe(ModItems.OBSIDIAN_LEGGINGS.get().getDefaultInstance(), OBSIDIAN, List.of(ModItems.OBSIDIAN_LEGGINGS.get().getDefaultInstance()));
        IJeiAnvilRecipe obsidian_boots = factory.createAnvilRecipe(ModItems.OBSIDIAN_BOOTS.get().getDefaultInstance(), OBSIDIAN, List.of(ModItems.OBSIDIAN_BOOTS.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_diamond_helmet = factory.createAnvilRecipe(ModItems.REINFORCED_DIAMOND_HELMET.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.REINFORCED_DIAMOND_HELMET.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_diamond_chestplate = factory.createAnvilRecipe(ModItems.REINFORCED_DIAMOND_CHESTPLATE.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.REINFORCED_DIAMOND_CHESTPLATE.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_diamond_leggings = factory.createAnvilRecipe(ModItems.REINFORCED_DIAMOND_LEGGINGS.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.REINFORCED_DIAMOND_LEGGINGS.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_diamond_boots = factory.createAnvilRecipe(ModItems.REINFORCED_DIAMOND_BOOTS.get().getDefaultInstance(), DIAMOND_BLOCK, List.of(ModItems.REINFORCED_DIAMOND_BOOTS.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_netherite_helmet = factory.createAnvilRecipe(ModItems.REINFORCED_NETHERITE_HELMET.get().getDefaultInstance(), NETHERITE_BLOCK, List.of(ModItems.REINFORCED_NETHERITE_HELMET.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_netherite_chestplate = factory.createAnvilRecipe(ModItems.REINFORCED_NETHERITE_CHESTPLATE.get().getDefaultInstance(), NETHERITE_BLOCK, List.of(ModItems.REINFORCED_NETHERITE_CHESTPLATE.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_netherite_leggings = factory.createAnvilRecipe(ModItems.REINFORCED_NETHERITE_LEGGINGS.get().getDefaultInstance(), NETHERITE_BLOCK, List.of(ModItems.REINFORCED_NETHERITE_LEGGINGS.get().getDefaultInstance()));
        IJeiAnvilRecipe reinforced_netherite_boots = factory.createAnvilRecipe(ModItems.REINFORCED_NETHERITE_BOOTS.get().getDefaultInstance(), NETHERITE_BLOCK, List.of(ModItems.REINFORCED_NETHERITE_BOOTS.get().getDefaultInstance()));

        List<IJeiAnvilRecipe> anvilRecipes = List.of(hunters_sword_level_1, hunters_sword_level_2, hunters_sword_level_3, hunters_sword_level_4,
                hunters_bow_level_1, hunters_bow_level_2, hunters_bow_level_3, hunters_bow_level_4, lumberjack_axe_level_1, lumberjack_axe_level_2,
                lumberjack_axe_level_3, lumberjack_axe_level_4, miners_hammer_level_1, miners_hammer_level_2, miners_hammer_level_3, miners_hammer_level_4,
                fishermans_rod_level_1, fishermans_rod_level_2, fishermans_rod_level_3, fishermans_rod_level_4, farmers_hoe_level_1, farmers_hoe_level_2,
                farmers_hoe_level_3, farmers_hoe_level_4, diggers_excavator_level_1, diggers_excavator_level_2, diggers_excavator_level_3, diggers_excavator_level_4,
                reinforced_iron_helmet, reinforced_iron_chestplate, reinforced_iron_leggings, reinforced_iron_boots, obsidian_helmet, obsidian_chestplate,
                obsidian_leggings, obsidian_boots, reinforced_diamond_helmet, reinforced_diamond_chestplate, reinforced_diamond_leggings, reinforced_diamond_boots,
                reinforced_netherite_helmet, reinforced_netherite_chestplate, reinforced_netherite_leggings, reinforced_netherite_boots);

        for (IJeiAnvilRecipe anvilRecipe : anvilRecipes) {
            registration.addRecipes(RecipeTypes.ANVIL, Collections.singletonList(anvilRecipe));
        }
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();

        addPotionRecipes(registration, factory);
        addAnvilRecipes(registration, factory);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(ModIds.JEI_ID, "jei");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JobsPlusJeiPlugin.jeiRuntime = jeiRuntime;
    }
}
