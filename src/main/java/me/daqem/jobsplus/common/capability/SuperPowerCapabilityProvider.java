package me.daqem.jobsplus.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SuperPowerCapabilityProvider<C> implements ICapabilitySerializable<CompoundTag> {

    private final Capability<C> capability;

    private final SuperPowerCapabilityImpl instance = new SuperPowerCapabilityImpl();
    public final LazyOptional<ISuperPowerCapability> lazyOptional = LazyOptional.of(() -> instance);

    protected SuperPowerCapabilityProvider(@Nonnull final Capability<C> capability) {
        this.capability = capability;
    }

    @Nonnull
    public static <C> SuperPowerCapabilityProvider<C> from(@Nonnull final Capability<C> cap) {
        return new SuperPowerCapabilityProvider<>(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == capability) return lazyOptional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }
}
