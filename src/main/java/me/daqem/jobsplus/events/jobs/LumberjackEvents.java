package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.item.LumberAxeItem;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.BlockPosUtil;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LumberjackEvents {

    private final ArrayList<BlockPos> timeoutList = new ArrayList<>();
    private final Jobs job = Jobs.LUMBERJACK;

    @SubscribeEvent
    public void onDiggingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (!(player.getMainHandItem().getItem() instanceof LumberAxeItem)) {
                    Block block = event.getState().getBlock();
                    if (BlockPosUtil.testAllSides(timeoutList, event.getPos())) {
                        if (block.getTags().contains(BlockTags.LOGS.getName())
                                || block.getTags().contains(BlockTags.CRIMSON_STEMS.getName())
                                || block.getTags().contains(BlockTags.WARPED_STEMS.getName())) {
                            ExpHandler.addEXPLow(player, job);
                        }
                    } else {
                        timeoutList.remove(event.getPos());
                    }
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
