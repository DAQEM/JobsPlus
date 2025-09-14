package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.confimation.ConfirmationScreen;
import com.daqem.jobsplus.client.gui.confimation.ConfirmationScreenState;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.c2s.ServerboundOpenJobsScreenPacket;
import com.daqem.jobsplus.networking.c2s.ServerboundStartJobPacket;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class StartJobButtonWidget extends CustomButtonWidget {

    private final static Component MESSAGE = JobsPlus.translatable("gui.jobs.start_job");

    private final JobsScreenState state;

    public StartJobButtonWidget(JobsScreenState state) {
        super(26, 168, Minecraft.getInstance().font.width(MESSAGE) + 20, 18, MESSAGE, null, button -> {
            Job selectedJob = state.getSelectedJob();
            JobInstance jobInstance = selectedJob.getJobInstance();
            int jobAmount = state.getJobs().stream().filter(job -> job.getLevel() > 0).toList().size();
            Component freeJobMessage = JobsPlus.translatable("gui.confirmation.purchase_job.free", jobInstance.getName());
            Component paidJobMessage = JobsPlus.translatable("gui.confirmation.purchase_job.paid", jobInstance.getName(), jobInstance.getPrice());
            if (selectedJob.getLevel() == 0) {
                Minecraft.getInstance().setScreen(new ConfirmationScreen(Minecraft.getInstance().screen, new ConfirmationScreenState(
                        jobAmount >= JobsPlusConfig.amountOfFreeJobs.get() ? paidJobMessage : freeJobMessage,
                        () -> {
                            NetworkManager.sendToServer(new ServerboundStartJobPacket(selectedJob.getJobInstance().getLocation()));
                            NetworkManager.sendToServer(new ServerboundOpenJobsScreenPacket());
                        }
                )));
            }
        });
        this.state = state;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, JobsPlus.getId("jobs/tab_bottom"), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + 10,
                this.getY() + 6,
                ARGB.color(this.alpha, isHoveredOrFocused() ? this.state.getSelectedJob().getJobInstance().getColorDecimal() : 0x1E1410),
                false
        );
    }
}
