package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuilderEvents {

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                if (JobGetters.jobIsEnabled(player, Jobs.BUILDER)) {
                    Block block = event.getPlacedBlock().getBlock();
                    ArrayList<String> bannedBlocks = new ArrayList<>(List.of("stone", "cobblestone", "granite", "diorite",
                            "andesite", "polished_granite", "polished_diorite", "polished_andesite", "dirt", "grass_block",
                            "calcite", "tuff", "coarse_dirt", "sand"));
                    if (!bannedBlocks.contains(block.getDescriptionId().replace("block.minecraft.", "")) && event.getPlacedBlock().getMaterial().isSolid()) {
                        float destroySpeed = event.getPlacedBlock().getDestroySpeed(player.level, event.getPos());
                        if (destroySpeed <= 2.5) {
                            ExpHandler.addEXPLowest(player, Jobs.BUILDER);
                        }
                        if (destroySpeed <= 5) {
                            ExpHandler.addEXPLow(player, Jobs.BUILDER);
                        } else {
                            ExpHandler.addEXPMid(player, Jobs.BUILDER);
                        }
                    }

                }
            }
        }
    }
}
