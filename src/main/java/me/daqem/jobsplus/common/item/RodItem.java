package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RodItem extends FishingRodItem {

    private long lastUsedTime = 0;

    public RodItem(Properties properties) {
        super(properties.tab(JobsPlus.TAB));
    }

    public @NotNull
    InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Jobs job = Jobs.FISHERMAN;
        int jobLevel = JobGetters.getJobLevel(player, job);
        final FishingHook fishing = player.fishing;
        if (JobGetters.jobIsEnabled(player, job)) {
            boolean isAllowedToUseRod = itemstack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_1.get() && jobLevel >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_1.get();
            if (itemstack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_2.get() && jobLevel >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_2.get())
                isAllowedToUseRod = true;
            if (itemstack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_3.get() && jobLevel >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_3.get())
                isAllowedToUseRod = true;
            if (itemstack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_4.get() && jobLevel >= Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_4.get())
                isAllowedToUseRod = true;
            if (isAllowedToUseRod) {
                if (fishing != null) {
                    if (!level.isClientSide) {
                        int i = fishing.retrieve(itemstack);
                        itemstack.hurtAndBreak(i, player, (player1) -> player1.broadcastBreakEvent(hand));
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                } else {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    if (!level.isClientSide) {
                        int k = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                        int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
                        level.addFreshEntity(new ModFishingHook(player, level, j, k));
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    player.gameEvent(GameEvent.ITEM_INTERACT_START);
                }
            } else {
                if (!level.isClientSide)
                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, TranslatableString.get("error.magic"));
            }
        } else {
            if (!level.isClientSide)
                HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, TranslatableString.get("error.magic"));
        }

        if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP3.get(), true)) {
            if (fishing != null && fishing.tickCount != 0 && !level.isClientSide) {
                if (lastUsedTime + 2000 < System.currentTimeMillis() || player.isCreative() || player.isOnGround()) {
                    if (level.getBlockState(fishing.blockPosition()) == Blocks.AIR.defaultBlockState()) {
                        player.setDeltaMovement((fishing.position().x - player.position().x) / 2, 1.1, (fishing.position().z - player.position().z) / 2);
                        player.hurtMarked = true;
                        lastUsedTime = System.currentTimeMillis();
                        player.addTag("cancelFallDamageForFisherman");
                    }
                } else {
                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.red() + "Cooldown: " + new DecimalFormat("0.00").format((double) ((lastUsedTime - System.currentTimeMillis()) + 2000) / 1000) + "s");
                }
            }

        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            int level = 0;
            Item item = stack.getItem();
            ArrayList<String> dropList = new ArrayList<>();
            String two = ChatColor.green() + "20% " + ChatColor.reset() + "2 drops";
            String three = ChatColor.green() + "15% " + ChatColor.reset() + "3 drops";
            String four = ChatColor.green() + "10% " + ChatColor.reset() + "4 drops";
            String five = ChatColor.green() + "5% " + ChatColor.reset() + "5 drops";
            if (item == ModItems.FISHERMANS_ROD_LEVEL_1.get()) {
                level = Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_1.get();
                dropList.add(two);
            }
            if (item == ModItems.FISHERMANS_ROD_LEVEL_2.get()) {
                level = Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_2.get();
                dropList.add(two);
                dropList.add(three);
            }
            if (item == ModItems.FISHERMANS_ROD_LEVEL_3.get()) {
                level = Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_3.get();
                dropList.add(two);
                dropList.add(three);
                dropList.add(four);
            }
            if (item == ModItems.FISHERMANS_ROD_LEVEL_4.get()) {
                level = Config.REQUIRED_LEVEL_FISHERMANS_ROD_LEVEL_4.get();
                dropList.add(two);
                dropList.add(three);
                dropList.add(four);
                dropList.add(five);
            }
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Fisherman"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(Component.literal(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getDescriptionId()).replace("item.jobsplus.fishermans_rod_level_", "")));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Drop chance:"));
            for (String s : dropList) {
                tooltip.add(Component.literal(s));
            }
        } else {
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack leftItem, @NotNull ItemStack rightItem) {
        return leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_1.get() && rightItem.getItem() == Items.IRON_BLOCK
                || leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_2.get() && rightItem.getItem() == Items.GOLD_BLOCK
                || leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_3.get() && rightItem.getItem() == Items.DIAMOND_BLOCK
                || leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_4.get() && rightItem.getItem() == Items.EMERALD_BLOCK;
    }
}
