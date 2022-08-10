package me.daqem.jobsplus;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_CHAT = "chat";
    public static final String CATEGORY_LEVEL_UP = "level_up";
    public static final String CATEGORY_MULTIPLIER = "multiplier";
    public static final String CATEGORY_CRAFTING = "crafting";
    public static final String CATEGORY_ALCHEMIST = "alchemist";
    public static final String CATEGORY_CRAFTING_TOOLS = "tools";
    public static final String CATEGORY_CRAFTING_TOOLS_MINER = "miner";
    public static final String CATEGORY_CRAFTING_TOOLS_DIGGER = "digger";
    public static final String CATEGORY_CRAFTING_TOOLS_LUMBERJACK = "lumberjack";
    public static final String CATEGORY_CRAFTING_TOOLS_FARMER = "farmer";
    public static final String CATEGORY_CRAFTING_TOOLS_HUNTER = "hunter";
    public static final String CATEGORY_CRAFTING_TOOLS_FISHERMAN = "fisherman";
    public static final String CATEGORY_CRAFTING_ARMOR = "armor";
    public static final String CATEGORY_CRAFTING_OTHER = "other";
    public static final String CATEGORY_CRAFTING_OTHER_ENCHANTER = "enchanter";
    public static final String CATEGORY_CRAFTING_OTHER_BUILDER = "builder";
    public static ForgeConfigSpec SERVER_CONFIG;
    //CATEGORY_GENERAL
    public static ForgeConfigSpec.IntValue JOB_START_COST;
    public static ForgeConfigSpec.IntValue JOB_STOP_COST;
    public static ForgeConfigSpec.IntValue POWERUP_COST;
    public static ForgeConfigSpec.IntValue AMOUNT_OF_FREE_JOBS;
    public static ForgeConfigSpec.IntValue JOB_LEVEL_TO_STOP_JOB_FOR_FREE;
    public static ForgeConfigSpec.BooleanValue SET_FIRST_JOB_AS_DISPLAY;

    //CATEGORY_CHAT
    public static ForgeConfigSpec.BooleanValue FORMAT_CHAT;
    public static ForgeConfigSpec.BooleanValue DISPLAY_JOB_IN_CHAT_AND_TAB;

    //CATEGORY_LEVEL_UP
    public static ForgeConfigSpec.IntValue COINS_PER_LEVEL_UP;

    //CATEGORY_MULTIPLIER
    public static ForgeConfigSpec.DoubleValue GLOBAL_EXP_MULTIPLIER;

    public static ForgeConfigSpec.DoubleValue ALCHEMIST_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue BUILDER_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue DIGGER_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue ENCHANTER_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue FARMER_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue FISHERMAN_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue HUNTER_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue LUMBERJACK_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue MINER_EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue SMITH_EXP_MULTIPLIER;

    //CATEGORY_CRAFTING
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXP_JAR;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_5;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_6;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_EXP_JAR_TO_EXPERIENCE_BOTTLE;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_CURSE_BREAKER;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_IRON_HELMET;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_IRON_BOOTS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_OBSIDIAN_HELMET;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_OBSIDIAN_BOOTS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_IRON_LEGGINGS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_OBSIDIAN_LEGGINGS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_IRON_CHESTPLATE;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_DIAMOND_HELMET;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_DIAMOND_BOOTS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_OBSIDIAN_CHESTPLATE;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_DIAMOND_LEGGINGS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_NETHERITE_HELMET;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_NETHERITE_BOOTS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_DIAMOND_CHESTPLATE;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_NETHERITE_LEGGINGS;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_REINFORCED_NETHERITE_CHESTPLATE;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTER_WOOL;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_SMALL_BACKPACK;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_MEDIUM_BACKPACK;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_LARGE_BACKPACK;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUGE_BACKPACK;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_ENDER_BACKPACK;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FARMERS_HOE_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FARMERS_HOE_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FARMERS_HOE_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FARMERS_HOE_LEVEL_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_4;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_1;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_2;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_3;
    public static ForgeConfigSpec.IntValue REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_4;

    //CATEGORY_ALCHEMIST

    public static ForgeConfigSpec.BooleanValue ALLOW_ALL_EFFECTS;

    static {
        ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
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
        serverBuilder.push(CATEGORY_CRAFTING);
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS);
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS_MINER);
        REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_1 = serverBuilder.defineInRange("required_level_miners_hammer_level_1", 5, 0, 100);
        REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_2 = serverBuilder.defineInRange("required_level_miners_hammer_level_2", 25, 0, 100);
        REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_3 = serverBuilder.defineInRange("required_level_miners_hammer_level_3", 50, 0, 100);
        REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_4 = serverBuilder.defineInRange("required_level_miners_hammer_level_4", 75, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS_DIGGER);
        REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_1 = serverBuilder.defineInRange("required_level_diggers_excavator_level_1", 5, 0, 100);
        REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_2 = serverBuilder.defineInRange("required_level_diggers_excavator_level_2", 25, 0, 100);
        REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_3 = serverBuilder.defineInRange("required_level_diggers_excavator_level_3", 50, 0, 100);
        REQUIRED_LEVEL_DIGGERS_EXCAVATOR_LEVEL_4 = serverBuilder.defineInRange("required_level_diggers_excavator_level_4", 75, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS_LUMBERJACK);
        REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_1 = serverBuilder.defineInRange("required_level_lumberjack_axe_level_1", 5, 0, 100);
        REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_2 = serverBuilder.defineInRange("required_level_lumberjack_axe_level_2", 25, 0, 100);
        REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_3 = serverBuilder.defineInRange("required_level_lumberjack_axe_level_3", 50, 0, 100);
        REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_4 = serverBuilder.defineInRange("required_level_lumberjack_axe_level_4", 75, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS_FARMER);
        REQUIRED_LEVEL_FARMERS_HOE_LEVEL_1 = serverBuilder.defineInRange("required_level_farmers_hoe_level_1", 5, 0, 100);
        REQUIRED_LEVEL_FARMERS_HOE_LEVEL_2 = serverBuilder.defineInRange("required_level_farmers_hoe_level_2", 25, 0, 100);
        REQUIRED_LEVEL_FARMERS_HOE_LEVEL_3 = serverBuilder.defineInRange("required_level_farmers_hoe_level_3", 50, 0, 100);
        REQUIRED_LEVEL_FARMERS_HOE_LEVEL_4 = serverBuilder.defineInRange("required_level_farmers_hoe_level_4", 75, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS_HUNTER);
        REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_1 = serverBuilder.defineInRange("required_level_hunters_sword_level_1", 5, 0, 100);
        REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_2 = serverBuilder.defineInRange("required_level_hunters_sword_level_2", 25, 0, 100);
        REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_3 = serverBuilder.defineInRange("required_level_hunters_sword_level_3", 50, 0, 100);
        REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_4 = serverBuilder.defineInRange("required_level_hunters_sword_level_4", 75, 0, 100);
        REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_1 = serverBuilder.defineInRange("required_level_hunters_bow_level_1", 5, 0, 100);
        REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_2 = serverBuilder.defineInRange("required_level_hunters_bow_level_2", 25, 0, 100);
        REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_3 = serverBuilder.defineInRange("required_level_hunters_bow_level_3", 50, 0, 100);
        REQUIRED_LEVEL_HUNTERS_BOW_LEVEL_4 = serverBuilder.defineInRange("required_level_hunters_bow_level_4", 75, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_TOOLS_FISHERMAN);
        REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_1 = serverBuilder.defineInRange("required_level_fishermans_rod_level_1", 5, 0, 100);
        REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_2 = serverBuilder.defineInRange("required_level_fishermans_rod_level_2", 25, 0, 100);
        REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_3 = serverBuilder.defineInRange("required_level_fishermans_rod_level_3", 50, 0, 100);
        REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_4 = serverBuilder.defineInRange("required_level_fishermans_rod_level_4", 75, 0, 100);
        serverBuilder.pop();
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_ARMOR);
        REQUIRED_LEVEL_REINFORCED_IRON_HELMET = serverBuilder.defineInRange("required_level_reinforced_iron_helmet", 1, 0, 100);
        REQUIRED_LEVEL_REINFORCED_IRON_CHESTPLATE = serverBuilder.defineInRange("required_level_reinforced_iron_chestplate", 10, 0, 100);
        REQUIRED_LEVEL_REINFORCED_IRON_LEGGINGS = serverBuilder.defineInRange("required_level_reinforced_iron_leggings", 5, 0, 100);
        REQUIRED_LEVEL_REINFORCED_IRON_BOOTS = serverBuilder.defineInRange("required_level_reinforced_iron_boots", 1, 0, 100);
        REQUIRED_LEVEL_OBSIDIAN_HELMET = serverBuilder.defineInRange("required_level_obsidian_helmet", 5, 0, 100);
        REQUIRED_LEVEL_OBSIDIAN_CHESTPLATE = serverBuilder.defineInRange("required_level_obsidian_chestplate", 20, 0, 100);
        REQUIRED_LEVEL_OBSIDIAN_LEGGINGS = serverBuilder.defineInRange("required_level_obsidian_leggings", 10, 0, 100);
        REQUIRED_LEVEL_OBSIDIAN_BOOTS = serverBuilder.defineInRange("required_level_obsidian_boots", 5, 0, 100);
        REQUIRED_LEVEL_REINFORCED_DIAMOND_HELMET = serverBuilder.defineInRange("required_level_reinforced_diamond_helmet", 20, 0, 100);
        REQUIRED_LEVEL_REINFORCED_DIAMOND_CHESTPLATE = serverBuilder.defineInRange("required_level_reinforced_diamond_chestplate", 50, 0, 100);
        REQUIRED_LEVEL_REINFORCED_DIAMOND_LEGGINGS = serverBuilder.defineInRange("required_level_reinforced_diamond_leggings", 35, 0, 100);
        REQUIRED_LEVEL_REINFORCED_DIAMOND_BOOTS = serverBuilder.defineInRange("required_level_reinforced_diamond_boots", 20, 0, 100);
        REQUIRED_LEVEL_REINFORCED_NETHERITE_HELMET = serverBuilder.defineInRange("required_level_reinforced_netherite_helmet", 50, 0, 100);
        REQUIRED_LEVEL_REINFORCED_NETHERITE_CHESTPLATE = serverBuilder.defineInRange("required_level_reinforced_netherite_chestplate", 100, 0, 100);
        REQUIRED_LEVEL_REINFORCED_NETHERITE_LEGGINGS = serverBuilder.defineInRange("required_level_reinforced_netherite_leggings", 75, 0, 100);
        REQUIRED_LEVEL_REINFORCED_NETHERITE_BOOTS = serverBuilder.defineInRange("required_level_reinforced_netherite_boots", 50, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_OTHER);
        REQUIRED_LEVEL_HUNTER_WOOL = serverBuilder.defineInRange("required_level_hunter_wool", 1, 0, 100);
        serverBuilder.push(CATEGORY_CRAFTING_OTHER_ENCHANTER);
        REQUIRED_LEVEL_EXP_JAR = serverBuilder.defineInRange("required_level_exp_jar", 5, 0, 100);
        REQUIRED_LEVEL_CURSE_BREAKER = serverBuilder.defineInRange("required_level_curse_breaker", 10, 0, 100);
        REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_1 = serverBuilder.defineInRange("required_level_experience_bottle_tier_1", 10, 0, 100);
        REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_2 = serverBuilder.defineInRange("required_level_experience_bottle_tier_2", 20, 0, 100);
        REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_3 = serverBuilder.defineInRange("required_level_experience_bottle_tier_3", 35, 0, 100);
        REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_4 = serverBuilder.defineInRange("required_level_experience_bottle_tier_4", 50, 0, 100);
        REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_5 = serverBuilder.defineInRange("required_level_experience_bottle_tier_5", 75, 0, 100);
        REQUIRED_LEVEL_EXPERIENCE_BOTTLE_TIER_6 = serverBuilder.defineInRange("required_level_experience_bottle_tier_6", 100, 0, 100);
        REQUIRED_LEVEL_EXP_JAR_TO_EXPERIENCE_BOTTLE = serverBuilder.defineInRange("required_level_exp_jar_to_experience_bottle", 25, 0, 100);
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_CRAFTING_OTHER_BUILDER);
        REQUIRED_LEVEL_SMALL_BACKPACK = serverBuilder.defineInRange("required_level_small_backpack", 5, 0, 100);
        REQUIRED_LEVEL_MEDIUM_BACKPACK = serverBuilder.defineInRange("required_level_medium_backpack", 15, 0, 100);
        REQUIRED_LEVEL_LARGE_BACKPACK = serverBuilder.defineInRange("required_level_large_backpack", 25, 0, 100);
        REQUIRED_LEVEL_HUGE_BACKPACK = serverBuilder.defineInRange("required_level_huge_backpack", 50, 0, 100);
        REQUIRED_LEVEL_ENDER_BACKPACK = serverBuilder.defineInRange("required_level_ender_backpack", 10, 0, 100);
        serverBuilder.pop();
        serverBuilder.pop();
        serverBuilder.pop();
        serverBuilder.push(CATEGORY_ALCHEMIST);
        ALLOW_ALL_EFFECTS = serverBuilder.comment("Don't remove effect when alchemist level isn't high enough if true.").define("allow_all_effects", false);
        serverBuilder.pop();

        SERVER_CONFIG = serverBuilder.build();
    }

}
