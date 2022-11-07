package me.daqem.jobsplus.common.crafting.construction;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.daqem.jobsplus.common.inventory.construction.container.ConstructionCraftingContainer;
import me.daqem.jobsplus.init.ModRecipes;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class ConstructionRecipe implements ConstructionCraftingRecipe, IConstructionRecipe<ConstructionCraftingContainer> {

    static int MAX_WIDTH = 5;
    static int MAX_HEIGHT = 5;
    private final ResourceLocation id;
    private final String group;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack result;
    private final Jobs job;
    private final int requiredLevel;

    public ConstructionRecipe(ResourceLocation id, String group, int width, int height,
                              NonNullList<Ingredient> recipeItems, ItemStack result, Jobs job, int requiredLevel) {
        this.id = id;
        this.group = group;
        this.width = width;
        this.height = height;
        this.recipeItems = recipeItems;
        this.result = result;
        this.job = job;
        this.requiredLevel = requiredLevel;
    }

    static Map<String, Ingredient> keyFromJson(JsonObject jsonObject) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            Ingredient ingredient = Ingredient.fromJson(entry.getValue());
            if (entry.getValue().getAsJsonObject().has("nbt")) {
                if (ingredient.getItems().length > 0) {
                    ingredient.getItems()[0].setTag(CraftingHelper.getNBT(entry.getValue().getAsJsonObject().get("nbt")));
                }
            }

            map.put(entry.getKey(), ingredient);
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    @SuppressWarnings("all")
    private static int firstNonSpace(String s) {
        int i;
        for (i = 0; i < s.length() && s.charAt(i) == ' '; ++i) {
        }

        return i;
    }
    
    @SuppressWarnings("all")
    private static int lastNonSpace(String s) {
        int i;
        for (i = s.length() - 1; i >= 0 && s.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] shrink(String... strings) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < strings.length; ++i1) {
            String s = strings[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (strings.length == l) {
            return new String[0];
        } else {
            String[] string = new String[strings.length - l - k];

            for (int k1 = 0; k1 < string.length; ++k1) {
                string[k1] = strings[k1 + k].substring(i, j + 1);
            }

            return string;
        }
    }

    static String[] patternFromJson(JsonArray p_44197_) {
        String[] string = new String[p_44197_.size()];
        if (string.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (string.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < string.length; ++i) {
                String s = GsonHelper.convertToString(p_44197_.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && string[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                string[i] = s;
            }

            return string;
        }
    }

    static NonNullList<Ingredient> dissolvePattern(String[] p_44203_, Map<String, Ingredient> p_44204_, int p_44205_, int p_44206_) {
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(p_44205_ * p_44206_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_44204_.keySet());
        set.remove(" ");

        for (int i = 0; i < p_44203_.length; ++i) {
            for (int j = 0; j < p_44203_[i].length(); ++j) {
                String s = p_44203_[i].substring(j, j + 1);
                Ingredient ingredient = p_44204_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonNullList.set(j + p_44205_ * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonNullList;
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public boolean matches(@NotNull ConstructionCraftingContainer container, @NotNull Level level) {
        for (int x = 0; x <= container.getWidth() - this.width; ++x) {
            for (int y = 0; y <= container.getHeight() - this.height; ++y) {
                if (this.matches(container, x, y, true)) {
                    return true;
                }

                if (this.matches(container, x, y, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(ConstructionCraftingContainer container, int x, int y, boolean p_44174_) {
        for (int x1 = 0; x1 < container.getWidth(); ++x1) {
            for (int y1 = 0; y1 < container.getHeight(); ++y1) {
                int k = x1 - x;
                int l = y1 - y;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (p_44174_) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                ItemStack containerItem = container.getItem(x1 + y1 * container.getWidth());
                ItemStack containerItemCopy = containerItem.copy();
                if (!ingredient.test(containerItem)) {
                    return false;
                } else if (ingredient.getItems().length > 0) {
                    if (ingredient.getItems()[0].hasTag()) {
                        ItemStack item = ingredient.getItems()[0].copy();
                        if (item.getTag() != null) {
                            if (containerItem.getTag() != null && containerItemCopy.getTag() != null) {
                                for (String key : containerItem.getTag().getAllKeys()) {
                                    if (!item.getTag().contains(key)) {
                                        containerItemCopy.getTag().remove(key);
                                    } else {
                                        if (key.equals("Damage") && item.getTag().contains("Damage")) {
                                            item.getTag().remove("Damage");
                                            containerItemCopy.getTag().remove("Damage");
                                        }
                                    }
                                }
                            }
                            if (!item.getTag().equals(containerItemCopy.getTag())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ConstructionCraftingContainer container) {
        return this.getResultItem().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.CONSTRUCTION_SERIALIZER.get();
    }

    public Jobs getJob() {
        return job;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<ConstructionRecipe> {

        @Override
        public @Nullable ConstructionRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buf) {
            int i = buf.readVarInt();
            int j = buf.readVarInt();
            String s = buf.readUtf();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i * j, Ingredient.EMPTY);
            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            ItemStack itemstack = buf.readItem();
            Jobs job = Jobs.valueOf(buf.readUtf());
            int requiredLevel = buf.readInt();
            return new ConstructionRecipe(recipeId, s, i, j, nonNullList, itemstack, job, requiredLevel);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull ConstructionRecipe recipe) {
            buf.writeVarInt(recipe.width);
            buf.writeVarInt(recipe.height);
            buf.writeUtf(recipe.group);

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buf);
            }

            buf.writeItem(recipe.result);
            buf.writeUtf(recipe.job.name());
            buf.writeInt(recipe.requiredLevel);
        }

        @Override
        public @NotNull ConstructionRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            String s = GsonHelper.getAsString(serializedRecipe, "group", "");
            Map<String, Ingredient> map = ConstructionRecipe.keyFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "key"));
            String[] string = ConstructionRecipe.shrink(ConstructionRecipe.patternFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "pattern")));
            int i = string[0].length();
            int j = string.length;
            NonNullList<Ingredient> nonNullList = ConstructionRecipe.dissolvePattern(string, map, i, j);
            ItemStack itemstack = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(serializedRecipe, "result"), true, true);
            Jobs job = Jobs.valueOf(serializedRecipe.get("job").getAsString());
            int requiredLevel = serializedRecipe.get("requiredLevel").getAsInt();
            return new ConstructionRecipe(recipeId, s, i, j, nonNullList, itemstack, job, requiredLevel);
        }
    }
}
