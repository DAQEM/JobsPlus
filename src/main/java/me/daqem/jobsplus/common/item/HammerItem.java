package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.events.jobs.MinerEvents;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.ToolFunctions;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class HammerItem extends PickaxeItem {

    public HammerItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if (!level.isClientSide) {
            if (JobGetters.jobIsEnabled(player, Jobs.MINER)) {
                if (player.isCrouching()) {
                    return true;
                }
                boolean allowedToUseHammer = false;
                Item item = player.getMainHandItem().getItem();
                int jobLevel = JobGetters.getJobLevel(player, Jobs.MINER);
                if (jobLevel >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_1.get() && item == ModItems.MINERS_HAMMER_LEVEL_1.get())
                    allowedToUseHammer = true;
                if (jobLevel >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_2.get() && item == ModItems.MINERS_HAMMER_LEVEL_2.get())
                    allowedToUseHammer = true;
                if (jobLevel >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_3.get() && item == ModItems.MINERS_HAMMER_LEVEL_3.get())
                    allowedToUseHammer = true;
                if (jobLevel >= Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_4.get() && item == ModItems.MINERS_HAMMER_LEVEL_4.get())
                    allowedToUseHammer = true;
                if (allowedToUseHammer) {
                    if (player.getMainHandItem().getItem() instanceof HammerItem) {
                        float originHardness = level.getBlockState(pos).getDestroySpeed(null, null);
                        if (player.getMainHandItem().getItem().isCorrectToolForDrops(level.getBlockState(pos))) {
                            int mode = 0;
                            if (player.getMainHandItem().getOrCreateTag().contains("mode")) {
                                mode = player.getMainHandItem().getOrCreateTag().getInt("mode");
                            }
                            if (state.getBlock() instanceof ShulkerBoxBlock) return true;
                            if (!MinerEvents.veinMinerArray.contains(player.getUUID())) {
                                ToolFunctions.breakInRadius(level, player, mode, (breakState) -> {
                                    double hardness = breakState.getDestroySpeed(null, null);
                                    boolean isEffective = player.getMainHandItem().isCorrectToolForDrops(breakState);
                                    boolean verifyHardness = hardness < originHardness * 5 && hardness > 0;
                                    return isEffective && verifyHardness;
                                }, true);
                            }
                            return true;
                        }
                    }
                } else {
                    HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
                    return true;
                }
            } else {
                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
            }
        }
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown() && !level.isClientSide) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof HammerItem) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("mode")) {
                    int tagInt = tag.getInt("mode");
                    if (stack.getItem() == ModItems.MINERS_HAMMER_LEVEL_1.get()) {
                        tag.putInt("mode", 0);
                    } else if (stack.getItem() == ModItems.MINERS_HAMMER_LEVEL_2.get()) {
                        if (tagInt != 1) {
                            tag.putInt("mode", ++tagInt);
                        } else {
                            tag.putInt("mode", 0);
                        }
                    } else if (stack.getItem() == ModItems.MINERS_HAMMER_LEVEL_3.get()) {
                        if (tagInt != 2) {
                            tag.putInt("mode", ++tagInt);
                        } else {
                            tag.putInt("mode", 0);
                        }
                    } else if (stack.getItem() == ModItems.MINERS_HAMMER_LEVEL_4.get()) {
                        if (tagInt != 3) {
                            tag.putInt("mode", ++tagInt);
                        } else {
                            tag.putInt("mode", 0);
                        }
                    }
                } else {
                    tag.putInt("mode", 0);
                }
                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, ChatColor.boldDarkGreen() + "Mode: " + ChatColor.green() + getModeString(stack));
                SoundHandler.playEXPOrbPickupSound(player, 0.7F, 1F);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            int level = 0;
            String modes = "";
            Item item = stack.getItem();
            if (item == ModItems.MINERS_HAMMER_LEVEL_1.get()) {
                level = Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_1.get();
                modes = "3x3";
            }
            if (item == ModItems.MINERS_HAMMER_LEVEL_2.get()) {
                level = Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_2.get();
                modes = "3x3, 3x3x3";
            }
            if (item == ModItems.MINERS_HAMMER_LEVEL_3.get()) {
                level = Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_3.get();
                modes = "3x3, 3x3x3, 5x5";
            }
            if (item == ModItems.MINERS_HAMMER_LEVEL_4.get()) {
                level = Config.REQUIRED_LEVEL_MINERS_HAMMER_LEVEL_4.get();
                modes = "3x3, 3x3x3, 5x5, 5x5x5";
            }
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Miner"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getRegistryName()).toString().replace("jobsplus:miners_hammer_level_", "")));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Modes: " + ChatColor.reset() + modes));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Controls:"));
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Shift + right-click to change the mode."));
        } else {
            if (stack.getOrCreateTag().contains("mode")) {
                tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Mode: " + ChatColor.reset() + getModeString(stack)));
            }
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

    public String getModeString(ItemStack stack) {
        if (stack.getOrCreateTag().getInt("mode") == 0) return "3x3";
        if (stack.getOrCreateTag().getInt("mode") == 1) return "3x3x3";
        if (stack.getOrCreateTag().getInt("mode") == 2) return "5x5";
        if (stack.getOrCreateTag().getInt("mode") == 3) return "5x5x5";
        return "";
    }
}
