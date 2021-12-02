package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class FarmerEvents {

    private final Jobs job = Jobs.FARMER;

    @SubscribeEvent
    public void onCropBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (JobGetters.jobIsEnabled(player, job)) {
            Block block = event.getState().getBlock();
            if (block instanceof CropBlock cropBlock) {
                if (cropBlock.isMaxAge(event.getState())) {
                    ExpHandler.addEXPLow(player, job);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCropBreak(BabyEntitySpawnEvent event) {
        Player player = event.getCausedByPlayer();
        if (player != null) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (!player.isCreative()) {
                    ExpHandler.addEXPHigh(player, job);
                }
            }
        }
    }

    @SubscribeEvent
    public void onSheepShear(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        if (JobGetters.jobIsEnabled(player, job)) {
            if (!player.isCreative()) {
                if (event.getTarget() instanceof Sheep sheep) {
                    if (!sheep.isSheared()) {
                        if (player.getItemInHand(InteractionHand.MAIN_HAND).getDescriptionId().equals("item.minecraft.shears")) {
                            ExpHandler.addEXPLow(player, job);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getPlayer();
        if (JobGetters.jobIsEnabled(player, job)) {
            if (!player.isCreative()) {
                if (event.getCrafting().getDescriptionId().equals("item.minecraft.bread")) {
                    ExpHandler.addEXPLowest(player, job);
                }
            }
        }
    }

}
