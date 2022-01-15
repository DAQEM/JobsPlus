package me.daqem.jobsplus.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerLoggedIn {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().refreshDisplayName();
    }
}
