package com.daqem.jobsplus.mixin;

import com.daqem.jobsplus.level.JobsPlusLevelData;
import com.daqem.jobsplus.level.JobsPlusServerLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements JobsPlusServerLevel {


    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, bl, bl2, l, i);
    }

    @Override
    public JobsPlusLevelData jobsplus$getLevelData() {
        return (JobsPlusLevelData) Objects.requireNonNull(Objects.requireNonNull(this.getServer()).getLevel(Level.OVERWORLD)).getLevelData();
    }
}