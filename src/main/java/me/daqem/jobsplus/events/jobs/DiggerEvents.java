package me.daqem.jobsplus.events.jobs;

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
public class DiggerEvents {

    private final ArrayList<BlockPos> timeoutList = new ArrayList<>();
    public final static ArrayList<String> lowestList = new ArrayList<>(List.of("dirt", "sand", "gravel"));
    public final static ArrayList<String> lowList = new ArrayList<>(List.of("grass_block", "coarse_dirt", "podzol", "rooted_dirt",
            "red_sand", "soul_sand", "farmland", "soul_soil", "mycelium", "dirt_path", "white_concrete_powder"
            , "orange_concrete_powder", "magenta_concrete_powder", "light_blue_concrete_powder", "yellow_concrete_powder",
            "lime_concrete_powder", "pink_concrete_powder", "gray_concrete_powder", "light_gray_concrete_powder",
            "cyan_concrete_powder", "purple_concrete_powder", "blue_concrete_powder", "brown_concrete_powder",
            "green_concrete_powder", "red_concrete_powder", "black_concrete_powder"));

    private final Jobs job = Jobs.DIGGER;

    @SubscribeEvent
    public void onDiggingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                Block block = event.getState().getBlock();
                if (BlockPosUtil.testAllSides(timeoutList, event.getPos())) {
                    if (lowList.contains(block.getDescriptionId().replace("block.minecraft.", ""))  ) {
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
