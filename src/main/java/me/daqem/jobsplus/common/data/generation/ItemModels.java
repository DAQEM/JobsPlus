package me.daqem.jobsplus.common.data.generation;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, JobsPlus.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheld(ModItems.SMALL_BACKPACK.get());
        handheld(ModItems.MEDIUM_BACKPACK.get());
        handheld(ModItems.LARGE_BACKPACK.get());
        handheld(ModItems.HUGE_BACKPACK.get());
        handheld(ModItems.ENDER_BACKPACK.get());
        handheld(ModItems.EXP_JAR.get());
        handheld(ModItems.EXPERIENCE_BOTTLE.get());
        handheld(ModItems.CURSE_BREAKER.get());
        handheld(ModItems.MINERS_HAMMER_LEVEL_1.get());
        handheld(ModItems.MINERS_HAMMER_LEVEL_2.get());
        handheld(ModItems.MINERS_HAMMER_LEVEL_3.get());
        handheld(ModItems.MINERS_HAMMER_LEVEL_4.get());
        handheld(ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get());
        handheld(ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get());
        handheld(ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get());
        handheld(ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get());
        handheld(ModItems.LUMBERJACK_AXE_LEVEL_1.get());
        handheld(ModItems.LUMBERJACK_AXE_LEVEL_2.get());
        handheld(ModItems.LUMBERJACK_AXE_LEVEL_3.get());
        handheld(ModItems.LUMBERJACK_AXE_LEVEL_4.get());
        handheld(ModItems.FARMERS_HOE_LEVEL_1.get());
        handheld(ModItems.FARMERS_HOE_LEVEL_2.get());
        handheld(ModItems.FARMERS_HOE_LEVEL_3.get());
        handheld(ModItems.FARMERS_HOE_LEVEL_4.get());
        handheld(ModItems.HUNTERS_SWORD_LEVEL_1.get());
        handheld(ModItems.HUNTERS_SWORD_LEVEL_2.get());
        handheld(ModItems.HUNTERS_SWORD_LEVEL_3.get());
        handheld(ModItems.HUNTERS_SWORD_LEVEL_4.get());

        generated(ModItems.OBSIDIAN_HELMET.get());
        generated(ModItems.OBSIDIAN_CHESTPLATE.get());
        generated(ModItems.OBSIDIAN_LEGGINGS.get());
        generated(ModItems.OBSIDIAN_BOOTS.get());
        generated(ModItems.REINFORCED_IRON_HELMET.get());
        generated(ModItems.REINFORCED_IRON_CHESTPLATE.get());
        generated(ModItems.REINFORCED_IRON_LEGGINGS.get());
        generated(ModItems.REINFORCED_IRON_BOOTS.get());
        generated(ModItems.REINFORCED_DIAMOND_HELMET.get());
        generated(ModItems.REINFORCED_DIAMOND_CHESTPLATE.get());
        generated(ModItems.REINFORCED_DIAMOND_LEGGINGS.get());
        generated(ModItems.REINFORCED_DIAMOND_BOOTS.get());
        generated(ModItems.REINFORCED_NETHERITE_HELMET.get());
        generated(ModItems.REINFORCED_NETHERITE_CHESTPLATE.get());
        generated(ModItems.REINFORCED_NETHERITE_LEGGINGS.get());
        generated(ModItems.REINFORCED_NETHERITE_BOOTS.get());

        withExistingParent("construction_table", JobsPlus.getId("block/construction_table"));
    }

    private void handheld(Item item) {
        String name = item.getDescriptionId().replace("item.jobsplus.", "");
        singleTexture(name, mcLoc("item/handheld"), "layer0", modLoc("item/" + name));
    }

    private void generated(Item item) {
        String name = item.getDescriptionId().replace("item.jobsplus.", "");
        singleTexture(name, ResourceLocation.tryParse("item/generated"), "layer0", modLoc("item/" + name));
    }
}
