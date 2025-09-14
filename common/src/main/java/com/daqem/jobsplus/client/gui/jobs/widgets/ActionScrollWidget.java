package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.uilib.api.component.IComponent;
import net.minecraft.client.gui.GuiGraphics;

public class ActionScrollWidget extends AbstractScrollWidget {

    public ActionScrollWidget(int width, int height) {
        super(width, height, 8);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        if (!getComponents().isEmpty()) {
            IComponent firstComponent = getComponents().getFirst();
            if (firstComponent.getHeight() > getHeight() && scrollAmount() < maxScrollAmount()) {
                guiGraphics.fillGradient(
                        this.getX(), this.getY() + getHeight() - 8, this.getX() + firstComponent.getWidth(), this.getY() + this.getHeight(),
                        0x00D8BF96, 0xFFD8BF96
                );
            }
            if (firstComponent.getHeight() > getHeight() && scrollAmount() > 0) {
                guiGraphics.fillGradient(
                        this.getX(), this.getY(), this.getX() + firstComponent.getWidth(), this.getY() + 8,
                        0xFFD8BF96, 0x00D8BF96
                );
            }
        }
    }
}
