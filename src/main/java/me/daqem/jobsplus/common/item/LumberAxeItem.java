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
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LumberAxeItem extends AxeItem {

    public LumberAxeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public @NotNull
    InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.isCrouching() && !level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("mode", tag.getInt("mode") == 0 ? 1 : 0);
            HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.boldDarkGreen() + "Mode: " + ChatColor.green() + getModeString(stack));
            SoundHandler.playChangeToolModeSound(player);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withAbout(String.valueOf(getMaxLogs()), TooltipBuilder.AboutType.AXE)
                .withControls(TooltipBuilder.ControlType.RIGHT_CLICK)
                .withMode(getModeString(stack))
                .withHoldShift()
                .withEnchantments(stack, false)
                .build());
    }

    public String getModeString(ItemStack stack) {
        return stack.getOrCreateTag().getInt("mode") == 0 ? "Tree Feller" : "Single Block";
    }

    public int getMaxLogs() {
        if (this == ModItems.LUMBERJACK_AXE_LEVEL_1.get()) return 16;
        else if (this == ModItems.LUMBERJACK_AXE_LEVEL_2.get()) return 48;
        else if (this == ModItems.LUMBERJACK_AXE_LEVEL_3.get()) return 128;
        else return 256;
    }
}
