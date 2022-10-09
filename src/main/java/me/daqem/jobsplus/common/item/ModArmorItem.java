package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.enums.Jobs;
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
        super(material, slot, properties, Jobs.SMITH);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(getJob(), getRequiredLevel())
                .withHoldShift()
                .build());
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack leftItem, @NotNull ItemStack rightItem) {
        if (rightItem.is(Items.IRON_BLOCK)) {
            return leftItem.is(ModItems.REINFORCED_IRON_HELMET.get())
                    || leftItem.is(ModItems.REINFORCED_IRON_CHESTPLATE.get())
                    || leftItem.is(ModItems.REINFORCED_IRON_LEGGINGS.get())
                    || leftItem.is(ModItems.REINFORCED_IRON_BOOTS.get());
        } else if (rightItem.is(Items.OBSIDIAN)) {
            return leftItem.is(ModItems.OBSIDIAN_HELMET.get())
                    || leftItem.is(ModItems.OBSIDIAN_CHESTPLATE.get())
                    || leftItem.is(ModItems.OBSIDIAN_LEGGINGS.get())
                    || leftItem.is(ModItems.OBSIDIAN_BOOTS.get());
        } else if (rightItem.is(Items.DIAMOND_BLOCK)) {
            return leftItem.is(ModItems.REINFORCED_DIAMOND_HELMET.get())
                    || leftItem.is(ModItems.REINFORCED_DIAMOND_CHESTPLATE.get())
                    || leftItem.is(ModItems.REINFORCED_DIAMOND_LEGGINGS.get())
                    || leftItem.is(ModItems.REINFORCED_DIAMOND_BOOTS.get());
        } else if (rightItem.is(Items.NETHERITE_BLOCK)) {
            return leftItem.is(ModItems.REINFORCED_NETHERITE_HELMET.get())
                    || leftItem.is(ModItems.REINFORCED_NETHERITE_CHESTPLATE.get())
                    || leftItem.is(ModItems.REINFORCED_NETHERITE_LEGGINGS.get())
                    || leftItem.is(ModItems.REINFORCED_NETHERITE_BOOTS.get());
        }
        return false;
    }

    @Override
    public boolean isFireResistant() {
        return isValidRepairItem(this.getDefaultInstance(), Items.NETHERITE_BLOCK.getDefaultInstance());
    }
}
