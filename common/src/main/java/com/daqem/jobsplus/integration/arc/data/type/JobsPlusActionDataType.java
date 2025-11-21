package com.daqem.jobsplus.integration.arc.data.type;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.Job;

import net.minecraft.resources.ResourceLocation;

public interface JobsPlusActionDataType<T> extends IActionDataType<T> {

    IActionDataType<Double> JOB_EXP = register(JobsPlus.getId("job_exp"));
    IActionDataType<Job> ONLY_FOR_JOB = register(JobsPlus.getId("only_for_job"));

    static <T> IActionDataType<T> register(ResourceLocation location) {
        return () -> location;
    }
}
