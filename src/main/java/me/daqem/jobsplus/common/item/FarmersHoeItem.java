package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class FarmersHoeItem extends HoeItem {

    public FarmersHoeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide && player.isCrouching()) {
            ItemStack stack = player.getMainHandItem();
            CompoundTag orCreateTag = stack.getOrCreateTag();
            int mode = orCreateTag.getInt("mode");
            if (orCreateTag.contains("mode")) {
                if (stack.getItem() == ModItems.FARMERS_HOE_LEVEL_1.get()) {
                    orCreateTag.putInt("mode", 0);
                } else {
                    if (stack.getItem() == ModItems.FARMERS_HOE_LEVEL_2.get()) {
                        if (mode == 0) {
                            orCreateTag.putInt("mode", 1);
                        } else {
                            orCreateTag.putInt("mode", 0);
                        }
                    } else if (stack.getItem() == ModItems.FARMERS_HOE_LEVEL_3.get()
                            || stack.getItem() == ModItems.FARMERS_HOE_LEVEL_4.get()) {
                        if (mode != 2) {
                            orCreateTag.putInt("mode", mode + 1);
                        } else {
                            orCreateTag.putInt("mode", 0);
                        }
                    }
                }
            } else {
                orCreateTag.putInt("mode", 0);
            }
            HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, ChatColor.boldDarkGreen() + "Mode: " + ChatColor.green() + getModeString(stack));
            SoundHandler.playEXPOrbPickupSound(player, 0.7F, 1F);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            int level = 0;
            String modes = "";
            Item item = stack.getItem();
            if (item == ModItems.FARMERS_HOE_LEVEL_1.get()) {
                level = Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_1.get();
                modes = "Single Block";
            }
            if (item == ModItems.FARMERS_HOE_LEVEL_2.get()) {
                level = Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_2.get();
                modes = "Single Block, 3x3";
            }
            if (item == ModItems.FARMERS_HOE_LEVEL_3.get()) {
                level = Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_3.get();
                modes = "Single Block, 3x3, 5x5";
            }
            if (item == ModItems.FARMERS_HOE_LEVEL_4.get()) {
                level = Config.REQUIRED_LEVEL_FARMERS_HOE_LEVEL_4.get();
                modes = "Single Block, 3x3, 5x5";
            }
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job: " + ChatColor.reset() + "Farmer"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "About:"));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getRegistryName()).toString().replace("jobsplus:farmers_hoe_level_", "")));
            tooltip.add(new KeybindComponent(ChatColor.green() + "Modes: " + ChatColor.reset() + modes));
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Controls:"));
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Right-click a crop to harvest."));
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Shift + right-click to change the mode."));
        } else {
            if (stack.getOrCreateTag().contains("mode")) {
                tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Mode: " + ChatColor.reset() + getModeString(stack)));
            }
            tooltip.add(new KeybindComponent(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(new KeybindComponent(""));
            tooltip.add(new KeybindComponent(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

    public String getModeString(ItemStack stack) {
        if (stack.getOrCreateTag().getInt("mode") == 0) return "Single Block";
        if (stack.getOrCreateTag().getInt("mode") == 1) return "3x3";
        if (stack.getOrCreateTag().getInt("mode") == 2) return "5x5";
        return "";
    }
}
