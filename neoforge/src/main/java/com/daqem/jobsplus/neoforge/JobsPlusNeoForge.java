package com.daqem.jobsplus.neoforge;

import com.daqem.jobsplus.JobsPlus;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(JobsPlus.MOD_ID)
public class JobsPlusNeoForge {

    public JobsPlusNeoForge(IEventBus modEventBus) {
        JobsPlus.init();

        EnvExecutor.getEnvSpecific(
                () -> () -> new SideProxyNeoForge.Client(modEventBus),
                () -> () -> new SideProxyNeoForge.Server(modEventBus)
        );
    }
}
