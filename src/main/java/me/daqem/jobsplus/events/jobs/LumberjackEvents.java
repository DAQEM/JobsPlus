package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.item.LumberAxeItem;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LumberjackEvents {

    private final Jobs job = Jobs.LUMBERJACK;

    @SubscribeEvent
    public void onBreakingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.getLevel().isClientSide) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;
        BlockState state = event.getState();
        Block block = state.getBlock();

        //POWERUP 3 (Get better apples from leaves)
        if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP3.get())) {
            if (block == Blocks.OAK_LEAVES || block == Blocks.DARK_OAK_LEAVES) {
                if (Block.getDrops(state, (ServerLevel) player.getLevel(), event.getPos(), null, player, player.getMainHandItem()).isEmpty()) {
                    double random = Math.random() * 100;
                    if (random <= 0.5)
                        ItemHandler.addFreshItemEntity(player.getLevel(), event.getPos(), Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance());
                    else if (random <= 3.5)
                        ItemHandler.addFreshItemEntity(player.getLevel(), event.getPos(), Items.GOLDEN_APPLE.getDefaultInstance());
                }
            }
        }

        if (!state.is(BlockTags.LOGS) && !state.is(BlockTags.PLANKS)) return;

        final BlockPos pos = event.getPos();
        Item itemInHand = player.getMainHandItem().getItem();

        //POWERUP 2 (Haste II on harvesting logs)
        if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 50, 1));
        }

        if (itemInHand instanceof LumberAxeItem lumberAxeItem) {

            //SUPERPOWER (Logs go into your inventory)
            if (JobGetters.hasSuperPowerEnabled(player, job)) {
                event.setCanceled(true);
                lumberAxeItem.mineBlock(player.getMainHandItem(), player.getLevel(), state, pos, player);
                player.getLevel().removeBlock(pos, false);
                ExpHandler.addEXPLow(player, job);
                ItemHandler.addItemsToInventoryOrDrop(new ItemStack(block.asItem()), player, player.getLevel(), pos, 0);
            }
        } else {

            ExpHandler.addEXPLow(player, job);

            //POWERUP 1 (A chance to get a double log drop)
            if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP1.get()) && Math.random() * 100 < 5) {
                //SUPERPOWER (Logs go into your inventory)
                if (JobGetters.hasSuperPowerEnabled(player, job))
                    ItemHandler.addItemsToInventoryOrDrop(new ItemStack(block.asItem()), player, player.getLevel(), pos, 0);
                    //NO SUPERPOWER (Logs DON'T go into your inventory)
                else ItemHandler.addFreshItemEntity(player.getLevel(), pos, block.asItem());
            }
        }
    }

    @SubscribeEvent
    public void onStripLog(BlockEvent.BlockToolModificationEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            if (!JobGetters.jobIsEnabled(serverPlayer, job)) return;
            if (event.getToolAction() != ToolActions.AXE_STRIP) return;
            if (AxeItem.getAxeStrippingState(event.getState()) == null) return;

            ExpHandler.addEXPLow(serverPlayer, job);
        }
    }
}
