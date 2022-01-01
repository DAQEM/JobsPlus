package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.enums.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class HunterBowItem extends BowItem {

    public HunterBowItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_1.get()) {
            return 72000;
        }
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_2.get()) {
            return 69000;
        }
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_3.get()) {
            return 66000;
        }
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_4.get()) {
            return 63000;
        }
        return 72000;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = player.getProjectile(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
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

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);

                        if (j > 0) {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5D + 0.5D + extraDamage(stack));
                        } else {

                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + 0.5D + extraDamage(stack));
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0) {
                            abstractarrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            abstractarrow.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, player, (p_40665_) -> p_40665_.broadcastBreakEvent(player.getUsedItemHand()));
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
        if (Screen.hasShiftDown()) {
            int level = 0;
            Item item = stack.getItem();
            if (item == ModItems.HUNTERS_BOW_LEVEL_1.get()) {
                level = 5;
            }
            if (item == ModItems.HUNTERS_BOW_LEVEL_2.get()) {
                level = 25;
            }
            if (item == ModItems.HUNTERS_BOW_LEVEL_3.get()) {
                level = 50;
            }
            if (item == ModItems.HUNTERS_BOW_LEVEL_4.get()) {
                level = 75;
            }
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Hunter"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getRegistryName()).toString().replace("jobsplus:hunters_bow_level_", "")));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Extra Base Damage: " + ChatColor.reset() + extraDamage(stack)));
        } else {
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

    private double extraDamage(ItemStack stack) {
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_1.get()) {
            return 1D;
        }
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_2.get()) {
            return 2.5D;
        }
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_3.get()) {
            return 4D;
        }
        if (stack.getItem() == ModItems.HUNTERS_BOW_LEVEL_4.get()) {
            return 6D;
        }
        return 0D;
    }
}
