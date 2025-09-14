package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;

public class ExperienceComponent extends EmptyComponent {

    public ExperienceComponent(JobsScreenState state) {
        super(0, 0, 117, 167);

        SpriteComponent bannerComponent = new SpriteComponent(9, 0, 98, 33, JobsPlus.getId("jobs/experience_banner"));
        ActionScrollComponent actionScrollComponent = new ActionScrollComponent(state);

        this.addComponent(bannerComponent);
        this.addComponent(actionScrollComponent);
    }
}
