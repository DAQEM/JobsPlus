package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.enums.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArmorItem extends ArmorItem {

    public ModArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (Screen.hasShiftDown()) {
            String level = "";
            Item item = stack.getItem();
            if (item == ModItems.REINFORCED_IRON_HELMET.get()
                    || item == ModItems.REINFORCED_IRON_BOOTS.get()) {
                level = "1";
            }
            if (item == ModItems.OBSIDIAN_HELMET.get()
                    || item == ModItems.OBSIDIAN_BOOTS.get()
                    || item == ModItems.REINFORCED_IRON_LEGGINGS.get()) {
                level = "5";
            }
            if (item == ModItems.OBSIDIAN_LEGGINGS.get()
                    || item == ModItems.REINFORCED_IRON_CHESTPLATE.get()) {
                level = "10";
            }
            if (item == ModItems.REINFORCED_DIAMOND_HELMET.get()
                    || item == ModItems.REINFORCED_DIAMOND_BOOTS.get()
                    || item == ModItems.OBSIDIAN_CHESTPLATE.get()) {
                level = "20";
            }
            if (item == ModItems.REINFORCED_DIAMOND_LEGGINGS.get()) {
                level = "35";
            }
            if (item == ModItems.REINFORCED_DIAMOND_CHESTPLATE.get()
                    || item == ModItems.REINFORCED_NETHERITE_HELMET.get()
                    || item == ModItems.REINFORCED_NETHERITE_BOOTS.get()) {
                level = "50";
            }
            if (item == ModItems.REINFORCED_NETHERITE_LEGGINGS.get()) {
                level = "75";
            }
            if (item == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get()) {
                level = "100";
            }
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Smith"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
        } else {
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
    }
}
