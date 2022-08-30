package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.capability.ModCapabilityImpl;
import me.daqem.jobsplus.common.capability.ModCapabilityProvider;
import me.daqem.jobsplus.common.capability.SuperPowerCapabilityImpl;
import me.daqem.jobsplus.common.capability.SuperPowerCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventAttachCapabilities {

    @SubscribeEvent
    public void onAttachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (event.getObject().level.isClientSide()) return;
            event.addCapability(ModCapabilityImpl.ID, ModCapabilityProvider.from(ModCapabilityImpl.MOD_CAPABILITY));
            event.addCapability(SuperPowerCapabilityImpl.ID, SuperPowerCapabilityProvider.from(SuperPowerCapabilityImpl.SUPERPOWER_CAPABILITY));
        }
    }
}
