package com.daqem.jobsplus.player.job.exp;

import com.daqem.jobsplus.JobsPlus;

public class ExpCollector {

    private int jobExp = 0;

    public void addExp(int exp) {
        JobsPlus.debug("Adding {} experience to the job.", exp);
        jobExp += exp;
    }

    public int getExp() {
        return jobExp;
    }

    public void clear() {
        jobExp = 0;
    }
}
