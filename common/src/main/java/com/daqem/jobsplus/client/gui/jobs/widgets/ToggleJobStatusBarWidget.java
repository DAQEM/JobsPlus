package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.util.ArrayList;
import java.util.List;

public class ToggleJobStatusBarWidget extends CustomButtonWidget {

    private final static Component MESSAGE = JobsPlus.translatable("gui.jobs.toggle_job_status_bar");
    private static final WidgetSprites SPRITES = new WidgetSprites(
            JobsPlus.getId("jobs/job_status_bar_toggle"),
            JobsPlus.getId("jobs/job_status_bar_toggle_active")
    );

    private final JobsScreenState state;

    public ToggleJobStatusBarWidget(JobsScreenState state) {
        super(107, 0, 7, 7, MESSAGE, null, button -> {
            List<String> jobs = new ArrayList<>(JobsPlusClientConfig.jobStatusBarJobs.get());
            String jobLocation = state.getSelectedJob().getJobInstance().getLocation().toString();
            if (jobs.contains(jobLocation)) {
                jobs.remove(jobLocation);
            } else {
                jobs.add(jobLocation);
            }
            JobsPlusClientConfig.jobStatusBarJobs.set(jobs);
            JobsPlusClientConfig.config.save();
        });
        this.state = state;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        List<String> jobs = JobsPlusClientConfig.jobStatusBarJobs.get();
        JobInstance jobInstance = state.getSelectedJob().getJobInstance();
        String jobLocation = jobInstance.getLocation().toString();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(true, jobs.contains(jobLocation) || this.isHovered()), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        guiGraphics.hLine(this.getX() + 2, this.getX() + 4, this.getY() + 3, 0xFF000000 | jobInstance.getColorDecimal());
    }
}
