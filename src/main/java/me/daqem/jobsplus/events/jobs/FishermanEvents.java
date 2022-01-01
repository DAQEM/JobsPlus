package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FishermanEvents {

    @SubscribeEvent
    public void onItemFished(ItemFishedEvent event) {
        Player player = event.getPlayer();
        Jobs job = Jobs.FISHERMAN;
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                int exp = 0;
                for (ItemStack itemStack : event.getDrops()) {
                    Item item = itemStack.getItem();
                    if (item.getDescriptionId().equals("item.minecraft.tropical_fish")) {
                        ExpHandler.addEXPHighest(player, job);
                    } else {
                        ArrayList<String> fish = new ArrayList<>(List.of("cod", "salmon", "pufferfish"));
                        ArrayList<String> treasure = new ArrayList<>(List.of("bow", "fishing_rod", "name_tag", "enchanted_book", "nautilus_shell", "saddle"));
                        if (fish.contains(item.getDescriptionId().replace("item.minecraft.", ""))) {
                            exp += ExpHandler.getEXPMid();
                        } else if (treasure.contains(item.getDescriptionId().replace("item.minecraft.", ""))) {
                            exp += ExpHandler.getEXPHighest();
                        } else {
                            exp += ExpHandler.getEXPLow();
                        }
                    }
                }
                if (exp != 0) {
                    ExpHandler.addJobEXP(player, job, exp);
                }
            }
        }
    }
}
