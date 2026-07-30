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


    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Override
    public JobsPlusLevelData jobsplus$getLevelData() {
        return (JobsPlusLevelData) Objects.requireNonNull(Objects.requireNonNull(this.getServer()).getLevel(Level.OVERWORLD)).getLevelData();
    }
}