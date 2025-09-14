package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.ActionsPaginationArrowLeftWidget;
import com.daqem.jobsplus.client.gui.jobs.widgets.ActionsPaginationArrowRightWidget;
import com.daqem.jobsplus.client.gui.jobs.widgets.ActionsPaginationDotWidget;
import com.daqem.uilib.gui.component.EmptyComponent;

import java.util.List;

public class ActionsPaginationComponent extends EmptyComponent {

    public ActionsPaginationComponent(List<IAction> actions, JobsScreenState state) {
        super(0, 0, 99, actions.size() > 1 ? 10 : 0);

        if (actions.size() > 1) {
            ActionsPaginationArrowLeftWidget leftArrow = new ActionsPaginationArrowLeftWidget(0, 0, actions, state);
            this.addWidget(leftArrow);

            for (int i = 0; i < actions.size(); i++) {
                this.addWidget(new ActionsPaginationDotWidget(i * 7 + 15, 3, actions.get(i), state));
            }

            ActionsPaginationArrowRightWidget rightArrow = new ActionsPaginationArrowRightWidget(actions.size() * 7 - 1 + 20, 0, actions, state);
            this.addWidget(rightArrow);

            this.setWidth(actions.size() * 7 - 1 + 30);
            this.centerHorizontally();
        } else {
            this.setWidth(0);
            this.setHeight(0);
        }
    }
}
