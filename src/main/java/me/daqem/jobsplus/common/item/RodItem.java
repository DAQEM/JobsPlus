package me.daqem.jobsplus.common.item;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
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

public class RodItem extends JobsPlusItem.Rod {

    private static final Jobs JOB = Jobs.FISHERMAN;
    private long lastUsedTime = 0;

    public RodItem(Properties properties) {
        super(properties.tab(JobsPlus.TAB));
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        final FishingHook fishing = player.fishing;
        if (JobGetters.jobIsEnabled(player, JOB)) {
            if (JobGetters.getJobLevel(player, JOB) >= getRequiredLevel()) {
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

                if (JobGetters.hasPowerupEnabled(player, JOB, CapType.POWER_UP3.get(), true)) {
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
            } else {
                if (!level.isClientSide)
                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, TranslatableString.get("error.magic"));
            }
        } else {
            if (!level.isClientSide)
                HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, TranslatableString.get("error.magic"));
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Fisherman"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + getRequiredLevel()));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(Component.literal(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getDescriptionId()).replace("item.jobsplus.fishermans_rod_level_", "")));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Drop chance:"));
            for (String s : getDrops()) {
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

    private ArrayList<String> getDrops() {
        ArrayList<String> dropList = new ArrayList<>();
        String two = ChatColor.green() + "20% " + ChatColor.reset() + "2 drops";
        String three = ChatColor.green() + "15% " + ChatColor.reset() + "3 drops";
        String four = ChatColor.green() + "10% " + ChatColor.reset() + "4 drops";
        String five = ChatColor.green() + "5% " + ChatColor.reset() + "5 drops";
        if (this == ModItems.FISHERMANS_ROD_LEVEL_1.get()) dropList.add(two);
        if (this == ModItems.FISHERMANS_ROD_LEVEL_2.get()) dropList.addAll(List.of(two, three));
        if (this == ModItems.FISHERMANS_ROD_LEVEL_3.get()) dropList.addAll(List.of(two, three, four));
        if (this == ModItems.FISHERMANS_ROD_LEVEL_4.get()) dropList.addAll(List.of(two, three, four, five));
        return dropList;
    }
}
