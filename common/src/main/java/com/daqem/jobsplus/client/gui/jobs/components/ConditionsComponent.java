package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.condition.NotCondition;
import com.daqem.arc.data.condition.OrCondition;
import com.daqem.arc.data.condition.block.BlockCondition;
import com.daqem.arc.data.condition.block.BlocksCondition;
import com.daqem.arc.data.condition.block.NotInBlockPosCacheCondition;
import com.daqem.arc.data.condition.block.ore.IsOreCondition;
import com.daqem.arc.data.condition.block.properties.BlockHardnessCondition;
import com.daqem.arc.data.condition.item.ItemCondition;
import com.daqem.arc.data.condition.item.ItemsCondition;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.components.conditions.*;
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
            TruncatedTextComponent noConditionsText = new TruncatedTextComponent(0, 2, getWidth(), JobsPlus.translatable("gui.jobs.no_conditions"), 0xFF1E1410);
            this.addComponent(noConditionsText);
            this.setHeight(noConditionsText.getHeight());
        } else {
            TruncatedTextComponent title = new TruncatedTextComponent(0, 2, getWidth(), JobsPlus.translatable("gui.jobs.conditions").withStyle(style -> style.withUnderlined(true)), 0xFF1E1410);
            this.addComponent(title);

            int yOffset = title.getHeight() + 4;
            Set<ICondition> parsedConditions = new HashSet<>();
            List<NotCondition> notConditions = getNotConditions(conditions);
            conditions = conditions.stream()
                    .sorted(Comparator.comparing(x -> {
                        if (x instanceof NotCondition) return 3;
                        if (x instanceof OrCondition) return 2;
                        if (x instanceof BlockCondition
                                || x instanceof BlocksCondition
                                || x instanceof ItemCondition
                                || x instanceof ItemsCondition
                        )
                            return 0;
                        return 1;
                    })).toList();
            for (ICondition condition : conditions) {
                if (parsedConditions.contains(condition)) continue;

                if (condition instanceof NotInBlockPosCacheCondition || condition instanceof OrCondition) {
                    parsedConditions.add(condition);
                    continue;
                }

                if (condition instanceof NotCondition) continue;

                if (condition instanceof BlockCondition || condition instanceof BlocksCondition) {
                    List<ICondition> blockConditions = conditions.stream()
                            .filter(c -> c instanceof BlockCondition
                                    || c instanceof BlocksCondition
                                    || c instanceof BlockHardnessCondition
                            ).toList();
                    List<ICondition> notBlockConditions = notConditions.stream()
                            .map(NotCondition::getConditions)
                            .flatMap(List::stream)
                            .filter(c -> c instanceof BlockCondition
                                    || c instanceof BlocksCondition
                                    || c instanceof BlockHardnessCondition
                                    || c instanceof IsOreCondition
                            ).toList();
                    Set<Block> allowedBlocks = new HashSet<>();
                    Set<Block> deniedBlocks = new HashSet<>();
                    for (ICondition blockCondition : blockConditions) {
                        if (blockCondition instanceof BlockCondition bc) {
                            allowedBlocks.add(bc.getBlock());
                            parsedConditions.add(blockCondition);
                        } else if (blockCondition instanceof BlocksCondition bcs) {
                            allowedBlocks.addAll(bcs.getAllBlocks(level.registryAccess()));
                            parsedConditions.add(blockCondition);
                        }
                    }
                    for (ICondition blockCondition : blockConditions) {
                        if (blockCondition instanceof BlockHardnessCondition bhc) {
                            for (Block allowedBlock : allowedBlocks) {
                                float hardness = allowedBlock.defaultBlockState().getDestroySpeed(Objects.requireNonNull(level), BlockPos.ZERO);
                                if (bhc.getMin() > hardness) {
                                    deniedBlocks.add(allowedBlock);
                                }
                                if (bhc.getMax() < hardness) {
                                    deniedBlocks.add(allowedBlock);
                                }
                            }
                            parsedConditions.add(blockCondition);
                        }
                    }
                    for (ICondition notBlockCondition : notBlockConditions) {
                        if (notBlockCondition instanceof BlockCondition nbc) {
                            deniedBlocks.add(nbc.getBlock());
                            parsedConditions.add(notBlockCondition);
                        } else if (notBlockCondition instanceof BlocksCondition nbcs) {
                            deniedBlocks.addAll(nbcs.getAllBlocks(level.registryAccess()));
                            parsedConditions.add(notBlockCondition);
                        } else if (notBlockCondition instanceof BlockHardnessCondition bhc) {
                            for (Block allowedBlock : allowedBlocks) {
                                float hardness = allowedBlock.defaultBlockState().getDestroySpeed(Objects.requireNonNull(level), BlockPos.ZERO);
                                if (bhc.getMin() > hardness) {
                                    deniedBlocks.add(allowedBlock);
                                }
                                if (bhc.getMax() < hardness) {
                                    deniedBlocks.add(allowedBlock);
                                }
                            }
                            parsedConditions.add(notBlockCondition);
                        } else if (notBlockCondition instanceof IsOreCondition) {
                            for (Block allowedBlock : allowedBlocks) {
                                if (getOreBlocks().contains(allowedBlock)) {
                                    deniedBlocks.add(allowedBlock);
                                }
                            }
                            parsedConditions.add(notBlockCondition);
                        }
                    }

                    BlockConditionComponent blockConditionComponent = new BlockConditionComponent(allowedBlocks, deniedBlocks, parentBounds);
                    blockConditionComponent.setY(yOffset);
                    this.addComponent(blockConditionComponent);
                    yOffset += blockConditionComponent.getHeight();

                    continue;
                }

                if (condition instanceof BlockHardnessCondition hardnessCondition) {
                    if (parsedConditions.contains(hardnessCondition)) continue;
                    if (conditions.stream().noneMatch(b -> b instanceof BlockCondition || b instanceof BlocksCondition)) {
                        Set<Block> allowedBlocks = level.registryAccess().lookupOrThrow(Registries.BLOCK).stream()
                                .filter(block -> {
                                    float hardness = block.defaultBlockState().getDestroySpeed(Objects.requireNonNull(level), BlockPos.ZERO);
                                    return hardnessCondition.getMin() <= hardness && hardness <= hardnessCondition.getMax();
                                })
                                .collect(HashSet::new, HashSet::add, HashSet::addAll);
                        BlockConditionComponent hardnessConditionComponent = new BlockConditionComponent(allowedBlocks, new HashSet<>(), parentBounds);
                        hardnessConditionComponent.setY(yOffset);
                        this.addComponent(hardnessConditionComponent);
                        yOffset += hardnessConditionComponent.getHeight();
                        parsedConditions.add(hardnessCondition);
                        continue;
                    }
                }

                if (condition instanceof IsOreCondition isOreCondition) {
                    if (parsedConditions.contains(isOreCondition)) continue;
                    if (conditions.stream().noneMatch(b -> b instanceof BlockCondition || b instanceof BlocksCondition)) {
                        List<ICondition> notBlockConditions = notConditions.stream()
                                .map(NotCondition::getConditions)
                                .flatMap(List::stream)
                                .filter(c -> c instanceof BlockCondition
                                        || c instanceof BlocksCondition
                                        || c instanceof BlockHardnessCondition
                                ).toList();
                        Set<Block> allowedBlocks = new HashSet<>(getOreBlocks());
                        Set<Block> deniedBlocks = new HashSet<>();
                        for (ICondition notBlockCondition : notBlockConditions) {
                            if (notBlockCondition instanceof BlockCondition nbc) {
                                deniedBlocks.add(nbc.getBlock());
                            } else if (notBlockCondition instanceof BlocksCondition nbcs) {
                                deniedBlocks.addAll(nbcs.getBlocks());
                            } else if (notBlockCondition instanceof BlockHardnessCondition bhc) {
                                for (Block allowedBlock : allowedBlocks) {
                                    float hardness = allowedBlock.defaultBlockState().getDestroySpeed(Objects.requireNonNull(level), BlockPos.ZERO);
                                    if (bhc.getMin() > hardness) {
                                        deniedBlocks.add(allowedBlock);
                                    }
                                    if (bhc.getMax() < hardness) {
                                        deniedBlocks.add(allowedBlock);
                                    }
                                }
                            }
                            parsedConditions.add(notBlockCondition);
                        }
                        BlockConditionComponent blockConditionComponent = new BlockConditionComponent(allowedBlocks, deniedBlocks, parentBounds);
                        blockConditionComponent.setY(yOffset);
                        this.addComponent(blockConditionComponent);
                        yOffset += blockConditionComponent.getHeight();
                        parsedConditions.add(isOreCondition);
                        continue;
                    }
                }

                if (condition instanceof ItemCondition || condition instanceof ItemsCondition) {
                    List<ICondition> itemConditions = conditions.stream()
                            .filter(c -> c instanceof ItemCondition
                                    || c instanceof ItemsCondition
                            ).toList();
                    List<ICondition> notItemConditions = notConditions.stream()
                            .map(NotCondition::getConditions)
                            .flatMap(List::stream)
                            .filter(c -> c instanceof ItemCondition
                                    || c instanceof ItemsCondition
                            ).toList();
                    Set<ItemStack> allowedItems = new HashSet<>();
                    Set<ItemStack> deniedItems = new HashSet<>();
                    for (ICondition itemCondition : itemConditions) {
                        if (itemCondition instanceof ItemCondition bc) {
                            allowedItems.add(bc.getItemStack());
                            parsedConditions.add(itemCondition);
                        } else if (itemCondition instanceof ItemsCondition bcs) {
                            allowedItems.addAll(bcs.getItemStacks(level.registryAccess()));
                            parsedConditions.add(itemCondition);
                        }
                    }
                    for (ICondition notItemCondition : notItemConditions) {
                        if (notItemCondition instanceof ItemCondition nbc) {
                            deniedItems.add(nbc.getItemStack());
                            parsedConditions.add(notItemCondition);
                        } else if (notItemCondition instanceof ItemsCondition nbcs) {
                            deniedItems.addAll(nbcs.getItemStacks(level.registryAccess()));
                            parsedConditions.add(notItemCondition);
                        }
                    }

                    ItemConditionComponent itemConditionComponent = new ItemConditionComponent(allowedItems, deniedItems, parentBounds);
                    itemConditionComponent.setY(yOffset);
                    this.addComponent(itemConditionComponent);
                    yOffset += itemConditionComponent.getHeight();

                    continue;
                }

                DefaultConditionComponent conditionComponent = new DefaultConditionComponent(condition);
                conditionComponent.setY(yOffset);
                this.addComponent(conditionComponent);
                yOffset += conditionComponent.getHeight();
            }

            for (NotCondition notCondition : notConditions) {
                if (parsedConditions.contains(notCondition)) continue;

                for (ICondition innerCondition : notCondition.getConditions()) {
                    if (parsedConditions.contains(innerCondition)) continue;

                    NotConditionComponent notConditionComponent = new NotConditionComponent(notCondition, innerCondition);
                    notConditionComponent.setY(yOffset);
                    this.addComponent(notConditionComponent);
                    yOffset += notConditionComponent.getHeight();

                    parsedConditions.add(innerCondition);
                }

                parsedConditions.add(notCondition);
            }

            this.setHeight(yOffset);
        }
    }

    private List<NotCondition> getNotConditions(List<ICondition> conditions) {
        return conditions.stream()
                .filter(c -> c instanceof NotCondition)
                .map(c -> (NotCondition) c)
                .toList();
    }

    private List<Block> getOreBlocks() {
        if (ORE_BLOCKS == null) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return List.of();
            List<Block> oreBlocks = level.registryAccess().lookupOrThrow(Registries.BLOCK).stream()
                    .filter(block -> new IsOreCondition(false).isMet(
                            new ActionDataBuilder((ArcPlayer) Minecraft.getInstance().player, ActionType.BREAK_BLOCK)
                                    .withData(ActionDataType.BLOCK_STATE, block.defaultBlockState())
                                    .build()
                    ))
                    .toList();
            ORE_BLOCKS = oreBlocks;
            return oreBlocks;
        }
        return ORE_BLOCKS;
    }
}
