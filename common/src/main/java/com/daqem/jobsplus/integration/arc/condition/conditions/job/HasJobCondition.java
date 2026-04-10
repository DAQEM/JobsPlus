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

public class HasJobCondition extends AbstractCondition implements IJobCondition {

    private final Identifier jobLocation;

    public HasJobCondition(boolean inverted, Identifier jobLocation) {
        super(inverted);
        this.jobLocation = jobLocation;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        JobInstance jobInstance = JobInstance.of(jobLocation);
        if (jobInstance != null) {
            if (jobInstance.getIdentifier().equals(jobLocation)) {
                if (actionData.getPlayer() instanceof JobsPlayer player) {
                    Job job = player.jobsplus$getJob(jobInstance);
                    if (job != null) {
                        return job.getLevel() > 0;
                    }
                }
            }
        } else {
            JobsPlus.API.LOGGER.error("Job {} does not exist! Trying to use it in the condition has_job.", jobLocation);
        }
        return false;
    }

    @Override
    public IConditionType<? extends ICondition> getType() {
        return JobsPlusConditionType.HAS_JOB;
    }
    @Override
    public Identifier getJobLocation() {
        return jobLocation;
    }

    @Override
    public int getRequiredLevel() {
        return 0;
    }

    @Override
    public Component getDescription() {
        JobInstance jobInstance = JobInstance.of(jobLocation);
        if (jobInstance == null) {
            return JobsPlus.API.literal("ERROR: Job not found: '" + this.jobLocation.toString() + "'");
        }
        return this.getDescription(jobInstance.getName());
    }

    public static class Serializer implements IConditionSerializer<HasJobCondition> {

        @Override
        public HasJobCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new HasJobCondition(
                    inverted,
                    getIdentifier(jsonObject, "job"));
        }

        @Override
        public HasJobCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new HasJobCondition(
                    inverted,
                    friendlyByteBuf.readIdentifier());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, HasJobCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeIdentifier(type.jobLocation);
        }
    }
}

