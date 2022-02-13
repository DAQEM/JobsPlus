package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SmithEvents {

    private final static ArrayList<String> toolsAndArmorList = new ArrayList<>(List.of("_axe", "_sword", "_pickaxe", "_shovel", "_hoe", "_helmet", "_chestplate", "_leggings", "_boots", "_level_1", "_level_2", "_level_3", "_level_4"));
    private final Jobs job = Jobs.SMITH;
    private final HashMap<Player, Boolean> grindstoneHashMap = new HashMap<>();
    private final HashMap<Player, Integer> furnaceHashmap = new HashMap<>();

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        craftAndRepair(event.getPlayer(), event.getCrafting());
    }

    @SubscribeEvent
    public void onAnvilRepair(AnvilRepairEvent event) {
        craftAndRepair(event.getPlayer(), event.getItemResult());
    }

    public void craftAndRepair(Player player, ItemStack item) {
        if (JobGetters.jobIsEnabled(player, job)) {
            if (!player.isCreative()) {
                for (String itemString : toolsAndArmorList) {
                    if (item.getDescriptionId().endsWith(itemString)) {
                        if (item.getDescriptionId().contains("leather")
                                || item.getDescriptionId().contains("wooden")
                                || item.getDescriptionId().contains("golden")) {
                            ExpHandler.addEXPLowest(player, job);
                        } else if (item.getDescriptionId().contains("iron")
                                || item.getDescriptionId().contains("stone")) {
                            ExpHandler.addEXPMid(player, job);
                        } else if (item.getDescriptionId().contains("diamond")
                                || item.getDescriptionId().contains("netherite")) {
                            ExpHandler.addEXPHigh(player, job);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickGrindstone(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (JobGetters.jobIsEnabled(player, job)) {
            AbstractContainerMenu containerMenu = player.containerMenu;
            if (containerMenu instanceof GrindstoneMenu) {
                if (grindstoneHashMap.containsKey(player) && grindstoneHashMap.get(player)) {
                    boolean firstSlot = false;
                    boolean secondSlot = false;
                    for (Slot slot : containerMenu.slots) {
                        if (!(slot.getItem().getItem() instanceof AirItem || slot.container instanceof Inventory)) {

                            if (slot.getContainerSlot() == 0) {
                                firstSlot = true;
                            }
                            if (slot.getContainerSlot() == 1) {
                                secondSlot = true;
                            }
                        }
                    }
                    if (!firstSlot && !secondSlot) {
                        ExpHandler.addEXPMid(player, job);
                    }
                }
                boolean firstSlot = false;
                boolean secondSlot = false;
                for (Slot slot : containerMenu.slots) {
                    if (!(slot.getItem().getItem() instanceof AirItem || slot.container instanceof Inventory)) {

                        if (slot.getContainerSlot() == 0) {
                            firstSlot = true;
                        }
                        if (slot.getContainerSlot() == 1) {
                            secondSlot = true;
                        }
                    }
                }
                grindstoneHashMap.put(player, firstSlot && secondSlot);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickFurnace(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (JobGetters.jobIsEnabled(player, job)) {
            AbstractContainerMenu containerMenu = player.containerMenu;
            if (containerMenu instanceof FurnaceMenu || containerMenu instanceof BlastFurnaceMenu) {
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
                            ArrayList<String> acceptedItems = new ArrayList<>(List.of("iron_ingot", "gold_ingot", "diamond", "emerald", "copper_ingot", "netherite_scrap"));
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
