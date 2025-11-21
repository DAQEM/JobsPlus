package com.daqem.jobsplus.integration.arc.reward.rewards.job;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.reward.type.JobsPlusRewardType;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

public class JobExpReward extends AbstractReward {

    private final double min;
    private final double max;

    public JobExpReward(double chance, int priority, double min, double max) {
        super(chance, priority);
        this.min = min;
        this.max = max;

        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("min and max must be non-negative for JobExpActionReward.");
        }
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max for JobExpActionReward.");
        }
    }

    @Override
    public IRewardType<?> getType() {
        return JobsPlusRewardType.JOB_EXP;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getSourceActionHolder() instanceof JobInstance jobInstance) {
            if (actionData.getPlayer() instanceof JobsServerPlayer jobsServerPlayer) {
                Job job = jobsServerPlayer.jobsplus$getJob(jobInstance);
                if (job != null) {
                    double exp = min + (max - min) * actionData.getPlayer().arc$getPlayer().getRandom().nextDouble();
                    job.addExperience(exp);
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public Component getDescription() {
        return this.getDescription(this.min, this.max);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public static class Serializer implements IRewardSerializer<JobExpReward> {
        @Override
        public JobExpReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new JobExpReward(
                    chance,
                    priority,
                    GsonHelper.getAsDouble(jsonObject, "min"),
                    GsonHelper.getAsDouble(jsonObject, "max"));
        }

        @Override
        public JobExpReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new JobExpReward(
                    chance,
                    priority,
                    friendlyByteBuf.readDouble(),
                    friendlyByteBuf.readDouble());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, JobExpReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.min);
            friendlyByteBuf.writeDouble(type.max);
        }
    }
}
