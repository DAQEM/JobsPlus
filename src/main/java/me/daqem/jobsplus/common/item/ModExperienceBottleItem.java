package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.common.entity.ModThrownExperienceBottle;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModExperienceBottleItem extends JobsPlusItem.ExperienceBottle {

    public ModExperienceBottleItem(Properties properties) {
        super(properties, Jobs.ENCHANTER);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            int exp = itemstack.getOrCreateTag().getInt("EXP");
            if (exp == 0) {
                int tier = itemstack.getOrCreateTag().getInt("tier");
                if (tier != 0) {
                    exp = tierToEXP(tier);
                }
            }
            ModThrownExperienceBottle thrownExperienceBottle = new ModThrownExperienceBottle(level, player, exp);
            thrownExperienceBottle.setItem(itemstack);
            thrownExperienceBottle.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.7F, 1.0F);
            level.addFreshEntity(thrownExperienceBottle);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        setRequiredLevel(JobGetters.getRequiredJobLevelForItem(stack));
        int tier = stack.getOrCreateTag().getInt("tier");
        int exp = stack.getOrCreateTag().getInt("EXP");
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Enchanter"));
            String level = getRequiredLevel() == 101 ? "depends on use." : String.valueOf(getRequiredLevel());
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
        } else {
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (exp != 0 || tier != 0) {
            tooltip.add(Component.literal(" "));
            if (exp != 0)
                tooltip.add(Component.literal(ChatColor.green() + "EXP: " + ChatColor.reset() + exp));
            if (tier != 0) {
                tooltip.add(Component.literal(ChatColor.green() + "Tier: " + ChatColor.reset() + tier));
                tooltip.add(Component.literal(ChatColor.green() + "EXP: " + ChatColor.reset() + tierToEXP(tier)));
            }
        }
    }

    public int tierToEXP(int tier) {
        if (tier == 1) {
            return 10;
        }
        if (tier == 2) {
            return 25;
        }
        if (tier == 3) {
            return 50;
        }
        if (tier == 4) {
            return 100;
        }
        if (tier == 5) {
            return 200;
        }
        if (tier == 6) {
            return 500;
        }
        return 0;
    }
}
