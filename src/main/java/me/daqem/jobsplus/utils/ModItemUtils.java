package me.daqem.jobsplus.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModItemUtils {

    public static void damageItem(int damage, ItemStack stack, Player player) {
        if (!player.isCreative() || !player.isSpectator()) {
            stack.hurtAndBreak(damage, player, player1 -> {
                player1.broadcastBreakEvent(player.getMainHandItem().is(stack.getItem()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            });
        }
    }
}
