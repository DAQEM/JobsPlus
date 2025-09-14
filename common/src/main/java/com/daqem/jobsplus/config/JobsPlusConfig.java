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

    public static final IConfigEntry<Double> coinsPerLevelUp;

    public static final IConfigEntry<Boolean> broadcastLevelUpMessage;

    public static final IConfigEntry<Boolean> showXPInActionBar;

    public static final IConfigEntry<Double> xpMultiplier;

    public static final IConfigEntry<Boolean> isDebug;

    static {
        IConfigBuilder config = new ConfigBuilder(JobsPlus.MOD_ID, "jobsplus-common", ConfigExtension.YAML, ConfigType.COMMON);

        config.push("jobs");
        enableDefaultJobs = config.defineBoolean("enable_default_jobs", true).withComments("if true, the default jobs are enabled. WARNING: setting this to false will erase all the stats for these jobs");
        amountOfFreeJobs = config.defineInteger("amount_of_free_jobs", 2, 0, Integer.MAX_VALUE).withComments("the amount of free jobs a player can have");
        maxJobs = config.defineInteger("max_jobs", Integer.MAX_VALUE, 0, Integer.MAX_VALUE).withComments("the maximum amount of jobs a player can have");
        broadcastLevelUpMessage = config.defineBoolean("show_level_up_message", true).withComments("if true, a message will be shown to all players when they level up a job");
        showXPInActionBar = config.defineBoolean("show_xp_in_action_bar", true).withComments("if true, the player's job XP gain will be shown in the action bar, when they gain XP");
        xpMultiplier = config.defineDouble("xp_multiplier", 1, 0, Double.MAX_VALUE).withComments("multiplier for the amount of XP a player gains");
        config.push("coins");
        coinsPerLevelUp = config.defineDouble("coins_per_level_up", 1, 0, Double.MAX_VALUE).withComments("the amount of coins a player gets when they level up a job");
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
