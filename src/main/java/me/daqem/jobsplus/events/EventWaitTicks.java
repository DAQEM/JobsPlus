package me.daqem.jobsplus.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class EventWaitTicks {

    public static void waitTicks(Player player, Type type, Block block, ArrayList<BlockPos> blockPosArray) {
        MinecraftForge.EVENT_BUS.register(new Object() {
            int delay = type.waitTicks;
            int i = 0;

            @SubscribeEvent
            public void onTick(TickEvent.LevelTickEvent event) {
                if (delay-- > 0) return;
                delay = type.waitTicks;
                if (type.isMultiBlockBreak) {
                    if (i < blockPosArray.size()) {
                        BlockPos blockPos = blockPosArray.get(i);
                        BlockState blockState = player.level.getBlockState(blockPos);
                        if (blockState.canHarvestBlock(player.level, blockPos, player)) {
                            ((ServerPlayer) player).gameMode.destroyBlock(blockPos);
                            player.getLevel().levelEvent(2001, blockPos, Block.getId(blockState));
                        } else {
                            MinecraftForge.EVENT_BUS.unregister(this);
                            return;
                        }
                        i++;
                    } else {
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                } else if (type == Type.GIVE_BLOCK_BACK) {
                    player.getInventory().add(new ItemStack(block.asItem(), 1));
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        });
    }

    public enum Type {
        TREE_FELLER(5, true),
        VEIN_MINER(5, true),
        GIVE_BLOCK_BACK(1, false);

        final int waitTicks;
        final boolean isMultiBlockBreak;

        Type(int waitTicks, boolean isMultiBlockBreak) {
            this.waitTicks = waitTicks;
            this.isMultiBlockBreak = isMultiBlockBreak;
        }
    }
}
