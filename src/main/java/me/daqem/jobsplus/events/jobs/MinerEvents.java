package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.MobEffectHandler;
import me.daqem.jobsplus.utils.ChatColor;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinerEvents {

    public final static ArrayList<Block> lowList = new ArrayList<>(List.of(Blocks.ANDESITE, Blocks.DIORITE,
            Blocks.GRANITE, Blocks.CALCITE, Blocks.DRIPSTONE_BLOCK, Blocks.SANDSTONE, Blocks.BLACKSTONE,
            Blocks.GILDED_BLACKSTONE, Blocks.BASALT, Blocks.BUDDING_AMETHYST, Blocks.AMETHYST_BLOCK,
            Blocks.TUFF, Blocks.OBSIDIAN));
    public final static ArrayList<Block> lowestList = new ArrayList<>(List.of(Blocks.STONE, Blocks.DEEPSLATE,
            Blocks.NETHERRACK, Blocks.END_STONE, Blocks.COBBLESTONE));
    public static final ArrayList<UUID> veinMinerArray = new ArrayList<>();
    public static final int ORE_BREAK_DELAY = 1;
    private final Jobs job = Jobs.MINER;

    private static void dropItems(Level level, List<ItemStack> stacks, BlockPos pos, int exp) {
        for (ItemStack stack : stacks) {
            if (exp > 0) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp));
            }
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (event.getLevel().isClientSide()) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        if ((block instanceof DropExperienceBlock && block != Blocks.SCULK) || block instanceof RedStoneOreBlock
                || block.getDescriptionId().endsWith("_ore")
                || block == Blocks.ANCIENT_DEBRIS) {
            ExpHandler.addEXPMid(player, job);
            MobEffectHandler.addPlayerPowerUpEffects(player, job);
        } else if (lowestList.contains(block)) {
            ExpHandler.addEXPLowest(player, job);
            MobEffectHandler.addPlayerPowerUpEffects(player, job);
        } else if (lowList.contains(block)
                || state.is(BlockTags.TERRACOTTA)
                || (player.getMainHandItem().getItem() instanceof PickaxeItem && player.getMainHandItem().isCorrectToolForDrops(state))) {
            ExpHandler.addEXPLow(player, job);
            MobEffectHandler.addPlayerPowerUpEffects(player, job);
        }
        if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get()) && !veinMinerArray.contains(player.getUUID())) {
            if (state.is(BlockTags.IRON_ORES) || state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.COPPER_ORES) || state.is(Blocks.ANCIENT_DEBRIS)) {
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) event.getLevel(), event.getPos(), null, player, player.getMainHandItem());
                event.setCanceled(true);
                event.getLevel().removeBlock(event.getPos(), false);
                dropItems((Level) event.getLevel(), ItemHandler.smeltedRawMaterials(player, drops), event.getPos(), 1);
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

        if (!event.getState().requiresCorrectToolForDrops()) return;
        if (!itemInHand.isCorrectToolForDrops(event.getState())) return;
        if (!JobGetters.jobIsEnabled(player, Jobs.MINER)) return;
        if (!(JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP1.get()) == 1)) return;
        if (level.isClientSide()) return;
        if (!veinMinerArray.contains(player.getUUID())) return;

        Block broken = event.getState().getBlock();
        // Also edit the one below
        boolean bool = (broken instanceof DropExperienceBlock && broken != Blocks.SCULK) || broken instanceof RedStoneOreBlock || broken.getDescriptionId().endsWith("_ore") || broken == Blocks.ANCIENT_DEBRIS;

        if (!bool) return;

        event.setCanceled(true);
        candidates.add(event.getPos());

        for (int i = 0; i < candidates.size(); i++) {
            if (ores.size() > 16 * 16 * 16) {
                ExpHandler.addEXPMid(player, Jobs.MINER);
                HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.red() + "This vein is too big to mine.");
                return;
            }

            BlockPos candidate = candidates.get(i);
            Block block = level.getBlockState(candidate).getBlock();

            // Also edit the one above
            if ((block instanceof DropExperienceBlock && block != Blocks.SCULK) || block instanceof RedStoneOreBlock || block.getDescriptionId().endsWith("_ore") || block == Blocks.ANCIENT_DEBRIS) {
                if (block == event.getState().getBlock()) {
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

        MinecraftForge.EVENT_BUS.register(new Object() {
            int delay = ORE_BREAK_DELAY, i = 0;

            @SubscribeEvent
            public void onTick(TickEvent.LevelTickEvent event) {
                if (delay-- > 0) return;
                if (i == ores.size()) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                    return;
                }

                delay = ORE_BREAK_DELAY;
                BlockPos ore = ores.get(i);
                attemptBreak(level, ore, player);
                player.getInventory().getSelected().hurtAndBreak(1, player, player1 -> {
                });

                ExpHandler.addEXPMid(player, Jobs.MINER);

                i++;
            }
        });

        veinMinerArray.remove(player.getUUID());
    }

    public void attemptBreak(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return;
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        boolean isEffective = (block instanceof DropExperienceBlock && block != Blocks.SCULK) || block instanceof RedStoneOreBlock || (!(block.getDescriptionId().startsWith("block.minecraft.")) && block.getDescriptionId().endsWith("_ore"));
        boolean witherImmune = state.is(BlockTags.WITHER_IMMUNE);

        if (!isEffective && witherImmune) return;

        level.destroyBlock(pos, false);

        if (player.isCreative()) return;

        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, player.getMainHandItem());
        int bonusLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
        int silkLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
        int exp = state.getExpDrop(level, level.random, pos, bonusLevel, silkLevel);
        if (state.is(BlockTags.IRON_ORES) || state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.COPPER_ORES) || state.is(Blocks.ANCIENT_DEBRIS)
                && JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get()))
            exp = 1;
        dropItems(level, ItemHandler.smeltedRawMaterials(player, drops), pos, exp);
    }
}
