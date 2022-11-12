package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class MobEffectHandler {

    public static void addPlayerPowerUpEffects(Player player, Jobs job) {
        if (JobGetters.hasSuperPowerEnabled(player, job, true)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5 * 20, 1));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5 * 20));
        } else if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP3.get(), true)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5 * 20));
        }
    }
}
