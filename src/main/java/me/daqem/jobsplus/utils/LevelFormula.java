package me.daqem.jobsplus.utils;

public class LevelFormula {

    public static int calcExp(int level) {
        double exponentiation1 = Math.pow(level, 3);
        double exponentiation2 = Math.pow(level, 2);
        return (int) ((0.04*(exponentiation1)+0.8*(exponentiation2)+2*level*25/level)*2);
    }

}
