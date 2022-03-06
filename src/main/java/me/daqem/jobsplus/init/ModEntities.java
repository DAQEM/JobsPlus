package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.common.entity.ModThrownExperienceBottle;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, JobsPlus.MOD_ID);

    public static final RegistryObject<EntityType<ModFishingHook>> FISHING_BOBBER = ENTITY_TYPES.register("fishing_bobber",
            () -> EntityType.Builder.<ModFishingHook>of(ModFishingHook::new, MobCategory.MISC)
                    .noSave().noSummon().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5).build("fishing_bobber"));
    public static final RegistryObject<EntityType<ModThrownExperienceBottle>> EXPERIENCE_BOTTLE = ENTITY_TYPES.register("experience_bottle",
            () -> EntityType.Builder.<ModThrownExperienceBottle>of(ModThrownExperienceBottle::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("experience_bottle"));
}