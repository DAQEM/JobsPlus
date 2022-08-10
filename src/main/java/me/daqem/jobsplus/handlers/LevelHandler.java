package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;

public class LevelHandler {

    public static int calcExp(int level) {
        if (level == 0) JobsPlus.LOGGER.error("Cannot divide by zero.");
        return (int) (100 + level * level * 0.5791);
    }
}
