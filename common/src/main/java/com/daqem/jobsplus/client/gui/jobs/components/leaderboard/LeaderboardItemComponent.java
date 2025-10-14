package com.daqem.jobsplus.client.gui.jobs.components.leaderboard;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.leaderboard.LeaderboardItemWidget;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.uilib.gui.component.EmptyComponent;

public class LeaderboardItemComponent extends EmptyComponent {

    public LeaderboardItemComponent(LeaderboardPlayer player, JobsScreenState state) {
        super(0, 0, 99, 20);

        LeaderboardItemWidget leaderboardItemWidget = new LeaderboardItemWidget(player, state);
        this.addWidget(leaderboardItemWidget);
    }
}