package me.daqem.jobsplus.client.tooltip;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TooltipBuilder {

    public static final Component WHITE_SPACE = JobsPlus.literal(" ");
    private final ArrayList<Component> componentList = new ArrayList<>();

    public TooltipBuilder withRequirement(ItemStack stack) {
        if (shouldShowComponent(ShiftType.SHIFT)) {
            Jobs job = ModRecipeManager.getJobClient(stack);
            if (job != null) {
                componentList.addAll(List.of(
                        JobsPlus.literal(ChatColor.boldDarkGreen() + JobsPlus.translatable("tooltip.requirements").getString()),
                        JobsPlus.literal(ChatColor.green() + JobsPlus.translatable("tooltip.job").getString() + ChatColor.reset() + ChatHandler.capitalizeWord(JobsPlus.translatable("job." + job.name().toLowerCase()).getString().toLowerCase())),
                        JobsPlus.literal(ChatColor.green() + JobsPlus.translatable("tooltip.level").getString() + ChatColor.reset() + ModRecipeManager.getRequiredJobLevelClient(stack))));
            }
        }
        return this;
    }

    public TooltipBuilder withAbout(Item item) {
        if (shouldShowComponent(ShiftType.SHIFT)) {
            componentList.addAll(List.of(
                    WHITE_SPACE,
                    JobsPlus.translatable("tooltip.about").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withBold(true)),
                    JobsPlus.translatable("tooltip.about." + item.getDescriptionId().split("\\.")[2]).withStyle(ChatFormatting.GRAY)));
        }
        return this;
    }

    public TooltipBuilder withAbout(String str, AboutType aboutType) {
        if (shouldShowComponent(ShiftType.SHIFT)) {
            componentList.addAll(List.of(WHITE_SPACE, JobsPlus.translatable("tooltip.about").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withBold(true))));
            if (aboutType == AboutType.MODES || aboutType == AboutType.HOE || aboutType == AboutType.HOE_LVL_4) {
                componentList.add(JobsPlus.translatable("tooltip.about.modes").withStyle(ChatFormatting.GREEN).append(ChatColor.white() + str));
            }
            if (aboutType == AboutType.HOE_LVL_4) {
                componentList.add(JobsPlus.translatable("tooltip.about.hoe").withStyle(ChatFormatting.GRAY));
            }
            if (aboutType == AboutType.BOW) {
                componentList.add(JobsPlus.translatable("tooltip.about.bow").withStyle(ChatFormatting.GREEN).append(ChatColor.white() + str));
            }
            if (aboutType == AboutType.AXE) {
                componentList.add(JobsPlus.translatable("tooltip.about.modes").withStyle(ChatFormatting.GREEN).append(ChatColor.white() + JobsPlus.translatable("tooltip.about.axe").getString()));
                componentList.add(JobsPlus.translatable("tooltip.about.axe.logs").withStyle(ChatFormatting.GREEN).append(ChatColor.white() + str));
            }
        }
        return this;
    }

    public TooltipBuilder withControls(ControlType controlType) {
        if (shouldShowComponent(ShiftType.SHIFT)) {
            componentList.addAll(List.of(
                    WHITE_SPACE,
                    JobsPlus.translatable("tooltip.controls").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withBold(true))));
            if (controlType == ControlType.HOE) {
                componentList.add(JobsPlus.translatable("tooltip.controls.right_click.hoe").withStyle(ChatFormatting.GRAY));
            }
            if (controlType == ControlType.RIGHT_CLICK || controlType == ControlType.HOE) {
                componentList.add(JobsPlus.translatable("tooltip.controls.right_click").withStyle(ChatFormatting.GRAY));
            }
            if (controlType == ControlType.INSERT_EXTRACT) {
                componentList.addAll(List.of(
                        JobsPlus.translatable("tooltip.controls.insert").withStyle(ChatFormatting.GRAY),
                        JobsPlus.translatable("tooltip.controls.extract").withStyle(ChatFormatting.GRAY)));
            }
        }
        return this;
    }

    public TooltipBuilder withMode(String mode) {
        if (shouldShowComponent(ShiftType.NO_SHIFT)) {
            componentList.add(JobsPlus.translatable("tooltip.modes.mode").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withBold(true)).append(ChatColor.white() + mode));
        }
        return this;
    }

    public TooltipBuilder withHoldShift() {
        if (shouldShowComponent(ShiftType.NO_SHIFT)) {
            componentList.add(JobsPlus.translatable("tooltip.hold_shift").withStyle(ChatFormatting.GRAY));
        }
        return this;
    }

    public TooltipBuilder withEnchantments(ItemStack stack, boolean force) {
        if (stack.isEnchanted() || force) {
            componentList.addAll(List.of(
                    WHITE_SPACE,
                    JobsPlus.literal(ChatColor.boldDarkGreen() + "Enchantments:")));
        }
        return this;
    }

    public TooltipBuilder withComponent(Component component, ShiftType shiftType) {
        if (shouldShowComponent(shiftType)) componentList.add(component);
        return this;
    }

    public TooltipBuilder withComponents(List<Component> components, ShiftType shiftType) {
        if (shouldShowComponent(shiftType)) componentList.addAll(components);
        return this;
    }

    private boolean shouldShowComponent(ShiftType shiftType) {
        return shiftType == ShiftType.BOTH || (shiftType == ShiftType.SHIFT && Screen.hasShiftDown()) || (shiftType == ShiftType.NO_SHIFT && !Screen.hasShiftDown());
    }

    public ArrayList<Component> build() {
        return componentList;
    }

    public enum ShiftType {
        SHIFT, NO_SHIFT, BOTH
    }

    public enum ControlType {
        RIGHT_CLICK, INSERT_EXTRACT, HOE
    }

    public enum AboutType {
        MODES, HOE, BOW, AXE, HOE_LVL_4
    }
}
