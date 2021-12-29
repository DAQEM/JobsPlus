package me.daqem.jobsplus.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModItemUtils {

    public static void damageItem(int damage, ItemStack stack, Player player) {
        if (!player.isCreative() || !player.isSpectator()) {
            stack.hurtAndBreak(damage, player, player1 -> {
            });
        }
    }
}
