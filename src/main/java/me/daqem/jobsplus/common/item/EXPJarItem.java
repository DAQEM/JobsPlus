package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.handlers.ExperienceHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EXPJarItem extends Item {

    private static final String EXP = "EXP";

    public EXPJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> resultHolder = super.use(level, player, hand);
        if (player instanceof ServerPlayer serverPlayer) {
            if (!(JobGetters.getJobLevel(player, ModRecipeManager.getJobServer(player.getItemInHand(hand))) >= ModRecipeManager.getRequiredJobLevelServer(player.getItemInHand(hand)))) {
                HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, JobsPlus.translatable("error.magic").withStyle(ChatFormatting.RED));
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
                HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, JobsPlus.literal(String.valueOf(nbtExp)).withStyle(ChatFormatting.DARK_GREEN).append(JobsPlus.translatable("success.exp.extract").withStyle(ChatFormatting.GREEN)));
                SoundHandler.playChangeToolModeSound(serverPlayer);
            } else {
                int totalExperience = (int) (ExperienceHandler.getExperienceForLevel(serverPlayer.experienceLevel) + (serverPlayer.experienceProgress * serverPlayer.getXpNeededForNextLevel()));

                if (totalExperience == 0) return resultHolder;

                if (stack.getOrCreateTag().contains(EXP)) nbt.putInt(EXP, nbtExp + totalExperience);
                else nbt.putInt(EXP, totalExperience);

                serverPlayer.giveExperiencePoints(-totalExperience);
                stack.setTag(nbt);
                HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, JobsPlus.literal(String.valueOf(totalExperience)).withStyle(ChatFormatting.DARK_GREEN).append(JobsPlus.translatable("success.exp.insert").withStyle(ChatFormatting.GREEN)));
                SoundHandler.playChangeToolModeSound(serverPlayer);
            }
            return InteractionResultHolder.success(stack);
        }
        return resultHolder;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level p_41422_, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, p_41422_, tooltip, flag);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withControls(TooltipBuilder.ControlType.INSERT_EXTRACT)
                .withComponent(getEXPComponent(stack), TooltipBuilder.ShiftType.NO_SHIFT)
                .withHoldShift()
                .build());
    }

    private Component getEXPComponent(ItemStack stack) {
        if (stack.getOrCreateTag().contains(EXP)) {
            final int exp = stack.getOrCreateTag().getInt(EXP);
            final int levelFromExperience = ExperienceHandler.getLevelFromExperience(exp);
            MutableComponent component = JobsPlus.translatable("tooltip.exp_jar.exp").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).withBold(true));
            if (exp == 0) {
                component.append(ChatColor.white() + JobsPlus.translatable("tooltip.exp_jar.empty").getString());
            } else {
                component.append(ChatColor.white() + exp);
                if (levelFromExperience != 0)
                    component.append(ChatColor.white() + JobsPlus.translatable(levelFromExperience == 1 ? "tooltip.exp_jar.level" : "tooltip.exp_jar.levels", levelFromExperience).getString());
            }
            return component;
        }
        return TooltipBuilder.WHITE_SPACE;
    }
}
