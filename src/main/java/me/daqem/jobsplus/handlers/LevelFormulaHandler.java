package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;

public class LevelFormulaHandler {

    public static int calcExp(int level) {
        if (level == 0) JobsPlus.LOGGER.error("Cannot divide by zero.");
        double exponentiation1 = Math.pow(level, 3);
        double exponentiation2 = Math.pow(level, 2);
        return (int) ((0.04*(exponentiation1)+0.8*(exponentiation2)+2*level*25/level)*2);
    }

}
