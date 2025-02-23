package com.daqem.jobsplus.neoforge;

import com.daqem.jobsplus.neoforge.data.JobManagerNeoForge;
import com.daqem.jobsplus.neoforge.data.PowerupManagerNeoForge;
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
