package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.item.LumberAxeItem;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LumberjackEvents {

    private final Jobs job = Jobs.LUMBERJACK;

    @SubscribeEvent
    public void onDiggingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.getLevel().isClientSide) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;
        BlockState state = event.getState();
        Block block = state.getBlock();
        if (JobGetters.hasEnabledPowerup(player, job, CapType.POWERUP3.get())) {
            if (block == Blocks.OAK_LEAVES || block == Blocks.DARK_OAK_LEAVES) {
                if (Block.getDrops(state, (ServerLevel) player.getLevel(), event.getPos(), null, player, player.getMainHandItem()).isEmpty()) {
                    double random = Math.random() * 100;
                    if (random < 1)
                        ItemHandler.addFreshItemEntity(player.getLevel(), event.getPos(), Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance());
                    else if (random < 3)
                        ItemHandler.addFreshItemEntity(player.getLevel(), event.getPos(), Items.GOLDEN_APPLE.getDefaultInstance());
                }
            }
        }
        if (!state.is(BlockTags.LOGS)) return;
        final BlockPos pos = event.getPos();
        Item itemInHand = player.getMainHandItem().getItem();
        if (JobGetters.hasEnabledPowerup(player, job, CapType.POWERUP2.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 50, 1));
        }
        if (itemInHand instanceof LumberAxeItem lumberAxeItem) {
            if (JobGetters.hasSuperPowerEnabled(player, job)) {
                event.setCanceled(true);
                lumberAxeItem.mineBlock(player.getMainHandItem(), player.getLevel(), state, pos, player);
                player.getLevel().removeBlock(pos, false);
                ExpHandler.addEXPLow(player, Jobs.LUMBERJACK);
                ItemHandler.addItemsToInventoryOrDrop(new ItemStack(block.asItem()), player, player.getLevel(), pos, 0);
            }
        } else {
            ExpHandler.addEXPLow(player, job);
            if (JobGetters.hasEnabledPowerup(player, job, CapType.POWERUP1.get()) && Math.random() * 100 < 5) {
                if (JobGetters.hasSuperPowerEnabled(player, job))
                    ItemHandler.addItemsToInventoryOrDrop(new ItemStack(block.asItem()), player, player.getLevel(), pos, 0);
                else ItemHandler.addFreshItemEntity(player.getLevel(), pos, block.asItem());
            }
        }
    }
}
