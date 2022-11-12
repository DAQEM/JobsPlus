package me.daqem.jobsplus.handlers;

public class LevelHandler {

    public static int calcExp(int level) {
        if (level == 0) return 0;
        return (int) (100 + level * level * 0.5791);
    }
}