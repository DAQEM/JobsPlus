package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.data.condition.item.ItemCondition;
import com.daqem.arc.data.condition.item.ItemsCondition;
import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.itemrestrictions.data.RestrictionType;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.component.EmptyComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Stream;

public class RecipesScrollContentComponent extends EmptyComponent {

    private static final int COLUMNS = 4;
    private static final int COMPONENT_WIDTH = 24;
    private static final int GAP = 1;

    private final JobsScreenState state;

    public RecipesScrollContentComponent(JobsScreenState state) {
        super(0, 0, COLUMNS * (COMPONENT_WIDTH + GAP) - GAP, 0);
        this.state = state;

        this.buildRecipeComponentGrid();
        int rows = (int) Math.ceil((double) this.getComponents().size() / COLUMNS);
        this.setHeight(rows * (COMPONENT_WIDTH + GAP) - GAP);
    }

    private void buildRecipeComponentGrid() {
        List<DisplayEntry> displayEntries = this.getDisplayEntries();
        displayEntries = this.removeDuplicateItems(displayEntries);
        displayEntries = displayEntries.stream()
                .sorted(Comparator.comparingInt(DisplayEntry::level)
                        .thenComparing(e -> e.stack().getHoverName().getString()))
                .toList();

        for (int i = 0; i < displayEntries.size(); i++) {
            DisplayEntry entry = displayEntries.get(i);
            int x = (i % COLUMNS) * (COMPONENT_WIDTH + GAP);
            int y = (i / COLUMNS) * (COMPONENT_WIDTH + GAP);
            this.addComponent(new RecipeItemComponent(x, y, i, entry.restrictionTypes(), entry.level(), entry.stack()));
        }
    }

    private List<DisplayEntry> removeDuplicateItems(List<DisplayEntry> displayEntries) {
        Map<ItemStack, List<DisplayEntry>> uniqueEntries = new HashMap<>();
        for (DisplayEntry displayEntry : displayEntries) {
            ItemStack stack = ItemStack.EMPTY;
            for (ItemStack uniqueItemStack : uniqueEntries.keySet()) {
                if (ItemStack.isSameItemSameComponents(displayEntry.stack(), uniqueItemStack)) {
                    stack = uniqueItemStack;
                    break;
                }
            }
            if (stack.isEmpty()) {
                uniqueEntries.put(displayEntry.stack(), new ArrayList<>(List.of(displayEntry)));
            } else {
                uniqueEntries.get(stack).add(displayEntry);
            }
        }
        List<DisplayEntry> result = new ArrayList<>();
        for (List<DisplayEntry> entries : uniqueEntries.values()) {
            List<DisplayEntry> orderedEntries = entries.stream().sorted(Comparator.comparingInt(DisplayEntry::level).reversed()).toList();
            Set<RestrictionType> combinedRestrictionTypes = new HashSet<>();
            for (DisplayEntry entry : orderedEntries) {
                entry.restrictionTypes.removeAll(combinedRestrictionTypes);
                combinedRestrictionTypes.addAll(entry.restrictionTypes);
                if (!entry.restrictionTypes.isEmpty()) {
                    result.add(entry);
                }
            }
        }
        return result;
    }

    private List<DisplayEntry> getDisplayEntries() {
        Map<ItemRestriction, Integer> itemRestrictions = this.state.getSelectedJob().getJobInstance().getItemRestrictions();
        if (Minecraft.getInstance().player != null) {
            return itemRestrictions.entrySet().stream().flatMap(entry -> {
                ItemRestriction itemRestriction = entry.getKey();
                int requiredLevel = entry.getValue();
                List<ICondition> conditions = itemRestriction.getConditions();
                return conditions.stream().flatMap(condition -> {
                    if (condition instanceof ItemCondition itemCondition) {
                        return Stream.of(new DisplayEntry(itemCondition.getItemStack(), requiredLevel, itemRestriction.getRestrictionTypes()));
                    } else if (condition instanceof ItemsCondition itemsCondition) {
                        List<DisplayEntry> entries = new ArrayList<>();
                        for (ItemStack item : itemsCondition.getItemStacks(Minecraft.getInstance().player.registryAccess())) {
                            entries.add(new DisplayEntry(item, requiredLevel, itemRestriction.getRestrictionTypes()));
                        }
                        return entries.stream();
                    }
                    return Stream.empty();
                });
            }).toList();
        } else {
            return List.of();
        }
    }

    private record DisplayEntry(ItemStack stack, int level, List<RestrictionType> restrictionTypes) {
    }
}