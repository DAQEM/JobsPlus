package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.mixin.client.AbstractScrollAreaAccessor;
import com.daqem.uilib.gui.widget.ScrollContainerWidget;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;

public abstract class AbstractScrollWidget extends ScrollContainerWidget {

    private final static int SCROLL_HANDLE_WIDTH = 14;
    private final static int SCROLL_HANDLE_HEIGHT = 15;

    private final int itemHeight;

    public AbstractScrollWidget(int width, int height, int itemHeight) {
        super(width, height, 0);
        this.itemHeight = itemHeight;
    }

    @Override
    protected int scrollerHeight() {
        return SCROLL_HANDLE_HEIGHT;
    }

    @Override
    public boolean updateScrolling(MouseButtonEvent event) {
        ((AbstractScrollAreaAccessor) this).jobsplus$setScrolling(this.scrollbarVisible()
                && this.isValidClickButton(event.buttonInfo())
                && event.x() >= this.scrollBarX()
                && event.x() <= this.scrollBarX() + SCROLL_HANDLE_WIDTH
                && event.y() >= this.getY()
                && event.y() < this.getBottom());
        return ((AbstractScrollAreaAccessor) this).jobsplus$getScrolling();
    }

    protected int scrollBarX() {
        return this.getRight() - SCROLL_HANDLE_WIDTH;
    }

    protected int scrollBarY() {
        int availableHeight = this.height - this.scrollerHeight() - 8;
        int baseY = (int)(this.scrollAmount() * availableHeight / this.maxScrollAmount()) + this.getY() + 4;
        return Mth.clamp(baseY, this.getY() + 4, this.getBottom() - this.scrollerHeight() - 4);
    }

    @Override
    protected void renderScrollbar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.scrollbarVisible()) {
            int scrollBarX = this.scrollBarX();
            int scrollerHeight = this.scrollerHeight();
            int scrollBarY = this.scrollBarY();
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    JobsPlus.getId("jobs/scroll_bar"),
                    scrollBarX + 3,
                    this.getY(),
                    8,
                    this.getHeight()
            );
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    JobsPlus.getId("jobs/scroll_handle"),
                    scrollBarX,
                    Mth.clamp(
                            scrollBarY,
                            this.getY() + 4,
                            this.getBottom() - scrollerHeight - 4
                    ),
                    14,
                    scrollerHeight
            );
            if (this.isOverScrollbar(mouseX, mouseY)) {
                guiGraphics.requestCursor(((AbstractScrollAreaAccessor) this).jobsplus$getScrolling() ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
            }
        }
    }

    @Override
    protected double scrollRate() {
        return this.itemHeight;
    }
}
