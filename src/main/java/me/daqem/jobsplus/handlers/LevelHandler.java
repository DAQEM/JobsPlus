package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;

public class LevelHandler {

    public static int calcExp(int level) {
        if (level == 0) JobsPlus.LOGGER.error("Cannot divide by zero.");
        return (int) ((0.0001 * (level * level * level) + 0.8 * (level * level) + 2 * level * 25 / level) * 2);
    }

}
