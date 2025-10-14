package com.daqem.jobsplus.integration.arc.holder.type;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;

public interface JobsPlusActionHolderType<T extends IActionHolder> extends IActionHolderType<T> {

    IActionHolderType<JobInstance> JOB_INSTANCE = IActionHolderType.register(JobsPlus.getId("job"), new JobInstance.Serializer());
    IActionHolderType<PowerupInstance> POWERUP_INSTANCE = IActionHolderType.register(JobsPlus.getId("powerup"), new PowerupInstance.Serializer());

    static void init() {
    }
}
