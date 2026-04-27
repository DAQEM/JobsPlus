package com.daqem.jobsplus.integration.arc.reward.rewards.job;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.reward.type.JobsPlusRewardType;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class JobExpReward extends AbstractReward {

    private final INumberProvider min;
    private final INumberProvider max;

    public JobExpReward(double chance, int priority, INumberProvider min, INumberProvider max) {
        super(chance, priority);
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getDescription() {
        return this.getDescription(this.min.getDescription(), this.max.getDescription());
    }

    public INumberProvider getMin() {
        return min;
    }

    public INumberProvider getMax() {
        return max;
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
                    double resolvedMin = min.resolve(actionData);
                    double resolvedMax = max.resolve(actionData);

                    if (resolvedMin > resolvedMax) {
                        double temp = resolvedMin;
                        resolvedMin = resolvedMax;
                        resolvedMax = temp;
                    }

                    double exp = resolvedMin + (resolvedMax - resolvedMin) * actionData.getPlayer().arc$getPlayer().getRandom().nextDouble();
                    job.addExperience(exp);
                }
            }
        }
        return new ActionResult();
    }

    public static class Serializer implements IRewardSerializer<JobExpReward> {
        @Override
        public JobExpReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new JobExpReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "min", new ConstantNumberProvider(0.0)),
                    getNumberProvider(jsonObject, "max", new ConstantNumberProvider(0.0))
            );
        }

        @Override
        public JobExpReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new JobExpReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    INumberProviderSerializer.fromNetworkStatic(buf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, JobExpReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.min, buf);
            INumberProviderSerializer.toNetwork(type.max, buf);
        }
    }
}