package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.jobs.tab.RightTab;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;

public class RightTabWidget extends CustomButtonWidget {

    private final JobsScreenState state;
    private final RightTab tab;

    public RightTabWidget(JobsScreenState state, RightTab tab) {
        super(0, 0, 30, 22, tab.getName(), null, button -> state.setSelectedRightTab(tab));
        this.state = state;
        this.tab = tab;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                this.state.getSelectedRightTab() == this.tab ? tab.getActiveSprite() : tab.getInactiveSprite(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight()
        );
    }
}
