package com.daqem.jobsplus.client.screen.job;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.components.ModalClickPasserComponent;
import com.daqem.jobsplus.client.components.ModalComponent;
import com.daqem.jobsplus.client.components.SpriteComponent;
import com.daqem.jobsplus.client.components.jobs.JobsComponent;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.player.JobsClientPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.AbstractScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class JobsScreen extends AbstractScreen {

    private final JobsScreenOptions options;
    private final @Nullable Screen previousScreen;

    private SpriteComponent discordIconComponent;
    private ModalComponent modalComponent;
    private ModalClickPasserComponent modalClickPasserComponent;

    public JobsScreen(JobsScreenOptions options, @Nullable Screen previousScreen) {
        super(JobsPlus.translatable("gui.title.jobs"));
        this.options = options;
        this.previousScreen = previousScreen;
    }

    @Override
    public void startScreen() {
        this.discordIconComponent = new SpriteComponent(JobsPlus.getId("icon/discord"), getWidth() - 18, getHeight() - 18, 16, 16);
        this.modalComponent = new ModalComponent(200);
        this.modalClickPasserComponent = new ModalClickPasserComponent(getWidth(), getHeight(), modalComponent);

        this.discordIconComponent.setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            try {
                Util.getPlatform().openUri(new URI("https://daqem.com/discord"));
            } catch (Exception ignored) {}
            return true;
        });

        this.addComponent(modalClickPasserComponent);
        this.addComponent(new JobsComponent(getTitle(), options, modalComponent));
        this.addComponent(discordIconComponent);
        this.addComponent(modalComponent);
    }

    @Override
    public void onResizeScreenRepositionComponents(int width, int height) {
        super.onResizeScreenRepositionComponents(width, height);

        this.discordIconComponent.setX(width - 18);
        this.discordIconComponent.setY(height - 18);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBlurredBackground(delta);
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.modalClickPasserComponent.setWidth(getWidth());
        this.modalClickPasserComponent.setHeight(getHeight());
    }

    public @Nullable Screen getPreviousScreen() {
        return previousScreen;
    }

    @Override
    public void onClose() {
        if (this.modalComponent.isVisible()) {
            this.modalComponent.setVisible(false);
            return;
        }
        if (this.previousScreen != null) {
            Minecraft.getInstance().setScreen(this.previousScreen);
        } else {
            super.onClose();
        }
    }
}
