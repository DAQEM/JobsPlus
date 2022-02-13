package me.daqem.jobsplus.capability;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class SuperPowerCapabilityImpl implements ISuperPowerCapability {

    public static final ResourceLocation ID = JobsPlus.getId("jobs_superpower");

    public static Capability<ISuperPowerCapability> SUPERPOWER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    private int[] superpower = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    public int[] getSuperpower() {
        return this.superpower;
    }

    @Override
    public void setSuperpower(int[] value) {
        this.superpower = value;
    }

    @Override
    public void copyForRespawn(SuperPowerCapabilityImpl oldStore) {
        this.superpower = oldStore.superpower;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag nbt = new CompoundTag();
        nbt.putIntArray("superpower", this.getSuperpower());
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.setSuperpower(nbt.getIntArray("superpower"));

    }
}
