package com.daqem.jobsplus.config;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

import java.util.List;

public class JobsPlusConfig {

    public static final IConfigEntry<Boolean> enableDefaultJobs;
    public static final IConfigEntry<Integer> amountOfFreeJobs;
    public static final IConfigEntry<Integer> maxJobs;
    public static final IConfigEntry<List<String>> excludedJobs;
    public static final IConfigEntry<String> experienceFormula;
    public static final IConfigEntry<Double> jobPriceMultiplier;
    public static final IConfigEntry<Boolean> broadcastLevelUpMessages;

    public static final IConfigEntry<Double> powerupPriceMultiplier;
    public static final IConfigEntry<List<String>> excludedPowerups;

    public static final IConfigEntry<Double> xpMultiplier;
    public static final IConfigEntry<Boolean> useDecimalValuesForXP;
    public static final IConfigEntry<Boolean> loseExpOnDeath;
    public static final IConfigEntry<Double> expLossPercentage;

    public static final IConfigEntry<Double> coinsPerLevelUp;

    public static final IConfigEntry<String> coinFormat;
    public static final IConfigEntry<String> expFormat;

    public static final IConfigEntry<Boolean> isDebug;

    static {
        IConfigBuilder config = new ConfigBuilder(JobsPlus.MOD_ID, "jobsplus-common", ConfigExtension.YAML, ConfigType.COMMON);

        config.push("jobs");
        enableDefaultJobs = config.defineBoolean("enable_default_jobs", true).withComments("if true, the default jobs are enabled. WARNING: setting this to false will erase all the stats for these jobs");
        amountOfFreeJobs = config.defineInteger("amount_of_free_jobs", 2, 0, Integer.MAX_VALUE).withComments("the amount of free jobs a player can have");
        maxJobs = config.defineInteger("max_jobs", Integer.MAX_VALUE, 0, Integer.MAX_VALUE).withComments("the maximum amount of jobs a player can have");
        excludedJobs = config.defineStringList("excluded_jobs", List.of()).withComments("A list of job IDs to exclude from the game. Example: ['jobsplus:miner', 'jobsplus:digger']");
        experienceFormula = config.defineString("experience_formula", "100 + level * level * 0.5791").withComments("The formula used to calculate experience required for the next level.");
        jobPriceMultiplier = config.defineDouble("job_price_multiplier", 1.0, 0.0, 100.0).withComments("A global multiplier for all job prices.");
        broadcastLevelUpMessages = config.defineBoolean("broadcast_level_up_messages", true).withComments("if true, a message will be shown to all players when they level up a job");

        config.push("powerups");
        powerupPriceMultiplier = config.defineDouble("powerup_price_multiplier", 1.0, 0.0, 100.0).withComments("A global multiplier for all powerup prices.");
        excludedPowerups = config.defineStringList("excluded_powerups", List.of()).withComments("A list of powerup IDs to exclude from the game. Example: ['jobsplus:miner/double_drops_iii', 'jobsplus:digger/shovel_efficiency_v']");
        config.pop();

        config.push("experience");
        xpMultiplier = config.defineDouble("xp_multiplier", 1, 0, Double.MAX_VALUE).withComments("multiplier for the amount of job XP a player gains");
        useDecimalValuesForXP = config.defineBoolean("use_decimal_values_for_xp", false).withComments("if true, decimal values will be used for job XP.");
        loseExpOnDeath = config.defineBoolean("lose_exp_on_death", false).withComments("If true, players will lose a percentage of their job experience upon death.");
        expLossPercentage = config.defineDouble("exp_loss_percentage", 0.25, 0.0, 1.0).withComments("The percentage of experience lost on death (0.25 = 25%).");
        config.pop();

        config.push("coins");
        coinsPerLevelUp = config.defineDouble("coins_per_level_up", 1, 0, Double.MAX_VALUE).withComments("the amount of coins a player gets when they level up a job");
        config.pop();

        config.pop();

        config.push("display");
        coinFormat = config.defineString("coin_format", "#,###.#", 1, 32).withComments("The format used to display coins.", "Uses DecimalFormat patterns.", "Examples: '#,##0.00', '0.0a', '##0‰'").dontSync();
        expFormat = config.defineString("exp_format", "#,###.#", 1, 32).withComments("The format used to display experience.", "Uses DecimalFormat patterns.", "Examples: '#,##0.00', '0.0a', '##0‰'").dontSync();
        config.pop();

        config.push("debug");
        isDebug = config.defineBoolean("is_debug", false).withComments("if true, debug mode is enabled");
        config.pop();

        config.build();
    }

    public static void init() {
    }
}