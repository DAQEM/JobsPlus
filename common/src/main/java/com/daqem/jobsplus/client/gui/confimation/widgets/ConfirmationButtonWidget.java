package com.daqem.jobsplus.client.gui.confimation.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ConfirmationButtonWidget extends CustomButtonWidget {

    public ConfirmationButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, new WidgetSprites(
                JobsPlus.API.getId("confirmation/button"),
                JobsPlus.API.getId("confirmation/button_hovered")
        ), onPress);
    }

    @Override
    protected void extractDefaultLabel(@NotNull ActiveTextCollector activeTextCollector) {
    }

    public void text(GuiGraphicsExtractor guiGraphics, Font font) {
        guiGraphics.text(
                font,
                this.getMessage(),
                this.getX() + (this.getWidth() - font.width(this.getMessage())) / 2 + 1,
                this.getY() + (this.getHeight() - 6) / 2,
                this.isHovered() ? 0xFFFFFFFF : 0xFFEAF0FF,
                false
        );
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int i, int j, float f) {
        super.extractContents(guiGraphics, i, j, f);
        this.text(guiGraphics, Minecraft.getInstance().font);
    }
}
