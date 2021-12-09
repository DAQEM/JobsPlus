package me.daqem.jobsplus.common.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.HashMap;
import java.util.UUID;

public class FlyingModEffect extends MobEffect {

    public static HashMap<UUID, Boolean> array = new HashMap<>();

    public FlyingModEffect() {
        super(MobEffectCategory.BENEFICIAL, 4183275);
    }
}
