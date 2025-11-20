package com.daqem.jobsplus.mixin;

import com.daqem.jobsplus.level.JobsPlusLevelData;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin implements JobsPlusLevelData {

    @Unique
    private Map<UUID, Map<ResourceLocation, LeaderboardPlayer>> jobsplus$playerJobEntries = new HashMap<>();
    @Unique
    private final Map<ResourceLocation, List<LeaderboardPlayer>> jobsplus$leaderboardCache = new HashMap<>();
    @Unique
    private final Map<ResourceLocation, Long> jobsplus$leaderboardCacheTime = new HashMap<>();

    @Override
    public Map<UUID, Map<ResourceLocation, LeaderboardPlayer>> jobsplus$getPlayerJobEntries() {
        return this.jobsplus$playerJobEntries;
    }

    @Override
    public void jobsplus$setPlayerJobEntries(Map<UUID, Map<ResourceLocation, LeaderboardPlayer>> entries) {
        this.jobsplus$playerJobEntries = entries;
    }

    @Override
    public void jobsplus$updatePlayerEntry(Player player, Job job) {
        if (job.getLevel() > 0) {
            Map<ResourceLocation, LeaderboardPlayer> playerEntries = this.jobsplus$playerJobEntries.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
            LeaderboardPlayer entry = playerEntries.computeIfAbsent(
                    job.getJobInstance().getLocation(),
                    k -> new LeaderboardPlayer(player.getUUID(), job.getJobInstance().getLocation()));

            entry.setPlayerName(player.getGameProfile().name());
            entry.setLevel(job.getLevel());
            entry.setExperience(job.getExperience());
        } else {
            jobsplus$removePlayerEntry(player, job);
        }
    }

    @Override
    public void jobsplus$removePlayerEntry(Player player, Job job) {
        Map<ResourceLocation, LeaderboardPlayer> playerEntries = this.jobsplus$playerJobEntries.get(player.getUUID());
        if (playerEntries != null) {
            playerEntries.remove(job.getJobInstance().getLocation());
            if (playerEntries.isEmpty()) {
                this.jobsplus$playerJobEntries.remove(player.getUUID());
            }
        }
    }

    @Override
    public List<LeaderboardPlayer> jobsplus$getSortedLeaderboard(ResourceLocation jobLocation) {
        long now = System.currentTimeMillis();
        if (jobsplus$leaderboardCache.containsKey(jobLocation) && now - jobsplus$leaderboardCacheTime.getOrDefault(jobLocation, 0L) < 30000) {
            return jobsplus$leaderboardCache.get(jobLocation);
        }

        AtomicInteger rank = new AtomicInteger(1);
        List<LeaderboardPlayer> sorted = this.jobsplus$playerJobEntries.values()
                .stream()
                .filter(entry -> entry.containsKey(jobLocation))
                .map(entry -> entry.get(jobLocation))
                .sorted(Comparator.comparing(LeaderboardPlayer::getLevel).thenComparing(LeaderboardPlayer::getExperience).reversed())
                .limit(100)
                .peek(player -> player.setRank(rank.getAndIncrement()))
                .toList();

        jobsplus$leaderboardCache.put(jobLocation, sorted);
        jobsplus$leaderboardCacheTime.put(jobLocation, now);
        return sorted;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/LevelSettings;Lnet/minecraft/world/level/levelgen/WorldOptions;Lnet/minecraft/world/level/storage/PrimaryLevelData$SpecialWorldProperty;Lcom/mojang/serialization/Lifecycle;)V", at = @At("RETURN"))
    private void constructorInit(CallbackInfo ci) {
        this.jobsplus$playerJobEntries = new HashMap<>();
    }

    @Inject(method = "parse", at = @At("RETURN"))
    private static <T> void parse(Dynamic<T> dynamic, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle lifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        OptionalDynamic<T> optionalDynamic = dynamic.get("JobsPlusLeaderboard");

        List<LeaderboardPlayer> players = optionalDynamic.get("players")
                .asStream()
                .map(LeaderboardPlayer::deserialize)
                .toList();

        if (cir.getReturnValue() instanceof JobsPlusLevelData data) {
            data.jobsplus$setPlayerJobEntries(players.stream()
                    .collect(HashMap::new, (map, player) ->
                            map.computeIfAbsent(player.getUuid(), k -> new HashMap<>()).put(player.getJobLocation(), player), HashMap::putAll)
            );
        }
    }

    @Inject(method = "setTagData(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    private void setTagData(RegistryAccess registryAccess, CompoundTag compoundTag, @Nullable CompoundTag compoundTag2, CallbackInfo ci) {
        CompoundTag leaderboardTag = new CompoundTag();
        ListTag playersTag = new ListTag();
        this.jobsplus$playerJobEntries.values().forEach(jobMap ->
                jobMap.values().forEach(player -> playersTag.add(player.toNbt()))
        );
        leaderboardTag.put("players", playersTag);
        compoundTag.put("JobsPlusLeaderboard", leaderboardTag);
    }
}