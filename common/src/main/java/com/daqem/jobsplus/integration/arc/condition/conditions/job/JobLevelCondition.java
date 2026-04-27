package com.daqem.jobsplus.integration.arc.condition.conditions.job;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.condition.type.JobsPlusConditionType;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class JobLevelCondition extends AbstractCondition implements IJobCondition {

    private static final String EMPTY_JOB_LOCATION = "jobsplus:empty";

    private final Identifier jobLocation;
    private final INumberProvider level;

    public JobLevelCondition(boolean inverted, Identifier jobLocation, INumberProvider level) {
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

        return playerJobLevel >= this.level.resolve(actionData);
    }

    @Override
    public int getRequiredLevel() {
        return level instanceof ConstantNumberProvider cnp ? (int) cnp.resolve(null) : 0;
    }

    @Override
    public IConditionType<? extends ICondition> getType() {
        return JobsPlusConditionType.JOB_LEVEL;
    }

    @Override
    public Identifier getJobLocation() {
        return jobLocation;
    }

    @Override
    public Component getDescription() {
        JobInstance jobInstance = JobInstance.of(this.jobLocation);
        if (jobInstance == null) {
            return JobsPlus.API.literal("ERROR: Job not found");
        }
        return this.getDescription(jobInstance.getName(), this.level.getDescription());
    }

    public static class Serializer implements IConditionSerializer<JobLevelCondition> {
        @Override
        public JobLevelCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new JobLevelCondition(
                    inverted,
                    Identifier.parse(GsonHelper.getAsString(jsonObject, "job", EMPTY_JOB_LOCATION)),
                    getNumberProvider(jsonObject, "level", new ConstantNumberProvider(0.0))
            );
        }

        @Override
        public JobLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new JobLevelCondition(inverted, buf.readIdentifier(), INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, JobLevelCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            buf.writeIdentifier(type.jobLocation);
            INumberProviderSerializer.toNetwork(type.level, buf);
        }
    }
}