package com.daqem.jobsplus.event.command;

import com.daqem.jobsplus.command.JobCommand;
import com.daqem.jobsplus.command.JobsPlusCommand;
import dev.architectury.event.events.common.CommandRegistrationEvent;

public class EventRegisterCommands {

    public static void registerEvent() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            JobCommand.registerCommand(dispatcher);
            JobsPlusCommand.registerCommand(dispatcher);
        });
    }
}
