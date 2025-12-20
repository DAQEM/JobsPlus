package com.daqem.jobsplus.integration.arc.action.actions.job;

import com.daqem.arc.api.action.AbstractAction;
import com.daqem.arc.api.action.IActionSerializer;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import com.daqem.jobsplus.integration.arc.action.type.JobsPlusActionType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.List;

public class JobLevelUpAction extends AbstractAction {

    public JobLevelUpAction(Identifier location, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return JobsPlusActionType.JOB_LEVEL_UP;
    }

    public static class Serializer implements IActionSerializer<JobLevelUpAction> {

        @Override
        public JobLevelUpAction fromJson(Identifier location, JsonObject jsonObject, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new JobLevelUpAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public JobLevelUpAction fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new JobLevelUpAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, JobLevelUpAction type) {
            IActionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
