package com.daqem.jobsplus.forge;

import com.daqem.jobsplus.JobsPlus;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(JobsPlus.MOD_ID)
public class JobsPlusForge {

    public JobsPlusForge(IEventBus modEventBus) {
        JobsPlus.init();

        EnvExecutor.getEnvSpecific(
                () -> () -> new SideProxyForge.Client(modEventBus),
                () -> () -> new SideProxyForge.Server(modEventBus)
        );
    }
}
