package me.daqem.jobsplus.events.tools;

import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class RodEvents {

    @SubscribeEvent
    public void onFish(ItemFishedEvent event) {
        if (event.getHookEntity() instanceof ModFishingHook) {
            Item item = event.getPlayer().getMainHandItem().getItem();
            Random random = new Random();
            int num = (int) (random.nextDouble() * 100);
            if (num == 0) num = 100;
            if (item == ModItems.FISHERMANS_ROD_LEVEL_1.get()) {
                if (num <= 10) {
//                    event.set
                }
            }
            if (item == ModItems.FISHERMANS_ROD_LEVEL_2.get()) {
                if (num <= 10) {

                }
                if (num > 10 && num <= 17) {

                }
            }
            if (item == ModItems.FISHERMANS_ROD_LEVEL_3.get()) {
                if (num <= 10) {

                }
                if (num > 10 && num <= 17) {

                }
                if (num > 17 && num <= 22) {

                }
            }
            if (item == ModItems.FISHERMANS_ROD_LEVEL_4.get()) {
                if (num <= 10) {

                }
                if (num > 10 && num <= 17) {

                }
                if (num > 17 && num <= 22) {

                }
                if (num > 22 && num <= 25) {

                }
            }
        }
    }

//    public void dropsHandler(int amount,) {
//
//    }
}
