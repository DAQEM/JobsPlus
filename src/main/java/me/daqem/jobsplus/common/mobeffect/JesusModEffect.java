package me.daqem.jobsplus.common.mobeffect;

import me.daqem.jobsplus.init.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class JesusModEffect extends MobEffect {

    public JesusModEffect() {
        super(MobEffectCategory.BENEFICIAL, 5566207);
    }

    public static void performEffect(LivingEntity entity) {
        if (entity.hasEffect(ModEffects.JESUS.get())) {
            if (Objects.equals(Objects.requireNonNull(entity.level.getBlockState(new BlockPos(entity.getX(), entity.getBlockY(), entity.getZ())).getBlock().getRegistryName()).toString(), "minecraft:water") &&
                entity.level.getBlockState(new BlockPos(entity.getX(), entity.getBlockY() + 1, entity.getZ())).isAir()) {
                Vec3 vec3 = entity.getDeltaMovement();
                entity.setDeltaMovement(vec3.x, 0.05D, vec3.z);
                if (entity.isFallFlying()) {
                    entity.setDeltaMovement(vec3.x, vec3.y, vec3.z);
                }
                entity.flyingSpeed = 0.050F;
            }
        }
    }
}
