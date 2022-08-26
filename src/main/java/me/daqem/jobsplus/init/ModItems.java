package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.block.BlockItemBase;
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

    public static final RegistryObject<Item> CONSTRUCTION_TABLE = ITEMS.register("construction_table", () -> new BlockItemBase(ModBlocks.CONSTRUCTION_TABLE.get()));

    public static final RegistryObject<Item> SMALL_BACKPACK = ITEMS.register("small_backpack", () -> new BackpackItem("small_backpack", Backpack.SMALL));
    public static final RegistryObject<Item> MEDIUM_BACKPACK = ITEMS.register("medium_backpack", () -> new BackpackItem("medium_backpack", Backpack.MEDIUM));
    public static final RegistryObject<Item> LARGE_BACKPACK = ITEMS.register("large_backpack", () -> new BackpackItem("large_backpack", Backpack.LARGE));
    public static final RegistryObject<Item> HUGE_BACKPACK = ITEMS.register("huge_backpack", () -> new BackpackItem("huge_backpack", Backpack.HUGE));
    public static final RegistryObject<Item> ENDER_BACKPACK = ITEMS.register("ender_backpack", () -> new BackpackItem("ender_backpack", Backpack.ENDER));

    public static final RegistryObject<Item> EXP_JAR = ITEMS.register("exp_jar", () -> new EXPJarItem(new Item.Properties().tab(JobsPlus.TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> EXPERIENCE_BOTTLE = ITEMS.register("experience_bottle", () -> new ModExperienceBottleItem(new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSE_BREAKER = ITEMS.register("curse_breaker", () -> new CurseBreakItem(new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Item> HUNTERS_SWORD_LEVEL_1 = ITEMS.register("hunters_sword_level_1", () -> new HunterSwordItem(ItemTiers.LEVEL_1, 6, -2.4F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> HUNTERS_SWORD_LEVEL_2 = ITEMS.register("hunters_sword_level_2", () -> new HunterSwordItem(ItemTiers.LEVEL_2, 7, -2.3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HUNTERS_SWORD_LEVEL_3 = ITEMS.register("hunters_sword_level_3", () -> new HunterSwordItem(ItemTiers.LEVEL_3, 9, -2.2F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> HUNTERS_SWORD_LEVEL_4 = ITEMS.register("hunters_sword_level_4", () -> new HunterSwordItem(ItemTiers.LEVEL_4, 11, -2F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> HUNTERS_BOW_LEVEL_1 = ITEMS.register("hunters_bow_level_1", () -> new HunterBowItem(new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON).durability(576)));
    public static final RegistryObject<Item> HUNTERS_BOW_LEVEL_2 = ITEMS.register("hunters_bow_level_2", () -> new HunterBowItem(new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON).durability(645)));
    public static final RegistryObject<Item> HUNTERS_BOW_LEVEL_3 = ITEMS.register("hunters_bow_level_3", () -> new HunterBowItem(new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE).durability(741)));
    public static final RegistryObject<Item> HUNTERS_BOW_LEVEL_4 = ITEMS.register("hunters_bow_level_4", () -> new HunterBowItem(new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC).durability(976)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_1 = ITEMS.register("lumberjack_axe_level_1", () -> new LumberAxeItem(ItemTiers.LEVEL_1, 7, -3.4F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_2 = ITEMS.register("lumberjack_axe_level_2", () -> new LumberAxeItem(ItemTiers.LEVEL_2, 8, -3.3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_3 = ITEMS.register("lumberjack_axe_level_3", () -> new LumberAxeItem(ItemTiers.LEVEL_3, 10, -3.2F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> LUMBERJACK_AXE_LEVEL_4 = ITEMS.register("lumberjack_axe_level_4", () -> new LumberAxeItem(ItemTiers.LEVEL_4, 12, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_1 = ITEMS.register("miners_hammer_level_1", () -> new HammerItem(ItemTiers.LEVEL_1, 2, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_2 = ITEMS.register("miners_hammer_level_2", () -> new HammerItem(ItemTiers.LEVEL_2, 2, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_3 = ITEMS.register("miners_hammer_level_3", () -> new HammerItem(ItemTiers.LEVEL_3, 3, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MINERS_HAMMER_LEVEL_4 = ITEMS.register("miners_hammer_level_4", () -> new HammerItem(ItemTiers.LEVEL_4, 3, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> FISHERMANS_ROD_LEVEL_1 = ITEMS.register("fishermans_rod_level_1", () -> new RodItem(new Item.Properties().rarity(Rarity.COMMON).durability(257)));
    public static final RegistryObject<Item> FISHERMANS_ROD_LEVEL_2 = ITEMS.register("fishermans_rod_level_2", () -> new RodItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(473)));
    public static final RegistryObject<Item> FISHERMANS_ROD_LEVEL_3 = ITEMS.register("fishermans_rod_level_3", () -> new RodItem(new Item.Properties().rarity(Rarity.RARE).durability(741)));
    public static final RegistryObject<Item> FISHERMANS_ROD_LEVEL_4 = ITEMS.register("fishermans_rod_level_4", () -> new RodItem(new Item.Properties().rarity(Rarity.EPIC).durability(976)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_1 = ITEMS.register("farmers_hoe_level_1", () -> new FarmersHoeItem(ItemTiers.LEVEL_1, 1, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_2 = ITEMS.register("farmers_hoe_level_2", () -> new FarmersHoeItem(ItemTiers.LEVEL_2, 1, -2F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_3 = ITEMS.register("farmers_hoe_level_3", () -> new FarmersHoeItem(ItemTiers.LEVEL_3, 2, -1F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> FARMERS_HOE_LEVEL_4 = ITEMS.register("farmers_hoe_level_4", () -> new FarmersHoeItem(ItemTiers.LEVEL_4, 2, 0F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_1 = ITEMS.register("diggers_excavator_level_1", () -> new ExcavatorItem(ItemTiers.LEVEL_1, 2, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_2 = ITEMS.register("diggers_excavator_level_2", () -> new ExcavatorItem(ItemTiers.LEVEL_2, 2, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_3 = ITEMS.register("diggers_excavator_level_3", () -> new ExcavatorItem(ItemTiers.LEVEL_3, 3, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DIGGERS_EXCAVATOR_LEVEL_4 = ITEMS.register("diggers_excavator_level_4", () -> new ExcavatorItem(ItemTiers.LEVEL_4, 3, -3F, new Item.Properties().tab(JobsPlus.TAB).rarity(Rarity.EPIC)));

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

}
