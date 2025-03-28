package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.AbstractSpriteComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class JobsEmblemComponent extends AbstractSpriteComponent<JobsEmblemComponent> {

    private static final LinkedList<ResourceLocation> DEFAULT_SPRITES = new LinkedList<>(List.of(
            JobsPlus.getId("emblem/stone"),
            JobsPlus.getId("emblem/bronze"),
            JobsPlus.getId("emblem/silver"),
            JobsPlus.getId("emblem/gold")
    ));

    private final Job job;

    public JobsEmblemComponent(int x, int y, Job job) {
        super(DEFAULT_SPRITES, x, y, 27, 27);
        this.job = job;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        int levelPercentage = job.getLevel() * 100 / job.getJobInstance().getMaxLevel();
        int index = levelPercentage / 25;
        if (index >= getSprites().size()) {
            index = getSprites().size() - 1;
        }
        graphics.blitSprite(RenderType::guiTextured, getSprite(index), 0, 0, getWidth(), getHeight());
    }
}
