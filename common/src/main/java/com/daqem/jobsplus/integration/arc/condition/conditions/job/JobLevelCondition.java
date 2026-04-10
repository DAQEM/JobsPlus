package com.daqem.jobsplus.integration.arc.condition.conditions.job;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.condition.type.JobsPlusConditionType;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class JobLevelCondition extends AbstractCondition implements IJobCondition {

    private static final String EMPTY_JOB_LOCATION = "jobsplus:empty";

    private final Identifier jobLocation;
    private final int level;

    public JobLevelCondition(boolean inverted, Identifier jobLocation, int level) {
        super(inverted);
        this.jobLocation = jobLocation;
        this.level = level;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        JobInstance jobInstance;
        if (!jobLocation.equals(Identifier.parse(EMPTY_JOB_LOCATION))) {
            jobInstance = JobInstance.of(jobLocation);
        } else if (actionData.getSourceActionHolder() instanceof JobInstance jobInstance2) {
            jobInstance = jobInstance2;
        } else {
            return false;
        }

        int playerJobLevel = 0;
        if (actionData.getPlayer() instanceof JobsPlayer player) {
            Job playerJob = player.jobsplus$getJob(jobInstance);
            if (playerJob != null) {
                playerJobLevel = playerJob.getLevel();
            }
        }

        return playerJobLevel >= this.level;
    }

    @Override
    public IConditionType<? extends ICondition> getType() {
        return JobsPlusConditionType.JOB_LEVEL;
    }
    
    public Identifier getJobLocation() {
        return jobLocation;
    }

    @Override
    public int getRequiredLevel() {
        return level;
    }

    @Override
    public Component getDescription() {
        JobInstance jobInstance = JobInstance.of(this.jobLocation);
        if (jobInstance == null) {
            return JobsPlus.API.literal("ERROR: Job not found: '" + this.jobLocation.toString() + "'");
        }
        return this.getDescription(jobInstance.getName(), this.level);
    }

    public static class Serializer implements IConditionSerializer<JobLevelCondition> {

        @Override
        public JobLevelCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new JobLevelCondition(
                    inverted,
                    Identifier.parse(GsonHelper.getAsString(jsonObject, "job", EMPTY_JOB_LOCATION)),
                    GsonHelper.getAsInt(jsonObject, "level"));
        }

        @Override
        public JobLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new JobLevelCondition(
                    inverted,
                    friendlyByteBuf.readIdentifier(),
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, JobLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeIdentifier(type.jobLocation);
            friendlyByteBuf.writeInt(type.level);
        }
    }
}
