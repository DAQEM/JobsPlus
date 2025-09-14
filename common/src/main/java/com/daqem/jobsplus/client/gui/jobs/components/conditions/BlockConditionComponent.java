package com.daqem.jobsplus.client.gui.jobs.components.conditions;

import com.daqem.arc.Arc;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.components.SlottedItemComponent;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockConditionComponent extends EmptyComponent {

    public BlockConditionComponent(Set<Block> allowedBlocks, Set<Block> deniedBlocks, Supplier<ScreenRectangle> parentBounds) {
        super(0, 0, 99, 0);
        List<Block> blocks = new ArrayList<>(allowedBlocks);
        blocks.removeAll(deniedBlocks);

        List<ItemStack> items = blocks.stream()
                .map(Block::asItem)
                .filter(item -> item != Items.AIR)
                .collect(Collectors.toSet()).stream()
                .map(Item::getDefaultInstance)
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
            MultiLineTextComponent isBlockTextComponent = new MultiLineTextComponent(10, 0, getWidth() - 10, JobsPlus.translatable("gui.jobs.condition.is", blocks.getFirst().getName()), 0xFF1E1410);
            this.addComponent(isBlockTextComponent);
            this.setHeight(isBlockTextComponent.getHeight());
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
