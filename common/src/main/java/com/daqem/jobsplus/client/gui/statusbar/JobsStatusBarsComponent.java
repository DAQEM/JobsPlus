package com.daqem.jobsplus.client.gui.statusbar;

import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.component.EmptyComponent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JobsStatusBarsComponent extends EmptyComponent {

    private final Map<Identifier, JobStatusBarComponent> statusbarComponents = new HashMap<>();
    private final JobsPlayer jobsPlayer;

    public JobsStatusBarsComponent(JobsPlayer jobsPlayer) {
        super(0, 0, 0, 0);
        this.jobsPlayer = jobsPlayer;
    }

    @Override
    public void renderBase(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        List<String> jobs = JobsPlusClientConfig.jobStatusBarJobs.get();

        // Handle addition and removal of status bar components based on config
        for (String jobLocation : jobs) {
            Identifier resourceLocation = Identifier.tryParse(jobLocation);
            if (resourceLocation != null && !statusbarComponents.containsKey(resourceLocation)) {
                Job job = jobsPlayer.jobsplus$getJob(resourceLocation);
                if (job == null) continue;
                JobStatusBarComponent component = new JobStatusBarComponent(job);
                statusbarComponents.put(resourceLocation, component);
                this.addComponent(component);
            }
        }
        statusbarComponents.keySet().removeIf(resourceLocation -> {
            if (!jobs.contains(resourceLocation.toString())) {
                this.removeComponent(statusbarComponents.get(resourceLocation));
                return true;
            }
            return false;
        });

        // Align components vertically based on their job level
        AtomicInteger yOffset = new AtomicInteger(0);
        statusbarComponents.values().stream()
                .sorted((a, b) -> Integer.compare(b.getJobLevel(), a.getJobLevel()))
                .forEach(component -> {
                    component.setY(yOffset.getAndAdd(component.getHeight() + 2) + 3);
                    component.setX(3);
                });

        // Update the height of this container component
        int height = yOffset.get();
        this.setHeight(height > 0 ? height - 2 + 6 : 0);
        this.setWidth(JobsPlusClientConfig.jobStatusBarWidth.get() + 6);

        if (JobsPlusClientConfig.jobStatusBarBackgroundEnabled.get() && !statusbarComponents.isEmpty()) {
            guiGraphics.fill(
                    this.getX(),
                    this.getY(),
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight(),
                    JobsPlusClientConfig.jobStatusBarBackgroundColor.get().intValue()
            );
            guiGraphics.hLine(
                    this.getX(),
                    this.getX() + this.getWidth() - 1,
                    this.getY(),
                    JobsPlusClientConfig.jobStatusBarBackgroundBorderColor.get().intValue()
            );
            guiGraphics.hLine(
                    this.getX(),
                    this.getX() + this.getWidth() - 1,
                    this.getY() + this.getHeight() - 1,
                    JobsPlusClientConfig.jobStatusBarBackgroundBorderColor.get().intValue()
            );
            guiGraphics.vLine(
                    this.getX(),
                    this.getY(),
                    this.getY() + this.getHeight(),
                    JobsPlusClientConfig.jobStatusBarBackgroundBorderColor.get().intValue()
            );
            guiGraphics.vLine(
                    this.getX() + this.getWidth() - 1,
                    this.getY(),
                    this.getY() + this.getHeight(),
                    JobsPlusClientConfig.jobStatusBarBackgroundBorderColor.get().intValue()
            );
        }

        // Render the components
        super.renderBase(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
    }
}
