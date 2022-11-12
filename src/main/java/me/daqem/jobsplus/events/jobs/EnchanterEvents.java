package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
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
    private final ArrayList<Player> matrixList = new ArrayList<>();

    @SubscribeEvent
    public void onEnchant(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu instanceof EnchantmentMenu) {
            if (JobGetters.jobIsEnabled(player, job)) {
                int lapisCount = containerMenu.getSlot(1).getItem().getCount();
                if (multiMap.containsKey(player)) {
                    if (containerMenu.getSlot(0).getItem().isEnchanted() || containerMenu.getSlot(0).getItem().getOrCreateTag().contains("StoredEnchantments")) {
                        if (lapisCount == 0) return;
                        Integer mapInt = (Integer) multiMap.get(player).get(1);
                        if (mapInt > lapisCount) {
                            int lapisUsed = mapInt - lapisCount;
                            if (lapisUsed == 1) {
                                ExpHandler.addEXPOneLapis(player, job);
                            }
                            if (lapisUsed == 2) {
                                ExpHandler.addEXPTwoLapis(player, job);
                            }
                            if (lapisUsed == 3) {
                                ExpHandler.addEXPTheeLapis(player, job);
                            }
                            multiMap.remove(player);
                            final ItemStack enchantedItem = containerMenu.getSlot(0).getItem();
                            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true)) {
                                if (enchantedItem.isEnchanted() || enchantedItem.is(Items.ENCHANTED_BOOK)) {
                                    Map<Enchantment, Integer> newMap = new HashMap<>();
                                    for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(enchantedItem).entrySet()) {
                                        newMap.put(entry.getKey(), entry.getKey().getMaxLevel() == 1 ? entry.getValue() : entry.getValue() + 1);
                                    }
                                    EnchantmentHelper.setEnchantments(newMap, enchantedItem);
                                    containerMenu.getSlot(0).set(enchantedItem);
                                    containerMenu.getSlot(0).setChanged();
                                }
                            }
                        }
                    }
                }
                if (!containerMenu.getSlot(0).getItem().isEnchanted() && !containerMenu.getSlot(0).getItem().getOrCreateTag().contains("StoredEnchantments")) {
                    multiMap.put(player, new ArrayList<>(List.of(false, lapisCount)));
                }
            } else if (containerMenu.toString().contains("MatrixEnchantingMenu")) {
                if (containerMenu.getSlot(0).hasItem() && containerMenu.getSlot(2).hasItem()) {
                    if (!this.matrixList.contains(player)) this.matrixList.add(player);
                } else {
                    if (matrixList.contains(player)) {
                        if (!containerMenu.getSlot(0).hasItem() && !containerMenu.getSlot(2).hasItem()) {
                            if (containerMenu.getCarried().isEnchanted()) {
                                ExpHandler.addEXPTwoLapis(player, job);
                            }
                        }
                    }
                    this.matrixList.remove(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerEXPPickup(PlayerXpEvent.PickupXp event) {
        if (JobGetters.hasPowerupEnabled(event.getEntity(), Jobs.ENCHANTER, CapType.POWER_UP3.get(), true)) {
            event.getOrb().value = event.getOrb().value * 2;
        }
    }

    @SubscribeEvent
    public void onPlayerTickGrindstone(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu instanceof GrindstoneMenu grindstoneMenu) {
            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP1.get(), true)) {
                final Slot slot = grindstoneMenu.getSlot(2);
                final ItemStack item = slot.getItem();
                Map<Enchantment, Integer> map = new HashMap<>();
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.deserializeEnchantments(item.getEnchantmentTags()).entrySet()) {
                    if (!entry.getKey().isCurse()) map.put(entry.getKey(), entry.getValue());
                }
                EnchantmentHelper.setEnchantments(map, item);
                slot.set(item);
                slot.setChanged();
            }
        }
    }

    @SubscribeEvent
    public void onAnvilUpdateCurseBreak(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;
        if (!(left.getDescriptionId().contains("item.minecraft.") || right.getDescriptionId().contains("item.minecraft.")
                || left.getDescriptionId().contains("item.jobsplus.") || right.getDescriptionId().contains("item.jobsplus.")))
            return;
        if (!JobGetters.hasSuperPowerEnabled(event.getPlayer(), job, true) || left.isEmpty() || right.isEmpty()) return;
        if (left.getItem() != right.getItem()) return;

        final Map<Enchantment, Integer> leftEnchantments = EnchantmentHelper.getEnchantments(left);
        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(right).entrySet()) {

            if (!leftEnchantments.containsKey(entry.getKey())) {
                leftEnchantments.put(entry.getKey(), entry.getValue());
            } else {
                leftEnchantments.put(entry.getKey(), Math.min(entry.getValue() + leftEnchantments.get(entry.getKey()), entry.getKey().getMaxLevel() + 1));
            }
        }
        out = left.copy();
        EnchantmentHelper.setEnchantments(leftEnchantments, out);
        event.setOutput(out);
        event.setCost(3);
    }

    @SubscribeEvent
    public void onAddEnchantUsingAnvil(AnvilRepairEvent event) {
        final Player player = event.getEntity();
        if (player.level.isClientSide) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;
        ExpHandler.addEXPMid(player, job);
    }
}
