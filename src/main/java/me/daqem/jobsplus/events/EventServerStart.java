package me.daqem.jobsplus.events;

import me.daqem.jobsplus.utils.JobItemEntry;
import me.daqem.jobsplus.utils.JobItemEntryHelper;
import me.daqem.jobsplus.utils.enums.RequiredLevels;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventServerStart {

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        for (JobItemEntry entry : JobItemEntryHelper.getItemEntriesAsArrayList()) {
            if (entry.getStack().getDescriptionId().contains(".jobsplus.")) {
                for (RequiredLevels value : RequiredLevels.values()) {
                    if (value.getStack().equals(entry.getStack(), true)) {
                        entry.setRequiredLevel(value.get());
                    }
                }
            }
        }
    }
}
