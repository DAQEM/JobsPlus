package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.components.JobSelectionItemComponent;
import com.daqem.jobsplus.player.job.Job;

import java.util.List;

public class JobSelectionWidget extends AbstractScrollWidget {

    public JobSelectionWidget(int width, int height, JobsScreenState state) {
        super(width, height, 20);
        List<Job> jobs = state.getJobs();
        for (Job job : jobs) {
            this.addComponent(new JobSelectionItemComponent(job, state));
        }
    }
}
