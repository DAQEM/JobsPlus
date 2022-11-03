package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.ModItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HunterBowItem extends BowItem {

    public HunterBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || stack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;
            ItemStack itemstack = player.getProjectile(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }
                float f = getPowerForTime(i);
                if (!(JobGetters.getJobLevel(player, ModRecipeManager.getJob(stack)) >= ModRecipeManager.getRequiredJobLevel(stack))) {
                    f = 0.1F;
                    if (!level.isClientSide) {
                        HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, JobsPlus.translatable("error.magic").withStyle(ChatFormatting.RED));
                    }
                }
                if (!((double) f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, player));
                    if (!level.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrow abstractarrow = arrowitem.createArrow(level, itemstack, player);
                        abstractarrow = customArrow(abstractarrow);
                        abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            abstractarrow.setCritArrow(true);
                        }
                        int j = stack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
                        if (j > 0) {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5D + 0.5D + extraDamage(stack));
                        } else {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + 0.5D + extraDamage(stack));
                        }
                        int k = stack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
                        if (k > 0) {
                            abstractarrow.setKnockback(k);
                        }
                        if (stack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
                            abstractarrow.setSecondsOnFire(100);
                        }
                        ModItemUtils.damageItem(1, stack, player);
                        if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }
                        level.addFreshEntity(abstractarrow);
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withAbout(String.valueOf(extraDamage(stack)), TooltipBuilder.AboutType.BOW)
                .withHoldShift()
                .withEnchantments(stack, false)
                .build());
    }

    private double extraDamage(ItemStack stack) {
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_1.get())) return 1D;
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_2.get())) return 2.5D;
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_3.get())) return 4D;
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_4.get())) return 6D;
        return 0D;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_1.get())) return 72000;
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_2.get())) return 69000;
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_3.get())) return 66000;
        if (stack.is(ModItems.HUNTERS_BOW_LEVEL_4.get())) return 63000;
        return 72000;
    }

    @Override
    public boolean isValidRepairItem(ItemStack leftItem, @NotNull ItemStack rightItem) {
        return leftItem.is(ModItems.HUNTERS_BOW_LEVEL_1.get()) && rightItem.is(Items.IRON_BLOCK)
                || leftItem.is(ModItems.HUNTERS_BOW_LEVEL_2.get()) && rightItem.is(Items.GOLD_BLOCK)
                || leftItem.is(ModItems.HUNTERS_BOW_LEVEL_3.get()) && rightItem.is(Items.DIAMOND_BLOCK)
                || leftItem.is(ModItems.HUNTERS_BOW_LEVEL_4.get()) && rightItem.is(Items.EMERALD_BLOCK);
    }
}
