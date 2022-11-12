package me.daqem.jobsplus.events;

import me.daqem.jobsplus.events.jobs.MinerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class EventWaitTicks {

    public static void waitTicks(Player player, Type type, Object[] objects) {
        MinecraftForge.EVENT_BUS.register(new Object() {
            int delay = type.waitTicks;
            int i = 0;

            @SubscribeEvent
            public void onTick(TickEvent.WorldTickEvent event) {
                if (delay-- > 0) return;
                delay = type.waitTicks;
                if (type.isMultiBlockBreak) {
                    @SuppressWarnings("unchecked")
                    ArrayList<BlockPos> blockPosArray = (ArrayList<BlockPos>) objects[0];
                    if (i < blockPosArray.size()) {
                        BlockPos blockPos = blockPosArray.get(i);
                        BlockState blockState = player.level.getBlockState(blockPos);
                        if (blockState.canHarvestBlock(player.level, blockPos, player)) {
                            ((ServerPlayer) player).gameMode.destroyBlock(blockPos);
                            player.getLevel().levelEvent(2001, blockPos, Block.getId(blockState));
                        } else {
                            MinecraftForge.EVENT_BUS.unregister(this);
                            if (!MinerEvents.veinMinerArray.contains(player.getUUID())) {
                                MinerEvents.veinMinerArray.add(player.getUUID());
                            }
                            return;
                        }
                        i++;
                    } else {
                        MinecraftForge.EVENT_BUS.unregister(this);
                        if (!MinerEvents.veinMinerArray.contains(player.getUUID())) {
                            MinerEvents.veinMinerArray.add(player.getUUID());
                        }
                    }
                } else if (type == Type.GIVE_BLOCK_BACK) {
                    Block block = (Block) objects[0];
                    player.getInventory().add(new ItemStack(block.asItem(), 1));
                    MinecraftForge.EVENT_BUS.unregister(this);
                } else if (type == Type.POTION) {
                    MobEffectInstance effect = (MobEffectInstance) objects[0];
                    int duration = (int) objects[1];
                    player.forceAddEffect(new MobEffectInstance(effect.getEffect(), duration,
                            effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()), null);
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        });
    }

    public enum Type {
        TREE_FELLER(5, true),
        VEIN_MINER(5, true),
        GIVE_BLOCK_BACK(1, false),
        POTION(1, false);

        final int waitTicks;
        final boolean isMultiBlockBreak;

        Type(int waitTicks, boolean isMultiBlockBreak) {
            this.waitTicks = waitTicks;
            this.isMultiBlockBreak = isMultiBlockBreak;
        }
    }
}
