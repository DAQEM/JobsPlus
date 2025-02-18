package com.daqem.jobsplus.config;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

public class JobsPlusConfig {

    public static final IConfigEntry<Boolean> enableDefaultJobs;
    public static final IConfigEntry<Integer> amountOfFreeJobs;
    public static final IConfigEntry<Integer> maxJobs;

    public static final IConfigEntry<Integer> coinsPerLevelUp;

    public static final IConfigEntry<Boolean> isDebug;

    static {
        IConfigBuilder config = new ConfigBuilder(JobsPlus.MOD_ID, "jobsplus-common", ConfigExtension.YAML, ConfigType.COMMON);

        config.push("jobs");
        enableDefaultJobs = config.defineBoolean("enable_default_jobs", true).withComments("if true, the default jobs are enabled. WARNING: setting this to false will erase all the stats for these jobs");
        amountOfFreeJobs = config.defineInteger("amount_of_free_jobs", 2, 0, Integer.MAX_VALUE).withComments("the amount of free jobs a player can have");
        maxJobs = config.defineInteger("max_jobs", Integer.MAX_VALUE, 0, Integer.MAX_VALUE).withComments("the maximum amount of jobs a player can have");
        config.push("coins");
        coinsPerLevelUp = config.defineInteger("coins_per_level_up", 1, 0, Integer.MAX_VALUE).withComments("the amount of coins a player gets when they level up a job");
        config.pop();
        config.pop();

        config.push("debug");
        isDebug = config.defineBoolean("is_debug", false).withComments("if true, debug mode is enabled");
        config.pop();

        config.build();
    }

    public static void init() {
    }
}
