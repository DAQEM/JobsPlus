package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class HunterSwordItem extends SwordItem {

    public HunterSwordItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            int level = 0;
            Item item = stack.getItem();
            if (item == ModItems.HUNTERS_SWORD_LEVEL_1.get()) level = Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_1.get();
            if (item == ModItems.HUNTERS_SWORD_LEVEL_2.get()) level = Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_2.get();
            if (item == ModItems.HUNTERS_SWORD_LEVEL_3.get()) level = Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_3.get();
            if (item == ModItems.HUNTERS_SWORD_LEVEL_4.get()) level = Config.REQUIRED_LEVEL_HUNTERS_SWORD_LEVEL_4.get();
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Hunter"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(Component.literal(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getDescriptionId()).replace("item.jobsplus.hunters_sword_level_", "")));
        } else {
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

}
