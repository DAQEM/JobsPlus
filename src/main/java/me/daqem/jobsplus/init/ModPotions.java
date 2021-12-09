package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.potion.brewing.ModBrewingRecipe;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, JobsPlus.MOD_ID);

    public static final RegistryObject<Potion> HASTE = POTIONS.register("haste", () -> new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 2400)));
    public static final RegistryObject<Potion> LONG_HASTE = POTIONS.register("long_haste", () -> new Potion("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 6000)));
    public static final RegistryObject<Potion> STRONG_HASTE = POTIONS.register("strong_haste", () -> new Potion("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 2400, 1)));
    public static final RegistryObject<Potion> LONG_STRONG_HASTE = POTIONS.register("long_strong_haste", () -> new Potion("haste", new MobEffectInstance(MobEffects.DIG_SPEED, 6000, 1)));

    public static final RegistryObject<Potion> LONG_STRONG_REGENERATION = POTIONS.register("long_strong_regeneration", () -> new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1200, 1)));
    public static final RegistryObject<Potion> EXTRA_LONG_STRONG_REGENERATION = POTIONS.register("extra_long_strong_regeneration", () -> new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 2400, 1)));
    public static final RegistryObject<Potion> EXTRA_STRONG_REGENERATION = POTIONS.register("extra_strong_regeneration", () -> new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 1200, 2)));

    public static final RegistryObject<Potion> EXTRA_STRONG_SWIFTNESS = POTIONS.register("extra_strong_swiftness", () -> new Potion("swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 2)));
    public static final RegistryObject<Potion> LONG_EXTRA_STRONG_SWIFTNESS = POTIONS.register("long_extra_strong_swiftness", () -> new Potion("swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 2)));
    public static final RegistryObject<Potion> SUPER_STRONG_SWIFTNESS = POTIONS.register("super_strong_swiftness", () -> new Potion("swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 3)));
    public static final RegistryObject<Potion> LONG_SUPER_STRONG_SWIFTNESS = POTIONS.register("long_super_strong_swiftness", () -> new Potion("swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 3)));
    
    public static final RegistryObject<Potion> EXTRA_STRONG_STRENGTH = POTIONS.register("extra_strong_strength", () -> new Potion("strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 2)));
    public static final RegistryObject<Potion> LONG_EXTRA_STRONG_STRENGTH = POTIONS.register("long_extra_strong_strength", () -> new Potion("strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 2)));
    public static final RegistryObject<Potion> SUPER_STRONG_STRENGTH = POTIONS.register("super_strong_strength", () -> new Potion("strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 3)));
    public static final RegistryObject<Potion> LONG_SUPER_STRONG_STRENGTH = POTIONS.register("long_super_strong_strength", () -> new Potion("strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 3)));

    public static final RegistryObject<Potion> LUCK = POTIONS.register("luck", () -> new Potion(new MobEffectInstance(MobEffects.LUCK, 2400)));
    public static final RegistryObject<Potion> LONG_LUCK = POTIONS.register("long_luck", () -> new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000)));
    public static final RegistryObject<Potion> STRONG_LUCK = POTIONS.register("strong_luck", () -> new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 2400, 1)));
    public static final RegistryObject<Potion> LONG_STRONG_LUCK = POTIONS.register("long_strong_luck", () -> new Potion("luck", new MobEffectInstance(MobEffects.LUCK, 6000, 1)));

    public static final RegistryObject<Potion> JESUS = POTIONS.register("jesus", () -> new Potion(new MobEffectInstance(ModEffects.JESUS.get(), 2400)));
    public static final RegistryObject<Potion> LONG_JESUS = POTIONS.register("long_jesus", () -> new Potion("jesus", new MobEffectInstance(ModEffects.JESUS.get(), 6000)));

    public static final RegistryObject<Potion> FLYING = POTIONS.register("flying", () -> new Potion(new MobEffectInstance(ModEffects.FLYING.get(), 6000)));
    public static final RegistryObject<Potion> LONG_FLYING = POTIONS.register("long_flying", () -> new Potion("flying", new MobEffectInstance(ModEffects.FLYING.get(), 12000)));

    public static void addPotionRecipes() {
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.HONEY_BOTTLE, ModPotions.HASTE.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.HASTE.get(), Items.REDSTONE, ModPotions.LONG_HASTE.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.HASTE.get(), Items.GLOWSTONE, ModPotions.STRONG_HASTE.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LONG_HASTE.get(), Items.GLOWSTONE, ModPotions.STRONG_HASTE.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.STRONG_HASTE.get(), Items.REDSTONE, ModPotions.LONG_STRONG_HASTE.get()));

        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.STRONG_REGENERATION, Items.REDSTONE, ModPotions.LONG_STRONG_REGENERATION.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LONG_STRONG_REGENERATION.get(), Items.REDSTONE, ModPotions.EXTRA_LONG_STRONG_REGENERATION.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.STRONG_REGENERATION, Items.GLOWSTONE, ModPotions.EXTRA_STRONG_REGENERATION.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LONG_STRONG_REGENERATION.get(), Items.GLOWSTONE, ModPotions.EXTRA_STRONG_REGENERATION.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.EXTRA_LONG_STRONG_REGENERATION.get(), Items.GLOWSTONE, ModPotions.EXTRA_STRONG_REGENERATION.get()));

        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.STRONG_SWIFTNESS, Items.GLOWSTONE, ModPotions.EXTRA_STRONG_SWIFTNESS.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.EXTRA_STRONG_SWIFTNESS.get(), Items.REDSTONE, ModPotions.LONG_EXTRA_STRONG_SWIFTNESS.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.EXTRA_STRONG_SWIFTNESS.get(), Items.GLOWSTONE, ModPotions.SUPER_STRONG_SWIFTNESS.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LONG_EXTRA_STRONG_SWIFTNESS.get(), Items.GLOWSTONE, ModPotions.SUPER_STRONG_SWIFTNESS.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.SUPER_STRONG_SWIFTNESS.get(), Items.REDSTONE, ModPotions.LONG_SUPER_STRONG_SWIFTNESS.get()));

        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.STRONG_STRENGTH, Items.GLOWSTONE, ModPotions.EXTRA_STRONG_STRENGTH.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.EXTRA_STRONG_STRENGTH.get(), Items.REDSTONE, ModPotions.LONG_EXTRA_STRONG_STRENGTH.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.EXTRA_STRONG_STRENGTH.get(), Items.GLOWSTONE, ModPotions.SUPER_STRONG_STRENGTH.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LONG_EXTRA_STRONG_STRENGTH.get(), Items.GLOWSTONE, ModPotions.SUPER_STRONG_STRENGTH.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.SUPER_STRONG_STRENGTH.get(), Items.REDSTONE, ModPotions.LONG_SUPER_STRONG_STRENGTH.get()));

        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.GLOW_INK_SAC, ModPotions.LUCK.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LUCK.get(), Items.REDSTONE, ModPotions.LONG_LUCK.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LUCK.get(), Items.GLOWSTONE, ModPotions.STRONG_LUCK.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.LONG_LUCK.get(), Items.GLOWSTONE, ModPotions.STRONG_LUCK.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.STRONG_LUCK.get(), Items.REDSTONE, ModPotions.LONG_STRONG_LUCK.get()));

        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.FEATHER, ModPotions.JESUS.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.JESUS.get(), Items.REDSTONE, ModPotions.LONG_JESUS.get()));

        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.AWKWARD, Items.NETHER_STAR, ModPotions.FLYING.get()));
        BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(ModPotions.FLYING.get(), Items.REDSTONE, ModPotions.LONG_FLYING.get()));
    }
}
