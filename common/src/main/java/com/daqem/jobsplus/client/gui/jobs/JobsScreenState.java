package com.daqem.jobsplus.client.gui.jobs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.jobsplus.networking.c2s.ServerboundRequestLeaderboardPacket;
import com.daqem.jobsplus.networking.c2s.ServerboundRequestPlayerJobsPacket;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;

import dev.architectury.networking.NetworkManager;

public class JobsScreenState {

    private final List<Job> jobs;
    private final List<Job> preformingJobs;
    private final List<Job> notPreformingJobs;
    private final double coins;
    private Job selectedJob;
    private RightTab selectedRightTab;
    private @Nullable IAction activeAction;
    private List<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
    private boolean isLoadingLeaderboard = false;
    private LeaderboardPlayer viewingPlayer = null;
    private List<Job> viewingPlayerJobs = new ArrayList<>();

    public JobsScreenState(List<Job> jobs, double coins) {
        this(jobs, coins, null, RightTab.EXPERIENCE);
    }

    public JobsScreenState(List<Job> jobs, double coins, Job selectedJob, RightTab selectedRightTab) {
        this.jobs = jobs.stream()
                .sorted(Comparator.comparing(Job::getLevel).reversed()
                        .thenComparingDouble(job -> -job.getExperience())
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
        return viewingPlayer == null ? jobs : viewingPlayerJobs;
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

    public RightTab getSelectedRightTab() {
        return selectedRightTab;
    }

    public void setSelectedJob(Job selectedJob) {
        stopViewingPlayer();
        this.selectedJob = selectedJob;
    }

    public void setSelectedRightTab(RightTab selectedRightTab) {
        stopViewingPlayer();
        this.selectedRightTab = selectedRightTab;
    }

    public @Nullable IAction getActiveAction() {
        return activeAction;
    }

    public void setActiveAction(@Nullable IAction activeAction) {
        stopViewingPlayer();
        this.activeAction = activeAction;
    }

    public int getActiveJobCount() {
        return (int) jobs.stream().filter(job -> job.getLevel() > 0).count();
    }

    public List<LeaderboardPlayer> getLeaderboardPlayers() {
        return leaderboardPlayers;
    }

    public void setLeaderboardPlayers(List<LeaderboardPlayer> leaderboardPlayers) {
        this.leaderboardPlayers = leaderboardPlayers;
        this.isLoadingLeaderboard = false;
    }

    public boolean isLoadingLeaderboard() {
        return isLoadingLeaderboard;
    }

    public void fetchInitialLeaderboardPlayers() {
        this.isLoadingLeaderboard = true;
        this.leaderboardPlayers = new ArrayList<>();
        fetchLeaderboardPlayers();
    }

    public void fetchLeaderboardPlayers() {
        NetworkManager.sendToServer(new ServerboundRequestLeaderboardPacket(getSelectedJob().getJobInstance().getIdentifier()));
    }

    public LeaderboardPlayer getViewingPlayer() {
        return viewingPlayer;
    }

    public List<Job> getViewingPlayerJobs() {
        return viewingPlayerJobs;
    }

    public void setViewingPlayerJobs(List<Job> viewingPlayerJobs) {
        this.viewingPlayerJobs = viewingPlayerJobs;
    }

    public void fetchViewingPlayerJobs(LeaderboardPlayer viewingPlayer) {
        this.viewingPlayer = viewingPlayer;
        this.viewingPlayerJobs = new ArrayList<>();
        NetworkManager.sendToServer(new ServerboundRequestPlayerJobsPacket(viewingPlayer.getUuid()));
    }

    public void stopViewingPlayer() {
        this.viewingPlayer = null;
        this.viewingPlayerJobs = new ArrayList<>();
    }
}