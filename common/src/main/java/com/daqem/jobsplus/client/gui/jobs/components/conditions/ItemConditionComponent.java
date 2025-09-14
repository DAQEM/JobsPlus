package com.daqem.jobsplus.client.gui.jobs.components.conditions;

import com.daqem.arc.Arc;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.components.SlottedItemComponent;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemConditionComponent extends EmptyComponent {

    public ItemConditionComponent(Set<ItemStack> allowedItems, Set<ItemStack> deniedItems, Supplier<ScreenRectangle> parentBounds) {
        super(0, 0, 99, 0);
        List<ItemStack> items = new ArrayList<>(allowedItems);
        items.removeAll(deniedItems);

        items = items.stream()
                .collect(Collectors.toMap(
                        ItemStack::getItem,
                        itemStack -> itemStack,
                        (existing, replacement) -> existing,
                        HashMap::new
                ))
                .values()
                .stream()
                .sorted((b1, b2) -> {
                    String name1 = b1.getItem().getName().getString();
                    String name2 = b2.getItem().getName().getString();
                    return name1.compareToIgnoreCase(name2);
                })
                .toList();

        if (items.isEmpty()) {
            this.setHeight(0);
        } else if (items.size() == 1) {
            TextComponent dotText = new TextComponent(0, 0, Component.literal(" • "), 0xFF1E1410);
            this.addComponent(dotText);
            MultiLineTextComponent isItemTextComponent = new MultiLineTextComponent(10, 0, getWidth() - 10, JobsPlus.translatable("gui.jobs.condition.is", items.getFirst().getItem().getName()), 0xFF1E1410);
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
