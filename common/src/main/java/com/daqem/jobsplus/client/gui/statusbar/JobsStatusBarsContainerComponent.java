package com.daqem.jobsplus.client.gui.statusbar;

import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.uilib.gui.component.EmptyComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class JobsStatusBarsContainerComponent extends EmptyComponent {

    private JobsPlayer cachedJobsPlayer = null;
    private JobsStatusBarsComponent jobsStatusBarsComponent = null;

    public JobsStatusBarsContainerComponent() {
        super(0, 0, 0, 0);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, JobsPlayer jobsPlayer) {
        int windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        if (this.getWidth() != windowWidth) {
            this.setWidth(windowWidth);
        }
        if (this.getHeight() != windowHeight) {
            this.setHeight(windowHeight);
        }

        if (cachedJobsPlayer == null) {
            cachedJobsPlayer = jobsPlayer;
            jobsStatusBarsComponent = new JobsStatusBarsComponent(jobsPlayer);
            this.addComponent(jobsStatusBarsComponent);
        } else if (jobsPlayer != cachedJobsPlayer) {
            cachedJobsPlayer = jobsPlayer;
            jobsStatusBarsComponent = new JobsStatusBarsComponent(jobsPlayer);
            this.clear();
            this.addComponent(jobsStatusBarsComponent);
        }

        if (this.jobsStatusBarsComponent != null) {
            JobStatusBarAlignmentHorizontal horizontalAlignment = JobsPlusClientConfig.jobStatusBarHorizontalAlignment.get();
            JobStatusBarAlignmentVertical verticalAlignment = JobsPlusClientConfig.jobStatusBarVerticalAlignment.get();
            int offsetX = JobsPlusClientConfig.jobStatusBarXOffset.get();
            int offsetY = JobsPlusClientConfig.jobStatusBarYOffset.get();

            // Update position based on alignment settings
            int x = switch (horizontalAlignment) {
                case LEFT -> offsetX;
                case CENTER -> (windowWidth - jobsStatusBarsComponent.getWidth()) / 2;
                case RIGHT -> windowWidth - jobsStatusBarsComponent.getWidth() - offsetX;
            };

            int y = switch (verticalAlignment) {
                case TOP -> offsetY;
                case CENTER -> (windowHeight - jobsStatusBarsComponent.getHeight()) / 2;
                case BOTTOM -> windowHeight - jobsStatusBarsComponent.getHeight() - 1 - offsetY;
            };

            jobsStatusBarsComponent.setX(x);
            jobsStatusBarsComponent.setY(y);
        }

        super.renderBase(guiGraphics, mouseX, mouseY, partialTick, windowWidth, windowHeight);
    }
}
