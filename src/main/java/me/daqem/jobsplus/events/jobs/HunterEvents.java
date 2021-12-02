package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class HunterEvents {

    @SubscribeEvent
    public void onHostileKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            Jobs job = Jobs.HUNTER;
            if (!player.isCreative()) {
                if (JobGetters.jobIsEnabled(player, job)) {
                    if (event.getEntity() instanceof Monster) {
                        if (event.getSource().isProjectile()) {
                            ExpHandler.addEXPHigh(player, job);
                        } else {
                            ExpHandler.addEXPMid(player, job);
                        }
                    }
                }
            }
        }
    }
}
