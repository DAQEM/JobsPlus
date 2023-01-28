package me.daqem.jobsplus;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_CHAT = "chat";
    public static final String CATEGORY_LEVEL_UP = "level_up";
    public static final String CATEGORY_MULTIPLIER = "multiplier";
    public static final String CATEGORY_ALCHEMIST = "alchemist";
    public static final String CATEGORY_MINER = "miner";
    public static final ForgeConfigSpec COMMON_CONFIG;
    //CATEGORY_GENERAL
    public static final ForgeConfigSpec.IntValue JOB_START_COST;
    public static final ForgeConfigSpec.IntValue JOB_STOP_COST;
    public static final ForgeConfigSpec.IntValue POWERUP_COST;
    public static final ForgeConfigSpec.IntValue AMOUNT_OF_FREE_JOBS;
    public static final ForgeConfigSpec.IntValue JOB_LEVEL_TO_STOP_JOB_FOR_FREE;
    public static final ForgeConfigSpec.BooleanValue SET_FIRST_JOB_AS_DISPLAY;

    //CATEGORY_CHAT
    public static final ForgeConfigSpec.BooleanValue FORMAT_CHAT;
    public static final ForgeConfigSpec.BooleanValue DISPLAY_JOB_IN_CHAT_AND_TAB;

    //CATEGORY_LEVEL_UP
    public static final ForgeConfigSpec.IntValue COINS_PER_LEVEL_UP;

    //CATEGORY_MULTIPLIER
    public static final ForgeConfigSpec.DoubleValue GLOBAL_EXP_MULTIPLIER;

    public static final ForgeConfigSpec.DoubleValue ALCHEMIST_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue BUILDER_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue DIGGER_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue ENCHANTER_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FARMER_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FISHERMAN_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue HUNTER_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue LUMBERJACK_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue MINER_EXP_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue SMITH_EXP_MULTIPLIER;

    //CATEGORY_ALCHEMIST
    public static final ForgeConfigSpec.BooleanValue ALLOW_ALL_EFFECTS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_POTION_OF_FLIGHT;

    //CATEGORY_MINER
    public static final ForgeConfigSpec.IntValue MAX_VEIN_MINER_BLOCKS;

    static {

        serverBuilder.push(CATEGORY_GENERAL);
        JOB_START_COST = serverBuilder.defineInRange("job_start_cost", 10, 1, 1000);
        JOB_STOP_COST = serverBuilder.defineInRange("job_stop_cost", 5, 1, 1000);
        POWERUP_COST = serverBuilder.defineInRange("powerup_cost", 10, 0, 1000);
        AMOUNT_OF_FREE_JOBS = serverBuilder.defineInRange("amount_of_free_jobs", 2, 1, 10);
        JOB_LEVEL_TO_STOP_JOB_FOR_FREE = serverBuilder.defineInRange("job_level_to_stop_job_for_free", 1, 0, 100);
        SET_FIRST_JOB_AS_DISPLAY = serverBuilder.define("set_first_job_as_display", true);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CHAT);
        FORMAT_CHAT = serverBuilder.comment("Make chat look fancy.").define("format_chat", true);
        DISPLAY_JOB_IN_CHAT_AND_TAB = serverBuilder.define("display_job_in_chat_and_tab", true);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_LEVEL_UP);
        COINS_PER_LEVEL_UP = serverBuilder.comment("Amount of coins given to a player each time they level up a job.").defineInRange("coins_per_level_up", 1, 0, Integer.MAX_VALUE);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_MULTIPLIER);
        serverBuilder.comment("""
                Job-EXP multipliers work like this: (default job-EXP * job-EXP multiplier) * global job-EXP multiplier.
                Example, player gets 4 job-EXP from fishing with a 1.45 fisherman multiplier and a 3.22 global multiplier:
                Outcome: (4 * 1.45) * 3.22 = 18.676 = 19 job-EXP gained (rounded).""");
        GLOBAL_EXP_MULTIPLIER = serverBuilder.defineInRange("global_exp_multiplier", 1, 0.01, 1000);
        ALCHEMIST_EXP_MULTIPLIER = serverBuilder.defineInRange("alchemist_exp_multiplier", 1, 0.01, 1000);
        BUILDER_EXP_MULTIPLIER = serverBuilder.defineInRange("builder_exp_multiplier", 1, 0.01, 1000);
        DIGGER_EXP_MULTIPLIER = serverBuilder.defineInRange("digger_exp_multiplier", 1, 0.01, 1000);
        ENCHANTER_EXP_MULTIPLIER = serverBuilder.defineInRange("enchanter_exp_multiplier", 1, 0.01, 1000);
        FARMER_EXP_MULTIPLIER = serverBuilder.defineInRange("farmer_exp_multiplier", 1, 0.01, 1000);
        FISHERMAN_EXP_MULTIPLIER = serverBuilder.defineInRange("fisherman_exp_multiplier", 1, 0.01, 1000);
        HUNTER_EXP_MULTIPLIER = serverBuilder.defineInRange("hunter_exp_multiplier", 1, 0.01, 1000);
        LUMBERJACK_EXP_MULTIPLIER = serverBuilder.defineInRange("lumberjack_exp_multiplier", 1, 0.01, 1000);
        MINER_EXP_MULTIPLIER = serverBuilder.defineInRange("miner_exp_multiplier", 1, 0.01, 1000);
        SMITH_EXP_MULTIPLIER = serverBuilder.defineInRange("smith_exp_multiplier", 1, 0.01, 1000);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_ALCHEMIST);
        ALLOW_ALL_EFFECTS = serverBuilder.comment("Don't remove effect when alchemist level isn't high enough if true.").define("allow_all_effects", false);
        ENABLE_POTION_OF_FLIGHT = serverBuilder.define("enable_potion_of_flight", true);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_MINER);
        MAX_VEIN_MINER_BLOCKS = serverBuilder.defineInRange("max_vein_miner_blocks", 511, 0, 4095);
        serverBuilder.pop();

        COMMON_CONFIG = serverBuilder.build();
    }

}
