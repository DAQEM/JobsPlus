package com.daqem.jobsplus.integration.arc.reward.type;

import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobCoinReward;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobExpMultiplierReward;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobExpReward;

public interface JobsPlusRewardType<T extends IReward> extends IRewardType<T> {

    IRewardType<JobExpReward> JOB_EXP = IRewardType.register(JobsPlus.API.getId("job_exp"), new JobExpReward.Serializer());
    IRewardType<JobExpMultiplierReward> JOB_EXP_MULTIPLIER = IRewardType.register(JobsPlus.API.getId("job_exp_multiplier"), new JobExpMultiplierReward.Serializer());
    IRewardType<JobCoinReward> JOB_COIN = IRewardType.register(JobsPlus.API.getId("job_coin"), new JobCoinReward.Serializer());

    static void init() {
    }
}
