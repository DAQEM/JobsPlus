package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.config.Config;
import me.daqem.jobsplus.events.EventWaitTicks;
import me.daqem.jobsplus.handlers.*;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinerEvents {

    public final static ArrayList<Block> lowList = new ArrayList<>(List.of(Blocks.ANDESITE, Blocks.DIORITE,
            Blocks.GRANITE, Blocks.CALCITE, Blocks.DRIPSTONE_BLOCK, Blocks.SANDSTONE, Blocks.BLACKSTONE,
            Blocks.GILDED_BLACKSTONE, Blocks.BASALT, Blocks.BUDDING_AMETHYST, Blocks.AMETHYST_BLOCK,
            Blocks.TUFF, Blocks.OBSIDIAN));
    public final static ArrayList<Block> lowestList = new ArrayList<>(List.of(Blocks.STONE, Blocks.DEEPSLATE,
            Blocks.NETHERRACK, Blocks.END_STONE, Blocks.COBBLESTONE));
    private final Jobs job = Jobs.MINER;

    private static void dropItems(Level level, List<ItemStack> stacks, BlockPos pos, int exp) {
        for (ItemStack stack : stacks) {
            if (exp > 0) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp));
            }
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
        }
    }

    public static void addMinerExpForMinedBlock(Player player, BlockState state) {
        int exp = 0;
        Block block = state.getBlock();
        if (BlockHandler.isOre(block)) {
            exp += ExpHandler.getEXPMid();
        } else if (lowestList.contains(block)
                || block.getDescriptionId().contains("soapstone")) {
            exp += ExpHandler.getEXPLowest();
        } else if (lowList.contains(block)
                || state.is(BlockTags.TERRACOTTA)
                || (player.getMainHandItem().getItem() instanceof PickaxeItem && player.getMainHandItem().isCorrectToolForDrops(state))) {
            exp += ExpHandler.getEXPLow();
        }
        if (exp != 0) {
            Jobs job = Jobs.MINER;
            ExpHandler.addJobEXP(player, job, exp);
            MobEffectHandler.addPlayerPowerUpEffects(player, job);
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (event.getLevel().isClientSide()) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        VeinMinerHandler.disableVeinMiningIfNotOre((ServerPlayer) player, block);

        addMinerExpForMinedBlock(player, state);

        //SMELT ORES AUTOMATICALLY
        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true) && VeinMinerHandler.isNotVeinMining((ServerPlayer) player)) {
            if (state.canHarvestBlock(event.getLevel(), event.getPos(), player) && !player.isCreative()) {
                BlockEntity blockentity = state.hasBlockEntity() ? event.getLevel().getBlockEntity(event.getPos()) : null;
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) event.getLevel(), event.getPos(), blockentity, player, player.getMainHandItem());
                List<ItemStack> stacks = ItemHandler.smeltedRawMaterials(player, drops, block);
                JobsPlus.LOGGER.error("one");
                if (!stacks.equals(drops)) {
                    event.setCanceled(true);
                    event.getLevel().removeBlock(event.getPos(), false);
                    dropItems((Level) event.getLevel(), new ArrayList<>(stacks), event.getPos(), stacks.size());
                }
            }
        }
    }

    @SubscribeEvent
    public void onVeinMine(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        Item itemInHand = player.getMainHandItem().getItem();
        ArrayList<BlockPos> ores = new ArrayList<>();
        ArrayList<BlockPos> candidates = new ArrayList<>();

        if (level.isClientSide()) return;
        if (!BlockHandler.isOre(event.getState().getBlock())) return;
        if (!event.getState().requiresCorrectToolForDrops()) return;
        if (!itemInHand.isCorrectToolForDrops(event.getState())) return;
        if (!JobGetters.jobIsEnabled(player, Jobs.MINER)) return;
        if (!(JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP1.get()) == 1)) return;
        if (VeinMinerHandler.isNotVeinMining((ServerPlayer) player)) return;

        event.setCanceled(true);
        candidates.add(event.getPos());

        for (int i = 0; i < candidates.size(); i++) {
            if (ores.size() > Config.MAX_VEIN_MINER_BLOCKS.get()) break;

            BlockPos candidate = candidates.get(i);
            Block block = level.getBlockState(candidate).getBlock();

            if (BlockHandler.isOre(block)) {
                if (event.getState().is(block)) {
                    ores.add(candidate);
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                BlockPos neighbor = candidate.offset(x, y, z);
                                if (candidates.contains(neighbor)) continue;
                                candidates.add(neighbor);
                            }
                        }
                    }
                }
            }
        }

        if (ores.size() == 0) return;
        EventWaitTicks.waitTicks(player, EventWaitTicks.Type.VEIN_MINER, new Object[]{ores});
    }
}
