package com.daqem.jobsplus.client.gui.powerups.skilltree;

import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
import com.daqem.jobsplus.client.gui.powerups.widgets.PowerupItemWidget;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.uilib.api.skilltree.ISkillTreeItem;
import com.daqem.uilib.api.widget.skilltree.ISkillTreeItemWidget;
import com.daqem.uilib.skilltree.AbstractSkillTreeItem;

import java.util.ArrayList;
import java.util.List;

public class PowerupsSkillTreeItem extends AbstractSkillTreeItem {

    private final PowerupsScreenState state;
    private final Powerup powerup;

    public PowerupsSkillTreeItem(PowerupsScreenState state, Powerup powerup) {
        this(state, powerup, false, new ArrayList<>());
    }

    public PowerupsSkillTreeItem(PowerupsScreenState state, Powerup powerup, boolean isRoot, List<ISkillTreeItem> children) {
        super(isRoot, children);
        this.state = state;
        this.powerup = powerup;
    }

    public Powerup getPowerup() {
        return powerup;
    }

    @Override
    public ISkillTreeItemWidget createWidget() {
        return new PowerupItemWidget(this, this.state, this.powerup);
    }
}
