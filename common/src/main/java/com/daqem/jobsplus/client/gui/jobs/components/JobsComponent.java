package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.widgets.PowerupsButtonWidget;
import com.daqem.uilib.gui.component.AbstractComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;

public class JobsComponent extends AbstractComponent {

    public JobsComponent(JobsScreenState state) {
        super(0, 0, 302, 204 + 2);

        JobSelectionComponent jobSelectionComponent = new JobSelectionComponent(state);
        SelectedJobComponent selectedJobComponent = new SelectedJobComponent(state);
        CoinsComponent coinsComponent = new CoinsComponent(state);
        TabSwitcherComponent tabSwitcherComponent = new TabSwitcherComponent(state);
        PowerupsButtonWidget powerupsButtonWidget = new PowerupsButtonWidget(state);

        this.addComponent(jobSelectionComponent);
        this.addComponent(selectedJobComponent);
        this.addComponent(coinsComponent);
        this.addComponent(tabSwitcherComponent);
        this.addWidget(powerupsButtonWidget);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                JobsPlus.getId("jobs/background"),
                this.getTotalX(),
                this.getTotalY(),
                this.getWidth(),
                this.getHeight() - 2
        );
    }
}
