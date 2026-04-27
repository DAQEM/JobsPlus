package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobExpReward;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class ActionItemComponent extends EmptyComponent {

    public ActionItemComponent(IAction action, JobsScreenState state, Supplier<ScreenRectangle> parentBounds) {
        super(0, 0, 99, 0);

        TruncatedTextComponent nameComponent = new TruncatedTextComponent(0, 0, getWidth(), action.getName(), state.getSelectedJob().getJobInstance().getColorDecimal() | 0xFF000000);
        JobExpReward jobExpReward = action.getRewards().stream()
                .filter(reward -> reward instanceof JobExpReward)
                .map(reward -> (JobExpReward) reward)
                .findFirst()
                .orElse(null);

        if (jobExpReward == null) return;

        INumberProvider minProvider = jobExpReward.getMin();
        INumberProvider maxProvider = jobExpReward.getMax();

        boolean isConstantMin = minProvider instanceof ConstantNumberProvider;
        boolean isConstantMax = maxProvider instanceof ConstantNumberProvider;

        ActionData dummyData = new ActionDataBuilder((ArcPlayer) Minecraft.getInstance().player, action.getType()).build();

        Component minDesc = isConstantMin
                ? JobsPlus.API.literal(JobsPlus.formatExp(minProvider.resolve(dummyData)))
                : minProvider.getDescription();

        Component maxDesc = isConstantMax
                ? JobsPlus.API.literal(JobsPlus.formatExp(maxProvider.resolve(dummyData)))
                : maxProvider.getDescription();

        boolean isSame;
        if (isConstantMin && isConstantMax) {
            isSame = minProvider.resolve(dummyData) == maxProvider.resolve(dummyData);
        } else {
            isSame = minDesc.getString().equals(maxDesc.getString());
        }

        Component experienceText = isSame ?
                JobsPlus.API.translatable("gui.jobs.experience.reward", minDesc) :
                JobsPlus.API.translatable("gui.jobs.experience.reward.range", minDesc, maxDesc);

        MultiLineTextComponent experienceComponent = new MultiLineTextComponent(0, nameComponent.getHeight(), getWidth(), experienceText, 0xFF1E1410);

        ConditionsComponent conditionsComponent = new ConditionsComponent(action.getConditions(), parentBounds);
        conditionsComponent.setY(nameComponent.getHeight() + experienceComponent.getHeight());

        this.addComponent(nameComponent);
        this.addComponent(experienceComponent);
        this.addComponent(conditionsComponent);

        this.setHeight(nameComponent.getHeight() + experienceComponent.getHeight() + conditionsComponent.getHeight() + 2);
    }
}