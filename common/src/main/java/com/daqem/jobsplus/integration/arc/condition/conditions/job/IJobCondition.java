package com.daqem.jobsplus.integration.arc.condition.conditions.job;

import net.minecraft.resources.Identifier;

public interface IJobCondition {

    Identifier getJobLocation();

    int getRequiredLevel();
}
