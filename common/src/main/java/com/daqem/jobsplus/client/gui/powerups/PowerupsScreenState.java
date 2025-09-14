package com.daqem.jobsplus.client.gui.powerups;

import com.daqem.jobsplus.player.job.Job;

public class PowerupsScreenState {

    private final Job job;
    private int coins;

    public PowerupsScreenState(Job job, int coins) {
        this.job = job;
        this.coins = coins;
    }

    public Job getJob() {
        return job;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
