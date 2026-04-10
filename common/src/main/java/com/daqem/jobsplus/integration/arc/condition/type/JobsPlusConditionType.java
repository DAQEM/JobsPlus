package com.daqem.jobsplus.integration.arc.condition.type;

import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.HasJobCondition;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.JobExperiencePercentageCondition;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.JobLevelCondition;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.powerup.PowerupNotActiveCondition;
import com.daqem.jobsplus.integration.arc.condition.conditions.powerup.HasPowerupActivatedCondition;

public interface JobsPlusConditionType<T extends ICondition> extends IConditionType<T> {

    IConditionType<JobExperiencePercentageCondition> JOB_EXPERIENCE_PERCENTAGE = IConditionType.register(JobsPlus.API.getId("job_experience_percentage"), new JobExperiencePercentageCondition.Serializer());
    IConditionType<JobLevelCondition> JOB_LEVEL = IConditionType.register(JobsPlus.API.getId("job_level"), new JobLevelCondition.Serializer());
    IConditionType<PowerupNotActiveCondition> POWERUP_NOT_ACTIVE = IConditionType.register(JobsPlus.API.getId("powerup_not_active"), new PowerupNotActiveCondition.Serializer());
    IConditionType<HasJobCondition> HAS_JOB = IConditionType.register(JobsPlus.API.getId("has_job"), new HasJobCondition.Serializer());

    IConditionType<HasPowerupActivatedCondition> HAS_POWERUP_ACTIVATED = IConditionType.register(JobsPlus.API.getId("has_powerup_activated"), new HasPowerupActivatedCondition.Serializer());

    static void init() {
    }
}
