package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FarmersHoeItem extends HoeItem {

    public static final String MODE = "mode";

    public FarmersHoeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, hand);
        if (level.isClientSide || !player.isCrouching()) return use;
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(MODE)) tag.putInt(MODE, 0);
        int tagInt = tag.getInt(MODE);
        if ((stack.getDescriptionId().contains("_level_2") && tagInt != 1) || ((stack.getDescriptionId().contains("_level_3") || stack.getDescriptionId().contains("_level_4")) && tagInt != 2)) {
            tag.putInt(MODE, ++tagInt);
        } else {
            tag.putInt(MODE, 0);
        }
        HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.boldDarkGreen() + "Mode: " + ChatColor.green() + getModeString(stack));
        SoundHandler.playChangeToolModeSound(player);
        return use;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.is(ModItems.FARMERS_HOE_LEVEL_4.get())) {
            tooltip.addAll(new TooltipBuilder()
                    .withRequirement(stack)
                    .withAbout(getAvailableModeString(stack), TooltipBuilder.AboutType.HOE_LVL_4)
                    .withControls(TooltipBuilder.ControlType.HOE)
                    .withMode(getModeString(stack))
                    .withHoldShift()
                    .withEnchantments(stack, false)
                    .build());
        } else {
            tooltip.addAll(new TooltipBuilder()
                    .withRequirement(stack)
                    .withAbout(getAvailableModeString(stack), TooltipBuilder.AboutType.HOE)
                    .withControls(TooltipBuilder.ControlType.HOE)
                    .withMode(getModeString(stack))
                    .withHoldShift()
                    .withEnchantments(stack, false)
                    .build());
        }
    }

    public String getModeString(ItemStack stack) {
        int mode = stack.getOrCreateTag().getInt(MODE);
        return mode == 0 ? "Single Block" : mode == 1 ? "3x3" : "5x5";
    }

    public String getAvailableModeString(ItemStack stack) {
        if (stack.getDescriptionId().contains("_level_1")) return "Single Block";
        if (stack.getDescriptionId().contains("_level_2")) return "Single Block, 3x3";
        return "Single Block, 3x3, 5x5";
    }
}
