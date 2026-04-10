package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.PowerupsButtonWidget;
import com.daqem.uilib.gui.component.AbstractComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public class JobsComponent extends AbstractComponent {

    private final JobsScreenState state;
    private final JobSelectionComponent jobSelectionComponent;
    private final SelectedJobComponent selectedJobComponent;
    private final CoinsComponent coinsComponent;
    private final PowerupsButtonWidget powerupsButtonWidget;
    private final PlayerProfileComponent playerProfileComponent;
    private boolean wasViewingPlayer = false;

    public JobsComponent(JobsScreenState state) {
        super(0, 0, 302, 204 + 2);
        this.state = state;

        this.jobSelectionComponent = new JobSelectionComponent(state);
        this.selectedJobComponent = new SelectedJobComponent(state);
        this.coinsComponent = new CoinsComponent(state);
        this.powerupsButtonWidget = new PowerupsButtonWidget(state);
        this.playerProfileComponent = new PlayerProfileComponent(state);
        TabSwitcherComponent tabSwitcherComponent = new TabSwitcherComponent(state);

        this.addComponent(jobSelectionComponent);
        this.addComponent(selectedJobComponent);
        this.addComponent(coinsComponent);
        this.addComponent(tabSwitcherComponent);
        this.addWidget(powerupsButtonWidget);
    }


    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                JobsPlus.API.getId("jobs/background"),
                this.getTotalX(),
                this.getTotalY(),
                this.getWidth(),
                this.getHeight() - 2
        );

        if (!wasViewingPlayer && state.getViewingPlayer() != null && !state.getViewingPlayerJobs().isEmpty()) {
            this.removeComponent(jobSelectionComponent);
            this.removeComponent(selectedJobComponent);
            this.removeComponent(coinsComponent);
            this.removeWidget(powerupsButtonWidget);
            this.addComponent(playerProfileComponent);
            wasViewingPlayer = true;
            this.updateParentPosition(getParentX(), getParentY(), parentWidth, parentHeight);
        } else if (wasViewingPlayer && state.getViewingPlayer() == null) {
            this.addComponent(jobSelectionComponent);
            this.addComponent(selectedJobComponent);
            this.addComponent(coinsComponent);
            this.addWidget(powerupsButtonWidget);
            this.removeComponent(playerProfileComponent);
            wasViewingPlayer = false;
        }
    }
}
