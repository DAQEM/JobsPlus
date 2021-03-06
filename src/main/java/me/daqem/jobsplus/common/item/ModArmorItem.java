package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
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
            int level = 0;
            Item item = stack.getItem();
            if (item == ModItems.REINFORCED_IRON_HELMET.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_IRON_HELMET.get();
            if (item == ModItems.REINFORCED_IRON_BOOTS.get()) level = Config.REQUIRED_LEVEL_REINFORCED_IRON_BOOTS.get();
            if (item == ModItems.OBSIDIAN_HELMET.get()) level = Config.REQUIRED_LEVEL_OBSIDIAN_HELMET.get();
            if (item == ModItems.OBSIDIAN_BOOTS.get()) level = Config.REQUIRED_LEVEL_OBSIDIAN_BOOTS.get();
            if (item == ModItems.REINFORCED_IRON_LEGGINGS.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_IRON_LEGGINGS.get();
            if (item == ModItems.OBSIDIAN_LEGGINGS.get()) level = Config.REQUIRED_LEVEL_OBSIDIAN_LEGGINGS.get();
            if (item == ModItems.REINFORCED_IRON_CHESTPLATE.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_IRON_CHESTPLATE.get();
            if (item == ModItems.REINFORCED_DIAMOND_HELMET.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_HELMET.get();
            if (item == ModItems.REINFORCED_DIAMOND_BOOTS.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_BOOTS.get();
            if (item == ModItems.OBSIDIAN_CHESTPLATE.get()) level = Config.REQUIRED_LEVEL_OBSIDIAN_CHESTPLATE.get();
            if (item == ModItems.REINFORCED_DIAMOND_LEGGINGS.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_LEGGINGS.get();
            if (item == ModItems.REINFORCED_DIAMOND_CHESTPLATE.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_DIAMOND_CHESTPLATE.get();
            if (item == ModItems.REINFORCED_NETHERITE_HELMET.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_HELMET.get();
            if (item == ModItems.REINFORCED_NETHERITE_BOOTS.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_BOOTS.get();
            if (item == ModItems.REINFORCED_NETHERITE_LEGGINGS.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_LEGGINGS.get();
            if (item == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get())
                level = Config.REQUIRED_LEVEL_REINFORCED_NETHERITE_CHESTPLATE.get();
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Smith"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
        } else {
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack leftItem, ItemStack rightItem) {
        if (rightItem.getItem() == Items.IRON_BLOCK) {
            return leftItem.getItem() == ModItems.REINFORCED_IRON_HELMET.get()
                    || leftItem.getItem() == ModItems.REINFORCED_IRON_CHESTPLATE.get()
                    || leftItem.getItem() == ModItems.REINFORCED_IRON_LEGGINGS.get()
                    || leftItem.getItem() == ModItems.REINFORCED_IRON_BOOTS.get();
        } else if (rightItem.getItem() == Items.OBSIDIAN) {
            return leftItem.getItem() == ModItems.OBSIDIAN_HELMET.get()
                    || leftItem.getItem() == ModItems.OBSIDIAN_CHESTPLATE.get()
                    || leftItem.getItem() == ModItems.OBSIDIAN_LEGGINGS.get()
                    || leftItem.getItem() == ModItems.OBSIDIAN_BOOTS.get();
        } else if (rightItem.getItem() == Items.DIAMOND_BLOCK) {
            return leftItem.getItem() == ModItems.REINFORCED_DIAMOND_HELMET.get()
                    || leftItem.getItem() == ModItems.REINFORCED_DIAMOND_CHESTPLATE.get()
                    || leftItem.getItem() == ModItems.REINFORCED_DIAMOND_LEGGINGS.get()
                    || leftItem.getItem() == ModItems.REINFORCED_DIAMOND_BOOTS.get();
        } else if (rightItem.getItem() == Items.NETHERITE_BLOCK) {
            return leftItem.getItem() == ModItems.REINFORCED_NETHERITE_HELMET.get()
                    || leftItem.getItem() == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get()
                    || leftItem.getItem() == ModItems.REINFORCED_NETHERITE_LEGGINGS.get()
                    || leftItem.getItem() == ModItems.REINFORCED_NETHERITE_BOOTS.get();
        }
        return false;
    }

    @Override
    public boolean isFireResistant() {
        return this == ModItems.REINFORCED_NETHERITE_HELMET.get()
                || this == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get()
                || this == ModItems.REINFORCED_NETHERITE_LEGGINGS.get()
                || this == ModItems.REINFORCED_NETHERITE_BOOTS.get();
    }
}
