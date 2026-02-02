package com.daqem.jobsplus.level;

import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface JobsPlusLevelData {
    Map<UUID, Map<Identifier, LeaderboardPlayer>> jobsplus$getPlayerJobEntries();
    void jobsplus$setPlayerJobEntries(Map<UUID, Map<Identifier, LeaderboardPlayer>> entries);
    void jobsplus$updatePlayerEntry(Player player, Job job);
    void jobsplus$removePlayerEntry(Player player, Job job);
    List<LeaderboardPlayer> jobsplus$getSortedLeaderboard(Identifier jobLocation);
    void jobsplus$validateAndSync(Player player, List<Job> currentJobs);
}