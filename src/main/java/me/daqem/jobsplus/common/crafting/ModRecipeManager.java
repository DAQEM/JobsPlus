package me.daqem.jobsplus.common.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.JobItemEntry;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModRecipeManager extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    public static final ModRecipeManager instance = new ModRecipeManager();
    public static final String ID = "id";
    public static final String NBT = "nbt";
    public static final String KEY = "key";
    public static final String ITEM = "item";
    public static final String PATTERN = "pattern";
    public static final String JOB = "job";
    public static final String REQUIRED_LEVEL = "requiredLevel";
    private static final ItemStack AIR = Items.AIR.getDefaultInstance();
    private ImmutableList<JobItemEntry> recipes = ImmutableList.of();

    public ModRecipeManager() {
        super(GSON, "custom_recipes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        final ImmutableList.Builder<JobItemEntry> builder = ImmutableList.builder();
        for (final Map.Entry<ResourceLocation, JsonElement> entry : jsonElementMap.entrySet()) {
            if (!entry.getKey().getNamespace().equals(JobsPlus.MOD_ID)) {
                continue;
            }

            final JobItemEntry jobItemEntry = tryParse(entry);
            if (jobItemEntry != null) {
                builder.add(jobItemEntry);
            }
        }

        this.recipes = builder.build();
        JobsPlus.LOGGER.info("Loaded {} recipes", this.recipes.size());
    }

    private JobItemEntry tryParse(final Map.Entry<ResourceLocation, JsonElement> entry) {
        try {

            final JsonObject data = (JsonObject) entry.getValue();
            final ResourceLocation recipeID = entry.getKey();
            boolean hasErrors = false;
            ItemStack stack = Items.AIR.getDefaultInstance();

            if (data.has(ID)) {
                ResourceLocation jobsItem = ResourceLocation.tryParse(data.get(ID).getAsString());
                if (jobsItem == null) {
                    JobsPlus.LOGGER.error("Missing or malformed field id in custom recipe file: " + recipeID);
                    hasErrors = true;
                }

                stack = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(jobsItem)).getDefaultInstance();
                if (stack == AIR) {
                    JobsPlus.LOGGER.error("Cannot find item for: {} in custom recipe: " + recipeID, jobsItem);
                    hasErrors = true;
                }

                if (data.has(NBT)) {
                    stack.setTag(TagParser.parseTag(data.get(NBT).getAsString()));
                }

            } else {
                JobsPlus.LOGGER.error("Missing or malformed field id in custom recipe file: " + recipeID);
                hasErrors = true;
            }

            Map<Character, ItemStack> charMap = new HashMap<>();

            if (data.has(KEY)) {
                final JsonObject key = data.get(KEY).getAsJsonObject();
                for (char c = 'A'; c <= 'Z'; ++c) {
                    if (key.has(String.valueOf(c))) {
                        final JsonObject keyEntry = key.get(String.valueOf(c)).getAsJsonObject();
                        if (keyEntry.has(ITEM)) {
                            ResourceLocation resourceLocation = ResourceLocation.tryParse(keyEntry.get(ITEM).getAsString());
                            if (resourceLocation == null) {
                                JobsPlus.LOGGER.error("Missing or malformed field item (key > {} > item) in custom recipe file: " + recipeID, c);
                                hasErrors = true;
                            }

                            ItemStack stack1 = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(resourceLocation)).getDefaultInstance();
                            if (stack1 == AIR) {
                                JobsPlus.LOGGER.error("Cannot find item for: {} in custom recipe: " + recipeID, resourceLocation);
                                hasErrors = true;
                            }

                            if (keyEntry.has(NBT)) {
                                stack1.setTag(TagParser.parseTag(keyEntry.get(NBT).getAsString()));
                            }

                            charMap.put(c, stack1);
                        } else {
                            JobsPlus.LOGGER.error("Missing or malformed field item (key > {} > item) in custom recipe file: " + recipeID, c);
                            hasErrors = true;
                        }
                    }
                }
            } else {
                JobsPlus.LOGGER.error("Missing or malformed field key in custom recipe file: " + recipeID);
                hasErrors = true;
            }

            if (charMap.isEmpty()) {
                JobsPlus.LOGGER.error("Missing field item (key > A) in custom recipe file: " + recipeID);
                hasErrors = true;
            }

            ArrayList<ItemStack> patternList = new ArrayList<>();

            if (data.has(PATTERN)) {
                JsonArray array = data.get(PATTERN).getAsJsonArray();
                if (array != null) {
                    if (array.size() == 5) {
                        for (int i = 0; i < 5; ++i) {
                            if (array.get(i).getAsString().length() != 5) {
                                JobsPlus.LOGGER.error("Array entry {} must have 5 characters in custom recipe file: " + recipeID, (i + 1));
                                hasErrors = true;
                            }
                            char[] charArray = array.get(i).getAsString().toCharArray();
                            for (char c : charArray) {
                                if (c == ' ') {
                                    patternList.add(AIR);
                                } else if (charMap.containsKey(c)) {
                                    patternList.add(charMap.get(c));
                                } else {
                                    JobsPlus.LOGGER.error("Unknown entry '{}' in pattern of custom recipe file: " + recipeID, c);
                                    hasErrors = true;
                                }
                            }
                        }
                    } else {
                        JobsPlus.LOGGER.error("Array in pattern doesn't have 5 entries in custom recipe file: " + recipeID);
                        hasErrors = true;
                    }
                } else {
                    JobsPlus.LOGGER.error("Missing or malformed array in key pattern of custom recipe file: " + recipeID);
                    hasErrors = true;
                }
            } else {
                JobsPlus.LOGGER.error("Missing or malformed field pattern in custom recipe file: " + recipeID);
                hasErrors = true;
            }

            if (patternList.size() != 25) {
                JobsPlus.LOGGER.error("Error during parsing of custom recipe file:" + recipeID + ", pattern does not contain 25 items.");
                hasErrors = true;
            }
            Jobs job = null;
            try {
                if (data.has(JOB)) job = Jobs.valueOf(data.get(JOB).getAsString());
                else {
                    JobsPlus.LOGGER.error("Missing or malformed field job in custom recipe file: " + recipeID);
                    hasErrors = true;
                }
            } catch (IllegalArgumentException e) {
                JobsPlus.LOGGER.error("Unknown job '{}' in custom recipe file: " + recipeID, data.get(JOB).getAsString());
                hasErrors = true;
            }

            int requiredLevel;
            if (data.has(REQUIRED_LEVEL)) requiredLevel = data.get(REQUIRED_LEVEL).getAsInt();
            else if (recipeID.toString().startsWith("jobsplus:")) {
                requiredLevel = 0;
            } else {
                JobsPlus.LOGGER.error("Missing or malformed field requiredLevel in custom recipe file:" + recipeID + " defaulting to 100.");
                requiredLevel = 100;
            }
            if (hasErrors) {
                JobsPlus.LOGGER.error("Cannot load custom recipe: " + recipeID);
                return null;
            } else return new JobItemEntry(recipeID, stack, patternList, job, requiredLevel);

        } catch (Exception e) {
            JobsPlus.LOGGER.error("Could not parse custom recipe entry for custom recipe file:" + entry.getKey(), e);
        }
        return null;
    }

    public ImmutableList<JobItemEntry> getRecipes() {
        return recipes;
    }
}
