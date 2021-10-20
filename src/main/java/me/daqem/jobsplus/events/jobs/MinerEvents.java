package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.BlockPosUtil;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class MinerEvents {

    private final ArrayList<BlockPos> timeoutList = new ArrayList<>();
    public final static ArrayList<String> lowList = new ArrayList<>(List.of("andesite", "diorite", "granite", "calcite", "dripstone", "dripstone_block", "sandstone"));
    public final static ArrayList<String> lowestList = new ArrayList<>(List.of("stone", "deepslate"));

    private final Jobs job = Jobs.MINER;

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                Block block = event.getState().getBlock();
                if (BlockPosUtil.testAllSides(timeoutList, event.getPos())) {
                    if (block instanceof OreBlock) {
                        ExpHandler.addEXPMid(player, job);
                    } else if (lowList.contains(block.getDescriptionId().replace("block.minecraft.", ""))  ) {
                        ExpHandler.addEXPLow(player, job);
                    } else if (lowestList.contains(block.getDescriptionId().replace("block.minecraft.", ""))  ) {
                        ExpHandler.addEXPLowest(player, job);
                    }
                } else {
                    timeoutList.remove(event.getPos());
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (!(timeoutList.size() < 10000)) {
                    timeoutList.remove(0);
                }
                timeoutList.add(event.getPos());
            }
        }
    }
}
