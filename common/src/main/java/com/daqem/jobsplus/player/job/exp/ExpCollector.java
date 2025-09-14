package com.daqem.jobsplus.player.job.exp;

import com.daqem.jobsplus.JobsPlus;

public class ExpCollector {

    private double jobExp = 0;

    public void addExp(double exp) {
        JobsPlus.debug("Adding {} experience to the job.", exp);
        jobExp += exp;
    }

    public double getExp() {
        return jobExp;
    }

    public void clear() {
        jobExp = 0;
    }
}
