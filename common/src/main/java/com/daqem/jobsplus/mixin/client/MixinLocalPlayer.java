package com.daqem.jobsplus.mixin.client;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player implements JobsPlayer {

    @Unique
    private final Map<ResourceLocation, Job> jobsplus$jobs = new HashMap<>();
    @Unique
    private double jobsplus$coins = 0;

    public MixinLocalPlayer(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Override
    public List<Job> jobsplus$getJobs() {
        return jobsplus$jobs.values().stream().toList();
    }

    @Override
    public List<JobInstance> jobsplus$getJobInstances() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Job> jobsplus$getInactiveJobs() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Job jobsplus$addNewJob(JobInstance jobInstance) {
        if (jobInstance.getLocation() == null) return null;
        Job job = jobsplus$getJob(jobInstance);
        if (job == null) {
            job = new Job(this, jobInstance, 1, 0);
            jobsplus$jobs.put(jobInstance.getLocation(), job);
            return job;
        }
        return null;
    }

    @Override
    public void jobsplus$removeJob(JobInstance jobInstance) {
        jobsplus$jobs.remove(jobInstance.getLocation());
    }

    @Override
    public void jobsplus$removeActionHolders(Job job) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Job jobsplus$getJob(@Nullable JobInstance jobInstance) {
        if (jobInstance == null) return null;
        return jobsplus$jobs.get(jobInstance.getLocation());
    }

    @Override
    public Job jobsplus$getJob(ResourceLocation jobLocation) {
        return jobsplus$getJob(JobInstance.of(jobLocation));
    }

    @Override
    public double jobsplus$getCoins() {
        return jobsplus$coins;
    }

    @Override
    public void jobsplus$addCoins(double coins) {
        this.jobsplus$coins += coins;
    }

    @Override
    public void jobsplus$setCoins(double coins) {
        this.jobsplus$coins = coins;
    }

    @Override
    public String jobsplus$getName() {
        return this.getDisplayName().getString();
    }

    @Override
    public Player jobsplus$getPlayer() {
        return this;
    }

    @Override
    public List<IActionHolder> jobsplus$getActionHolders() {
        throw new UnsupportedOperationException();
    }
}
