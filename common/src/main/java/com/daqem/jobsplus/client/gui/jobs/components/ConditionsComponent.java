package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.condition.OrCondition;
import com.daqem.arc.data.condition.block.*;
import com.daqem.arc.data.condition.item.ItemCondition;
import com.daqem.arc.data.condition.item.ItemsCondition;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.components.conditions.BlockConditionComponent;
import com.daqem.jobsplus.client.gui.jobs.components.conditions.DefaultConditionComponent;
import com.daqem.jobsplus.client.gui.jobs.components.conditions.ItemConditionComponent;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Supplier;

public class ConditionsComponent extends EmptyComponent {

    private static List<Block> ORE_BLOCKS = null;

    public ConditionsComponent(List<ICondition> conditions, Supplier<ScreenRectangle> parentBounds) {
        super(0, 0, 99, 9);

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        if (conditions.isEmpty()) {
            TruncatedTextComponent noConditionsText = new TruncatedTextComponent(0, 2, getWidth(), JobsPlus.API.translatable("gui.jobs.no_conditions"), 0xFF1E1410);
            this.addComponent(noConditionsText);
            this.setHeight(noConditionsText.getHeight());
        } else {
            TruncatedTextComponent title = new TruncatedTextComponent(0, 2, getWidth(), JobsPlus.API.translatable("gui.jobs.conditions").withStyle(style -> style.withUnderlined(true)), 0xFF1E1410);
            this.addComponent(title);

            int yOffset = title.getHeight() + 4;
            Set<ICondition> processedConditions = new HashSet<>();

            List<ICondition> blockConditions = conditions.stream().filter(c -> c instanceof BlockCondition || c instanceof BlocksCondition || c instanceof IsOreCondition || c instanceof BlockHardnessCondition).toList();
            List<ICondition> itemConditions = conditions.stream().filter(c -> c instanceof ItemCondition || c instanceof ItemsCondition).toList();

            if (!blockConditions.isEmpty()) {
                processedConditions.addAll(blockConditions);
                Set<Block> allowedBlocks = new HashSet<>();
                Set<Block> deniedBlocks = new HashSet<>();

                blockConditions.stream()
                        .filter(c -> !c.isInverted() && (c instanceof BlockCondition || c instanceof BlocksCondition))
                        .forEach(c -> {
                            if (c instanceof BlockCondition bc) allowedBlocks.add(bc.getBlock());
                            else allowedBlocks.addAll(((BlocksCondition) c).getAllBlocks(level.registryAccess()));
                        });

                if (allowedBlocks.isEmpty()) {
                    blockConditions.stream()
                            .filter(c -> !c.isInverted() && (c instanceof IsOreCondition || c instanceof BlockHardnessCondition))
                            .findFirst().ifPresent(c -> {
                                if (c instanceof IsOreCondition) {
                                    allowedBlocks.addAll(getOreBlocks());
                                } else {
                                    BlockHardnessCondition bhc = (BlockHardnessCondition) c;
                                    level.registryAccess().lookupOrThrow(Registries.BLOCK).forEach(block -> {

                                        ActionData actionData = new ActionDataBuilder((ArcPlayer) Minecraft.getInstance().player, IActionType.BREAK_BLOCK)
                                                .withData(IActionDataType.BLOCK_STATE, block.defaultBlockState())
                                                .withData(IActionDataType.BLOCK_POSITION, BlockPos.ZERO)
                                                .withData(IActionDataType.WORLD, level)
                                                .build();

                                        if (bhc.isMet(actionData)) {
                                            allowedBlocks.add(block);
                                        }
                                    });
                                }
                            });
                }

                blockConditions.stream()
                        .filter(c -> c.isInverted() && (c instanceof BlockCondition || c instanceof BlocksCondition))
                        .forEach(c -> {
                            if (c instanceof BlockCondition bc) deniedBlocks.add(bc.getBlock());
                            else deniedBlocks.addAll(((BlocksCondition) c).getAllBlocks(level.registryAccess()));
                        });

                List<ICondition> refiningConditions = blockConditions.stream()
                        .filter(c -> c instanceof IsOreCondition || c instanceof BlockHardnessCondition)
                        .toList();

                Set<Block> toRemove = new HashSet<>();
                for (Block block : allowedBlocks) {

                    // Pre-build the ActionData context for this specific block
                    ActionData actionData = new ActionDataBuilder((ArcPlayer) Minecraft.getInstance().player, IActionType.BREAK_BLOCK)
                            .withData(IActionDataType.BLOCK_STATE, block.defaultBlockState())
                            .withData(IActionDataType.BLOCK_POSITION, BlockPos.ZERO)
                            .withData(IActionDataType.WORLD, level)
                            .build();

                    for (ICondition refiner : refiningConditions) {
                        boolean isMet = false;

                        if (refiner instanceof IsOreCondition oreCond) {
                            isMet = oreCond.isMet(actionData);
                        } else if (refiner instanceof BlockHardnessCondition bhc) {
                            isMet = bhc.isMet(actionData);
                        }

                        if ((!refiner.isInverted() && !isMet) || (refiner.isInverted() && isMet)) {
                            toRemove.add(block);
                            break;
                        }
                    }
                }
                allowedBlocks.removeAll(toRemove);
                deniedBlocks.addAll(toRemove);

                BlockConditionComponent blockConditionComponent = new BlockConditionComponent(allowedBlocks, deniedBlocks, parentBounds);
                if (blockConditionComponent.getHeight() > 0) {
                    blockConditionComponent.setY(yOffset);
                    this.addComponent(blockConditionComponent);
                    yOffset += blockConditionComponent.getHeight();
                } else {
                    // Fallback: If the visual block grid didn't render (e.g., purely negative conditions like inverted is_ore),
                    // remove them from processed so they gracefully fall back to text rendering!
                    processedConditions.removeAll(blockConditions);
                }
            }

            if (!itemConditions.isEmpty()) {
                processedConditions.addAll(itemConditions);
                Set<ItemStack> allowedItems = new HashSet<>();
                Set<ItemStack> deniedItems = new HashSet<>();

                for (ICondition condition : itemConditions) {
                    Set<ItemStack> stacks = new HashSet<>();
                    if (condition instanceof ItemCondition ic) stacks.add(ic.getItemStack());
                    else if (condition instanceof ItemsCondition ics) stacks.addAll(ics.getItemStacks(level.registryAccess()));

                    if (condition.isInverted()) {
                        deniedItems.addAll(stacks);
                    } else {
                        allowedItems.addAll(stacks);
                    }
                }

                ItemConditionComponent itemConditionComponent = new ItemConditionComponent(allowedItems, deniedItems, parentBounds);
                if (itemConditionComponent.getHeight() > 0) {
                    itemConditionComponent.setY(yOffset);
                    this.addComponent(itemConditionComponent);
                    yOffset += itemConditionComponent.getHeight();
                } else {
                    // Fallback for purely inverted item conditions too
                    processedConditions.removeAll(itemConditions);
                }
            }

            for (ICondition condition : conditions) {
                if (processedConditions.contains(condition)) continue;
                if (condition instanceof NotInBlockPosCacheCondition || condition instanceof OrCondition) continue;

                DefaultConditionComponent conditionComponent = new DefaultConditionComponent(condition);
                conditionComponent.setY(yOffset);
                this.addComponent(conditionComponent);
                yOffset += conditionComponent.getHeight();
            }

            this.setHeight(yOffset);
        }
    }

    private List<Block> getOreBlocks() {
        if (ORE_BLOCKS == null) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return List.of();
            List<Block> oreBlocks = level.registryAccess().lookupOrThrow(Registries.BLOCK).stream()
                    .filter(block -> new IsOreCondition(false).isMet(
                            new ActionDataBuilder((ArcPlayer) Minecraft.getInstance().player, IActionType.BREAK_BLOCK)
                                    .withData(IActionDataType.BLOCK_STATE, block.defaultBlockState())
                                    .build()
                    ))
                    .toList();
            ORE_BLOCKS = oreBlocks;
            return oreBlocks;
        }
        return ORE_BLOCKS;
    }
}