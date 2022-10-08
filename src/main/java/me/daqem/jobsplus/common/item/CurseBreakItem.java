package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurseBreakItem extends JobsPlusItem {
    public CurseBreakItem(Properties properties) {
        super(properties);
    }

    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Enchanter"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + getRequiredLevel()));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(Component.literal(ChatColor.gray() + "Combine this item with an item containing a curse in an anvil to remove the curses."));
        } else {
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Enchantments:"));
        tooltip.add(Component.literal(ChatColor.gray() + "Curse Breaker"));
    }
}
