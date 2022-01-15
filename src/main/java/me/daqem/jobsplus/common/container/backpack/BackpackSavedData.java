package me.daqem.jobsplus.common.container.backpack;

import me.daqem.jobsplus.handlers.BackpackItemHandler;
import me.daqem.jobsplus.utils.enums.Backpack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;
import java.util.UUID;

public class BackpackSavedData {

    private final UUID uuid;
    private final BackpackItemHandler inventory;
    private final LazyOptional<IItemHandler> optional;
    private Backpack tier;

    public BackpackSavedData(UUID uuid, Backpack tier) {
        this.uuid = uuid;
        this.tier = tier;

        this.inventory = new BackpackItemHandler(tier.slots);
        this.optional = LazyOptional.of(() -> this.inventory);
    }

    public BackpackSavedData(UUID uuid, CompoundTag incomingNBT) {
        this.uuid = uuid;
        this.tier = Backpack.values()[Math.min(incomingNBT.getInt("Tier"), Backpack.HUGE.ordinal())];
        this.inventory = new BackpackItemHandler(this.tier.slots);
        this.inventory.deserializeNBT(incomingNBT.getCompound("Inventory"));
        this.optional = LazyOptional.of(() -> this.inventory);
    }

    public static Optional<BackpackSavedData> fromNBT(CompoundTag nbt) {
        if (nbt.contains("UUID")) {
            UUID uuid = nbt.getUUID("UUID");
            return Optional.of(new BackpackSavedData(uuid, nbt));
        }
        return Optional.empty();
    }

    public LazyOptional<IItemHandler> getOptional() {
        return this.optional;
    }

    public IItemHandler getHandler() {
        return this.inventory;
    }

    public Backpack getTier() {
        return this.tier;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void upgrade(Backpack newTier) {
        if (newTier.ordinal() > this.tier.ordinal()) {
            this.tier = newTier;
            this.inventory.upgrade(this.tier.slots);
        }
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("UUID", this.uuid);
        nbt.putInt("Tier", this.tier.ordinal());
        nbt.put("Inventory", this.inventory.serializeNBT());
        return nbt;
    }
}
