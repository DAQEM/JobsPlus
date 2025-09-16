package com.daqem.jobsplus.player;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface JobsPlayer {

    List<Job> jobsplus$getJobs();

    List<JobInstance> jobsplus$getJobInstances();

    List<Job> jobsplus$getInactiveJobs();

    @Nullable
    Job jobsplus$addNewJob(JobInstance job);

    void jobsplus$removeJob(JobInstance job);

    void jobsplus$removeActionHolders(Job job);

    Job jobsplus$getJob(@Nullable JobInstance jobLocation);

    double jobsplus$getCoins();

    void jobsplus$addCoins(double coins);

    void jobsplus$setCoins(double coins);

    String jobsplus$getName();

    Player jobsplus$getPlayer();

    List<IActionHolder> jobsplus$getActionHolders();
}
