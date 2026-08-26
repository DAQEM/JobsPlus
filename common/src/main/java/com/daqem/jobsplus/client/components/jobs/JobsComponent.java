package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.components.JobsButtonComponent;
import com.daqem.jobsplus.client.components.ModalComponent;
import com.daqem.jobsplus.client.components.TabGroupComponent;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.screen.job.tab.LeftTab;
import com.daqem.jobsplus.client.screen.job.tab.RightTab;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.c2s.ServerboundStartJobPacket;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.api.client.gui.component.event.OnClickEvent;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.uilib.client.gui.text.Text;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Objects;
import java.util.stream.Stream;

public class JobsComponent extends AbstractComponent<JobsComponent> {

    public static final int WIDTH = 326;
    public static final int HEIGHT = 166;
    public static final int GAP = 3;
    public static final int LEFT = 147;
    public static final int RIGHT = WIDTH - LEFT - GAP;

    private static final Font font = Minecraft.getInstance().font;

    private final JobsScreenOptions options;
    private final TextComponent coinsComponent;
    private final JobInfoComponent jobInfoComponent;
    private final JobItemRestrictionsComponent jobItemRestrictionsComponent;
    private final JobPowerupsComponent jobPowerupsComponent;
    private final JobExperienceComponent jobExperienceComponent;
    private final JobsButtonComponent startJobButtonComponent;
    private final ModalComponent modalComponent;
    private Job cachedJob;
    private double cachedCoins;

    public JobsComponent(Component title, JobsScreenOptions options, ModalComponent modalComponent) {
        super(null, 0, 0, WIDTH, HEIGHT);
        this.options = options;
        this.modalComponent = modalComponent;
        this.cachedJob = options.getSelectedJob();
        this.cachedCoins = options.getCoins();

        center();

        Text titleText = new Text(font, title, 7, 6);
        Text coinsText = new Text(font, JobsPlus.translatable("gui.coins.top", JobsPlus.formatCoin(options.getCoins())), LEFT, 6);

        JobsBackgroundComponent jobsBackgroundComponent = new JobsBackgroundComponent(WIDTH, HEIGHT, LEFT, RIGHT);
        TextComponent titleComponent = new TextComponent(titleText);
        JobsScrollComponent jobsScrollComponent = new JobsScrollComponent(7, 15, 116, 140, options);
        this.coinsComponent = new TextComponent(coinsText);
        TabGroupComponent leftTabsGroupComponent = new TabGroupComponent(
                Stream.of(LeftTab.values())
                        .map(LeftTab::getOptions)
                        .toList(), 0, -28, options);
        TabGroupComponent rightTabsGroupComponent = new TabGroupComponent(
                Stream.of(RightTab.values())
                        .map(RightTab::getOptions)
                        .toList(), LEFT + GAP, -28, options);
        this.jobInfoComponent = new JobInfoComponent(LEFT + GAP, 6, RIGHT, 140, options);
        this.jobItemRestrictionsComponent = new JobItemRestrictionsComponent(LEFT + GAP, 6, RIGHT, 140, options);
        this.jobPowerupsComponent = new JobPowerupsComponent(LEFT + GAP, 6, RIGHT, 140, options);
        this.jobExperienceComponent = new JobExperienceComponent(LEFT + GAP, 6, RIGHT, 140, options);
        this.startJobButtonComponent = new JobsButtonComponent(0, HEIGHT + GAP, WIDTH, 20, JobsPlus.translatable("gui.job.start"), (clickedObject, screen, mouseX, mouseY, button) -> {
            JobInstance jobInstance = options.getSelectedJob().getJobInstance();

            if (JobsPlusConfig.amountOfFreeJobs.get() <= options.getPreformingJobs().size() && jobInstance.getPrice() > options.getCoins()) {
                openModal(
                        JobsPlus.translatable("gui.jobs.not_enough_coins.title"),
                        JobsPlus.translatable("gui.jobs.not_enough_coins.description"),
                        (clickedObject1, screen1, mouseX1, mouseY1, button1) -> {
                            ModalComponent modal = (ModalComponent) clickedObject1.getParent();
                            if (modal != null) {
                                modal.close();
                            }
                            return true;
                        }, true
                );
            } else if (JobsPlusConfig.maxJobs.get() <= options.getPreformingJobs().size()) {
                openModal(
                        JobsPlus.translatable("gui.jobs.max_jobs_reached.title"),
                        JobsPlus.translatable("gui.jobs.max_jobs_reached.description"),
                        (clickedObject1, screen1, mouseX1, mouseY1, button1) -> {
                            ModalComponent modal = (ModalComponent) clickedObject1.getParent();
                            if (modal != null) {
                                modal.close();
                            }
                            return true;
                        }, true
                );

            }else {
                openModal(
                        JobsPlus.translatable("gui.job.start"),
                        JobsPlusConfig.amountOfFreeJobs.get() > options.getPreformingJobs().size() ?
                                JobsPlus.translatable("gui.job.start.description.free",
                                        jobInstance.getName().withColor(jobInstance.getColorDecimal())) :
                                JobsPlus.translatable("gui.job.start.description.not_free",
                                        jobInstance.getName().withColor(jobInstance.getColorDecimal()),
                                        JobsPlus.literal(String.valueOf(jobInstance.getPrice())).withColor(jobInstance.getColorDecimal())),
                        (clickedObject1, screen1, mouseX1, mouseY1, button1) -> {
                            NetworkManager.sendToServer(new ServerboundStartJobPacket(jobInstance.getLocation()));
                            return true;
                        }, false);
            }
            return true;
        });

        titleText.setTextColor(ChatFormatting.DARK_GRAY);
        coinsText.setTextColor(ChatFormatting.DARK_GRAY);
        jobInfoComponent.setVisible(options.getSelectedRightTab() == RightTab.INFO);
        jobItemRestrictionsComponent.setVisible(options.getSelectedRightTab() == RightTab.CRAFTING);
        jobPowerupsComponent.setVisible(options.getSelectedRightTab() == RightTab.POWER_UPS);
        jobExperienceComponent.setVisible(options.getSelectedRightTab() == RightTab.EXP);

        this.addChild(jobsBackgroundComponent);
        this.addChild(titleComponent);
        this.addChild(jobsScrollComponent);
        this.addChild(coinsComponent);
        this.addChild(leftTabsGroupComponent);
        this.addChild(rightTabsGroupComponent);
        this.addChild(jobInfoComponent);
        this.addChild(jobItemRestrictionsComponent);
        this.addChild(jobPowerupsComponent);
        this.addChild(jobExperienceComponent);
        this.addChild(startJobButtonComponent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.coinsComponent.setX(-this.coinsComponent.getWidth() - 7);
        jobInfoComponent.setVisible(options.getSelectedRightTab() == RightTab.INFO);
        jobItemRestrictionsComponent.setVisible(options.getSelectedRightTab() == RightTab.CRAFTING);
        jobPowerupsComponent.setVisible(options.getSelectedRightTab() == RightTab.POWER_UPS);
        jobExperienceComponent.setVisible(options.getSelectedRightTab() == RightTab.EXP);
        if (cachedJob != options.getSelectedJob()) {
            cachedJob = options.getSelectedJob();
            jobItemRestrictionsComponent.resetScroll();
            jobExperienceComponent.resetScroll();
        }
        if (cachedCoins != options.getCoins()) {
            cachedCoins = options.getCoins();
            if (coinsComponent.getText() != null) {
                Component component = JobsPlus.translatable("gui.coins.top", JobsPlus.formatCoin(options.getCoins()));
                int width = font.width(component);
                coinsComponent.getText().setText(component);
                coinsComponent.getText().setWidth(width);
                coinsComponent.setWidth(width);
            }
        }
        startJobButtonComponent.setVisible(options.getNotPreformingJobs().contains(options.getSelectedJob()) && JobsPlusConfig.maxJobs.get() > options.getPreformingJobs().size());
    }

    public void openModal(Component title, Component description, OnClickEvent<ButtonComponent> confirmEvent, boolean onlyConfirm) {
        Objects.requireNonNull(modalComponent.getTitle().getText()).setText(title);
        Objects.requireNonNull(modalComponent.getDescription().getText()).setText(description);
        modalComponent.getConfirmButton().setOnClickEvent(confirmEvent);
        modalComponent.getCancelButton().setVisible(!onlyConfirm);
        if (onlyConfirm) {
            modalComponent.getConfirmButton().setX((modalComponent.getWidth()) / 4);
        } else {
            modalComponent.getConfirmButton().setX((modalComponent.getWidth() + GAP) / 2);
        }
        modalComponent.setVisible(true);
    }

    @Override
    public boolean preformOnClickEvent(double mouseX, double mouseY, int button) {
        if (modalComponent.isVisible()) return false;
        return super.preformOnClickEvent(mouseX, mouseY, button);
    }

    public ModalComponent getModalComponent() {
        return modalComponent;
    }
}
