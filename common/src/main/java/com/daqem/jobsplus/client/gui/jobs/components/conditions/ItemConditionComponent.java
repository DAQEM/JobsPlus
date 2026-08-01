package com.daqem.jobsplus.client.gui.jobs.components.conditions;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.components.SlottedItemComponent;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ItemConditionComponent extends EmptyComponent {

    public ItemConditionComponent(Set<ItemStack> allowedItems, Set<ItemStack> deniedItems, Supplier<ScreenRectangle> parentBounds) {
        super(0, 0, 99, 0);

        List<ItemStack> uniqueAllowed = new ArrayList<>();
        for (ItemStack item : allowedItems) {
            boolean isDuplicate = false;
            for (ItemStack uniqueItem : uniqueAllowed) {
                if (ItemStack.isSameItemSameComponents(item, uniqueItem)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueAllowed.add(item);
            }
        }

        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : uniqueAllowed) {
            boolean isDenied = false;
            for (ItemStack deniedItem : deniedItems) {
                if (ItemStack.isSameItemSameComponents(item, deniedItem)) {
                    isDenied = true;
                    break;
                }
            }
            if (!isDenied) {
                items.add(item);
            }
        }

        items = items.stream()
                .sorted((b1, b2) -> {
                    String name1 = b1.getDisplayName().getString();
                    String name2 = b2.getDisplayName().getString();
                    return name1.compareToIgnoreCase(name2);
                })
                .toList();

        if (items.isEmpty()) {
            this.setHeight(0);
        } else if (items.size() == 1) {
            TextComponent dotText = new TextComponent(0, 0, Component.literal(" • "), 0xFF1E1410);
            this.addComponent(dotText);
            MultiLineTextComponent isItemTextComponent = new MultiLineTextComponent(10, 0, getWidth() - 10, JobsPlus.API.translatable("gui.jobs.condition.is", items.getFirst().getDisplayName()), 0xFF1E1410);
            this.addComponent(isItemTextComponent);
            this.setHeight(isItemTextComponent.getHeight());
        } else {
            final int columns = 4;
            final int rows = (int) Math.ceil((double) items.size() / columns);

            int startY = 0;
            int slotSize = 24;
            for (int i = 0; i < items.size(); i++) {
                ItemStack itemStack = items.get(i);
                int x = (i % columns) * (slotSize + 1);
                int y = startY + (i / columns) * (slotSize + 1);
                this.addComponent(new SlottedItemComponent(x, y, i, itemStack, parentBounds));
            }

            this.setHeight(startY + rows * (slotSize + 1));
        }
    }
}
