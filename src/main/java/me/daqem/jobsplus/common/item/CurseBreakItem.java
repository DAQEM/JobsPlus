package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseBreakItem extends Item {

    public CurseBreakItem(Properties properties) {
        super(properties);
    }

    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withAbout(this)
                .withHoldShift()
                .withEnchantments(stack, true)
                .withComponent(JobsPlus.translatable("item.jobsplus.curse_breaker").withStyle(ChatFormatting.GRAY), TooltipBuilder.ShiftType.BOTH)
                .build());
    }
}
