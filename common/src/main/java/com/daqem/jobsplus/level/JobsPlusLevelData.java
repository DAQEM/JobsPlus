package com.daqem.jobsplus.level;

import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;

public interface JobsPlusLevelData {
    Map<UUID, Map<ResourceLocation, LeaderboardPlayer>> jobsplus$getPlayerJobEntries();
    void jobsplus$setPlayerJobEntries(Map<UUID, Map<ResourceLocation, LeaderboardPlayer>> entries);
    void jobsplus$updatePlayerEntry(Player player, Job job);
    void jobsplus$removePlayerEntry(Player player, Job job);
}