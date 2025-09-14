package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.RecipesScrollWidget;
import com.daqem.uilib.api.component.IComponent;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;

public class RecipesScrollComponent extends EmptyComponent {

    public RecipesScrollComponent(JobsScreenState state) {
        super(0, 43, 117, 124);

        RecipesScrollWidget recipesScrollWidget = new RecipesScrollWidget(getWidth(), getHeight(), state);

        IComponent scrollContentComponent = recipesScrollWidget.getComponents().getFirst();
        if (scrollContentComponent.getHeight() <= getHeight()) {
            this.setWidth(scrollContentComponent.getWidth());
            this.centerHorizontally();
        }

        if (scrollContentComponent.getComponents().isEmpty()) {
            MultiLineTextComponent noRecipesText = new MultiLineTextComponent(0, 0, getWidth(), JobsPlus.translatable("gui.jobs.no_recipes"), 0xFFD8BF96);
            this.addComponent(noRecipesText);
        }

        this.addWidget(recipesScrollWidget);
    }
}
