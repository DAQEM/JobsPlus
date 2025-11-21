package com.daqem.jobsplus.client.gui.powerups;

import com.daqem.jobsplus.player.job.Job;

public class PowerupsScreenState {

    private final Job job;
    private double coins;

    public PowerupsScreenState(Job job, double coins) {
        this.job = job;
        this.coins = coins;
    }

    public Job getJob() {
        return job;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }
}
