package com.daqem.jobsplus.client.gui.jobs.components.leaderboard;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.leaderboard.LeaderboardScrollWidget;
import com.daqem.uilib.gui.component.EmptyComponent;

public class LeaderboardScrollComponent extends EmptyComponent {

    public LeaderboardScrollComponent(JobsScreenState state) {
        super(0, 48, 117, 119);

        LeaderboardScrollWidget leaderboardScrollWidget = new LeaderboardScrollWidget(getWidth(), getHeight(), state);
        this.addWidget(leaderboardScrollWidget);
    }
}