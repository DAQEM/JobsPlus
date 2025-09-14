package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.jobsplus.client.gui.jobs.widgets.RightTabWidget;
import com.daqem.uilib.gui.component.EmptyComponent;

public class RightTabComponent extends EmptyComponent {

    public RightTabComponent(int x, int y, JobsScreenState state, RightTab tab) {
        super(x, y, 30, 22);

        RightTabWidget rightTabWidget = new RightTabWidget(state, tab);

        this.addWidget(rightTabWidget);
    }
}
