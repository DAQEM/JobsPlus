package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.JobSelectionWidget;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TextComponent;

public class JobSelectionComponent extends EmptyComponent {

    public JobSelectionComponent(JobsScreenState state) {
        super(21, 66, 117, 120);

        JobSelectionWidget jobSelectionWidget = new JobSelectionWidget(getWidth(), getHeight(), state);
        TextComponent title = new TextComponent(0, -10, JobsPlus.translatable("gui.jobs.job_selection"), 0xFFD8BF96);

        this.addWidget(jobSelectionWidget);
        this.addComponent(title);
    }
}
