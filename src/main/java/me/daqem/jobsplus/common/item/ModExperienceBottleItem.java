package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.entity.ModThrownExperienceBottle;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModExperienceBottleItem extends ExperienceBottleItem {

    public ModExperienceBottleItem(Properties properties) {
        super(properties);
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
        int tier = stack.getOrCreateTag().getInt("tier");
        int exp = stack.getOrCreateTag().getInt("EXP");
        if (Screen.hasShiftDown()) {
            tooltip.add(JobsPlus.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(JobsPlus.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Enchanter"));
            int requiredJobLevel = ModRecipeManager.getRequiredJobLevelServer(stack);
            String level = requiredJobLevel == 101 ? "depends on use." : String.valueOf(requiredJobLevel);
            tooltip.add(JobsPlus.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
        } else {
            tooltip.add(JobsPlus.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (exp != 0 || tier != 0) {
            tooltip.add(JobsPlus.literal(" "));
            if (exp != 0)
                tooltip.add(JobsPlus.literal(ChatColor.green() + "EXP: " + ChatColor.reset() + exp));
            if (tier != 0) {
                tooltip.add(JobsPlus.literal(ChatColor.green() + "Tier: " + ChatColor.reset() + tier));
                tooltip.add(JobsPlus.literal(ChatColor.green() + "EXP: " + ChatColor.reset() + tierToEXP(tier)));
            }
        }
    }

    public int tierToEXP(int tier) {
        return tier == 1 ? 10 : tier == 2 ? 25 : tier == 3 ? 50 : tier == 4 ? 100 : tier == 5 ? 200 : tier == 6 ? 500 : 0;
    }
}
