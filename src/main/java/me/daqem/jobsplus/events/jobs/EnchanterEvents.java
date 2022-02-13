package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchanterEvents {

    private final Map<Player, ArrayList<Object>> multiMap = new HashMap<>();
    private final Jobs job = Jobs.ENCHANTER;

    @SubscribeEvent
    public void onEnchant(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (JobGetters.jobIsEnabled(player, job)) {
            AbstractContainerMenu containerMenu = player.containerMenu;
            if (containerMenu instanceof EnchantmentMenu) {
                int lapisCount = containerMenu.getSlot(1).getItem().getCount();
                if (multiMap.containsKey(player)) {
                    if (containerMenu.getSlot(0).getItem().isEnchanted() || containerMenu.getSlot(0).getItem().getOrCreateTag().contains("StoredEnchantments")) {
                        if (lapisCount == 0) return;
                        Integer mapInt = (Integer) multiMap.get(player).get(1);
                        if (mapInt > lapisCount) {
                            int lapisUsed = mapInt - lapisCount;
                            if (lapisUsed == 1) {
                                ExpHandler.addEXPLow(player, job);
                            }
                            if (lapisUsed == 2) {
                                ExpHandler.addEXPMid(player, job);
                            }
                            if (lapisUsed == 3) {
                                ExpHandler.addEXPHighest(player, job);
                            }
                            multiMap.remove(player);
                        }
                    }
                }
                if (!containerMenu.getSlot(0).getItem().isEnchanted() && !containerMenu.getSlot(0).getItem().getOrCreateTag().contains("StoredEnchantments")) {
                    multiMap.put(player, new ArrayList<>(List.of(false, lapisCount)));
                }
            }
        }
    }
}
