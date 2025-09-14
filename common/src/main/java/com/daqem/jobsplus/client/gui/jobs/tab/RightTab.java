package com.daqem.jobsplus.client.gui.jobs.tab;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum RightTab implements ITab {
    EXPERIENCE(JobsPlus.translatable("gui.jobs.tab.experience"), JobsPlus.getId("jobs/tab_1_active"), JobsPlus.getId("jobs/tab_1_inactive")),
    RECIPES(JobsPlus.translatable("gui.jobs.tab.recipes"), JobsPlus.getId("jobs/tab_2_active"), JobsPlus.getId("jobs/tab_2_inactive"));

    private final Component name;
    private final ResourceLocation activeSprite;
    private final ResourceLocation inactiveSprite;

    RightTab(Component name, ResourceLocation activeSprite, ResourceLocation inactiveSprite) {
        this.name = name;
        this.activeSprite = activeSprite;
        this.inactiveSprite = inactiveSprite;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public ResourceLocation getActiveSprite() {
        return activeSprite;
    }

    @Override
    public ResourceLocation getInactiveSprite() {
        return inactiveSprite;
    }
}
