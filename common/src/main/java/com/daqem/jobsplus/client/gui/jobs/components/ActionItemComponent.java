package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobExpReward;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
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
        Component experienceText = jobExpReward.getMin() == jobExpReward.getMax() ?
                JobsPlus.translatable("gui.jobs.experience.reward", jobExpReward.getMin()) :
                JobsPlus.translatable("gui.jobs.experience.reward.range", jobExpReward.getMin(), jobExpReward.getMax());
        TruncatedTextComponent experienceComponent = new TruncatedTextComponent(0, nameComponent.getHeight(), getWidth(), experienceText, 0xFF1E1410);

        ConditionsComponent conditionsComponent = new ConditionsComponent(action.getConditions(), parentBounds);
        conditionsComponent.setY(nameComponent.getHeight() + experienceComponent.getHeight());


        this.addComponent(nameComponent);
        this.addComponent(experienceComponent);
        this.addComponent(conditionsComponent);

        this.setHeight(nameComponent.getHeight() + experienceComponent.getHeight() + conditionsComponent.getHeight() + 2);
    }
}
