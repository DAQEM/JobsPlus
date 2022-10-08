package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.handlers.ExperienceHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EXPJarItem extends JobsPlusItem {

    private static final String EXP = "EXP";

    public EXPJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> resultHolder = super.use(level, player, hand);
        if (player instanceof ServerPlayer serverPlayer) {
            if (!(JobGetters.getJobLevel(serverPlayer, Jobs.ENCHANTER) >= getRequiredLevel())) {
                HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, TranslatableString.get("error.magic"));
                return resultHolder;
            }

            InteractionHand usedHand = serverPlayer.getMainHandItem().getItem() instanceof EXPJarItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            if (usedHand != hand) return resultHolder;

            ItemStack stack = serverPlayer.getItemInHand(usedHand);
            CompoundTag nbt = stack.hasTag() ? stack.getTag() : new CompoundTag();

            if (nbt == null) return resultHolder;

            int nbtExp = nbt.getInt(EXP);

            if (serverPlayer.isShiftKeyDown()) {
                if (!stack.getOrCreateTag().contains(EXP)) return resultHolder;
                if (nbtExp == 0) return resultHolder;

                serverPlayer.giveExperiencePoints(nbtExp);
                nbt.putInt(EXP, 0);
                HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, Component.literal(String.valueOf(nbtExp)).withStyle(ChatFormatting.DARK_GREEN).append(JobsPlus.translatable("success.exp.extract").copy().withStyle(ChatFormatting.GREEN)));
                SoundHandler.playChangeToolModeSound(serverPlayer);
            } else {
                int totalExperience = (int) (ExperienceHandler.getExperienceForLevel(serverPlayer.experienceLevel) + (serverPlayer.experienceProgress * serverPlayer.getXpNeededForNextLevel()));

                if (totalExperience == 0) return resultHolder;

                if (stack.getOrCreateTag().contains(EXP)) nbt.putInt(EXP, nbtExp + totalExperience);
                else nbt.putInt(EXP, totalExperience);

                serverPlayer.giveExperiencePoints(-totalExperience);
                stack.setTag(nbt);
                HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, Component.literal(String.valueOf(nbtExp)).withStyle(ChatFormatting.DARK_GREEN).append(JobsPlus.translatable("success.exp.insert").copy().withStyle(ChatFormatting.GREEN)));
                SoundHandler.playChangeToolModeSound(serverPlayer);
            }
            return InteractionResultHolder.success(stack);
        }
        return resultHolder;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level p_41422_, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, p_41422_, tooltip, flag);

        if (Screen.hasShiftDown()) {
            tooltip.addAll(List.of(
                    Component.literal(ChatColor.boldDarkGreen() + "Requirements:"),
                    Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Enchanter"),
                    Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + getRequiredLevel()),
                    Component.literal(""),
                    Component.literal(ChatColor.boldDarkGreen() + "Controls:"),
                    Component.literal(ChatColor.gray() + "Right-click to insert EXP"),
                    Component.literal(ChatColor.gray() + "Shift + right-click to extract EXP."))
            );
        } else {
            if (stack.getOrCreateTag().contains(EXP)) {
                final int exp = stack.getOrCreateTag().getInt(EXP);
                if (exp == 0) {
                    tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + "Empty"));
                } else if (ExperienceHandler.getLevelFromExperience(exp) == 0) {
                    tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + exp));
                } else if (ExperienceHandler.getLevelFromExperience(exp) == 1) {
                    tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + exp + ", or " + ExperienceHandler.getLevelFromExperience(exp) + " level"));
                } else {
                    tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + exp + ", or " + ExperienceHandler.getLevelFromExperience(exp) + " levels"));
                }
            }
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
    }
}
