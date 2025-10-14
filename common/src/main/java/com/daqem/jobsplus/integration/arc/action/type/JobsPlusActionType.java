package com.daqem.jobsplus.integration.arc.action.type;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.IActionType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.action.actions.job.JobExpAction;
import com.daqem.jobsplus.integration.arc.action.actions.job.JobLevelUpAction;

public interface JobsPlusActionType<T extends IAction> extends IActionType<T> {

    IActionType<JobExpAction> JOB_EXP = IActionType.register(JobsPlus.getId("on_job_exp"), new JobExpAction.Serializer());
    IActionType<JobLevelUpAction> JOB_LEVEL_UP = IActionType.register(JobsPlus.getId("on_job_level_up"), new JobLevelUpAction.Serializer());

    static void init() {
    }
}
