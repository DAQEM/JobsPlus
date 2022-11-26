package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.handlers.VeinMinerHandler;
import me.daqem.jobsplus.utils.ToolFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExcavatorItem extends ShovelItem {

    public ExcavatorItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if (level.isClientSide) return super.canAttackBlock(state, level, pos, player);
        ItemStack stack = player.getMainHandItem().getItem() instanceof ShovelItem ? player.getMainHandItem() : player.getOffhandItem();
        VeinMinerHandler.disableVeinMiningIfNotOre((ServerPlayer) player, state.getBlock());
        return ToolFunctions.breakInRadius(state, level, pos, player, ModRecipeManager.getJobServer(stack), ModRecipeManager.getRequiredJobLevelServer(stack));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown() && !level.isClientSide) ItemHandler.switchMode(player, hand);
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withAbout(ItemHandler.getAvailableModeString(stack), TooltipBuilder.AboutType.MODES)
                .withControls(TooltipBuilder.ControlType.RIGHT_CLICK)
                .withMode(ItemHandler.getModeString(stack))
                .withHoldShift()
                .withEnchantments(stack, false)
                .build());
    }
}
