package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.StartJobButtonWidget;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.item.ItemComponent;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public class SelectedJobComponent extends EmptyComponent {

    private final JobsScreenState state;
    private final TruncatedTextComponent jobTitleComponent;
    private final ItemComponent jobIconComponent;
    private final StartJobButtonWidget startJobButtonWidget;

    public SelectedJobComponent(JobsScreenState state) {
        super(21, 20, 117, 34);
        this.state = state;
        this.jobTitleComponent = new TruncatedTextComponent(26, 0, 90, Component.empty(), 0);
        this.jobIconComponent = new ItemComponent(4, 4, ItemStack.EMPTY);
        this.startJobButtonWidget = new StartJobButtonWidget(this.state);

        SpriteComponent jobIconSlotComponent = new SpriteComponent(0, 0, 24, 24, JobsPlus.getId("jobs/job_icon_slot"));
        SpriteComponent separatorComponent = new SpriteComponent(0, 27, 113, 7, JobsPlus.getId("jobs/separator_line"));

        this.addComponent(this.jobTitleComponent);
        this.addComponent(jobIconSlotComponent);
        this.addComponent(separatorComponent);
        this.addComponent(this.jobIconComponent);
        if (this.state.getSelectedJob().getLevel() == 0 && canStartNewJob()) {
            this.addWidget(this.startJobButtonWidget);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        Job selectedJob = this.state.getSelectedJob();
        JobInstance jobInstance = selectedJob.getJobInstance();

        this.jobTitleComponent.setText(jobInstance.getName().copy().withStyle(Style.EMPTY.withBold(true)));
        this.jobTitleComponent.setColor(jobInstance.getColorDecimal() | 0xFF000000);
        this.jobIconComponent.setItemStack(jobInstance.getIconItem());

        if (selectedJob.getLevel() > 0) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(getTotalX() + 26, getTotalY() + Minecraft.getInstance().font.lineHeight);
            guiGraphics.pose().scale(0.75f, 0.75f);
            guiGraphics.drawString(Minecraft.getInstance().font, JobsPlus.translatable("gui.jobs.level", selectedJob.getLevel()), 0, 0, 0xFF1E1410, false);
            guiGraphics.drawString(Minecraft.getInstance().font, JobsPlus.translatable("gui.jobs.experience", selectedJob.getExperience(), selectedJob.getExperienceForNextLevel()), 0, Minecraft.getInstance().font.lineHeight, 0xFF1E1410, false);
            guiGraphics.pose().popMatrix();

            this.removeWidget(this.startJobButtonWidget);
        } else {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(getTotalX() + 26, getTotalY() + Minecraft.getInstance().font.lineHeight);
            guiGraphics.pose().scale(0.75f, 0.75f);
            if (canStartNewJob()) {
                guiGraphics.drawString(Minecraft.getInstance().font, JobsPlus.translatable("gui.jobs.price", jobInstance.getPrice()), 0, 0, 0xFF1E1410, false);
            } else {
                guiGraphics.drawString(Minecraft.getInstance().font, JobsPlus.translatable("gui.jobs.max_jobs", JobsPlusConfig.maxJobs.get()), 0, 0, 0xFFFF5555, false);
            }
            guiGraphics.pose().popMatrix();
            if (jobInstance.getPrice() > this.state.getCoins() && !canStartFreeJob()) {
                this.removeWidget(this.startJobButtonWidget);
            } else {
                if (!this.getWidgets().contains(this.startJobButtonWidget) && canStartNewJob()) {
                    this.addWidget(this.startJobButtonWidget);
                    this.updateParentPosition(getParentX(), getParentY(), parentWidth, parentHeight);
                }
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
    }

    private boolean canStartNewJob() {
        return this.state.getActiveJobCount() < JobsPlusConfig.maxJobs.get();
    }

    private boolean canStartFreeJob() {
        return this.state.getActiveJobCount() < JobsPlusConfig.amountOfFreeJobs.get();
    }
}
