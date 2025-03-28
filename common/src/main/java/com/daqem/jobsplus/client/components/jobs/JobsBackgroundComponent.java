package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.client.gui.component.AbstractSpriteComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class JobsBackgroundComponent extends AbstractSpriteComponent<JobsBackgroundComponent> {

    private final int left;
    private final int right;

    private static final LinkedList<ResourceLocation> DEFAULT_SPRITES = new LinkedList<>(List.of(
            JobsPlus.getId("widget/background")
    ));

    public JobsBackgroundComponent(int width, int height, int left, int right) {
        super(DEFAULT_SPRITES, 0, 0, width, height);
        this.left = left;
        this.right = right;
    }

    @Override
    public void startRenderable() {
        super.startRenderable();
        center();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        graphics.blitSprite(RenderType::guiTextured, getSprite(0), 0, 0, left, getHeight());
        graphics.blitSprite(RenderType::guiTextured, getSprite(0), getWidth() - right, 0, right, getHeight());
    }
}
