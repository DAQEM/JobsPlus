package com.daqem.jobsplus.client.gui.statusbar;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.component.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class JobStatusBarComponent extends AbstractComponent {

    private final Job job;

    public JobStatusBarComponent(Job job) {
        super(0, 0, 0, 0);
        this.job = job;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        setWidth(JobsPlusClientConfig.jobStatusBarWidth.get() - 1);
        boolean isDetailed = JobsPlusClientConfig.jobStatusBarDetailed.get();
        int lineHeight = Minecraft.getInstance().font.lineHeight;
        setHeight(isDetailed ? 3 + lineHeight : 3);
        int yOffset = isDetailed ? lineHeight : 0;
        guiGraphics.horizontalLine(getTotalX() + 1, getTotalX() + getWidth() - 1, getTotalY() + yOffset, JobsPlusClientConfig.jobStatusBarColor.get().intValue());
        guiGraphics.horizontalLine(getTotalX(), getTotalX() + getWidth(), getTotalY() + 1 + yOffset, JobsPlusClientConfig.jobStatusBarColor.get().intValue());
        guiGraphics.horizontalLine(getTotalX() + 1, getTotalX() + getWidth() - 1, getTotalY() + 1 + yOffset, JobsPlusClientConfig.jobStatusBarEmptyColor.get().intValue());
        guiGraphics.horizontalLine(getTotalX() + 1, getTotalX() + getWidth() - 1, getTotalY() + 2 + yOffset, JobsPlusClientConfig.jobStatusBarColor.get().intValue());

        double experiencePercentage = job.getExperiencePercentage();
        if ( experiencePercentage > 0) {
            guiGraphics.horizontalLine(getTotalX() + 1, getTotalX() + (int) (getWidth() * (experiencePercentage / 100.0f)) - 1, getTotalY() + 1 + yOffset, 0xFF000000 | job.getJobInstance().getColorDecimal());
        }

        if (isDetailed) {
            Font font = Minecraft.getInstance().font;
            String jobName = job.getJobInstance().getName().getString();
            String suffix = " " + job.getLevel() + " (" + JobsPlus.formatExp(experiencePercentage) + "%)";

            // Start with full text
            String text = jobName + suffix;

            // Check if text is too wide
            if (font.width(text) > getWidth()) {
                String ellipsis = "...";
                int suffixWidth = font.width(suffix);
                int ellipsisWidth = font.width(ellipsis);

                // Calculate how much space is left for the name after reserving space for suffix and ellipsis
                int availableNameWidth = getWidth() - suffixWidth - ellipsisWidth;

                if (availableNameWidth > 0) {
                    // Truncate name based on visual width
                    String truncatedName = font.plainSubstrByWidth(jobName, availableNameWidth);
                    text = truncatedName + ellipsis + suffix;
                } else {
                    // Fallback if space is extremely tight
                    text = suffix;
                }
            }

            guiGraphics.text(
                    font,
                    text,
                    getTotalX(),
                    getTotalY(),
                    0xFF000000 | job.getJobInstance().getColorDecimal(),
                    false
            );
        }
    }

    public int getJobLevel() {
        return job.getLevel();
    }
}