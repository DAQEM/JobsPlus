package com.daqem.jobsplus.neoforge.client;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.JobsPlusClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = JobsPlus.MOD_ID, dist = Dist.CLIENT)
public class JobsPlusNeoForgeClient {

    public JobsPlusNeoForgeClient(IEventBus modEventBus) {
        JobsPlusClient.init();
        registerEvents(modEventBus);
    }

    private void registerEvents(IEventBus modEventBus) {
        modEventBus.addListener(this::registerKeyBindings);
    }

    private void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(JobsPlusClient.OPEN_MENU);
    }
}
