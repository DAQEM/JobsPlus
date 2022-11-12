package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.item.LumberAxeItem;
import me.daqem.jobsplus.events.EventWaitTicks;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.ChatColor;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LumberjackEvents {

    private final Jobs job = Jobs.LUMBERJACK;

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        final BlockState state = event.getState();
        if (player.getLevel().isClientSide) return;
        if (!state.is(Blocks.OAK_LEAVES) && !state.is(Blocks.DARK_OAK_LEAVES)
                && !state.is(BlockTags.LOGS) && !state.is(BlockTags.PLANKS)) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        final Block block = state.getBlock();

        powerUpBetterApples(event, player, state);

        if (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS)) {
            final BlockPos pos = event.getPos();

            ExpHandler.addEXPLow(player, job);

            powerUpHaste(player);
            if (!superpowerAddToInventory(event, player, block, pos)) {
                attemptFellTree(player.level, pos, player);
            }
            powerupDoubleLogs(player, block, pos);
        }
    }

    private void powerupDoubleLogs(Player player, Block block, BlockPos pos) {
        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP1.get(), true) && Math.random() * 100 < 5) {
            //SUPERPOWER (Logs go into your inventory)
            if (JobGetters.hasSuperPowerEnabled(player, job, true))
                ItemHandler.addItemsToInventoryOrDrop(new ItemStack(block.asItem()), player, player.getLevel(), pos, 0);
                //NO SUPERPOWER (Logs DON'T go into your inventory)
            else ItemHandler.addFreshItemEntity(player.getLevel(), pos, block.asItem());
        }
    }

    private boolean superpowerAddToInventory(BlockEvent.BreakEvent event, Player player, Block block, BlockPos pos) {
        if (JobGetters.hasSuperPowerEnabled(player, job, true)) {
            event.setCanceled(true);
            attemptFellTree(player.level, pos, player);
            player.getLevel().removeBlock(pos, false);
            ItemHandler.addItemsToInventoryOrDrop(new ItemStack(block.asItem()), player, player.getLevel(), pos, 0);
            return true;
        }
        return false;
    }

    private void powerUpHaste(Player player) {
        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 50, 1));
        }
    }

    private void powerUpBetterApples(BlockEvent.BreakEvent event, Player player, BlockState state) {
        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP3.get(), true)) {
            if (state.getBlock() == Blocks.OAK_LEAVES || state.getBlock() == Blocks.DARK_OAK_LEAVES) {
                if (Block.getDrops(state, (ServerLevel) player.getLevel(), event.getPos(), null, player, player.getMainHandItem()).isEmpty()) {
                    double random = Math.random() * 100;
                    if (random <= 0.5)
                        ItemHandler.addFreshItemEntity(player.getLevel(), event.getPos(), Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance());
                    else if (random <= 3.5)
                        ItemHandler.addFreshItemEntity(player.getLevel(), event.getPos(), Items.GOLDEN_APPLE.getDefaultInstance());
                }
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

    public void attemptFellTree(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return;
        if (player.getMainHandItem().getItem() instanceof LumberAxeItem lumberAxeItem) {
            if (JobGetters.getJobLevel(player, job) < ModRecipeManager.getRequiredJobLevelServer(player.getMainHandItem()))
                return;
            int maxLogs = lumberAxeItem.getMaxLogs();

            ArrayList<BlockPos> logs = new ArrayList<>();
            ArrayList<BlockPos> candidates = new ArrayList<>();
            candidates.add(pos);

            for (int i = 0; i < candidates.size(); i++) {
                if (logs.size() > maxLogs) {
                    ExpHandler.addEXPLow(player, job);
                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.red() + "This tree is too big to cut down with this axe.");
                    return;
                }

                BlockPos candidate = candidates.get(i);

                if (level.getBlockState(candidate).is(BlockTags.LOGS)) {
                    logs.add(candidate);
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockPos neighbor = candidate.offset(x, y, z);
                                if (candidates.contains(neighbor)) continue;
                                candidates.add(neighbor);
                            }
                        }
                    }
                }
            }
            if (logs.size() == 0) return;
            EventWaitTicks.waitTicks(player, EventWaitTicks.Type.TREE_FELLER, new Object[]{logs});
        }
    }
}
