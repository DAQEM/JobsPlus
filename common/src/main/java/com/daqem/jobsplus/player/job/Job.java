package com.daqem.jobsplus.player.job;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.event.triggers.JobEvents;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobLevelPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundSyncJobPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.exp.ExpCollector;
import com.daqem.jobsplus.player.job.powerup.JobPowerupManager;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Job {

    public static final Codec<Job> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("job_instance").forGetter(job -> job.getJobInstance().getIdentifier()),
            Codec.INT.fieldOf("level").forGetter(Job::getLevel),
            Codec.DOUBLE.fieldOf("experience").forGetter(Job::getExperience),
            Codec.list(Powerup.CODEC).fieldOf("powerups").forGetter(job -> job.getPowerupManager().getAllPowerups())
    ).apply(instance, (jobInstanceLocation, level, experience, powerups) -> new Job(null, jobInstanceLocation, level, experience, new ArrayList<>(powerups))));

    private final JobInstance jobInstance;
    private final JobPowerupManager powerupManager;
    private JobsPlayer player;
    private int level;
    private double experience;
    private final ExpCollector expCollector = new ExpCollector();
    private boolean isStatsDirty = false;
    private boolean isPowerupsDirty = false;

    public Job(JobsPlayer player, JobInstance jobInstance) {
        this(player, jobInstance, 0, 0, new ArrayList<>());
    }

    public Job(JobsPlayer player, JobInstance jobInstance, int level, double experience) {
        this(player, jobInstance, level, experience, new ArrayList<>());
    }

    public Job(JobsPlayer player, Identifier jobInstanceLocation, int level, double experience, @NotNull List<Powerup> powerups) {
        this(player, JobManager.getInstance().getJobs().get(jobInstanceLocation), level, experience, powerups);
    }

    public Job(JobsPlayer player, JobInstance jobInstance, int level, double experience, @NotNull List<Powerup> powerups) {
        this.player = player;
        this.jobInstance = jobInstance;
        this.powerupManager = new JobPowerupManager(this, powerups);
        this.level = level;
        this.experience = experience;
    }

    public JobInstance getJobInstance() {
        return jobInstance;
    }

    public JobPowerupManager getPowerupManager() {
        return powerupManager;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        int oldLevel = this.level;
        this.level = Math.clamp(level, 0, JobsPlusConfig.maxLevel.get());

        if (oldLevel != this.level) {
            markStatsDirty();
        }
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        setExperience(experience, true);
    }

    public void setExperience(double experience, boolean triggerEvent) {
        setExperience(experience, triggerEvent, true);
    }

    public void setExperience(double experience, boolean triggerEvent, boolean triggerLevelUpCheck) {
        if (level >= JobsPlusConfig.maxLevel.get()) {
            this.experience = 0;
            return;
        }
        if (!player.jobsplus$isExpEnabled()) {
            return;
        }
        double change = experience - this.experience;
        expCollector.addExp(change);

        if (this.experience != experience) {
            this.experience = experience;
            markStatsDirty();
        }

        if (triggerLevelUpCheck) {
            checkForLevelUp();
        }
        if (triggerEvent) {
            JobEvents.onJobExperience(player, this, change);
        }
    }

    public void addExperience(double experience) {
        setExperience(getExperience() + (experience * JobsPlusConfig.experienceMultiplier.get()), true);
    }

    public void addExperienceWithoutEvent(double experience) {
        setExperience(getExperience() + (experience * JobsPlusConfig.experienceMultiplier.get()), false);
    }

    private void checkForLevelUp() {
        double experienceToLevelUp = getExperienceToLevelUp(level);
        if (experience >= experienceToLevelUp) {
            setLevel(level + 1);
            setExperience(experience - experienceToLevelUp, false, false);
            JobEvents.onJobLevelUp(player, this);
        }
    }

    public static double getExperienceToLevelUp(int level) {
        return JobLevelCalculator.getExperienceForLevel(level);
    }

    public void setPlayer(JobsPlayer player) {
        this.player = player;
    }

    public double getExperiencePercentage() {
        double expToLevel = getExperienceToLevelUp(level);
        if (expToLevel == 0) return 0.0D;
        return experience / expToLevel * 100;
    }

    public ExpCollector getExpCollector() {
        return expCollector;
    }

    public double getExperienceForNextLevel() {
        return getExperienceToLevelUp(level);
    }

    public void markStatsDirty() {
        this.isStatsDirty = true;
    }

    public void markPowerupsDirty() {
        this.isPowerupsDirty = true;
    }

    public @Nullable MutableComponent getExperienceGainMessage() {
        double exp = expCollector.getExp();
        if (exp >= 0.1) {
            return JobsPlus.translatable("job.exp.gain",
                            JobsPlus.formatExp(exp),
                            jobInstance.getName().getString())
                    .withStyle(style -> style.withColor(jobInstance.getColorDecimal()))
                    .withStyle(ChatFormatting.BOLD);
        }
        return null;
    }

    public List<IActionHolder> getActiveHolders() {
        List<IActionHolder> holders = new ArrayList<>();
        holders.add(this.jobInstance);
        holders.addAll(powerupManager.getAllActivePowerups().stream()
                .map(Powerup::getPowerupInstance)
                .toList());
        return holders;
    }

    public void updateArcActionHolders() {
        if (player.jobsplus$getPlayer() instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$addActionHolder(this.jobInstance);

            for (Powerup powerup : powerupManager.getAllPowerups()) {
                if (powerup.getState() == PowerupState.ACTIVE) {
                    arcPlayer.arc$addActionHolder(powerup.getPowerupInstance());
                } else {
                    arcPlayer.arc$removeActionHolder(powerup.getPowerupInstance());
                }
            }
        }
    }

    public void dispose() {
        if (player.jobsplus$getPlayer() instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$removeActionHolder(this.jobInstance);

            for (Powerup powerup : powerupManager.getAllPowerups()) {
                arcPlayer.arc$removeActionHolder(powerup.getPowerupInstance());
            }
        }
    }

    public void tick() {
        if (this.isPowerupsDirty) {
            this.sendClientSyncPacket();
            this.updateArcActionHolders();
            if (player instanceof JobsServerPlayer serverPlayer) {
                serverPlayer.jobsplus$getLevelData().jobsplus$updatePlayerEntry(serverPlayer.jobsplus$getPlayer(), this);
            }
            this.isPowerupsDirty = false;
            this.isStatsDirty = false;
        } else if (this.isStatsDirty) {
            this.sendClientLevelPacket();
            if (player instanceof JobsServerPlayer serverPlayer) {
                serverPlayer.jobsplus$getLevelData().jobsplus$updatePlayerEntry(serverPlayer.jobsplus$getPlayer(), this);
            }
            this.isStatsDirty = false;
        }
    }

    public void sendClientSyncPacket() {
        if (player instanceof JobsServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer.jobsplus$getServerPlayer(), new ClientboundSyncJobPacket(this));
        }
    }

    public void sendClientLevelPacket() {
        if (player instanceof JobsServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer.jobsplus$getServerPlayer(), new ClientboundSyncJobLevelPacket(this));
        }
    }

    public static class Serializer {

        public static Job fromNetwork(FriendlyByteBuf friendlyByteBuf, JobsPlayer player) {
            Identifier jobInstanceLocation = friendlyByteBuf.readIdentifier();
            int level = friendlyByteBuf.readInt();
            double experience = friendlyByteBuf.readDouble();
            int powerupCount = friendlyByteBuf.readVarInt();
            List<Powerup> powerups = new ArrayList<>();
            for (int i = 0; i < powerupCount; i++) {
                Identifier powerupLocation = friendlyByteBuf.readIdentifier();
                PowerupState state = friendlyByteBuf.readEnum(PowerupState.class);
                PowerupInstance powerupInstance = PowerupInstance.of(powerupLocation);
                if (powerupInstance == null) continue;
                powerups.add(new Powerup(powerupInstance, state));
            }
            return new Job(player, jobInstanceLocation, level, experience, powerups);
        }

        public static void toNetwork(FriendlyByteBuf friendlyByteBuf, Job job) {
            friendlyByteBuf.writeIdentifier(job.getJobInstance().getIdentifier());
            friendlyByteBuf.writeInt(job.getLevel());
            friendlyByteBuf.writeDouble(job.getExperience());
            List<Powerup> allPowerups = job.getPowerupManager().getAllPowerups()
                    .stream()
                    .filter(powerup -> powerup != null && powerup.getPowerupInstance() != null)
                    .toList();
            friendlyByteBuf.writeVarInt(allPowerups.size());
            for (Powerup powerup : allPowerups) {
                friendlyByteBuf.writeIdentifier(powerup.getPowerupInstance().getIdentifier());
                friendlyByteBuf.writeEnum(powerup.getState());
            }
        }
    }
}