package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.ActionScrollWidget;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobExpReward;
import com.daqem.uilib.gui.component.EmptyComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ActionScrollComponent extends EmptyComponent {

    private final JobsScreenState state;
    private IAction cachedAction;
    private final ActionScrollWidget actionScrollWidget;

    public ActionScrollComponent(JobsScreenState state) {
        super(0, 38, 117, 130);
        this.state = state;

        this.actionScrollWidget = new ActionScrollWidget(getWidth(), getHeight());
        List<IAction> actions = this.getSortedActions(state);
        state.setActiveAction(actions.isEmpty() ? null : actions.getFirst());
        this.cachedAction = state.getActiveAction();

        if (state.getActiveAction() != null) {
            this.actionScrollWidget.addComponent(new ActionItemComponent(this.cachedAction, state, this.actionScrollWidget::getRectangle));

            ActionsPaginationComponent paginationComponent = new ActionsPaginationComponent(actions, state);
            paginationComponent.setY(getHeight() - paginationComponent.getHeight());

            if (paginationComponent.getHeight() > 0) {
                this.actionScrollWidget.setHeight(getHeight() - paginationComponent.getHeight() - 2);
            }

            this.addWidget(this.actionScrollWidget);
            this.addComponent(paginationComponent);
        } else {
            this.setWidth(0);
            this.setHeight(0);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        if (this.cachedAction != null && this.cachedAction != this.state.getActiveAction()) {
            this.cachedAction = this.state.getActiveAction();
            this.actionScrollWidget.clearComponents();
            this.actionScrollWidget.addComponent(new ActionItemComponent(this.cachedAction, this.state, this.actionScrollWidget::getRectangle));
            this.actionScrollWidget.setScrollAmount(0);
            this.updateParentPosition(getParentX(), getParentY(), parentWidth, parentHeight);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
    }

    private List<IAction> getSortedActions(JobsScreenState state) {
        return state.getSelectedJob().getJobInstance().getActions().stream()
                .filter(action -> action.getRewards().stream().anyMatch(reward -> reward instanceof JobExpReward))
                .sorted(Comparator.comparing(o -> {
                    JobExpReward jobExpReward = o.getRewards().stream()
                            .filter(reward -> reward instanceof JobExpReward)
                            .map(reward -> (JobExpReward) reward)
                            .findFirst()
                            .orElse(null);
                    if (jobExpReward == null) {
                        return 0;
                    } else {
                        return jobExpReward.getMax();
                    }
                }))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.getTotalX(), this.getTotalY(), getWidth(), getHeight());
    }
}
