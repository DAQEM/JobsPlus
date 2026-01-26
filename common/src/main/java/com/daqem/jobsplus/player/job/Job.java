package com.daqem.jobsplus.player.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.NotNull;

import com.daqem.jobsplus.Constants;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.event.triggers.JobEvents;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

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
        this.powerupManager = new JobPowerupManager(powerups);
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
        this.level = Math.clamp(level, 0, JobsPlusConfig.maxLevel.get());
        if (player instanceof JobsServerPlayer serverPlayer) {
            serverPlayer.jobsplus$getLevelData().jobsplus$updatePlayerEntry(serverPlayer.jobsplus$getPlayer(), this);
            sendClientSyncPacket();
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
        experience *= JobsPlusConfig.experienceMultiplier.get();
        double change = experience - this.experience;
        expCollector.addExp(change);
        this.experience = experience;
        if (triggerLevelUpCheck) {
            checkForLevelUp();
        }
        if (triggerEvent) {
            JobEvents.onJobExperience(player, this, change);
        }
        if (player instanceof JobsServerPlayer serverPlayer) {
            serverPlayer.jobsplus$getLevelData().jobsplus$updatePlayerEntry(serverPlayer.jobsplus$getPlayer(), this);
            if (triggerLevelUpCheck) sendClientSyncPacket();
        }
    }

    public void addExperience(double experience) {
        setExperience(getExperience() + experience, true);
    }

    public void addExperienceWithoutEvent(double experience) {
        setExperience(getExperience() + experience, false);
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

    public CompoundTag toNBT() {
        CompoundTag jobTag = new CompoundTag();

        jobTag.putString(Constants.JOB_INSTANCE_LOCATION, getJobInstance().getIdentifier().toString());
        jobTag.putInt(Constants.LEVEL, getLevel());
        jobTag.putDouble(Constants.EXPERIENCE, getExperience());

        ListTag powerupsTag = new ListTag();

        for (Powerup powerup : powerupManager.getAllPowerups()
                .stream()
                .filter(powerup -> powerup != null && powerup.getPowerupInstance() != null)
                .toList()
        ) {
            CompoundTag powerupTag = new CompoundTag();

            powerupTag.putString(Constants.POWERUP_LOCATION, powerup.getPowerupInstance().getIdentifier().toString());
            powerupTag.putString(Constants.POWERUP_STATE, powerup.getState().name());

            powerupsTag.add(powerupTag);
        }

        jobTag.put(Constants.POWERUPS, powerupsTag);

        return jobTag;
    }

    public static Job fromNBT(JobsPlayer player, CompoundTag tag) {
        AtomicReference<Job> job = new AtomicReference<>();
        tag.getString(Constants.JOB_INSTANCE_LOCATION).ifPresent(jobLocation -> {
            tag.getInt(Constants.LEVEL).ifPresent(level -> {
                double exp = tag.getDouble(Constants.EXPERIENCE)
                        .or(() -> tag.getInt(Constants.EXPERIENCE).map(Integer::doubleValue))
                        .orElse(0.0);
                List<Powerup> powerups = new ArrayList<>();
                tag.getList(Constants.POWERUPS).ifPresent(powerupsTag -> {
                    for (Tag powerupTag : powerupsTag) {
                        CompoundTag powerupNBT = (CompoundTag) powerupTag;
                        powerupNBT.getString(Constants.POWERUP_LOCATION).ifPresent(powerupLocationString -> {
                            powerupNBT.getString(Constants.POWERUP_STATE).ifPresent(powerupState -> {
                                Identifier powerupLocation = Identifier.tryParse(powerupLocationString);
                                if (powerupLocation == null) return;
                                PowerupInstance powerupInstance = PowerupInstance.of(powerupLocation);
                                if (powerupInstance == null) return;
                                powerups.add(new Powerup(powerupInstance, PowerupState.valueOf(powerupState)));
                            });
                        });
                    }
                });
                job.set(new Job(player, Identifier.parse(jobLocation), level, exp, powerups));
            });
        });
        return job.get();
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

    public void sendClientSyncPacket() {
        if (player instanceof JobsServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer.jobsplus$getServerPlayer(), new ClientboundSyncJobPacket(this));
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
