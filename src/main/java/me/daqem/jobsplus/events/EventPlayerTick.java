package me.daqem.jobsplus.events;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerTick {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
//            event.player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent((jobHandler) -> {
//                int currentValue = jobHandler.getAlchemist()[CapType.LEVEL.get()];
//                JobsPlus.LOGGER.info(currentValue + " | | | " + jobHandler + " " + event.player.getScoreboardName() + " server");
//                jobHandler.setAlchemist(new int[]{0, currentValue + 1, 0});
//            });
//        }
//        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START) {
//            event.player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent((jobHandler) -> {
//                int currentValue = jobHandler.getAlchemist()[CapType.LEVEL.get()];
//                JobsPlus.LOGGER.info(currentValue + " | | | " + jobHandler + " " + event.player.getScoreboardName() + " client");
//                jobHandler.setAlchemist(new int[]{0, currentValue + 1, 0});
//            });
//        }
    }
}
