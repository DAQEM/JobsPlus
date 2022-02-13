package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.packet.PacketDisableVeinMiner;
import me.daqem.jobsplus.utils.BlockPosUtil;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinerEvents {

    public final static ArrayList<String> lowList = new ArrayList<>(List.of("andesite", "diorite", "granite", "calcite", "dripstone", "dripstone_block", "sandstone", "blackstone"));
    public final static ArrayList<String> lowestList = new ArrayList<>(List.of("stone", "deepslate", "netherrack", "end_stone"));
    public static final ArrayList<BlockPos> timeoutList = new ArrayList<>();
    public static final ArrayList<UUID> veinMinerArray = new ArrayList<>();
    public static final int ORE_BREAK_DELAY = 1;
    private final Jobs job = Jobs.MINER;

    private static void dropItems(Level level, List<ItemStack> stacks, BlockPos pos, int exp) {
        for (ItemStack stack : stacks) {
            if (exp > 0) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY(), pos.getZ(), exp));
            }
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack));
        }
    }

    public static void addPlayerPowerUpEffects(Player player, Jobs job) {
        if (JobGetters.hasSuperPowerEnabled(player, job)) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5 * 20, 1));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5 * 20));
        } else if (JobGetters.hasEnabledPowerup(player, job, CapType.POWERUP3.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5 * 20));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            if (!event.getWorld().isClientSide()) {
                if (JobGetters.jobIsEnabled(player, job)) {
                    Block block = event.getState().getBlock();
                    if (BlockPosUtil.testAllSides(timeoutList, event.getPos())) {
                        if (block instanceof OreBlock || block instanceof RedStoneOreBlock
                                || (!(block.getDescriptionId().startsWith("block.minecraft.")) && block.getDescriptionId().endsWith("_ore"))
                                || block.getDescriptionId().equals("block.minecraft.ancient_debris")) {
                            ExpHandler.addEXPMid(player, job);
                            addPlayerPowerUpEffects(player, job);
                        } else if (lowestList.contains(block.getDescriptionId().replace("block.minecraft.", ""))) {
                            ExpHandler.addEXPLowest(player, job);
                            addPlayerPowerUpEffects(player, job);
                        } else if (lowList.contains(block.getDescriptionId().replace("block.minecraft.", ""))) {
                            ExpHandler.addEXPLow(player, job);
                            addPlayerPowerUpEffects(player, job);
                        } else if (BlockTags.TERRACOTTA.contains(block)) {
                            ExpHandler.addEXPLow(player, job);
                            addPlayerPowerUpEffects(player, job);
                        }
                    } else {
                        timeoutList.remove(event.getPos());
                    }
                    if (JobGetters.hasEnabledPowerup(player, job, CapType.POWERUP2.get())) {
                        if (BlockTags.IRON_ORES.contains(block) || BlockTags.GOLD_ORES.contains(block) || BlockTags.COPPER_ORES.contains(block)) {
                            List<ItemStack> drops = Block.getDrops(event.getState(), (ServerLevel) event.getWorld(), event.getPos(), null, player, player.getMainHandItem());
                            event.setCanceled(true);
                            event.getWorld().removeBlock(event.getPos(), false);
                            dropItems((Level) event.getWorld(), ItemHandler.smeltedRawMaterials(player, drops), event.getPos(), 1);
                        }
                    }
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

    @SubscribeEvent
    public void onVeinMine(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (veinMinerArray.contains(player.getUUID())) {
            if (JobGetters.jobIsEnabled(player, Jobs.MINER)) {
                if (JobGetters.getPowerup(player, Jobs.MINER, CapType.POWERUP1.get()) == 1) {
                    Level level = (Level) event.getWorld();
                    if (!level.isClientSide()) {
                        Item itemInHand = player.getMainHandItem().getItem();
                        if (itemInHand instanceof PickaxeItem) {
                            int maxOres = 16 * 16 * 16;
                            ArrayList<BlockPos> ores = new ArrayList<>();
                            ArrayList<BlockPos> candidates = new ArrayList<>();
                            candidates.add(event.getPos());

                            for (int i = 0; i < candidates.size(); i++) {
                                if (ores.size() > maxOres) {
                                    if (!timeoutList.contains(event.getPos())) {
                                        ExpHandler.addEXPMid(player, Jobs.MINER);
                                    }
                                    HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, ChatColor.red() + "This vein is too big to mine.");
                                    return;
                                }

                                BlockPos candidate = candidates.get(i);
                                Block block = level.getBlockState(candidate).getBlock();

                                if (block instanceof OreBlock || block instanceof RedStoneOreBlock || (!(block.getDescriptionId().startsWith("block.minecraft.")) && block.getDescriptionId().endsWith("_ore"))) {
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
                            if (ores.size() == 0) return;
                            MinecraftForge.EVENT_BUS.register(new Object() {
                                int delay = ORE_BREAK_DELAY;
                                int i = 0;

                                @SubscribeEvent
                                public void onTick(TickEvent.WorldTickEvent event) {
                                    if (delay-- > 0) return;
                                    delay = ORE_BREAK_DELAY;
                                    if (i < ores.size()) {
                                        BlockPos ore = ores.get(i);
                                        attemptBreak(level, ore, player);
                                        if (!timeoutList.contains(ore)) {
                                            ExpHandler.addEXPMid(player, Jobs.MINER);
                                        }
                                        i++;
                                    } else {
                                        MinecraftForge.EVENT_BUS.unregister(this);
                                    }
                                }
                            });
                        }
                    }
                }
            }
            if (player instanceof ServerPlayer serverPlayer) {
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketDisableVeinMiner());
            }
        }
    }

    public void attemptBreak(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            boolean isEffective = block instanceof OreBlock || block instanceof RedStoneOreBlock || (!(block.getDescriptionId().startsWith("block.minecraft.")) && block.getDescriptionId().endsWith("_ore"));
            boolean witherImmune = BlockTags.WITHER_IMMUNE.contains(state.getBlock());

            if (isEffective && !witherImmune) {
                level.destroyBlock(pos, false);
                if (!player.isCreative()) {
                    Block.dropResources(state, level, pos, null, player, player.getMainHandItem());
                    int bonusLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
                    int silkLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
                    int exp = state.getExpDrop(level, pos, bonusLevel, silkLevel);
                    if (exp > 0) {
                        level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY(), pos.getZ(), exp));
                    }
                }
            }
        }
    }
}
