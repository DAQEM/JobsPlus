package me.daqem.jobsplus.common.container.backpack;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.enums.Backpack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BackpackHandler extends SavedData {

    public static final BackpackHandler blankClient = new BackpackHandler();
    private static final HashMap<UUID, BackpackSavedData> data = new HashMap<>();
    private static final String NAME = JobsPlus.MOD_ID + "_backpack_data";

    public static BackpackHandler get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            return Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD))
                    .getDataStorage().computeIfAbsent(BackpackHandler::load, BackpackHandler::new, NAME);
        else
            return blankClient;
    }

    public static BackpackHandler load(CompoundTag nbt) {
        if (nbt.contains("Backpacks")) {
            ListTag list = nbt.getList("Backpacks", Tag.TAG_COMPOUND);
            list.forEach((backpackNBT) -> BackpackSavedData.fromNBT((CompoundTag) backpackNBT).ifPresent((backpack) -> data.put(backpack.getUuid(), backpack)));
        }
        return new BackpackHandler();
    }

    public HashMap<UUID, BackpackSavedData> getMap() {
        return data;
    }

    public BackpackSavedData getOrCreateBackpack(UUID uuid, Backpack tier) {
        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new BackpackSavedData(id, tier);
        });
    }

    public LazyOptional<IItemHandler> getCapability(ItemStack stack) {
        if (stack.getOrCreateTag().contains("UUID")) {
            UUID uuid = Objects.requireNonNull(stack.getTag()).getUUID("UUID");
            if (data.containsKey(uuid))
                return data.get(uuid).getOptional();
        }

        return LazyOptional.empty();
    }

    @Override
    @Nonnull
    public CompoundTag save(CompoundTag compound) {
        ListTag backpacks = new ListTag();
        data.forEach(((uuid, BackpackSavedData) -> backpacks.add(BackpackSavedData.toNBT())));
        compound.put("Backpacks", backpacks);
        return compound;
    }
}
