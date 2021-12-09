package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JobsPlus.MOD_ID);

//    public static final RegistryObject<Item> SMALL_BACKPACK = ITEMS.register("small_backpack", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> MEDIUM_BACKPACK = ITEMS.register("medium_backpack", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> BIG_BACKPACK = ITEMS.register("big_backpack", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> HUGE_BACKPACK = ITEMS.register("huge_backpack", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//
//    public static final RegistryObject<Item> BUILDERS_WAND = ITEMS.register("builders_wand", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> HUNTERS_SWORD = ITEMS.register("hunters_sword", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> HUNTERS_BOW = ITEMS.register("hunters_bow", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> LUMBERJACKS_AXE = ITEMS.register("lumberjacks_axe", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> MINERS_HAMMER = ITEMS.register("miners_hammer", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> FISHERMANS_ROD = ITEMS.register("fishermans_rod", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> FARMERS_HOE = ITEMS.register("farmers_hoe", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> DIGGERS_EXCAVATOR = ITEMS.register("diggers_excavator", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//
//    public static final RegistryObject<Item> OBSIDIAN_HELMET = ITEMS.register("obsidian_helmet", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> OBSIDIAN_CHESTPLATE = ITEMS.register("obsidian_chestplate", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> OBSIDIAN_LEGGINGS = ITEMS.register("obsidian_leggings", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> OBSIDIAN_BOOTS = ITEMS.register("obsidian_boots", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_IRON_HELMET = ITEMS.register("reinforced_iron_helmet", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_IRON_CHESTPLATE = ITEMS.register("reinforced_iron_chestplate", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_IRON_LEGGINGS = ITEMS.register("reinforced_iron_leggings", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_IRON_BOOTS = ITEMS.register("reinforced_iron_boots", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_DIAMOND_HELMET = ITEMS.register("reinforced_diamond_helmet", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_DIAMOND_CHESTPLATE = ITEMS.register("reinforced_diamond_chestplate", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_DIAMOND_LEGGINGS = ITEMS.register("reinforced_diamond_leggings", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_DIAMOND_BOOTS = ITEMS.register("reinforced_diamond_boots", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_NETHERITE_HELMET = ITEMS.register("reinforced_netherite_helmet", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_NETHERITE_CHESTPLATE = ITEMS.register("reinforced_netherite_chestplate", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_NETHERITE_LEGGINGS = ITEMS.register("reinforced_netherite_leggings", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> REINFORCED_NETHERITE_BOOTS = ITEMS.register("reinforced_netherite_boots", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//
//    public static final RegistryObject<Item> EXP_JAR = ITEMS.register("exp_jar", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> CURSE_BREAKER = ITEMS.register("curse_breaker", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//
//    public static final RegistryObject<Item> LEVEL_2_TOOL_UPGRADE = ITEMS.register("level_2_tool_upgrade", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> LEVEL_3_TOOL_UPGRADE = ITEMS.register("level_3_tool_upgrade", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> LEVEL_4_TOOL_UPGRADE = ITEMS.register("level_4_tool_upgrade", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//
//    public static final RegistryObject<Item> LEVEL_25_TROPHY = ITEMS.register("level_25_trophy", () -> new BlockItemBase(ModBlocks.LEVEL_25_TROPHY.get()));
//    public static final RegistryObject<Item> LEVEL_50_TROPHY = ITEMS.register("level_50_trophy", () -> new BlockItemBase(ModBlocks.LEVEL_50_TROPHY.get()));
//    public static final RegistryObject<Item> LEVEL_100_TROPHY = ITEMS.register("level_100_trophy", () -> new BlockItemBase(ModBlocks.LEVEL_100_TROPHY.get()));
}
