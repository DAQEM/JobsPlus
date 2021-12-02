package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.BlockPosUtil;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class LumberjackEvents {

    private final ArrayList<BlockPos> timeoutList = new ArrayList<>();
    public final static ArrayList<String> lowList = new ArrayList<>(List.of("oak_log", "jungle_log", "spruce_log", "birch_log", "dark_oak_log", "acacia_log", "warped_stem", "crimson_stem"));

    private final Jobs job = Jobs.LUMBERJACK;

    @SubscribeEvent
    public void onDiggingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                Block block = event.getState().getBlock();
                if (BlockPosUtil.testAllSides(timeoutList, event.getPos())) {
                    if (lowList.contains(block.getDescriptionId().replace("block.minecraft.", ""))  ) {
                        ExpHandler.addEXPLow(player, job);
                    } else {
                        JobsPlus.LOGGER.info(block.getDescriptionId());
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
