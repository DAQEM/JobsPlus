package com.daqem.jobsplus.integration.arc.reward.rewards.job;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.jobsplus.integration.arc.reward.type.JobsPlusRewardType;
import com.daqem.jobsplus.player.JobsServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

public class JobCoinReward extends AbstractReward {

    private final int amount;

    public JobCoinReward(double chance, int priority, int amount) {
        super(chance, priority);
        this.amount = amount;
    }

    @Override
    public IRewardType<?> getType() {
        return JobsPlusRewardType.JOB_COIN;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        if (player instanceof JobsServerPlayer jobsServerPlayer) {
            jobsServerPlayer.jobsplus$addCoins(this.amount);
        }
        return new ActionResult();
    }

    @Override
    public Component getDescription() {
        return this.getDescription(this.amount);
    }

    public static class Serializer implements IRewardSerializer<JobCoinReward> {

        @Override
        public JobCoinReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new JobCoinReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "amount"));
        }

        @Override
        public JobCoinReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new JobCoinReward(
                    chance,
                    priority,
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, JobCoinReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeInt(type.amount);
        }
    }
}
