package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HunterSwordItem extends JobsPlusItem.Sword {

    public HunterSwordItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties, Jobs.HUNTER);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(getJob(), getRequiredLevel())
                .withHoldShift()
                .withEnchantments(stack, false)
                .build());
    }

}
