package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.events.EventWaitTicks;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.MobEffectHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;
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
    private final Jobs job = Jobs.MINER;

    private static void dropItems(Level level, List<ItemStack> stacks, BlockPos pos, int exp) {
        for (ItemStack stack : stacks) {
            if (exp > 0) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp));
            }
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isOre(Block block) {
        return ((block instanceof OreBlock)
                || block instanceof RedStoneOreBlock
                || block.builtInRegistryHolder().containsTag(new TagKey<>(Registry.BLOCK_REGISTRY, new ResourceLocation("forge", "ores")))
                || block.getDescriptionId().endsWith("_ore")
                || block == Blocks.ANCIENT_DEBRIS);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (event.getWorld().isClientSide()) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        if (isOre(block)) {
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
        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true) && !veinMinerArray.contains(player.getUUID())) {
            if (state.is(BlockTags.IRON_ORES) || state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.COPPER_ORES) || state.is(Blocks.ANCIENT_DEBRIS)) {
                event.setCanceled(true);
                event.getWorld().removeBlock(event.getPos(), false);
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) event.getWorld(), event.getPos(), null, player, player.getMainHandItem());
                List<ItemStack> stacks = ItemHandler.smeltedRawMaterials(player, drops);
                int exp = 0;
                for (ItemStack stack : stacks) {
                    if (stack.is(Items.COPPER_INGOT) || stack.is(Items.IRON_INGOT) || stack.is(Items.GOLD_INGOT) || stack.is(Items.NETHERITE_INGOT))
                        ++exp;
                }
                dropItems((Level) event.getWorld(), stacks, event.getPos(), exp);
            }
        }
    }

    @SubscribeEvent
    public void onVeinMine(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getWorld();
        Item itemInHand = player.getMainHandItem().getItem();
        ArrayList<BlockPos> ores = new ArrayList<>();
        ArrayList<BlockPos> candidates = new ArrayList<>();

        if (level.isClientSide()) return;
        if (!isOre(event.getState().getBlock())) return;
        if (!event.getState().requiresCorrectToolForDrops()) return;
        if (!itemInHand.isCorrectToolForDrops(event.getState())) return;
        if (!JobGetters.jobIsEnabled(player, Jobs.MINER)) return;
        if (!(JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP1.get()) == 1)) return;
        if (!veinMinerArray.contains(player.getUUID())) return;

        event.setCanceled(true);
        candidates.add(event.getPos());

        for (int i = 0; i < candidates.size(); i++) {
            if (ores.size() > Config.MAX_VEIN_MINER_BLOCKS.get()) break;

            BlockPos candidate = candidates.get(i);
            Block block = level.getBlockState(candidate).getBlock();

            if (isOre(block)) {
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
        veinMinerArray.remove(player.getUUID());
    }
}
