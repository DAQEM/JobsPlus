package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;

public class ActionsPaginationDotWidget extends CustomButtonWidget {

    private final static WidgetSprites SPRITES = new WidgetSprites(
            JobsPlus.API.getId("jobs/pagination_inactive"),
            JobsPlus.API.getId("jobs/pagination_active")
    );

    private final IAction action;
    private final JobsScreenState state;

    public ActionsPaginationDotWidget(int x, int y, IAction action, JobsScreenState state) {
        super(x, y, 6, 4, action.getName(), null, button -> state.setActiveAction(action));
        this.action = action;
        this.state = state;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                SPRITES.get(true, this.state.getActiveAction() == this.action),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight()
        );
    }
}
