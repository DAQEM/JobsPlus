package me.daqem.jobsplus.events;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JobsPlus.MOD_ID)
public class EventAddReloadListener {

    @SubscribeEvent
    public static void dataLoading(AddReloadListenerEvent event) {
        event.addListener(ModRecipeManager.instance);
    }
}
