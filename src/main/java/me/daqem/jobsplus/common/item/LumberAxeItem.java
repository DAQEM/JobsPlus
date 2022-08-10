package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LumberAxeItem extends AxeItem {

    public static final int LOG_BREAK_DELAY = 1;

    public LumberAxeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    public static void attemptBreak(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            BlockState state = level.getBlockState(pos);
            boolean isEffective = state.is(BlockTags.LOGS);
            boolean witherImmune = state.is(BlockTags.WITHER_IMMUNE);

            if (isEffective && !witherImmune) {
                level.destroyBlock(pos, false);
                final Jobs job = Jobs.LUMBERJACK;

                // Drop Extra Block
                if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP1.get()) && Math.random() * 100 < 5) {
                    if (JobGetters.hasSuperPowerEnabled(player, job))
                        ItemHandler.addItemsToInventoryOrDrop(new ItemStack(state.getBlock().asItem()), player, player.getLevel(), pos, 0);
                    else ItemHandler.addFreshItemEntity(player.getLevel(), pos, state.getBlock().asItem());
                }

                //Drop Normal Drop
                if (JobGetters.hasSuperPowerEnabled(player, job))
                    ItemHandler.addItemsToInventoryOrDrop(new ItemStack(state.getBlock().asItem()), player, player.getLevel(), pos, 0);
                else Block.dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entityLiving) {
        if (entityLiving instanceof Player player) {
            if (state.is(BlockTags.LOGS)) {
                if (player.getMainHandItem().getOrCreateTag().getInt("mode") == 0) {
                    stack.hurtAndBreak(3, entityLiving, entityLiving1 -> {
                    });
                    int jobLevel = JobGetters.getJobLevel(player, Jobs.LUMBERJACK);
                    Item itemInHand = player.getMainHandItem().getItem();
                    boolean isAllowedToUseAxe = jobLevel >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_1.get() && itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_1.get();
                    if (!isAllowedToUseAxe)
                        isAllowedToUseAxe = jobLevel >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_2.get() && itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_2.get();
                    if (!isAllowedToUseAxe)
                        isAllowedToUseAxe = jobLevel >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_3.get() && itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_3.get();
                    if (!isAllowedToUseAxe)
                        isAllowedToUseAxe = jobLevel >= Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_4.get() && itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_4.get();
                    if (isAllowedToUseAxe) {
                        attemptFellTree(level, pos, player);
                    } else {
                        HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
                    }
                } else {
                    ExpHandler.addEXPLow(player, Jobs.LUMBERJACK);
                }
            }
        }
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    private void attemptFellTree(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            Item itemInHand = player.getMainHandItem().getItem();
            int maxLogs = 0;
            if (itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_1.get()) maxLogs = 16;
            if (itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_2.get()) maxLogs = 48;
            if (itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_3.get()) maxLogs = 128;
            if (itemInHand == ModItems.LUMBERJACK_AXE_LEVEL_4.get()) maxLogs = 256;
            if (maxLogs == 0) {
                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
                return;
            }

            ArrayList<BlockPos> logs = new ArrayList<>();
            ArrayList<BlockPos> candidates = new ArrayList<>();
            candidates.add(pos);

            for (int i = 0; i < candidates.size(); i++) {
                if (logs.size() > maxLogs) {
                    ExpHandler.addEXPLow(player, Jobs.LUMBERJACK);
                    HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, ChatColor.red() + "This tree is too big to cut down with this axe.");
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
            ExpHandler.addJobEXP(player, Jobs.LUMBERJACK, ExpHandler.getEXPLow() * logs.size());
            MinecraftForge.EVENT_BUS.register(new Object() {
                int delay = LOG_BREAK_DELAY;
                int i = 0;

                @SubscribeEvent
                public void onTick(TickEvent.LevelTickEvent event) {
                    if (delay-- > 0) return;
                    delay = LOG_BREAK_DELAY;
                    if (i < logs.size()) {
                        BlockPos log = logs.get(i);
                        attemptBreak(level, log, player);
                        i++;
                    } else {
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            });
        }
    }

    @Override
    public @NotNull
    InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();
        if (player.isCrouching() && !level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("mode")) {
                if (tag.getInt("mode") == 0) tag.putInt("mode", 1);
                else tag.putInt("mode", 0);
            } else {
                tag.putInt("mode", 0);
            }
            HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, ChatColor.boldDarkGreen() + "Mode: " + ChatColor.green() + getModeString(stack));
            SoundHandler.playEXPOrbPickupSound(player, 0.7F, 1F);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            int level = 0, logs = 0;
            Item item = stack.getItem();
            if (item == ModItems.LUMBERJACK_AXE_LEVEL_1.get()) {
                level = Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_1.get();
                logs = 16;
            }
            if (item == ModItems.LUMBERJACK_AXE_LEVEL_2.get()) {
                level = Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_2.get();
                logs = 48;
            }
            if (item == ModItems.LUMBERJACK_AXE_LEVEL_3.get()) {
                level = Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_3.get();
                logs = 128;
            }
            if (item == ModItems.LUMBERJACK_AXE_LEVEL_4.get()) {
                level = Config.REQUIRED_LEVEL_LUMBERJACK_AXE_LEVEL_4.get();
                logs = 256;
            }
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Lumberjack"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(Component.literal(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getDescriptionId()).replace("item.jobsplus.lumberjack_axe_level_", "")));
            tooltip.add(Component.literal(ChatColor.green() + "Modes: " + ChatColor.reset() + "Tree Feller and Single Block"));
            tooltip.add(Component.literal(ChatColor.green() + "Max Logs: " + ChatColor.reset() + logs));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Controls:"));
            tooltip.add(Component.literal(ChatColor.gray() + "Shift + right-click to change the mode."));
        } else {
            if (stack.getOrCreateTag().contains("mode")) {
                tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Mode: " + ChatColor.reset() + getModeString(stack)));
            }
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

    public String getModeString(ItemStack stack) {
        if (stack.getOrCreateTag().getInt("mode") == 0) return "Tree Feller";
        if (stack.getOrCreateTag().getInt("mode") == 1) return "Single Block";
        return "";
    }
}
