package com.daqem.jobsplus.event.command;

import com.daqem.jobsplus.command.JobCommand;
import com.daqem.jobsplus.command.JobsPlusCommand;
import com.daqem.knot.Knot;

public class EventRegisterCommands {

    public static void registerEvent() {
        Knot.Events.Server.COMMAND_REGISTER.register((dispatcher, context, selection) -> {
            JobCommand.registerCommand(dispatcher);
            JobsPlusCommand.registerCommand(dispatcher);
        });
    }
}
