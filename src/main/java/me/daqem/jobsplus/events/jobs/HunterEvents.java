package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HunterEvents {

    private final Jobs job = Jobs.HUNTER;
    private final HashMap<Player, Integer> furnaceHashmap = new HashMap<>();

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                if (JobGetters.jobIsEnabled(player, job)) {
                    if (event.getEntity() instanceof Monster || event.getEntity() instanceof Animal || event.getEntity() instanceof WaterAnimal || event.getEntity() instanceof Slime) {
                        if (event.getSource().isProjectile()) {
                            ExpHandler.addEXPHigh(player, job);
                        } else {
                            ExpHandler.addEXPMid(player, job);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickFurnace(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (JobGetters.jobIsEnabled(player, job)) {
            AbstractContainerMenu containerMenu = player.containerMenu;
            if (containerMenu instanceof FurnaceMenu || containerMenu instanceof SmokerMenu) {
                for (Slot slot : containerMenu.slots) {
                    if (!(slot.container instanceof Inventory)) {
                        ItemStack item = slot.getItem();
                        int itemCount = item.getCount();
                        if (slot.getContainerSlot() == 2) {
                            if (furnaceHashmap.containsKey(player)) {
                                if (item.getItem() instanceof AirItem) {
                                    int exp = 0;
                                    for (int i = 0; i < furnaceHashmap.get(player); i++) {
                                        exp += ExpHandler.getEXPLow();
                                    }
                                    ExpHandler.addJobEXP(player, job, exp);
                                    furnaceHashmap.remove(player);
                                } else {
                                    if (itemCount < furnaceHashmap.get(player)) {
                                        int exp = 0;
                                        for (int i = 0; i < furnaceHashmap.get(player) - itemCount; i++) {
                                            exp += ExpHandler.getEXPLow();
                                        }
                                        ExpHandler.addJobEXP(player, job, exp);
                                        furnaceHashmap.put(player, furnaceHashmap.get(player) - itemCount);

                                    }
                                }

                            }
                            ArrayList<String> acceptedItems = new ArrayList<>(List.of("cooked_chicken", "cooked_beef", "cooked_mutton", "cooked_porkchop", "cooked_rabbit"));
                            if (acceptedItems.contains(item.getItem().getDescriptionId().replace("item.minecraft.", ""))) {
                                furnaceHashmap.put(player, itemCount);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onInventoryClose(PlayerContainerEvent.Close event) {
        furnaceHashmap.remove(event.getPlayer());
    }
}
