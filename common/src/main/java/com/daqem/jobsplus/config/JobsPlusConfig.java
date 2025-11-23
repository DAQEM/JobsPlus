package com.daqem.jobsplus.config;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.JobLevelCalculator;
import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.event.ConfigEvent;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

import java.util.List;

public class JobsPlusConfig {

    public static final IConfigEntry<Boolean> enableDefaultJobs;
    public static final IConfigEntry<Integer> amountOfFreeJobs;
    public static final IConfigEntry<Integer> maxJobs;
    public static final IConfigEntry<List<String>> excludedJobs;
    public static final IConfigEntry<String> experienceFormula;

    public static final IConfigEntry<List<String>> excludedPowerups;

    public static final IConfigEntry<Integer> coinsPerLevelUp;

    public static final IConfigEntry<String> coinFormat;
    public static final IConfigEntry<String> expFormat;


    static {
        IConfigBuilder builder = new ConfigBuilder(JobsPlus.MOD_ID, "jobsplus-common", ConfigExtension.YAML, ConfigType.COMMON, YamlConfigExpectPlatform.getConfigDirectory().resolve("jobsplus"));

        builder.push("jobs");
        enableDefaultJobs = builder.defineBoolean("enable_default_jobs", true).withComments("if true, the default jobs are enabled. WARNING: setting this to false will erase all the stats for these jobs");
        amountOfFreeJobs = builder.defineInteger("amount_of_free_jobs", 2, 0, 1000).withComments("the amount of free jobs a player can have");
        maxJobs = builder.defineInteger("max_jobs", 1000, 0, 1000).withComments("the maximum amount of jobs a player can have");
        excludedJobs = builder.defineStringList("excluded_jobs", List.of()).withComments("A list of job IDs to exclude from the game. Example: ['jobsplus:miner', 'jobsplus:digger']");
        experienceFormula = builder.defineString("experience_formula", "100 + level * level * 0.5791")
                .withComments("The formula used to calculate experience required for the next level.",
                        "Available variables: 'level'",
                        "Supported operators: +, -, *, /, ^, (, )"
                );
        builder.push("powerups");
        excludedPowerups = builder.defineStringList("excluded_powerups", List.of()).withComments("A list of powerup IDs to exclude from the game. Example: ['jobsplus:miner/double_drops_iii', 'jobsplus:digger/shovel_efficiency_v']");
        builder.pop();
        builder.push("coins");
        coinsPerLevelUp = builder.defineInteger("coins_per_level_up", 1, 0, 1000).withComments("the amount of coins a player gets when they level up a job");
        builder.pop();
        builder.pop();

        builder.push("display");
        coinFormat = builder.defineString("coin_format", "#,###.#", 1, 32).withComments("The format used to display coins.", "Uses DecimalFormat patterns.", "Examples: '#,##0.00', '0.0a', '##0‰'").dontSync();
        expFormat = builder.defineString("exp_format", "#,###.#", 1, 32).withComments("The format used to display experience.", "Uses DecimalFormat patterns.", "Examples: '#,##0.00', '0.0a', '##0‰'").dontSync();
        builder.pop();

        builder.build();

        ConfigEvent.ON_UPDATE.register((config, level) -> {
            if (config.getModId().equals(JobsPlus.MOD_ID) && config.getType().equals(ConfigType.COMMON)) {
                JobLevelCalculator.resetCache();
            }
        });
    }

    public static void init() {
        JobLevelCalculator.resetCache();
    }
}
