package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.uilib.gui.component.EmptyComponent;

public class TabSwitcherComponent extends EmptyComponent {

    public TabSwitcherComponent(JobsScreenState state) {
        super(164, 18, 150, 167);

        RightTabComponent experienceTab = new RightTabComponent(getWidth() - 30, 9, state, RightTab.EXPERIENCE);
        RightTabComponent recipesTab = new RightTabComponent(getWidth() - 30, 32, state, RightTab.RECIPES);
        RightPageContentComponent rightPageContentComponent = new RightPageContentComponent(state);

        this.addComponent(experienceTab);
        this.addComponent(recipesTab);
        this.addComponent(rightPageContentComponent);
    }
}
