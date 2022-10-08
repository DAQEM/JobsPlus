package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModArmorItem extends JobsPlusItem.Armor {

    public ModArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Smith"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + getRequiredLevel()));
        } else {
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
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
        return this == ModItems.REINFORCED_NETHERITE_HELMET.get() || this == ModItems.REINFORCED_NETHERITE_CHESTPLATE.get()
                || this == ModItems.REINFORCED_NETHERITE_LEGGINGS.get() || this == ModItems.REINFORCED_NETHERITE_BOOTS.get();
    }
}
