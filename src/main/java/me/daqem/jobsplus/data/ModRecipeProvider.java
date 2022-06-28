package me.daqem.jobsplus.data;

import com.google.gson.JsonObject;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(ModItems.SMALL_BACKPACK.get())
                .pattern("ILI")
                .pattern("LSL")
                .pattern("ILI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('L', Tags.Items.LEATHER)
                .define('S', Tags.Items.STRING)
                .unlockedBy("iron_ingot", has(Items.IRON_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.MEDIUM_BACKPACK.get())
                .pattern("GLG")
                .pattern("LBL")
                .pattern("GLG")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('L', Tags.Items.LEATHER)
                .define('B', ModItems.SMALL_BACKPACK.get())
                .unlockedBy("gold_ingot", has(Items.GOLD_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.LARGE_BACKPACK.get())
                .pattern("DLD")
                .pattern("LBL")
                .pattern("DLD")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('L', Tags.Items.LEATHER)
                .define('B', ModItems.MEDIUM_BACKPACK.get())
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUGE_BACKPACK.get())
                .pattern("ELE")
                .pattern("LBL")
                .pattern("ELE")
                .define('E', Tags.Items.GEMS_EMERALD)
                .define('L', Tags.Items.LEATHER)
                .define('B', ModItems.LARGE_BACKPACK.get())
                .unlockedBy("emerald", has(Items.EMERALD))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.ENDER_BACKPACK.get())
                .pattern("GLG")
                .pattern("LEL")
                .pattern("GLG")
                .define('G', Tags.Items.ENDER_PEARLS)
                .define('L', Tags.Items.LEATHER)
                .define('E', Tags.Items.CHESTS_ENDER)
                .unlockedBy("ender_chest", has(Items.ENDER_CHEST))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.EXP_JAR.get())
                .pattern("PPP")
                .pattern("GEG")
                .pattern("GGG")
                .define('P', ItemTags.PLANKS)
                .define('G', Tags.Items.GLASS)
                .define('E', Tags.Items.GEMS_EMERALD)
                .unlockedBy("emerald", has(Items.EMERALD))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_IRON_HELMET.get())
                .pattern("DND")
                .pattern("B B")
                .define('D', Items.DIAMOND)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_IRON_CHESTPLATE.get())
                .pattern("N N")
                .pattern("BDB")
                .pattern("BBB")
                .define('D', Items.DIAMOND)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_IRON_LEGGINGS.get())
                .pattern("DND")
                .pattern("B B")
                .pattern("B B")
                .define('D', Items.DIAMOND)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_IRON_BOOTS.get())
                .pattern("D D")
                .pattern("B B")
                .define('D', Items.DIAMOND)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.OBSIDIAN_HELMET.get())
                .pattern("DND")
                .pattern("B B")
                .define('D', Items.DIAMOND_BLOCK)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.OBSIDIAN)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.OBSIDIAN_CHESTPLATE.get())
                .pattern("N N")
                .pattern("BDB")
                .pattern("BBB")
                .define('D', Items.DIAMOND_BLOCK)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.OBSIDIAN)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.OBSIDIAN_LEGGINGS.get())
                .pattern("DND")
                .pattern("B B")
                .pattern("B B")
                .define('D', Items.DIAMOND_BLOCK)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.OBSIDIAN)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.OBSIDIAN_BOOTS.get())
                .pattern("D D")
                .pattern("B B")
                .define('D', Items.DIAMOND_BLOCK)
                .define('B', Items.OBSIDIAN)
                .unlockedBy("obsidian", has(Items.OBSIDIAN))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_DIAMOND_HELMET.get())
                .pattern("DND")
                .pattern("B B")
                .define('D', Items.NETHERITE_INGOT)
                .define('N', Items.NETHER_STAR)
                .define('B', Items.DIAMOND_BLOCK)
                .unlockedBy("nether_star", has(Items.NETHER_STAR))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_DIAMOND_CHESTPLATE.get())
                .pattern("N N")
                .pattern("BDB")
                .pattern("BBB")
                .define('D', Items.NETHERITE_INGOT)
                .define('N', Items.NETHER_STAR)
                .define('B', Items.DIAMOND_BLOCK)
                .unlockedBy("nether_star", has(Items.NETHER_STAR))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_DIAMOND_LEGGINGS.get())
                .pattern("DND")
                .pattern("B B")
                .pattern("B B")
                .define('D', Items.NETHERITE_INGOT)
                .define('N', Items.NETHER_STAR)
                .define('B', Items.DIAMOND_BLOCK)
                .unlockedBy("nether_star", has(Items.NETHER_STAR))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_DIAMOND_BOOTS.get())
                .pattern("D D")
                .pattern("B B")
                .define('D', Items.NETHERITE_INGOT)
                .define('B', Items.DIAMOND_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_NETHERITE_HELMET.get())
                .pattern("DND")
                .pattern("B B")
                .define('D', Items.NETHERITE_INGOT)
                .define('N', Items.NETHER_STAR)
                .define('B', Items.NETHERITE_BLOCK)
                .unlockedBy("nether_star", has(Items.NETHER_STAR))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_NETHERITE_CHESTPLATE.get())
                .pattern("N N")
                .pattern("BDB")
                .pattern("BBB")
                .define('D', Items.NETHERITE_INGOT)
                .define('N', Items.NETHER_STAR)
                .define('B', Items.NETHERITE_BLOCK)
                .unlockedBy("nether_star", has(Items.NETHER_STAR))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_NETHERITE_LEGGINGS.get())
                .pattern("DND")
                .pattern("B B")
                .pattern("B B")
                .define('D', Items.NETHERITE_INGOT)
                .define('N', Items.NETHER_STAR)
                .define('B', Items.NETHERITE_BLOCK)
                .unlockedBy("nether_star", has(Items.NETHER_STAR))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.REINFORCED_NETHERITE_BOOTS.get())
                .pattern("D D")
                .pattern("B B")
                .define('D', Items.NETHERITE_INGOT)
                .define('B', Items.NETHERITE_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.MINERS_HAMMER_LEVEL_1.get())
                .pattern("DBD")
                .pattern("BSB")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.DIAMOND)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.MINERS_HAMMER_LEVEL_2.get())
                .pattern("NHN")
                .pattern("BSB")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('N', Items.NETHERITE_INGOT)
                .define('H', ModItems.MINERS_HAMMER_LEVEL_1.get())
                .define('B', Items.GOLD_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.MINERS_HAMMER_LEVEL_3.get())
                .pattern("NHN")
                .pattern("BSB")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('N', Items.NETHERITE_INGOT)
                .define('H', ModItems.MINERS_HAMMER_LEVEL_2.get())
                .define('B', Items.DIAMOND_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.MINERS_HAMMER_LEVEL_4.get())
                .pattern("NHN")
                .pattern("BSB")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('N', Items.NETHERITE_BLOCK)
                .define('H', ModItems.MINERS_HAMMER_LEVEL_3.get())
                .define('B', Items.EMERALD_BLOCK)
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get())
                .pattern(" DI")
                .pattern(" SD")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get())
                .pattern(" DI")
                .pattern(" ED")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.GOLD_BLOCK)
                .define('E', ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get())
                .pattern(" DI")
                .pattern(" ED")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.DIAMOND_BLOCK)
                .define('E', ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get())
                .pattern(" DI")
                .pattern(" ED")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_BLOCK)
                .define('I', Items.EMERALD_BLOCK)
                .define('E', ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.LUMBERJACK_AXE_LEVEL_1.get())
                .pattern("DII")
                .pattern(" SI")
                .pattern(" SD")
                .define('S', Items.STICK)
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.LUMBERJACK_AXE_LEVEL_2.get())
                .pattern("DII")
                .pattern(" EI")
                .pattern(" SD")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.GOLD_BLOCK)
                .define('E', ModItems.LUMBERJACK_AXE_LEVEL_1.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.LUMBERJACK_AXE_LEVEL_3.get())
                .pattern("DII")
                .pattern(" EI")
                .pattern(" SD")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.DIAMOND_BLOCK)
                .define('E', ModItems.LUMBERJACK_AXE_LEVEL_2.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.LUMBERJACK_AXE_LEVEL_4.get())
                .pattern("DII")
                .pattern(" EI")
                .pattern(" SD")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_BLOCK)
                .define('I', Items.EMERALD_BLOCK)
                .define('E', ModItems.LUMBERJACK_AXE_LEVEL_3.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FARMERS_HOE_LEVEL_1.get())
                .pattern("DII")
                .pattern("DS ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FARMERS_HOE_LEVEL_2.get())
                .pattern("DII")
                .pattern("DE ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.GOLD_BLOCK)
                .define('E', ModItems.FARMERS_HOE_LEVEL_1.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FARMERS_HOE_LEVEL_3.get())
                .pattern("DII")
                .pattern("DE ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.DIAMOND_BLOCK)
                .define('E', ModItems.FARMERS_HOE_LEVEL_2.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FARMERS_HOE_LEVEL_4.get())
                .pattern("DII")
                .pattern("DE ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_BLOCK)
                .define('I', Items.EMERALD_BLOCK)
                .define('E', ModItems.FARMERS_HOE_LEVEL_3.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_SWORD_LEVEL_1.get())
                .pattern(" D ")
                .pattern("IDI")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_SWORD_LEVEL_2.get())
                .pattern(" D ")
                .pattern("IEI")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.GOLD_BLOCK)
                .define('E', ModItems.HUNTERS_SWORD_LEVEL_1.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_SWORD_LEVEL_3.get())
                .pattern(" D ")
                .pattern("IEI")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.DIAMOND_BLOCK)
                .define('E', ModItems.HUNTERS_SWORD_LEVEL_2.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_SWORD_LEVEL_4.get())
                .pattern(" D ")
                .pattern("IEI")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.NETHERITE_BLOCK)
                .define('I', Items.EMERALD_BLOCK)
                .define('E', ModItems.HUNTERS_SWORD_LEVEL_3.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_BOW_LEVEL_1.get())
                .pattern(" IS")
                .pattern("D S")
                .pattern(" IS")
                .define('S', Items.STRING)
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_BOW_LEVEL_2.get())
                .pattern(" IS")
                .pattern("DES")
                .pattern(" IS")
                .define('S', Items.STRING)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.GOLD_BLOCK)
                .define('E', ModItems.HUNTERS_BOW_LEVEL_1.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_BOW_LEVEL_3.get())
                .pattern(" IS")
                .pattern("DES")
                .pattern(" IS")
                .define('S', Items.STRING)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.DIAMOND_BLOCK)
                .define('E', ModItems.HUNTERS_BOW_LEVEL_2.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.HUNTERS_BOW_LEVEL_4.get())
                .pattern(" IS")
                .pattern("DES")
                .pattern(" IS")
                .define('S', Items.STRING)
                .define('D', Items.NETHERITE_BLOCK)
                .define('I', Items.EMERALD_BLOCK)
                .define('E', ModItems.HUNTERS_BOW_LEVEL_3.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FISHERMANS_ROD_LEVEL_1.get())
                .pattern("  I")
                .pattern(" DS")
                .pattern("D S")
                .define('S', Items.STRING)
                .define('D', Items.DIAMOND)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("diamond", has(Items.DIAMOND))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FISHERMANS_ROD_LEVEL_2.get())
                .pattern("  I")
                .pattern(" ES")
                .pattern("D S")
                .define('S', Items.STRING)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.GOLD_BLOCK)
                .define('E', ModItems.FISHERMANS_ROD_LEVEL_1.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FISHERMANS_ROD_LEVEL_3.get())
                .pattern("  I")
                .pattern(" ES")
                .pattern("D S")
                .define('S', Items.STRING)
                .define('D', Items.NETHERITE_INGOT)
                .define('I', Items.DIAMOND_BLOCK)
                .define('E', ModItems.FISHERMANS_ROD_LEVEL_2.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.FISHERMANS_ROD_LEVEL_4.get())
                .pattern("  I")
                .pattern(" ES")
                .pattern("D S")
                .define('S', Items.STRING)
                .define('D', Items.NETHERITE_BLOCK)
                .define('I', Items.EMERALD_BLOCK)
                .define('E', ModItems.FISHERMANS_ROD_LEVEL_3.get())
                .unlockedBy("netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.CURSE_BREAKER.get())
                .pattern("DGD")
                .pattern("GEG")
                .pattern("DGD")
                .define('G', Items.GLOW_LICHEN)
                .define('D', Tags.Items.GEMS_EMERALD)
                .define('E', Items.COBWEB)
                .unlockedBy("cobweb", has(Items.COBWEB))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.EXPERIENCE_BOTTLE.get())
                .pattern(" G ")
                .pattern("GEG")
                .pattern(" G ")
                .define('G', Tags.Items.GLASS)
                .define('E', ModItems.EXP_JAR.get())
                .unlockedBy("exp_jar", has(ModItems.EXP_JAR.get()))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get()));

        ShapedRecipeBuilder.shaped(ModItems.EXPERIENCE_BOTTLE.get())
                .pattern("BGR")
                .pattern("GEG")
                .pattern("UGS")
                .define('G', Tags.Items.GLASS)
                .define('R', Items.ROTTEN_FLESH)
                .define('B', Items.BONE)
                .define('S', Items.STRING)
                .define('U', Items.GUNPOWDER)
                .define('E', Tags.Items.GEMS_EMERALD)
                .unlockedBy("emerald", has(Items.EMERALD))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPED_JOB_BASED_RECIPE.get(), JobsPlus.getId("experience_bottle_tier_1")));

        ShapedRecipeBuilder.shaped(ModItems.EXPERIENCE_BOTTLE.get())
                .pattern("BGR")
                .pattern("GEG")
                .pattern("UGS")
                .define('G', Tags.Items.GLASS)
                .define('R', Items.ROTTEN_FLESH)
                .define('B', Items.BONE)
                .define('S', Items.STRING)
                .define('U', Items.GUNPOWDER)
                .define('E', ModItems.EXPERIENCE_BOTTLE.get())
                .unlockedBy("experience_bottle", has(ModItems.EXPERIENCE_BOTTLE.get()))
                .save(RecipeInjector.Inject(consumer, ModRecipes.UPGRADE_RECIPE.get(), JobsPlus.getId("experience_bottle_other_tiers")));

        ShapelessRecipeBuilder.shapeless(Items.WHITE_WOOL)
                .requires(ItemTags.WOOL)
                .unlockedBy("wool", has(ItemTags.WOOL))
                .save(RecipeInjector.Inject(consumer, ModRecipes.SHAPELESS_JOB_BASED_RECIPE.get()));
    }

    static class RecipeInjector implements FinishedRecipe {

        final FinishedRecipe inner;
        RecipeSerializer<?> serializerOverride;
        ResourceLocation location;

        public RecipeInjector(FinishedRecipe innerIn, RecipeSerializer<?> serializerOverrideIn) {
            this.inner = innerIn;
            this.serializerOverride = serializerOverrideIn;
        }

        public RecipeInjector(FinishedRecipe innerIn, RecipeSerializer<?> serializerOverrideIn, ResourceLocation location) {
            this.inner = innerIn;
            this.location = location;
            this.serializerOverride = serializerOverrideIn;
        }

        public static Consumer<FinishedRecipe> Inject(Consumer<FinishedRecipe> consumer, RecipeSerializer<?> serializer) {
            return iFinishedRecipe -> consumer.accept(new RecipeInjector(iFinishedRecipe, serializer));
        }

        public static Consumer<FinishedRecipe> Inject(Consumer<FinishedRecipe> consumer, RecipeSerializer<?> serializer, ResourceLocation location) {
            return iFinishedRecipe -> consumer.accept(new RecipeInjector(iFinishedRecipe, serializer, location));
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject jsonObject) {
            this.inner.serializeRecipeData(jsonObject);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.location == null ? this.inner.getId() : this.location;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return this.inner.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.inner.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.inner.getAdvancementId();
        }

        @Override
        public @NotNull JsonObject serializeRecipe() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", Objects.requireNonNullElseGet(this.serializerOverride, this.inner::getType).toString());
            serializeRecipeData(jsonObject);
            return jsonObject;
        }
    }
}
