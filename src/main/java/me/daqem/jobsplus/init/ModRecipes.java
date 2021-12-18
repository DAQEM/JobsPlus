package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.BackpackUpgradeRecipe;
import me.daqem.jobsplus.common.crafting.EXPJarRecipe;
import me.daqem.jobsplus.common.crafting.WoolRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JobsPlus.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> BACKPACK_UPGRADE_RECIPE = RECIPES.register("backpack_upgrade", BackpackUpgradeRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> EXP_JAR_RECIPE = RECIPES.register("exp_jar", EXPJarRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> WOOL_RECIPE = RECIPES.register("wool", WoolRecipe.Serializer::new);
}
