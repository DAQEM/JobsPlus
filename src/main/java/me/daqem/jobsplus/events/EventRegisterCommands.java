package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.command.JobCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventRegisterCommands {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        JobCommand.register(event.getDispatcher());
    }
}
