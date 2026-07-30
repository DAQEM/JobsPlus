package com.daqem.jobsplus.mixin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.daqem.jobsplus.level.JobsPlusLevelData;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.OptionalDynamic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.PrimaryLevelData;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin implements JobsPlusLevelData {

    @Unique
    private Map<UUID, Map<Identifier, LeaderboardPlayer>> jobsplus$playerJobEntries = new HashMap<>();
    @Unique
    private final Map<Identifier, List<LeaderboardPlayer>> jobsplus$leaderboardCache = new HashMap<>();
    @Unique
    private final Map<Identifier, Long> jobsplus$leaderboardCacheTime = new HashMap<>();

    @Override
    public Map<UUID, Map<Identifier, LeaderboardPlayer>> jobsplus$getPlayerJobEntries() {
        return this.jobsplus$playerJobEntries;
    }

    @Override
    public void jobsplus$setPlayerJobEntries(Map<UUID, Map<Identifier, LeaderboardPlayer>> entries) {
        this.jobsplus$playerJobEntries = entries;
    }

    @Override
    public void jobsplus$updatePlayerEntry(Player player, Job job) {
        jobsplus$leaderboardCache.remove(job.getJobInstance().getIdentifier());

        if (job.getLevel() > 0) {
            Map<Identifier, LeaderboardPlayer> playerEntries = this.jobsplus$playerJobEntries.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
            LeaderboardPlayer entry = playerEntries.computeIfAbsent(
                    job.getJobInstance().getIdentifier(),
                    k -> new LeaderboardPlayer(player.getUUID(), job.getJobInstance().getIdentifier()));

            entry.setPlayerName(player.getGameProfile().name());
            entry.setLevel(job.getLevel());
            entry.setExperience(job.getExperience());
        } else {
            jobsplus$removePlayerEntry(player, job);
        }
    }

    @Override
    public void jobsplus$removePlayerEntry(Player player, Job job) {
        jobsplus$leaderboardCache.remove(job.getJobInstance().getIdentifier());

        Map<Identifier, LeaderboardPlayer> playerEntries = this.jobsplus$playerJobEntries.get(player.getUUID());
        if (playerEntries != null) {
            playerEntries.remove(job.getJobInstance().getIdentifier());
            if (playerEntries.isEmpty()) {
                this.jobsplus$playerJobEntries.remove(player.getUUID());
            }
        }
    }

    @Override
    public List<LeaderboardPlayer> jobsplus$getSortedLeaderboard(Identifier jobLocation) {
        long now = System.currentTimeMillis();
        if (jobsplus$leaderboardCache.containsKey(jobLocation) && now - jobsplus$leaderboardCacheTime.getOrDefault(jobLocation, 0L) < 30000) {
            return jobsplus$leaderboardCache.get(jobLocation);
        }

        AtomicInteger rank = new AtomicInteger(1);
        List<LeaderboardPlayer> sorted = this.jobsplus$playerJobEntries.values()
                .stream()
                .filter(entry -> entry.containsKey(jobLocation))
                .map(entry -> entry.get(jobLocation))
                .sorted(Comparator.comparingInt(LeaderboardPlayer::getLevel).thenComparingDouble(LeaderboardPlayer::getExperience).reversed())
                .limit(100)
                .peek(player -> player.setRank(rank.getAndIncrement()))
                .toList();

        jobsplus$leaderboardCache.put(jobLocation, sorted);
        jobsplus$leaderboardCacheTime.put(jobLocation, now);
        return sorted;
    }

    @Override
    public void jobsplus$validateAndSync(Player player, List<Job> currentJobs) {
        Map<Identifier, LeaderboardPlayer> playerEntries = this.jobsplus$playerJobEntries.get(player.getUUID());

        if (playerEntries != null) {
            List<Identifier> jobsToRemove = new ArrayList<>();

            Set<Identifier> currentJobIds = currentJobs.stream()
                    .map(j -> j.getJobInstance().getIdentifier())
                    .collect(Collectors.toSet());

            for (Identifier jobLoc : playerEntries.keySet()) {
                if (!currentJobIds.contains(jobLoc)) {
                    jobsToRemove.add(jobLoc);
                }
            }

            for (Identifier jobLoc : jobsToRemove) {
                playerEntries.remove(jobLoc);
                jobsplus$leaderboardCache.remove(jobLoc);
            }
        }

        for (Job job : currentJobs) {
            this.jobsplus$updatePlayerEntry(player, job);
        }
    }

    @Inject(method = "<init>(Ljava/util/UUID;ZLnet/minecraft/world/level/storage/LevelData$RespawnData;JIZLjava/util/Set;Ljava/util/Set;Lnet/minecraft/world/level/LevelSettings;Lnet/minecraft/world/level/storage/PrimaryLevelData$SpecialWorldProperty;Lcom/mojang/serialization/Lifecycle;)V", at = @At("RETURN"))
    private void constructorInit(CallbackInfo ci) {
        this.jobsplus$playerJobEntries = new HashMap<>();
    }

    @Inject(method = "parse(Lcom/mojang/serialization/Dynamic;Lnet/minecraft/world/level/LevelSettings;Lnet/minecraft/world/level/storage/PrimaryLevelData$SpecialWorldProperty;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/world/level/storage/PrimaryLevelData;", at = @At("RETURN"))
    private static <T> void parse(Dynamic<T> input, LevelSettings settings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        OptionalDynamic<T> optionalDynamic = input.get("JobsPlusLeaderboard");

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

    @Inject(method = "setTagData(Lnet/minecraft/nbt/CompoundTag;Ljava/util/UUID;)V", at = @At("HEAD"))
    private void setTagData(CompoundTag tag, @Nullable UUID singlePlayerUUID, CallbackInfo ci) {
        CompoundTag leaderboardTag = new CompoundTag();
        ListTag playersTag = new ListTag();
        this.jobsplus$playerJobEntries.values().forEach(jobMap ->
                jobMap.values().forEach(player -> playersTag.add(player.toNbt()))
        );
        leaderboardTag.put("players", playersTag);
        tag.put("JobsPlusLeaderboard", leaderboardTag);
    }
}