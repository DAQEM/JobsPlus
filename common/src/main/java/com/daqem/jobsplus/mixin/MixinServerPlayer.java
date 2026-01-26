package com.daqem.jobsplus.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.daqem.jobsplus.player.LeaderboardPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.level.JobsPlusLevelData;
import com.daqem.jobsplus.level.JobsPlusServerLevel;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.ServerPlayerData;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.jobsplus.player.job.exp.ExpCollector;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.mojang.authlib.GameProfile;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements JobsServerPlayer {

    @Unique
    private List<Job> jobsplus$jobs = new ArrayList<>();
    @Unique
    private double jobsplus$coins = 0;
    @Unique
    private boolean jobsplus$isExpEnabled = true;

    public MixinServerPlayer(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Override
    public List<Job> jobsplus$getJobs() {
        return jobsplus$jobs;
    }

    @Override
    public List<JobInstance> jobsplus$getJobInstances() {
        return jobsplus$jobs.stream().map(Job::getJobInstance).toList();
    }

    @Override
    public List<Job> jobsplus$getInactiveJobs() {
        return JobManager.getInstance().getJobs().values().stream()
                .filter(jobInstance -> !jobsplus$getJobInstances().contains(jobInstance))
                .map(jobInstance -> new Job(this, jobInstance))
                .toList();
    }

    @Override
    public @Nullable Job jobsplus$addNewJob(@NotNull JobInstance jobInstance) {
        if (jobInstance.getIdentifier() == null) return null;
        Job job = jobsplus$getJob(jobInstance);
        if (job == null) {
            job = new Job(this, jobInstance, 1, 0);
            jobsplus$jobs.add(job);
            jobsplus$updateJob(job);
            jobsplus$getLevelData().jobsplus$updatePlayerEntry(this, job);
            return job;
        }
        return null;
    }

    @Override
    public void jobsplus$removeJob(JobInstance jobInstance) {
        Job job = jobsplus$getJob(jobInstance);
        if (job != null) {
            jobsplus$jobs.remove(job);
            jobsplus$removeActionHolders(job);
            jobsplus$getLevelData().jobsplus$removePlayerEntry(this, job);
        }
    }

    @Override
    public void jobsplus$removeActionHolders(Job job) {
        if (jobsplus$getServerPlayer() instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$removeActionHolder(job.getJobInstance());
            job.getPowerupManager().getAllPowerups().forEach(powerup -> arcPlayer.arc$removeActionHolder(powerup.getPowerupInstance()));
        }
    }

    @Override
    public @Nullable Job jobsplus$getJob(@Nullable JobInstance jobLocation) {
        if (jobLocation == null) return null;
        return this.jobsplus$jobs.stream()
                .filter(job -> job.getJobInstance().getIdentifier().equals(jobLocation.getIdentifier()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Job jobsplus$getJob(Identifier jobLocation) {
        return this.jobsplus$jobs.stream()
                .filter(job -> job.getJobInstance().getIdentifier().equals(jobLocation))
                .findFirst()
                .orElse(null);
    }

    @Override
    public @Nullable Powerup jobsplus$getPowerup(PowerupInstance powerupInstance) {
        return jobsplus$getJobs().stream()
                .map(Job::getPowerupManager)
                .flatMap(powerupManager -> powerupManager.getAllPowerups().stream())
                .filter(powerup -> powerup.getPowerupInstance().getIdentifier().equals(powerupInstance.getIdentifier()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public JobsPlusLevelData jobsplus$getLevelData() {
        return ((JobsPlusServerLevel) Objects.requireNonNull(Objects.requireNonNull(this.level().getServer()).getLevel(Level.OVERWORLD))).jobsplus$getLevelData();
    }

    @Override
    public double jobsplus$getCoins() {
        return jobsplus$coins;
    }

    @Override
    public void jobsplus$addCoins(double coins) {
        this.jobsplus$setCoins(Mth.clamp(this.jobsplus$coins + coins, 0, Double.MAX_VALUE));
    }

    @Override
    public void jobsplus$setCoins(double coins) {
        this.jobsplus$coins = coins;
    }

    @Override
    public List<IActionHolder> jobsplus$getActionHolders() {
        List<IActionHolder> actionHolders = new ArrayList<>(jobsplus$getJobInstances());
        actionHolders.addAll(jobsplus$getJobs().stream()
                .map(Job::getPowerupManager)
                .flatMap(powerupManager -> powerupManager.getAllPowerups().stream()
                        .filter(powerup -> powerup.getState() == PowerupState.ACTIVE))
                .map(Powerup::getPowerupInstance)
                .toList());
        return actionHolders;
    }

    @Override
    public ServerPlayer jobsplus$getServerPlayer() {
        //noinspection DataFlowIssue
        return (ServerPlayer) (Object) this;
    }

    @Override
    public String jobsplus$getName() {
        return super.getName().getString();
    }

    @Override
    public void jobsplus$updateJob(Job job) {
        this.jobsplus$updateActionHolders(job);
        job.sendClientSyncPacket();
    }

    @Override
    public void jobsplus$updateActionHolders(Job job) {
        if (jobsplus$getServerPlayer() instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$removeActionHolder(job.getJobInstance());
            job.getPowerupManager().getAllPowerups().forEach(powerup -> arcPlayer.arc$removeActionHolder(powerup.getPowerupInstance()));
            arcPlayer.arc$addActionHolder(job.getJobInstance());
            job.getPowerupManager().getAllPowerups().stream()
                    .filter(powerup -> powerup.getState() == PowerupState.ACTIVE)
                    .forEach(powerup -> arcPlayer.arc$addActionHolder(powerup.getPowerupInstance()));
        }
    }

    @Override
    public Player jobsplus$getPlayer() {
        return jobsplus$getServerPlayer();
    }

    @Override
    public boolean jobsplus$isExpEnabled() {
        return jobsplus$isExpEnabled;
    }

    @Override
    public void jobsplus$setExpEnabled(boolean enabled) {
        this.jobsplus$isExpEnabled = enabled;
    }

    @Inject(at = @At("TAIL"), method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V")
    public void restoreFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        if (oldPlayer instanceof JobsServerPlayer oldJobsServerPlayer) {
            this.jobsplus$jobs = oldJobsServerPlayer.jobsplus$getJobs();
            this.jobsplus$coins = oldJobsServerPlayer.jobsplus$getCoins();
            this.jobsplus$isExpEnabled = oldJobsServerPlayer.jobsplus$isExpEnabled();

            this.jobsplus$jobs.forEach(job -> job.setPlayer(this));

            if (jobsplus$getServerPlayer() instanceof ArcServerPlayer arcServerPlayer) {
                List<IActionHolder> iActionHolders = this.jobsplus$getActionHolders();
                arcServerPlayer.arc$addActionHolders(new ArrayList<>(iActionHolders));
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.store("JobsPlus", ServerPlayerData.CODEC, new ServerPlayerData(
                this.jobsplus$jobs,
                this.jobsplus$coins)
        );
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        valueInput.read("JobsPlus", ServerPlayerData.CODEC).ifPresent(serverPlayerData -> {
            this.jobsplus$jobs = serverPlayerData.jobs().stream()
                    .filter(job -> job.getJobInstance() != null)
                    .peek(job -> {
                        job.setPlayer(this);
                        // We do not blindly update here anymore, validation happens below
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            this.jobsplus$coins = serverPlayerData.coins();

            if (jobsplus$getServerPlayer() instanceof ArcServerPlayer arcServerPlayer) {
                List<IActionHolder> iActionHolders = this.jobsplus$getActionHolders();
                arcServerPlayer.arc$addActionHolders(new ArrayList<>(iActionHolders));
            }
        });

        JobsPlusLevelData levelData = jobsplus$getLevelData();
        Map<UUID, Map<Identifier, LeaderboardPlayer>> allEntries = levelData.jobsplus$getPlayerJobEntries();
        Map<Identifier, LeaderboardPlayer> playerEntries = allEntries.get(this.getUUID());

        if (playerEntries != null) {
            List<Identifier> jobsToRemove = new ArrayList<>();
            for (Identifier jobLocation : playerEntries.keySet()) {
                Job job = jobsplus$getJob(jobLocation);
                if (job == null || job.getLevel() <= 0) {
                    jobsToRemove.add(jobLocation);
                }
            }

            for (Identifier jobLocation : jobsToRemove) {
                JobInstance dummyInstance = JobInstance.of(jobLocation);
                if (dummyInstance != null) {
                    levelData.jobsplus$removePlayerEntry(this, new Job(this, dummyInstance));
                } else {
                    playerEntries.remove(jobLocation);
                }
            }
        }

        for (Job job : this.jobsplus$jobs) {
            levelData.jobsplus$updatePlayerEntry(this, job);
        }
    }

    @Inject(at = @At("TAIL"), method = "tick()V")
    public void tickTail(CallbackInfo ci) {
        jobsplus$jobs.forEach((job) -> {
            ExpCollector expCollector = job.getExpCollector();
            double exp = expCollector.getExp();
            if (exp > 0) {
                JobInstance jobInstance = job.getJobInstance();
                MutableComponent component = JobsPlus.translatable("job.exp.gain", JobsPlus.formatExp(exp), jobInstance.getName().getString()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(jobInstance.getColorDecimal()))).withStyle(ChatFormatting.BOLD);
                jobsplus$getServerPlayer().sendSystemMessage(component, true);
            }
            expCollector.clear();
        });
    }

    @Inject(at = @At("HEAD"), method = "die")
    public void dieHead(DamageSource damageSource, CallbackInfo ci) {
        if (JobsPlusConfig.loseExpOnDeath.get()) {
            Double percentLoss = JobsPlusConfig.expLossPercentage.get();
            jobsplus$jobs.forEach(job -> job.setExperience(job.getExperience() * (1.0 - percentLoss)));
        }
    }
}