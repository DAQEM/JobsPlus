package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.common.item.BackpackItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BackpackUtils {

    public static boolean filterItem(ItemStack stack) {
        return !(stack.getItem() instanceof BackpackItem) &&
                !Objects.requireNonNull(stack.getItem().getDescriptionId()).contains("backpack") &&
                !stack.getItem().getDescriptionId().contains("shulker_box");
    }

    @Nonnull
    public static Optional<UUID> getUUID(@Nonnull ItemStack stack) {
        if (stack.getItem() instanceof BackpackItem && stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("UUID"))
            return Optional.of(stack.getTag().getUUID("UUID"));
        else
            return Optional.empty();
    }
}
