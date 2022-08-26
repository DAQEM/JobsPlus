package me.daqem.jobsplus.handlers;

public class LevelHandler {

    public static int calcExp(int level) {
        return (int) (100 + level * level * 0.5791);
    }
}