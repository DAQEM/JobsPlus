package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
                    if (item == Items.TROPICAL_FISH) {
                        exp += 25;
                    } else {
                        ArrayList<Item> treasure = new ArrayList<>(List.of(Items.BOW, Items.FISHING_ROD, Items.NAME_TAG, Items.ENCHANTED_BOOK, Items.NAUTILUS_SHELL, Items.SADDLE));
                        if (ItemTags.FISHES.contains(item.asItem())) {
                            exp += ExpHandler.getEXPHigh();
                        } else if (treasure.contains(item)) {
                            exp += ExpHandler.getEXPFishing();
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
