package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class AlchemistEvents {

    private final ArrayList<ItemStack> itemStackArrayList = new ArrayList<>();

    //  ON POTION BREW
    @SubscribeEvent
    public void onPotionBrewedTake(PlayerBrewedPotionEvent event) {
        Player player = event.getPlayer();
        if (itemStackArrayList.contains(event.getStack())) {
            itemStackArrayList.remove(event.getStack());
            if (!player.isCreative()) {
                if (JobGetters.jobIsEnabled(player, Jobs.ALCHEMIST)) {
                    ExpHandler.addEXPHigh(player, Jobs.ALCHEMIST);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPotionBrewed(PotionBrewEvent.Post event) {
        if (!(event.getItem(0).getItem() instanceof AirItem)) {
            itemStackArrayList.add(event.getItem(0));
        }
        if (!(event.getItem(1).getItem() instanceof AirItem)) {
            itemStackArrayList.add(event.getItem(1));
        }
        if (!(event.getItem(2).getItem() instanceof AirItem)) {
            itemStackArrayList.add(event.getItem(2));
        }
    }

    //  ON POTION DRINK
    @SubscribeEvent
    public void onPotionConsume(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && event.getItem().getItem() instanceof PotionItem potionItem) {
            if (JobGetters.jobIsEnabled(player, Jobs.ALCHEMIST)) {
                if (!(potionItem.getDescriptionId(event.getItem()).equals("Water Bottle"))) {
                    if (!player.isCreative()) {
                        if (!player.level.isClientSide()) {
                            ExpHandler.addEXPMid(player, Jobs.ALCHEMIST);
                        }
                    }
                }
            }
        }
    }

    //  ON SPLASH POTION THROW
    @SubscribeEvent
    public void onPotionThrow(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof Player player && event.getItemStack().getItem() instanceof SplashPotionItem splashPotionItem) {
            if (JobGetters.jobIsEnabled(player, Jobs.ALCHEMIST)) {
                if (!(splashPotionItem.getDescriptionId(event.getItemStack()).equals("Water Bottle"))) {
                    if (!player.isCreative()) {
                        if (event.getSide() == LogicalSide.SERVER) {
                            ExpHandler.addEXPMid(player, Jobs.ALCHEMIST);
                        }
                    }
                }
            }
        }
    }

    //  ON CRAFT POTION
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof Player player && !player.level.isClientSide()) {
            if (!player.isCreative()) {
                if (JobGetters.jobIsEnabled(player, Jobs.ALCHEMIST)) {
                    Item item = event.getCrafting().getItem();
                    String itemName = item.getDescriptionId();
                    ArrayList<String> items = new ArrayList<>(List.of("item.minecraft.fermented_spider_eye",
                            "item.minecraft.blaze_powder", "item.minecraft.glistering_melon_slice",
                            "item.minecraft.magma_cream", "item.minecraft.golden_carrot", "item.minecraft.turtle_helmet"));
                    if (items.contains(itemName)) {
                        ExpHandler.addEXPLowest(player, Jobs.ALCHEMIST);
                    } else {
                        System.out.println(items);
                        System.out.println(itemName);
                    }
                }
            }
        }
    }

    //  ON KILL ENTITY
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                if (JobGetters.jobIsEnabled(player, Jobs.ALCHEMIST)) {
                    Entity target = event.getEntity();
                    if (target instanceof Phantom || target instanceof Rabbit) {
                        ExpHandler.addEXPMid(player, Jobs.ALCHEMIST);
                    }
                    if (target instanceof Ghast || target instanceof Pufferfish) {
                        ExpHandler.addEXPHigh(player, Jobs.ALCHEMIST);
                    }
                }
            }
        }
    }

    //  ON NETHER WART HARVEST
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getState().getBlock();
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, Jobs.ALCHEMIST)) {
                if (block instanceof NetherWartBlock) {
                    if (CropHandler.stateToAge(event.getState()) == 3) {
                        ExpHandler.addEXPLow(player, Jobs.ALCHEMIST);
                    }
                }
            }
        }
    }
}