package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.JobSelectionItemWidget;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.component.EmptyComponent;

public class JobSelectionItemComponent extends EmptyComponent {

    public JobSelectionItemComponent(Job job, JobsScreenState state) {
        super(0, 0, 99, 20);

        JobSelectionItemWidget jobSelectionItemWidget = new JobSelectionItemWidget(job, state);

        this.addWidget(jobSelectionItemWidget);
    }
}
