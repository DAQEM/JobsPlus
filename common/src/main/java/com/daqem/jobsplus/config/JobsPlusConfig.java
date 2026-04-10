package com.daqem.jobsplus.config;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.JobLevelCalculator;
import com.daqem.knot.api.platform.Platform;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.event.ConfigEvent;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

import java.util.List;

public class JobsPlusConfig {

    public static final IConfigEntry<Integer> amountOfFreeJobs;
    public static final IConfigEntry<Integer> maxJobs;
    public static final IConfigEntry<List<String>> excludedJobs;
    public static final IConfigEntry<String> experienceFormula;
    public static final IConfigEntry<Integer> maxLevel;
    public static final IConfigEntry<Double> jobPriceMultiplier;

    public static final IConfigEntry<List<String>> excludedPowerups;
    public static final IConfigEntry<Double> powerupPriceMultiplier;

    public static final IConfigEntry<Integer> coinsPerLevelUp;

    public static final IConfigEntry<Double> experienceMultiplier;
    public static final IConfigEntry<Boolean> loseExpOnDeath;
    public static final IConfigEntry<Double> expLossPercentage;

    public static final IConfigEntry<String> coinFormat;
    public static final IConfigEntry<String> expFormat;

    public static final IConfigEntry<Boolean> sendJobLevelUpMessages;


    static {
        IConfigBuilder builder = new ConfigBuilder(
                JobsPlus.MOD_ID,
                "jobsplus-common",
                ConfigExtension.YAML,
                ConfigType.COMMON,
                Platform.INFO.getConfigFolder().resolve("jobsplus")
        );

        builder.push("jobs");
        amountOfFreeJobs = builder.defineInteger("amount_of_free_jobs", 2, 0, 1000).withComments("the amount of free jobs a player can have");
        maxJobs = builder.defineInteger("max_jobs", 1000, 0, 1000).withComments("the maximum amount of jobs a player can have");
        excludedJobs = builder.defineStringList("excluded_jobs", List.of()).withComments("A list of job IDs to exclude from the game. Example: ['jobsplus:miner', 'jobsplus:digger']");
        experienceFormula = builder.defineString("experience_formula", "100 + level * level * 0.5791")
                .withComments("The formula used to calculate experience required for the next level.",
                        "Available variables: 'level'",
                        "Supported operators: +, -, *, /, ^, (, )"
                );
        maxLevel = builder.defineInteger("max_level", 10_000, 1, 100_000).withComments("the maximum level a player can reach in a job");
        jobPriceMultiplier = builder.defineDouble("job_price_multiplier", 1.0, 0.0, 100.0).withComments("A global multiplier for all job prices.");

        builder.push("powerups");
        powerupPriceMultiplier = builder.defineDouble("powerup_price_multiplier", 1.0, 0.0, 100.0).withComments("A global multiplier for all powerup prices.");
        excludedPowerups = builder.defineStringList("excluded_powerups", List.of()).withComments("A list of powerup IDs to exclude from the game. Example: ['jobsplus:miner/double_drops_iii', 'jobsplus:digger/shovel_efficiency_v']");
        builder.pop();

        builder.push("coins");
        coinsPerLevelUp = builder.defineInteger("coins_per_level_up", 1, 0, 1000).withComments("the amount of coins a player gets when they level up a job");
        builder.pop();

        builder.push("experience");
        experienceMultiplier = builder.defineDouble("experience_multiplier", 1.0, 0.0, 100.0)
                .withComments("A global multiplier for all job experience gained.");
        loseExpOnDeath = builder.defineBoolean("lose_exp_on_death", false)
                .withComments("If true, players will lose a percentage of their job experience upon death.");
        expLossPercentage = builder.defineDouble("exp_loss_percentage", 0.25, 0.0, 1.0)
                .withComments("The percentage of experience lost on death (0.25 = 25%).");
        builder.pop();

        builder.pop();

        builder.push("display");
        coinFormat = builder.defineString("coin_format", "#,###.#", 1, 32).withComments("The format used to display coins.", "Uses DecimalFormat patterns.", "Examples: '#,##0.00', '0.0a', '##0‰'").dontSync();
        expFormat = builder.defineString("exp_format", "#,###.#", 1, 32).withComments("The format used to display experience.", "Uses DecimalFormat patterns.", "Examples: '#,##0.00', '0.0a', '##0‰'").dontSync();
        builder.pop();

        builder.push("messages");
        sendJobLevelUpMessages = builder.defineBoolean("send_job_level_up_messages", true).withComments("If true, players will receive a message in chat when they level up a job.");
        builder.pop();

        builder.build();

        ConfigEvent.register((config, level) -> {
            if (config.getModId().equals(JobsPlus.MOD_ID) && config.getType().equals(ConfigType.COMMON)) {
                JobLevelCalculator.resetCache();
            }
        });
    }

    public static void init() {
    }
}
