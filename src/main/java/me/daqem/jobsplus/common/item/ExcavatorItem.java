package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.ToolFunctions;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExcavatorItem extends JobsPlusItem.Shovel {

    public ExcavatorItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties, Jobs.DIGGER);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        return ToolFunctions.breakInRadius(state, level, pos, player, getJob(), getRequiredLevel());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> use = super.use(level, player, hand);
        if (!player.isShiftKeyDown() || level.isClientSide) return use;
        ItemHandler.switchMode(player, hand);
        return use;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(getJob(), getRequiredLevel())
                .withAbout(ItemHandler.getAvailableModeString(stack), TooltipBuilder.AboutType.MODES)
                .withControls(TooltipBuilder.ControlType.RIGHT_CLICK)
                .withMode(ItemHandler.getModeString(stack))
                .withHoldShift()
                .withEnchantments(stack, false)
                .build());
    }
}
