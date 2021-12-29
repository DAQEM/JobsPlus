package me.daqem.jobsplus.events;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventClone {

    public static void onDeath(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(oldStore -> {
            event.getEntity().getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(newStore -> {
                newStore.copyForRespawn((ModCapabilityImpl) oldStore);
            });
        });
        event.getOriginal().invalidateCaps();
    }
}
