package me.daqem.jobsplus.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class JobItemEntryHelper {

    public static ArrayList<ItemStack> getRecipe(ItemStack itemStack, ArrayList<JobItemEntry> list) {
        for (JobItemEntry entry : list) {
            if (entry.getStack().getItem() == itemStack.getItem()) {
                if (entry.getStack().equals(itemStack, true))
                    return entry.getPatternList();
            }
        }

        final ItemStack is = Items.BEDROCK.getDefaultInstance();
        return new ArrayList<>(List.of(is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is, is));
    }

    public static List<JobItemEntry> getItemEntries() {
        return ModRecipeManager.instance.getRecipes();
    }

    public static ArrayList<JobItemEntry> getItemEntriesAsArrayList() {
        return new ArrayList<>(ModRecipeManager.instance.getRecipes());
    }

    public static ArrayList<ItemStack> getStacksForJob(Jobs job, ArrayList<JobItemEntry> list) {
        ArrayList<ItemStack> list1 = new ArrayList<>();
        list.sort(Comparator.comparing(JobItemEntry::getRecipeID));
        list.sort(Comparator.comparing(JobItemEntry::getRequiredLevel));
        for (JobItemEntry entry : list) {
            if (entry.getJob() == job) list1.add(entry.getStack());
        }
        return list1;
    }

    public static Map<ItemStack, Integer> getRequiredLevelsForStacks(ArrayList<JobItemEntry> list) {
        Map<ItemStack, Integer> map = new HashMap<>();
        for (JobItemEntry entry : list) {
            map.put(entry.getStack(), entry.getRequiredLevel());
        }
        return map;
    }

    public static int getRequiredLevelForStack(ItemStack stack) {
        for (JobItemEntry entry : getItemEntries()) {
            if (entry.getStack().equals(stack, true)) return entry.getRequiredLevel();
        }
        return 100;
    }

    public static Jobs getJobForStack(ItemStack stack, ArrayList<JobItemEntry> list) {
        for (Jobs job : Jobs.values()) {
            for (ItemStack itemStack : getStacksForJob(job, list)) {
                if (itemStack.equals(stack, true)) return job;
            }
        }
        return null;
    }

    public static CompoundTag toTag() {
        CompoundTag recipeID = new CompoundTag();
        for (JobItemEntry entry : getItemEntries()) {
            CompoundTag main = new CompoundTag();
            main.putString("id", entry.getStack().getItem().getDescriptionId().replace("item.", "").replace("block.", "").replace(".", ":"));
            if (entry.getStack().getTag() != null && entry.getStack().hasTag())
                main.putString("nbt", entry.getStack().getTag().toString());
            main.putString("job", entry.getJob().name());
            main.putInt("requiredLevel", entry.getRequiredLevel());

            ListTag listTag = new ListTag();
            for (int i = 0; i < 25; ++i) {
                CompoundTag compoundTag = new CompoundTag();
                final ItemStack stack = entry.getPatternList().get(i);
                compoundTag.putString("name", stack.getDescriptionId().replace("item.", "").replace("block.", "").replace(".", ":"));
                if (stack.getTag() != null && stack.hasTag()) compoundTag.putString("nbt", stack.getTag().toString());
                listTag.add(compoundTag);
            }
            main.put("pattern", listTag);

            recipeID.put(entry.getRecipeID().toString(), main);
        }
        return recipeID;
    }

    public static ArrayList<JobItemEntry> fromTag(CompoundTag tag) {
        ArrayList<JobItemEntry> list = new ArrayList<>();
        for (String key : tag.getAllKeys()) {
            final CompoundTag compound = tag.getCompound(key);
            ItemStack stack = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(compound.getString("id")))).getDefaultInstance();
            try {
                if (!compound.getString("nbt").equals("")) stack.setTag(TagParser.parseTag(compound.getString("nbt")));
            } catch (CommandSyntaxException ignored) {
            }
            ArrayList<ItemStack> stackList = new ArrayList<>();

            for (int i = 0; i < 25; ++i) {
                ItemStack stack1 = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(compound.getList("pattern", Tag.TAG_COMPOUND).getCompound(i).getString("name")))).getDefaultInstance();
                if (!compound.getList("pattern", Tag.TAG_COMPOUND).getCompound(i).getString("nbt").equals("")) {
                    try {
                        stack1.setTag(TagParser.parseTag(compound.getList("pattern", Tag.TAG_COMPOUND).getCompound(i).getString("nbt")));
                    } catch (CommandSyntaxException ignored) {
                    }
                }
                stackList.add(stack1);
            }

            list.add(new JobItemEntry(ResourceLocation.tryParse(key), stack, stackList, Jobs.valueOf(compound.getString("job")), compound.getInt("requiredLevel")));
        }
        return list;
    }
}
