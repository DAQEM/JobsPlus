package com.daqem.jobsplus.client.gui.jobs;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.jobsplus.player.job.Job;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class JobsScreenState {

    private final List<Job> jobs;
    private final List<Job> preformingJobs;
    private final List<Job> notPreformingJobs;
    private int coins;
    private Job selectedJob;
    private RightTab selectedRightTab;
    private @Nullable IAction activeAction;

    public JobsScreenState(List<Job> jobs, int coins) {
        this(jobs, coins, null, RightTab.EXPERIENCE);
    }

    public JobsScreenState(List<Job> jobs, int coins, Job selectedJob, RightTab selectedRightTab) {
        this.jobs = jobs.stream()
                .sorted(Comparator.comparing(Job::getLevel).reversed()
                        .thenComparingInt(job -> -job.getExperience())
                        .thenComparing(job -> job.getJobInstance().getName().getString()))
                .toList();
        this.preformingJobs = this.jobs.stream().filter(job -> job.getLevel() > 0).toList();
        this.notPreformingJobs = this.jobs.stream().filter(job -> job.getLevel() <= 0).toList();
        this.coins = coins;
        this.selectedJob = selectedJob != null ? selectedJob : this.jobs.getFirst();
        this.selectedRightTab = selectedRightTab;
        this.activeAction = null;
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

    public int getCoins() {
        return coins;
    }

    public Job getSelectedJob() {
        return selectedJob;
    }

    public RightTab getSelectedRightTab() {
        return selectedRightTab;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }

    public void setSelectedRightTab(RightTab selectedRightTab) {
        this.selectedRightTab = selectedRightTab;
    }

    public @Nullable IAction getActiveAction() {
        return activeAction;
    }

    public void setActiveAction(@Nullable IAction activeAction) {
        this.activeAction = activeAction;
    }

    public int getActiveJobCount() {
        return (int) jobs.stream().filter(job -> job.getLevel() > 0).count();
    }
}