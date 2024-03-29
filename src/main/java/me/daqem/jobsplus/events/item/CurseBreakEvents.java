package me.daqem.jobsplus.events.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CurseBreakEvents {

    @SubscribeEvent
    public void onAnvilUpdateCurseBreak(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;
        if (JobGetters.getJobLevel(event.getPlayer(), Jobs.ENCHANTER) >= ModRecipeManager.getRequiredJobLevelServer(ModItems.CURSE_BREAKER.get().getDefaultInstance())) {
            if (!left.isEmpty() && !right.isEmpty() && right.getItem() == ModItems.CURSE_BREAKER.get()) {
                out = left.copy();
                Map<Enchantment, Integer> map = new HashMap<>();
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(out).entrySet()) {
                    if (!entry.getKey().isCurse()) map.put(entry.getKey(), entry.getValue());
                }
                if (EnchantmentHelper.getEnchantments(out).size() == map.size()) return;
                if (out.getItem() == Items.ENCHANTED_BOOK) {
                    ListTag tags = new ListTag();
                    for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                        tags.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(entry.getKey()), entry.getValue()));
                    }
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.put("StoredEnchantments", tags);
                    out.setTag(compoundTag);
                    if (!Objects.equals(event.getName(), "")) {
                        event.setOutput(out.setHoverName(JobsPlus.literal(Objects.requireNonNull(event.getName()))));
                        if (tags.isEmpty())
                            event.setOutput(Items.BOOK.getDefaultInstance().setHoverName(JobsPlus.literal(event.getName())));
                    } else {
                        event.setOutput(out);
                        if (tags.isEmpty()) event.setOutput(Items.BOOK.getDefaultInstance());
                    }

                } else {
                    EnchantmentHelper.setEnchantments(map, out);
                    if (!Objects.equals(event.getName(), ""))
                        event.setOutput(out.setHoverName(JobsPlus.literal(Objects.requireNonNull(event.getName()))));
                    else event.setOutput(out);
                }
                event.setCost(10);
            }
        }
    }
}
