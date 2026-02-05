package com.daqem.jobsplus.player;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.job.Job;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public interface JobsPlayer {

    List<Job> jobsplus$getJobs();

    List<JobInstance> jobsplus$getJobInstances();

    List<Job> jobsplus$getInactiveJobs();

    @Nullable
    Job jobsplus$addNewJob(JobInstance job);

    void jobsplus$removeJob(JobInstance job);

    Job jobsplus$getJob(@Nullable JobInstance jobInstance);

    Job jobsplus$getJob(Identifier jobLocation);

    double jobsplus$getCoins();

    void jobsplus$addCoins(double coins);

    void jobsplus$setCoins(double coins);

    String jobsplus$getName();

    Player jobsplus$getPlayer();

    List<IActionHolder> jobsplus$getActionHolders();

    boolean jobsplus$isExpEnabled();

    void jobsplus$setExpEnabled(boolean enabled);

    boolean jobsplus$isCoinsEnabled();

    void jobsplus$setCoinsEnabled(boolean enabled);
}
