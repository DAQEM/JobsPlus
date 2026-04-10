package com.daqem.jobsplus.fabric.client;

import com.daqem.jobsplus.client.JobsPlusClient;
import com.daqem.knot.Knot;
import net.fabricmc.api.ClientModInitializer;

public class JobsPlusFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        JobsPlusClient.init();
        registerKeyBindings();
    }

    private static void registerKeyBindings() {
        Knot.KEY_MAPPING_REGISTRY.register(JobsPlusClient.OPEN_MENU);
    }
}
