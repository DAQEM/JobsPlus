package com.daqem.jobsplus.player.job.exp;

public class ExpCollector {

    private double jobExp = 0;

    public void addExp(double exp) {
        jobExp += exp;
    }

    public double getExp() {
        return jobExp;
    }

    public void clear() {
        jobExp = 0;
    }
}
