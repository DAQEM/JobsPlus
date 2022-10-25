package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AlchemistEvents {

    private static final Jobs JOB = Jobs.ALCHEMIST;
    private static final ArrayList<ItemStack> itemStackArrayList = new ArrayList<>();
    private static final String WATER = "potion.effect.water";
    private static final ArrayList<Item> MATERIALS = new ArrayList<>(List.of(Items.SPIDER_EYE, Items.BLAZE_POWDER,
            Items.GLISTERING_MELON_SLICE, Items.MAGMA_CREAM, Items.GOLDEN_CARROT, Items.TURTLE_HELMET));

    //  ON POTION BREW
    @SubscribeEvent
    public void onPotionBrewedTake(PlayerBrewedPotionEvent event) {
        Player player = event.getEntity();
        if (itemStackArrayList.contains(event.getStack())) {
            itemStackArrayList.remove(event.getStack());
            if (JobGetters.jobIsEnabled(player, JOB)) ExpHandler.addEXPBrewing(player, JOB);
        }
    }

    @SubscribeEvent
    public void onPotionBrewed(PotionBrewEvent.Post event) {
        for (int i = 0; i < 3; i++) {
            if (!(event.getItem(i).getItem() instanceof AirItem)) itemStackArrayList.add(event.getItem(i));
        }
    }

    //  ON POTION DRINK
    @SubscribeEvent
    public void onPotionConsume(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && event.getItem().getItem() instanceof PotionItem potionItem) {
            if (JobGetters.jobIsEnabled(serverPlayer, JOB)) {
                if (!(potionItem.getDescriptionId().contains(WATER))) ExpHandler.addEXPMid(serverPlayer, JOB);
            }
        }
    }

    //  ON SPLASH POTION THROW
    @SubscribeEvent
    public void onPotionThrow(PlayerInteractEvent.RightClickItem event) {
        final Item item = event.getItemStack().getItem();
        if (event.getEntity() instanceof ServerPlayer serverPlayer && (item instanceof SplashPotionItem || item instanceof LingeringPotionItem)) {
            if (JobGetters.jobIsEnabled(serverPlayer, JOB)) {
                if (!(item.getDescriptionId().contains(WATER))) ExpHandler.addEXPMid(serverPlayer, JOB);
            }
        }
    }

    //  ON CRAFT POTION
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (JobGetters.jobIsEnabled(serverPlayer, JOB)) {
                if (MATERIALS.contains(event.getCrafting().getItem())) ExpHandler.addEXPLowest(serverPlayer, JOB);
            }
        }
    }

    //  ON KILL ENTITY
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            if (JobGetters.jobIsEnabled(serverPlayer, JOB)) {
                Entity target = event.getEntity();
                if (target instanceof Phantom || target instanceof Rabbit) ExpHandler.addEXPMid(serverPlayer, JOB);
                if (target instanceof Ghast || target instanceof Pufferfish) ExpHandler.addEXPHigh(serverPlayer, JOB);
            }
        }
    }

    //  ON NETHER WART HARVEST
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.level.isClientSide) return;
        if (JobGetters.jobIsEnabled(player, JOB) && event.getState().getBlock() instanceof NetherWartBlock) {
            if (CropHandler.stateToAge(event.getState()) == 3) ExpHandler.addEXPLow(player, JOB);
        }
    }
}
