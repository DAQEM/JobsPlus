package me.daqem.jobsplus.utils.enums;

import me.daqem.jobsplus.JobsPlus;

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
                return JobsPlus.translatable("job." + jobs.name().toLowerCase()).getString();
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
        int jobId = job.get();
        for (Jobs forJob : Jobs.values()) {
            if (jobId == forJob.get()) {
                return jobId;
            }
        }
        return jobId;
    }

    public static Jobs getJobFromInt(int jobId) {
        for (Jobs job : Jobs.values()) {
            if (jobId == job.get()) {
                return job;
            }
        }
        return null;
    }

    public static Jobs getFromString(String jobString) {
        try {
            return Jobs.valueOf(jobString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public int get() {
        return value;
    }

    public boolean is(Jobs job) {
        return this.equals(job);
    }
}
