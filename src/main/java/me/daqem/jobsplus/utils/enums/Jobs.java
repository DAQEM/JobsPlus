package me.daqem.jobsplus.utils.enums;

public enum Jobs {
    ALCHEMIST(0),
    BUILDER(1),
    DIGGER(4),
    FARMER(5),
    FISHERMAN(6),
    ENCHANTER(7),
    HUNTER(8),
    LUMBERJACK(9),
    MINER(10),
    SMITH(11);

    private final int value;

    Jobs(final int newValue) {
        value = newValue;
    }

    public static String getString(int value) {
        if (value > 1) value = value + 2;
        for (Jobs jobs : Jobs.values()) {
            if (value == jobs.get()) {
                return jobs.name();
            }
        }
        return "";
    }

    public static int getJobInt(Jobs job) {
        int value = job.get();
        if (value > 1) value = value - 2;
        for (Jobs jobs : Jobs.values()) {
            if (value == jobs.get()) {
                return value;
            }
        }
        return value;
    }

    public static Jobs getJobFromInt(int job) {
        if (job == 0) return ALCHEMIST;
        if (job == 1) return BUILDER;
        if (job == 2) return DIGGER;
        if (job == 3) return FARMER;
        if (job == 4) return FISHERMAN;
        if (job == 5) return ENCHANTER;
        if (job == 6) return HUNTER;
        if (job == 7) return LUMBERJACK;
        if (job == 8) return MINER;
        if (job == 9) return SMITH;
        return null;
    }

    public int get() {
        return value;
    }
}
