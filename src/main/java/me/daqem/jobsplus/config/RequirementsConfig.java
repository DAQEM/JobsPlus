package me.daqem.jobsplus.config;

import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequirementsConfig {

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue ADD_JOBSPLUS_ITEMS;
    public static ForgeConfigSpec.ConfigValue<List<?>> REQUIREMENTS;

    static {
        ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
        ADD_JOBSPLUS_ITEMS = serverBuilder.comment("True if all Jobs+ items should be added every time the game starts. Note: if false, new Jobs+ won't be added either.")
                .define("add_jobsplus_items", true);

        REQUIREMENTS = serverBuilder.comment("Required job and levels per item. Example: [[\"minecraft:wooden_pixaxe\", \"MINER\", 1], [\"minecraft:stone_pixaxe\", \"MINER\", 5]]")
                .defineList("requirements", List.of(), o -> {
                    String errorMessage;

                    if (o instanceof List<?> arrayList) {
                        if (arrayList.size() == 3) {
                            if (arrayList.get(0) instanceof String itemKey) {
                                if (arrayList.get(1) instanceof String jobString) {
                                    if (Jobs.getFromString(jobString) != null) {
                                        if (arrayList.get(2) instanceof Integer) {
                                            @SuppressWarnings("deprecation")
                                            boolean flag = Registry.ITEM.get(new ResourceLocation(itemKey.split(":")[0], itemKey.split(":")[1])) != Items.AIR;
                                            if (flag) {
                                                return true;
                                            } else errorMessage = itemKey + " is not a valid item.";
                                        } else errorMessage = arrayList.get(2) + " should be of value Integer.";
                                    } else errorMessage = arrayList.get(1) + " is not a valid job.";
                                } else errorMessage = arrayList.get(1) + " should be of value String.";
                            } else errorMessage = arrayList.get(0) + " should be of value String.";
                        } else
                            errorMessage = arrayList + " should contain the following: [Item ID, Job name, Required Level]";
                    } else {
                        return false;
                    }

                    throw new IllegalArgumentException(errorMessage + " (in config file jobsplus-server-requirements.toml)");
                });
        SERVER_CONFIG = serverBuilder.build();
    }

    public static Map<String, Tuple<String, Integer>> getDefaultRequirements() {
        Map<String, Tuple<String, Integer>> map = new HashMap<>();
        map.put("jobsplus:small_backpack", new Tuple<>(Jobs.BUILDER.name(), 1));
        map.put("jobsplus:medium_backpack", new Tuple<>(Jobs.BUILDER.name(), 100));
        map.put("jobsplus:large_backpack", new Tuple<>(Jobs.BUILDER.name(), 100));
        map.put("jobsplus:huge_backpack", new Tuple<>(Jobs.BUILDER.name(), 100));
        map.put("jobsplus:ender_backpack", new Tuple<>(Jobs.BUILDER.name(), 100));
        map.put("jobsplus:exp_jar", new Tuple<>(Jobs.ENCHANTER.name(), 100));
        map.put("jobsplus:experience_bottle", new Tuple<>(Jobs.ENCHANTER.name(), 100));
        map.put("jobsplus:curse_breaker", new Tuple<>(Jobs.ENCHANTER.name(), 100));
        map.put("jobsplus:hunters_sword_level_1", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_sword_level_2", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_sword_level_3", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_sword_level_4", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_bow_level_1", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_bow_level_2", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_bow_level_3", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:hunters_bow_level_4", new Tuple<>(Jobs.HUNTER.name(), 100));
        map.put("jobsplus:lumberjack_axe_level_1", new Tuple<>(Jobs.LUMBERJACK.name(), 100));
        map.put("jobsplus:lumberjack_axe_level_2", new Tuple<>(Jobs.LUMBERJACK.name(), 100));
        map.put("jobsplus:lumberjack_axe_level_3", new Tuple<>(Jobs.LUMBERJACK.name(), 100));
        map.put("jobsplus:lumberjack_axe_level_4", new Tuple<>(Jobs.LUMBERJACK.name(), 100));
        map.put("jobsplus:miners_hammer_level_1", new Tuple<>(Jobs.MINER.name(), 100));
        map.put("jobsplus:miners_hammer_level_2", new Tuple<>(Jobs.MINER.name(), 100));
        map.put("jobsplus:miners_hammer_level_3", new Tuple<>(Jobs.MINER.name(), 100));
        map.put("jobsplus:miners_hammer_level_4", new Tuple<>(Jobs.MINER.name(), 100));
        map.put("jobsplus:fishermans_rod_level_1", new Tuple<>(Jobs.FISHERMAN.name(), 100));
        map.put("jobsplus:fishermans_rod_level_2", new Tuple<>(Jobs.FISHERMAN.name(), 100));
        map.put("jobsplus:fishermans_rod_level_3", new Tuple<>(Jobs.FISHERMAN.name(), 100));
        map.put("jobsplus:fishermans_rod_level_4", new Tuple<>(Jobs.FISHERMAN.name(), 100));
        map.put("jobsplus:farmers_hoe_level_1", new Tuple<>(Jobs.FARMER.name(), 100));
        map.put("jobsplus:farmers_hoe_level_2", new Tuple<>(Jobs.FARMER.name(), 100));
        map.put("jobsplus:farmers_hoe_level_3", new Tuple<>(Jobs.FARMER.name(), 100));
        map.put("jobsplus:farmers_hoe_level_4", new Tuple<>(Jobs.FARMER.name(), 100));
        map.put("jobsplus:diggers_excavator_level_1", new Tuple<>(Jobs.DIGGER.name(), 100));
        map.put("jobsplus:diggers_excavator_level_2", new Tuple<>(Jobs.DIGGER.name(), 100));
        map.put("jobsplus:diggers_excavator_level_3", new Tuple<>(Jobs.DIGGER.name(), 100));
        map.put("jobsplus:diggers_excavator_level_4", new Tuple<>(Jobs.DIGGER.name(), 100));
        map.put("jobsplus:reinforced_iron_helmet", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_iron_chestplate", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_iron_leggings", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_iron_boots", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:obsidian_helmet", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:obsidian_chestplate", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:obsidian_leggings", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:obsidian_boots", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_diamond_helmet", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_diamond_chestplate", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_diamond_leggings", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_diamond_boots", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_netherite_helmet", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_netherite_chestplate", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_netherite_leggings", new Tuple<>(Jobs.SMITH.name(), 100));
        map.put("jobsplus:reinforced_netherite_boots", new Tuple<>(Jobs.SMITH.name(), 100));
        return map;
    }
}
