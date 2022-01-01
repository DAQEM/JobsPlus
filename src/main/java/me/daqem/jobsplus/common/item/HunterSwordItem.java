package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.enums.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
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
            if (item == ModItems.HUNTERS_SWORD_LEVEL_1.get()) {
                level = 5;
            }
            if (item == ModItems.HUNTERS_SWORD_LEVEL_2.get()) {
                level = 25;
            }
            if (item == ModItems.HUNTERS_SWORD_LEVEL_3.get()) {
                level = 50;
            }
            if (item == ModItems.HUNTERS_SWORD_LEVEL_4.get()) {
                level = 75;
            }
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Hunter"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getRegistryName()).toString().replace("jobsplus:hunters_sword_level_", "")));
        } else {
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

}
