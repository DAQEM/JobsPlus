package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ShapedJobBasedRecipe;
import me.daqem.jobsplus.common.crafting.ShapelessJobBasedRecipe;
import me.daqem.jobsplus.common.crafting.UpgradeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JobsPlus.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> UPGRADE_RECIPE = RECIPES.register("upgrade_recipe", UpgradeRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPED_JOB_BASED_RECIPE = RECIPES.register("shaped_job_based_recipe", ShapedJobBasedRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> SHAPELESS_JOB_BASED_RECIPE = RECIPES.register("shapeless_job_based_recipe", ShapelessJobBasedRecipe.Serializer::new);
}
