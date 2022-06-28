package me.daqem.jobsplus.utils;

import com.mojang.brigadier.context.CommandContext;
import me.daqem.jobsplus.common.container.backpack.BackpackHandler;
import me.daqem.jobsplus.common.item.BackpackItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

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

    public static Set<String> getUUIDSuggestions(CommandContext<CommandSourceStack> commandSource) {
        BackpackHandler backpacks = BackpackHandler.get();
        Set<String> list = new HashSet<>();

        backpacks.getMap().forEach((uuid, backpackData) -> list.add(uuid.toString()));

        return list;
    }
}
