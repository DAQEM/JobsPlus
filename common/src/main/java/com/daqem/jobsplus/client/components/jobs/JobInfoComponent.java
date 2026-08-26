package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.SolidColorComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.text.multiline.MultiLineText;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class JobInfoComponent extends AbstractComponent<JobInfoComponent> {

    private final JobsScreenOptions options;
    private final TextComponent title;
    private final TextComponent level;
    private final TextComponent experience;
    private final TextComponent description;
    private final TextComponent wantJob;

    private Job cachedJob;

    public JobInfoComponent(int x, int y, int width, int height, JobsScreenOptions options) {
        super(null, x, y, width, height);
        this.options = options;
        this.cachedJob = getJob();

        Font font = Minecraft.getInstance().font;
        Text titleText = new Text(font, getTitleText(), 4, 0);
        Text levelText = new Text(font, getLevelText(), 8, 18);
        Text experienceText = new Text(font, getExperienceText(), 70, 18);
        MultiLineText descriptionText = new MultiLineText(font, getDescriptionText(), 8, 32, width - 16);
        Text wantJobText = new Text(font, getWantJobText(), 8, 18);

        this.title = new TextComponent(titleText);
        this.level = new TextComponent(levelText);
        this.experience = new TextComponent(experienceText);
        this.description = new TextComponent(descriptionText);
        this.wantJob = new TextComponent(wantJobText);

        titleText.setBold(true);
        titleText.setTextColor(getJob().getJobInstance().getColorDecimal());
        levelText.setTextColor(ChatFormatting.DARK_GRAY);
        experienceText.setTextColor(ChatFormatting.DARK_GRAY);
        descriptionText.setTextColor(ChatFormatting.DARK_GRAY);
        wantJobText.setTextColor(ChatFormatting.DARK_GRAY);
        this.title.setScale(2F);

        this.addChild(title);
        this.addChild(level);
        this.addChild(experience);
        this.addChild(new SolidColorComponent(7, 27, width - 14, 1, 0xFFFFFFFF));
        this.addChild(description);
        this.addChild(wantJob);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (cachedJob != options.getSelectedJob()) {
            Objects.requireNonNull(title.getText()).setText(getTitleText());
            title.getText().setTextColor(getJob().getJobInstance().getColorDecimal());
            Objects.requireNonNull(level.getText()).setText(getLevelText());
            Objects.requireNonNull(experience.getText()).setText(getExperienceText());
            Objects.requireNonNull(description.getText()).setText(getDescriptionText());
            Objects.requireNonNull(wantJob.getText()).setText(getWantJobText());
            cachedJob = getJob();
        }
    }

    private Component getTitleText() {
        return options.getSelectedJob().getJobInstance().getName();
    }

    private Component getLevelText() {
        if (getJob().getLevel() <= 0) return JobsPlus.literal("");
        return JobsPlus.translatable("gui.level", JobsPlus.literal(String.valueOf(getJob().getLevel())).withStyle(ChatFormatting.WHITE));
    }

    private Component getExperienceText() {
        if (getJob().getLevel() <= 0) return JobsPlus.literal("");
        return JobsPlus.translatable("gui.exp", JobsPlus.literal(JobsPlus.formatExp(getJob().getExperience()) + "/" + JobsPlus.formatExp(Job.getExperienceToLevelUp(getJob().getLevel()))).withStyle(ChatFormatting.WHITE));
    }

    private Component getDescriptionText() {
        return getJob().getJobInstance().getDescription();
    }

    private Component getWantJobText() {
        if (getJob().getLevel() > 0) return JobsPlus.literal("");
        return JobsPlus.translatable("gui.want_this_job.price", JobsPlus.literal(String.valueOf(getJob().getJobInstance().getPrice())).withStyle(ChatFormatting.WHITE), JobsPlus.translatable("gui.price.coins").withStyle(ChatFormatting.WHITE));
    }

    private Job getJob() {
        return options.getSelectedJob();
    }
}
