package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.handlers.ExperienceHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
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

    public EXPJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            if (JobGetters.getJobLevel(player, Jobs.ENCHANTER) >= 5) {
                InteractionHand usedHand = InteractionHand.OFF_HAND;
                if (player.getMainHandItem().getItem() instanceof EXPJarItem) {
                    usedHand = InteractionHand.MAIN_HAND;
                }
                ItemStack stack = player.getItemInHand(usedHand);
                if (usedHand == hand) {
                    CompoundTag nbt;
                    if (stack.hasTag()) {
                        nbt = stack.getTag();
                    } else {
                        nbt = new CompoundTag();
                    }
                    if (nbt == null) return super.use(level, player, hand);
                    if (player.isShiftKeyDown()) {
                        if (stack.getOrCreateTag().contains("EXP")) {
                            player.giveExperiencePoints(nbt.getInt("EXP"));
                            if (nbt.getInt("EXP") != 0) {
                                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("success.exp.extract", nbt.getInt("EXP")));
                                SoundHandler.playLevelUpSound(player, 0.7F, 1F);
                            }
                            nbt.putInt("EXP", 0);
                        }
                    } else {
                        int totalExperience = (int) (ExperienceHandler.getExperienceForLevel(player.experienceLevel) + (player.experienceProgress * player.getXpNeededForNextLevel()));
                        if (stack.getOrCreateTag().contains("EXP")) {
                            nbt.putInt("EXP", nbt.getInt("EXP") + totalExperience);
                        } else {
                            nbt.putInt("EXP", totalExperience);
                        }
                        if (totalExperience != 0) {
                            HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("success.exp.insert", totalExperience));
                            SoundHandler.playEXPOrbPickupSound(player, 0.7F, 1F);
                        }
                        player.giveExperiencePoints(-totalExperience);
                        stack.setTag(nbt);
                    }
                    return InteractionResultHolder.success(stack);
                }
            } else {
                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level p_41422_, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, p_41422_, tooltip, flag);

        if (Screen.hasShiftDown()) {
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Enchanter"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + 5));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Controls:"));
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Right-click to insert EXP"));
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Shift + right-click to extract EXP."));
        } else {
            if (stack.getOrCreateTag().contains("EXP")) {
                final int exp = stack.getOrCreateTag().getInt("EXP");
                if (exp == 0) {
                    tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + "Empty"));
                } else if (ExperienceHandler.getLevelFromExperience(exp) == 0) {
                    tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + exp));
                } else if (ExperienceHandler.getLevelFromExperience(exp) == 1) {
                    tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + exp + ", or " + ExperienceHandler.getLevelFromExperience(exp) + " level"));
                } else {
                    tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "EXP: " + ChatColor.reset() + exp + ", or " + ExperienceHandler.getLevelFromExperience(exp) + " levels"));
                }
            }
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
    }
}
