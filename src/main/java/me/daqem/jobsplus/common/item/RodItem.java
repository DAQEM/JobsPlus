package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.ModItemUtils;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
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

public class RodItem extends FishingRodItem {

    private long lastUsedTime = 0;

    public RodItem(Properties properties) {
        super(properties.tab(JobsPlus.TAB));
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        final FishingHook fishing = player.fishing;
        Jobs job = ModRecipeManager.getJobServer(player.getItemInHand(hand));
        if (JobGetters.jobIsEnabled(player, job)) {
            if (JobGetters.getJobLevel(player, job) >= ModRecipeManager.getRequiredJobLevelServer(player.getItemInHand(hand))) {
                if (fishing != null) {
                    if (!level.isClientSide) {
                        int i = fishing.retrieve(itemstack);
                        ModItemUtils.damageItem(i, itemstack, player);
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    level.gameEvent(GameEvent.FISHING_ROD_REEL_IN, player);
                } else {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    if (!level.isClientSide) {
                        int k = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                        int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
                        level.addFreshEntity(new ModFishingHook(player, level, j, k));
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(GameEvent.FISHING_ROD_CAST, player);
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
            } else {
                if (!level.isClientSide)
                    HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, JobsPlus.translatable("error.magic").withStyle(ChatFormatting.RED));
            }
        } else {
            if (!level.isClientSide)
                HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, JobsPlus.translatable("error.magic").withStyle(ChatFormatting.RED));
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withComponent(TooltipBuilder.WHITE_SPACE, TooltipBuilder.ShiftType.SHIFT)
                .withComponent(JobsPlus.translatable("tooltip.about.rod.drop_chance").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withBold(true)), TooltipBuilder.ShiftType.SHIFT)
                .withComponents(getDrops(), TooltipBuilder.ShiftType.SHIFT)
                .withHoldShift().withEnchantments(stack, false)
                .build());
    }

    @Override
    public boolean isValidRepairItem(ItemStack leftItem, @NotNull ItemStack rightItem) {
        return leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_1.get() && rightItem.getItem() == Items.IRON_BLOCK
                || leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_2.get() && rightItem.getItem() == Items.GOLD_BLOCK
                || leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_3.get() && rightItem.getItem() == Items.DIAMOND_BLOCK
                || leftItem.getItem() == ModItems.FISHERMANS_ROD_LEVEL_4.get() && rightItem.getItem() == Items.EMERALD_BLOCK;
    }

    private ArrayList<Component> getDrops() {
        ArrayList<Component> dropList = new ArrayList<>();
        Component two = getDropsComponent(20, 2);
        Component three = getDropsComponent(15, 3);
        Component four = getDropsComponent(10, 4);
        Component five = getDropsComponent(5, 5);
        if (this == ModItems.FISHERMANS_ROD_LEVEL_1.get()) dropList.add(two);
        if (this == ModItems.FISHERMANS_ROD_LEVEL_2.get()) dropList.addAll(List.of(two, three));
        if (this == ModItems.FISHERMANS_ROD_LEVEL_3.get()) dropList.addAll(List.of(two, three, four));
        if (this == ModItems.FISHERMANS_ROD_LEVEL_4.get()) dropList.addAll(List.of(two, three, four, five));
        return dropList;
    }

    private Component getDropsComponent(int percentage, int drops) {
        return JobsPlus.literal(ChatColor.green() + percentage + "% " + ChatColor.reset() + JobsPlus.translatable("tooltip.about.rod.drops", drops).getString());
    }
}
