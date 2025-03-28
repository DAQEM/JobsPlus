package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.ItemComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.uilib.client.gui.component.texture.NineSlicedTextureComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.texture.Textures;
import com.daqem.uilib.client.util.SoundManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;

import java.text.DecimalFormat;
import java.util.Objects;

public class JobsScrollItemComponent extends NineSlicedTextureComponent {

    private final JobsScreenOptions options;
    private final Job job;

    public JobsScrollItemComponent(int x, int y, int width, int height, JobsScreenOptions options, Job job) {
        super(Textures.SCROLL_BAR_BACKGROUND, x, y, width, height);
        this.options = options;
        this.job = job;

        int activeJobs = (int) options.getJobs().stream().filter(j -> j.getLevel() > 0).count();
        int jobCost = activeJobs >= JobsPlusConfig.amountOfFreeJobs.get() ? job.getJobInstance().getPrice() : 0;

        Text jobName = new Text(Minecraft.getInstance().font, job.getJobInstance().getName());
        Text wantJob = new Text(Minecraft.getInstance().font, JobsPlus.translatable("gui.want_this_job"));
        Text price = new Text(Minecraft.getInstance().font, JobsPlus.translatable("gui.price", JobsPlus.literal(String.valueOf(jobCost)).withStyle(ChatFormatting.WHITE), JobsPlus.translatable("gui.price.coins").withStyle(ChatFormatting.WHITE)));
        Text level = new Text(Minecraft.getInstance().font, JobsPlus.translatable("gui.level", JobsPlus.literal(String.valueOf(job.getLevel())).withStyle(ChatFormatting.WHITE)));
        Text experience = new Text(Minecraft.getInstance().font, JobsPlus.translatable("gui.exp", JobsPlus.literal(Math.round(job.getExperiencePercentage() * 10.0D) / 10.0D + "%").withStyle(ChatFormatting.WHITE)));

        jobName.setBold(true);
        jobName.setTextColor(job.getLevel() > 0 ? ChatFormatting.GREEN : ChatFormatting.RED);
        wantJob.setTextColor(ChatFormatting.AQUA);
        price.setTextColor(ChatFormatting.AQUA);
        level.setTextColor(ChatFormatting.AQUA);
        experience.setTextColor(ChatFormatting.AQUA);

        this.addChild(new JobsEmblemComponent(3, 5, job));
        this.addChild(new ItemComponent(8, 10, job.getJobInstance().getIconItem(), true));
        this.addChild(new TextComponent(33, 4, jobName));
        if (job.getLevel() > 0) {
            this.addChild(new TextComponent(33, 15, level));
            this.addChild(new TextComponent(33, 24, experience));
        } else {
            this.addChild(new TextComponent(33, 15, wantJob));
            this.addChild(new TextComponent(33, 24, price));
        }

        setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            if (Objects.requireNonNull(Objects.requireNonNull(getParent()).getParent()).isTotalHovered(mouseX, mouseY)) {
                options.setSelectedJob(job);
                SoundManager.playUIClick();
                return true;
            }
            return false;
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        if (options.getSelectedJob() == job) {
            color = ARGB.colorFromFloat(.75F, .75F, .75F, 1F);
        } else if (isTotalHovered(mouseX, mouseY)) {
            color = ARGB.colorFromFloat(.9F, .9F, .9F, 1F);
        }
        super.render(graphics, mouseX, mouseY, delta, color);
    }

    public Job getJob() {
        return job;
    }
}
