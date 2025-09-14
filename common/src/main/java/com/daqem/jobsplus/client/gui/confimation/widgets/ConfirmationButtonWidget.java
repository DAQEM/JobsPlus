package com.daqem.jobsplus.client.gui.confimation.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;

public class ConfirmationButtonWidget extends CustomButtonWidget {

    public ConfirmationButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, new WidgetSprites(
                JobsPlus.getId("confirmation/button"),
                JobsPlus.getId("confirmation/button_hovered")
        ), onPress);
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int i) {
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + (this.getWidth() - font.width(this.getMessage())) / 2 + 1,
                this.getY() + (this.getHeight() - 6) / 2,
                this.isHovered() ? 0xFFFFFFFF : 0xFFEAF0FF,
                false
        );
    }
}
