package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.item.*;
import me.daqem.jobsplus.utils.enums.Backpack;
import me.daqem.jobsplus.utils.enums.ItemTiers;
import me.daqem.jobsplus.utils.enums.ModArmorMaterials;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JobsPlus.MOD_ID);

    public static final RegistryObject<Item> SMALL_BACKPACK = ITEMS.register("small_backpack", () -> new BackpackItem("small_backpack", Backpack.SMALL));
    public static final RegistryObject<Item> MEDIUM_BACKPACK = ITEMS.register("medium_backpack", () -> new BackpackItem("medium_backpack", Backpack.MEDIUM));
    public static final RegistryObject<Item> LARGE_BACKPACK = ITEMS.register("large_backpack", () -> new BackpackItem("large_backpack", Backpack.LARGE));
    public static final RegistryObject<Item> HUGE_BACKPACK = ITEMS.register("huge_backpack", () -> new BackpackItem("huge_backpack", Backpack.HUGE));
    public static final RegistryObject<Item> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new BackpackItem("ender_backpack", Backpack.ENDER));

    public static final RegistryObject<Item> EXP_JAR = ITEMS.register("exp_jar", () -> new EXPJarItem(new Item.Properties().tab(JobsPlus.TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
    //    public static final RegistryObject<Item> CURSE_BREAKER = ITEMS.register("curse_breaker", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));

    //    public static final RegistryObject<Item> BUILDERS_WAND = ITEMS.register("builders_wand", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> HUNTERS_SWORD = ITEMS.register("hunters_sword", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
//    public static final RegistryObject<Item> HUNTERS_BOW = ITEMS.register("hunters_bow", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_1 = ITEMS.register("lumberjack_axe_level_1", () -> new LumberAxeItem(ItemTiers.LEVEL_1, 3, 0, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_2 = ITEMS.register("lumberjack_axe_level_2", () -> new LumberAxeItem(ItemTiers.LEVEL_2, 4, 0, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_3 = ITEMS.register("lumberjack_axe_level_3", () -> new LumberAxeItem(ItemTiers.LEVEL_3, 5, 0, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_4 = ITEMS.register("lumberjack_axe_level_4", () -> new LumberAxeItem(ItemTiers.LEVEL_4, 6, 0, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_1 = ITEMS.register("miners_hammer_level_1", () -> new HammerItem(ItemTiers.LEVEL_1, 0, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_2 = ITEMS.register("miners_hammer_level_2", () -> new HammerItem(ItemTiers.LEVEL_2, 1, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_3 = ITEMS.register("miners_hammer_level_3", () -> new HammerItem(ItemTiers.LEVEL_3, 2, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_4 = ITEMS.register("miners_hammer_level_4", () -> new HammerItem(ItemTiers.LEVEL_4, 4, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    //    public static final RegistryObject<Item> FISHERMANS_ROD = ITEMS.register("fishermans_rod", () -> new Item(new Item.Properties().tab(JobsPlus.TAB)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_1 = ITEMS.register("farmers_hoe_level_1", () -> new FarmersHoeItem(ItemTiers.LEVEL_1, 0, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_2 = ITEMS.register("farmers_hoe_level_2", () -> new FarmersHoeItem(ItemTiers.LEVEL_2, 1, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_3 = ITEMS.register("farmers_hoe_level_3", () -> new FarmersHoeItem(ItemTiers.LEVEL_3, 2, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_4 = ITEMS.register("farmers_hoe_level_4", () -> new FarmersHoeItem(ItemTiers.LEVEL_4, 4, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_1 = ITEMS.register("diggers_excavator_level_1", () -> new ExcavatorItem(ItemTiers.LEVEL_1, 0, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_2 = ITEMS.register("diggers_excavator_level_2", () -> new ExcavatorItem(ItemTiers.LEVEL_2, 0, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_3 = ITEMS.register("diggers_excavator_level_3", () -> new ExcavatorItem(ItemTiers.LEVEL_3, 0, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_4 = ITEMS.register("diggers_excavator_level_4", () -> new ExcavatorItem(ItemTiers.LEVEL_4, 0, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    //
    public static final RegistryObject<Item> REINFORCED_IRON_HELMET = ITEMS.register("reinforced_iron_helmet", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_IRON, EquipmentSlot.HEAD, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> REINFORCED_IRON_CHESTPLATE = ITEMS.register("reinforced_iron_chestplate", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_IRON, EquipmentSlot.CHEST, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> REINFORCED_IRON_LEGGINGS = ITEMS.register("reinforced_iron_leggings", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_IRON, EquipmentSlot.LEGS, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> REINFORCED_IRON_BOOTS = ITEMS.register("reinforced_iron_boots", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_IRON, EquipmentSlot.FEET, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> OBSIDIAN_HELMET = ITEMS.register("obsidian_helmet", () -> new ModArmorItem(ModArmorMaterials.OBSIDIAN, EquipmentSlot.HEAD, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> OBSIDIAN_CHESTPLATE = ITEMS.register("obsidian_chestplate", () -> new ModArmorItem(ModArmorMaterials.OBSIDIAN, EquipmentSlot.CHEST, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> OBSIDIAN_LEGGINGS = ITEMS.register("obsidian_leggings", () -> new ModArmorItem(ModArmorMaterials.OBSIDIAN, EquipmentSlot.LEGS, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> OBSIDIAN_BOOTS = ITEMS.register("obsidian_boots", () -> new ModArmorItem(ModArmorMaterials.OBSIDIAN, EquipmentSlot.FEET, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> REINFORCED_DIAMOND_HELMET = ITEMS.register("reinforced_diamond_helmet", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_DIAMOND, EquipmentSlot.HEAD, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> REINFORCED_DIAMOND_CHESTPLATE = ITEMS.register("reinforced_diamond_chestplate", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_DIAMOND, EquipmentSlot.CHEST, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> REINFORCED_DIAMOND_LEGGINGS = ITEMS.register("reinforced_diamond_leggings", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_DIAMOND, EquipmentSlot.LEGS, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> REINFORCED_DIAMOND_BOOTS = ITEMS.register("reinforced_diamond_boots", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_DIAMOND, EquipmentSlot.FEET, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> REINFORCED_NETHERITE_HELMET = ITEMS.register("reinforced_netherite_helmet", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_NETHERITE, EquipmentSlot.HEAD, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> REINFORCED_NETHERITE_CHESTPLATE = ITEMS.register("reinforced_netherite_chestplate", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_NETHERITE, EquipmentSlot.CHEST, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> REINFORCED_NETHERITE_LEGGINGS = ITEMS.register("reinforced_netherite_leggings", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_NETHERITE, EquipmentSlot.LEGS, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> REINFORCED_NETHERITE_BOOTS = ITEMS.register("reinforced_netherite_boots", () -> new ModArmorItem(ModArmorMaterials.REINFORCED_NETHERITE, EquipmentSlot.FEET, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));

//    public static final RegistryObject<Item> LEVEL_25_TROPHY = ITEMS.register("level_25_trophy", () -> new BlockItemBase(ModBlocks.LEVEL_25_TROPHY.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
//    public static final RegistryObject<Item> LEVEL_50_TROPHY = ITEMS.register("level_50_trophy", () -> new BlockItemBase(ModBlocks.LEVEL_50_TROPHY.get(), new Item.Properties().rarity(Rarity.RARE)));
//    public static final RegistryObject<Item> LEVEL_100_TROPHY = ITEMS.register("level_100_trophy", () -> new BlockItemBase(ModBlocks.LEVEL_100_TROPHY.get(), new Item.Properties().rarity(Rarity.EPIC)));
}
