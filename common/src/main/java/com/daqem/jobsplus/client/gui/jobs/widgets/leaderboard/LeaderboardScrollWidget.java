package com.daqem.jobsplus.client.gui.jobs.widgets.leaderboard;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.components.leaderboard.LeaderboardItemComponent;
import com.daqem.jobsplus.client.gui.jobs.widgets.AbstractScrollWidget;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.uilib.api.component.IComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class LeaderboardScrollWidget extends AbstractScrollWidget {

    private final JobsScreenState state;
    private List<LeaderboardPlayer> cachedPlayers;

    public LeaderboardScrollWidget(int width, int height, JobsScreenState state) {
        super(width, height, 20);
        this.state = state;
        rebuildComponents();
    }

    private void rebuildComponents() {
        this.clearComponents();
        List<LeaderboardPlayer> players = state.getLeaderboardPlayers();
        if (players.isEmpty()) {
            this.addComponent(new MultiLineTextComponent(
                    0,
                    0,
                    this.getWidth(),
                    JobsPlus.translatable("gui.jobs.leaderboard.no_players"),
                    0xFFD8BF96
            ));
        } else {
            for (LeaderboardPlayer player : players) {
                LeaderboardItemComponent component = new LeaderboardItemComponent(player, state);
                this.addComponent(component);
            }
        }
        this.cachedPlayers = players;

        for (IComponent component : getComponents()) {
            component.updateParentPosition(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }

    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (cachedPlayers != state.getLeaderboardPlayers()) {
            rebuildComponents();
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }
}