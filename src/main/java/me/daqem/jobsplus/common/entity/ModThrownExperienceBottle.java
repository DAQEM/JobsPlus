package me.daqem.jobsplus.common.entity;

import me.daqem.jobsplus.init.ModEntities;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ModThrownExperienceBottle extends ThrowableItemProjectile {

    private int exp;

    public ModThrownExperienceBottle(EntityType<? extends ModThrownExperienceBottle> entityType, Level level) {
        super(entityType, level);
    }

    public ModThrownExperienceBottle(Level level, LivingEntity entity, int exp) {
        super(ModEntities.EXPERIENCE_BOTTLE.get(), entity, level);
        this.exp = exp;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.EXPERIENCE_BOTTLE.get();
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level instanceof ServerLevel) {
            this.level.levelEvent(2002, this.blockPosition(), PotionUtils.getColor(Potions.WATER));
            ExperienceOrb.award((ServerLevel) this.level, this.position(), this.exp);
            this.discard();
        }

    }
}