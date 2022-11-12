package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = JobsPlus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JobsPlus.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> CONSTRUCTION_SERIALIZER = RECIPE_SERIALIZERS.register("construction", ConstructionRecipe.Serializer::new);

    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, ConstructionRecipeType.ID, ConstructionRecipeType.INSTANCE);
    }
}
