package me.daqem.jobsplus.utils.enums;

import net.minecraft.network.chat.Component;

public enum Jobs {
    ALCHEMIST(0),
    BUILDER(1),
    DIGGER(2),
    ENCHANTER(3),
    FARMER(4),
    FISHERMAN(5),
    HUNTER(6),
    LUMBERJACK(7),
    MINER(8),
    SMITH(9);

    private final int value;

    Jobs(final int newValue) {
        value = newValue;
    }

    public static String getString(int value) {
        for (Jobs jobs : Jobs.values()) {
            if (value == jobs.get()) {
                return Component.translatable("job." + jobs.name().toLowerCase()).getString();
            }
        }
        return "";
    }

    public static String getEnglishString(int value) {
        for (Jobs jobs : Jobs.values()) {
            if (value == jobs.get()) {
                return jobs.name();
            }
        }
        return "";
    }

    public static int getJobInt(Jobs job) {
        int value = job.get();
        for (Jobs jobs : Jobs.values()) {
            if (value == jobs.get()) {
                return value;
            }
        }
        return value;
    }

    public static Jobs getJobFromInt(int job) {
        for (Jobs jobs : Jobs.values()) {
            if (job == jobs.get()) {
                return jobs;
            }
        }
        return null;
    }

    public int get() {
        return value;
    }
}
