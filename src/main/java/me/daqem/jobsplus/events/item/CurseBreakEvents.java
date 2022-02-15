package me.daqem.jobsplus.events.item;

import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class CurseBreakEvents {

    @SubscribeEvent
    public void onAnvilUpdateCurseBreak(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;
        if (JobGetters.getJobLevel(event.getPlayer(), Jobs.ENCHANTER) >= 10 && !left.isEmpty() && !right.isEmpty() && right.getItem() == ModItems.CURSE_BREAKER.get()) {
            out = left.copy();
            Map<Enchantment, Integer> map = new HashMap<>();
            EnchantmentHelper.deserializeEnchantments(out.getEnchantmentTags()).forEach((key, value) -> {
                if (!key.isCurse()) map.put(key, value);
            });
            EnchantmentHelper.setEnchantments(map, out);
            event.setCost(30);
            event.setOutput(out);
        }
    }
}
