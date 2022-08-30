package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ConstructionRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JobsPlus.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> CONSTRUCTION_SERIALIZER = RECIPE_SERIALIZERS.register("construction", ConstructionRecipe.Serializer::new);

    public static final DeferredRegister<RecipeType<?>> RECIPES_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, JobsPlus.MOD_ID);

    public static final RegistryObject<RecipeType<ConstructionRecipe>> CONSTRUCTION_TYPE = RECIPES_TYPES.register("construction", () -> ConstructionRecipe.Type.INSTANCE);
}
