package com.daqem.jobsplus.client.components.powerup;

import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.texture.RepeatingTextureComponent;
import com.daqem.uilib.client.gui.texture.Texture;
import net.minecraft.client.gui.GuiGraphics;

public class PowerupTreeContainerComponent extends AbstractComponent<PowerupTreeContainerComponent> {

    private final PowerupTreeComponent treeComponent;

    public PowerupTreeContainerComponent(int x, int y, int width, int height, JobsScreenOptions options) {
        super(null, x, y, width, height);

        this.treeComponent = new PowerupTreeComponent(0, 0, width, height, options);
        RepeatingTextureComponent background = new RepeatingTextureComponent(new Texture(options.getSelectedJob().getJobInstance().getPowerupBackground(), 0, 0, 16, 16), 0, 0, width, height);

        this.addChild(background);
        this.addChild(treeComponent);

        treeComponent.centerVertically();
        treeComponent.setY(treeComponent.getY() - (PowerupComponent.SIZE / 2));
        treeComponent.setX(treeComponent.getX() + PowerupTreeComponent.PADDING);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void renderBase(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(getTotalX(), getTotalY(), getTotalX() + getWidth(), getTotalY() + getHeight());
        super.renderBase(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }

    @Override
    public void renderTooltipsBase(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.pose().pushPose();
        super.renderTooltipsBase(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean preformOnClickEvent(double mouseX, double mouseY, int button) {
        if (isTotalHovered(mouseX, mouseY)) {
            return super.preformOnClickEvent(mouseX, mouseY, button);
        }
        return false;
    }

    public PowerupTreeComponent getTreeComponent() {
        return treeComponent;
    }
}
