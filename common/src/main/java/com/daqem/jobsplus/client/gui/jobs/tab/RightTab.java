package com.daqem.jobsplus.client.gui.jobs.tab;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public enum RightTab implements ITab {
    EXPERIENCE(JobsPlus.API.translatable("gui.jobs.tab.experience"), JobsPlus.API.getId("jobs/tab_1_active"), JobsPlus.API.getId("jobs/tab_1_inactive")),
    RECIPES(JobsPlus.API.translatable("gui.jobs.tab.recipes"), JobsPlus.API.getId("jobs/tab_2_active"), JobsPlus.API.getId("jobs/tab_2_inactive")),
    LEADERBOARD(JobsPlus.API.translatable("gui.jobs.tab.leaderboard"), JobsPlus.API.getId("jobs/tab_3_active"), JobsPlus.API.getId("jobs/tab_3_inactive"));

    private final Component name;
    private final Identifier activeSprite;
    private final Identifier inactiveSprite;

    RightTab(Component name, Identifier activeSprite, Identifier inactiveSprite) {
        this.name = name;
        this.activeSprite = activeSprite;
        this.inactiveSprite = inactiveSprite;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public Identifier getActiveSprite() {
        return activeSprite;
    }

    @Override
    public Identifier getInactiveSprite() {
        return inactiveSprite;
    }
}
