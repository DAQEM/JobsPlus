package com.daqem.jobsplus.forge;

import com.daqem.jobsplus.forge.data.JobManagerNeoForge;
import com.daqem.jobsplus.forge.data.PowerupManagerNeoForge;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupManager;

public class JobsPlusExpectPlatformImpl {

    public static JobManager getJobManager() {
        return new JobManagerNeoForge();
    }

    public static PowerupManager getPowerupManager() {
        return new PowerupManagerNeoForge();
    }
}
