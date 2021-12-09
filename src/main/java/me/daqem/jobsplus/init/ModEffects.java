package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.mobeffect.FlyingModEffect;
import me.daqem.jobsplus.common.mobeffect.JesusModEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, JobsPlus.MOD_ID);

    public static final RegistryObject<MobEffect> JESUS = EFFECTS.register("jesus", JesusModEffect::new);
    public static final RegistryObject<MobEffect> FLYING = EFFECTS.register("flying", FlyingModEffect::new);

}
