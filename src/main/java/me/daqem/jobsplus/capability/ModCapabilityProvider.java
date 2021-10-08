package me.daqem.jobsplus.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModCapabilityProvider<C> implements ICapabilitySerializable<CompoundTag> {

    private final Capability<C> capability;

    //    private final Capability<IModCapability> capability = ModCapabilityImpl.MOD_CAPABILITY;
    private final ModCapabilityImpl instance = new ModCapabilityImpl();
    public final LazyOptional<IModCapability> lazyOptional = LazyOptional.of(()->instance);

    protected ModCapabilityProvider(@Nonnull final Capability<C> capability) {
        this.capability = capability;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == capability) return lazyOptional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return instance.serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { instance.deserializeNBT(nbt); }

    @Nonnull
    public static <C> ModCapabilityProvider<C> from(@Nonnull final Capability<C> cap) {
        return new ModCapabilityProvider<>(cap);
    }
}
