package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.capability.ModCapabilityImpl;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventRegisterCapabilities {

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent evt) {
        evt.register(ModCapabilityImpl.class);
    }
}
