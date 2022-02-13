package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.CropHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmerEvents {

    public static final ArrayList<BlockPos> blockPosArrayList = new ArrayList<>();
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
            } else {
                if (block == Blocks.CACTUS || block == Blocks.SUGAR_CANE) {
                    if (!blockPosArrayList.contains(event.getPos())) {
                        if (event.getWorld().getBlockState(event.getPos().above()).getBlock() == Blocks.SUGAR_CANE ||
                                event.getWorld().getBlockState(event.getPos().above()).getBlock() == Blocks.CACTUS) {
                            if (!blockPosArrayList.contains(event.getPos().above())) {
                                ExpHandler.addEXPMid(player, job);
                            } else {
                                ExpHandler.addEXPLow(player, job);
                            }
                        } else {
                            ExpHandler.addEXPLow(player, job);
                        }
                    }
                } else if (block.getDescriptionId().equals("block.farmersdelight.tomatoes")) {
                    if (CropHandler.stateToAge(event.getState()) == 7) {
                        ExpHandler.addEXPLow(player, job);
                    }
                } else if (block == Blocks.MELON || block == Blocks.PUMPKIN) {
                    if (!blockPosArrayList.contains(event.getPos())) {
                        ExpHandler.addEXPLow(player, job);
                    }
                } else if (block == Blocks.COCOA) {
                    if (CropHandler.stateToAgeString(event.getState()).startsWith("2")) {
                        ExpHandler.addEXPLow(player, job);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreed(BabyEntitySpawnEvent event) {
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

    @SubscribeEvent
    public void onCropPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                Block block = event.getPlacedBlock().getBlock();
                if (block == Blocks.SUGAR_CANE || block == Blocks.CACTUS || block == Blocks.PUMPKIN || block == Blocks.MELON) {
                    blockPosArrayList.add(event.getPos());
                }
            }
        }
    }
}
