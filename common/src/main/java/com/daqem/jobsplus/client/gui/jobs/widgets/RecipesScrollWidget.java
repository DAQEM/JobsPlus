package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.components.RecipesScrollContentComponent;

public class RecipesScrollWidget extends AbstractScrollWidget {

    public RecipesScrollWidget(int width, int height, JobsScreenState state) {
        super(width, height, 25);

        RecipesScrollContentComponent recipesScrollContentComponent = new RecipesScrollContentComponent(state);
        this.addComponent(recipesScrollContentComponent);
    }
}
