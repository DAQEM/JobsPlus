package com.daqem.jobsplus.client.gui.jobs.components.leaderboard;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TextAlign;
import com.daqem.uilib.gui.component.text.TextComponent;
import net.minecraft.client.gui.GuiGraphics;

public class LeaderboardComponent extends AbstractComponent {

    private final JobsScreenState jobsScreenState;
    private final LeaderboardScrollComponent leaderboardScrollComponent;
    private boolean hasLoaded = false;
    private int ticker = 0;

    public LeaderboardComponent(JobsScreenState state) {
        super(0, 0, 117, 167);
        this.jobsScreenState = state;
        state.fetchInitialLeaderboardPlayers();

        SpriteComponent bannerComponent = new SpriteComponent(0, 0, 117, 33, JobsPlus.getId("jobs/leaderboard_banner"));
        this.leaderboardScrollComponent = new LeaderboardScrollComponent(state);

        this.addComponent(bannerComponent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        if (!hasLoaded && !jobsScreenState.isLoadingLeaderboard()) {
            this.hasLoaded = true;
            this.addComponent(leaderboardScrollComponent);
            this.updateParentPosition(getParentX(), getParentY(), parentWidth, parentHeight);
        }

        if (ticker++ >= 20) {
            ticker = 0;
            jobsScreenState.fetchLeaderboardPlayers();
        }
    }
}