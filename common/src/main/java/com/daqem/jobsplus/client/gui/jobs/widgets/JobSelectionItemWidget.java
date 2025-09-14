package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;

public class JobSelectionItemWidget extends CustomButtonWidget {

    private static final WidgetSprites SPRITES = new WidgetSprites(
            JobsPlus.getId("jobs/job_button"),
            JobsPlus.getId("jobs/job_button_hovered")
    );

    private final Job job;
    private final JobsScreenState state;

    public JobSelectionItemWidget(Job job, JobsScreenState state) {
        super(0, 0, 99, 19, job.getJobInstance().getName(), SPRITES, button -> state.setSelectedJob(job));
        this.job = job;
        this.state = state;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.active, this.isHovered() || this.job == this.state.getSelectedJob()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(this.getX() + 5, (this.getY() + 3));
        guiGraphics.pose().scale(0.75f, 0.75f);
        guiGraphics.renderFakeItem(this.job.getJobInstance().getIconItem(), 0, 0);
        guiGraphics.pose().popMatrix();
        guiGraphics.drawString(minecraft.font, this.getMessage().copy().withColor(this.job.getJobInstance().getColorDecimal()), this.getX() + 20, this.getY() + (this.job.getLevel() > 0 ? 4 : 5), 0xFF1E1410, false);
        if (this.job.getLevel() > 0) {
            guiGraphics.drawString(minecraft.font, JobsPlus.literal(this.job.getLevel() + ""), this.getX() + this.getWidth() - 2 - minecraft.font.width(this.job.getLevel() + ""), this.getY() + 4, 0xFFD8BF96, false);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, JobsPlus.getId("jobs/exp_bar"), getX() + 20, getY() + 13, 76, 3);
            double expPercentage = this.job.getExperiencePercentage();
            int expWidth = (int) Mth.clamp(expPercentage / 100 * 75, 1, 75);
            guiGraphics.fill(getX() + 21, getY() + 14, getX() + 21 + expWidth, getY() + 15, job.getJobInstance().getColorDecimal() | 0xFF000000);
        }
    }
}
