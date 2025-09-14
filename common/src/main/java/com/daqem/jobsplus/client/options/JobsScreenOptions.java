package com.daqem.jobsplus.client.options;

import com.daqem.jobsplus.client.screen.job.tab.LeftTab;
import com.daqem.jobsplus.client.screen.job.tab.RightTab;
import com.daqem.jobsplus.client.screen.job.tab.SideTab;
import com.daqem.jobsplus.player.job.Job;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class JobsScreenOptions {

    private final List<Job> jobs;
    private final List<Job> preformingJobs;
    private final List<Job> notPreformingJobs;
    private double coins;
    private Job selectedJob;
    private LeftTab selectedLeftTab;
    private RightTab selectedRightTab;
    private SideTab selectedSideTab;

    public JobsScreenOptions(List<Job> jobs, double coins) {
        this(jobs, coins, jobs.stream().findFirst().orElse(null), LeftTab.ALL, RightTab.INFO, null);
    }

    public JobsScreenOptions(List<Job> jobs, double coins, Job selectedJob, LeftTab selectedLeftTab, RightTab selectedRightTab, @Nullable SideTab selectedSideTab) {
        this.jobs = jobs.stream()
                .sorted(Comparator.comparing(Job::getLevel).reversed()
                        .thenComparing(job -> job.getJobInstance().getName().getString()))
                .toList();
        this.preformingJobs = this.jobs.stream().filter(job -> job.getLevel() > 0).toList();
        this.notPreformingJobs = this.jobs.stream().filter(job -> job.getLevel() <= 0).toList();
        this.coins = coins;
        this.selectedJob = selectedJob;
        this.selectedLeftTab = selectedLeftTab;
        this.selectedRightTab = selectedRightTab;
        this.selectedSideTab = selectedSideTab;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public List<Job> getPreformingJobs() {
        return preformingJobs;
    }

    public List<Job> getNotPreformingJobs() {
        return notPreformingJobs;
    }

    public double getCoins() {
        return coins;
    }

    public Job getSelectedJob() {
        return selectedJob;
    }

    public LeftTab getSelectedLeftTab() {
        return selectedLeftTab;
    }

    public RightTab getSelectedRightTab() {
        return selectedRightTab;
    }

    public SideTab getSelectedSideTab() {
        return selectedSideTab;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }

    public void setSelectedLeftTab(LeftTab selectedLeftTab) {
        this.selectedLeftTab = selectedLeftTab;
    }

    public void setSelectedRightTab(RightTab selectedRightTab) {
        this.selectedRightTab = selectedRightTab;
    }

    public void setSelectedSideTab(SideTab selectedSideTab) {
        this.selectedSideTab = selectedSideTab;
    }
}