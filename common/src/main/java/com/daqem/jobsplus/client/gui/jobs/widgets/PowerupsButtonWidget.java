package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreen;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class PowerupsButtonWidget extends CustomButtonWidget {

    private final static Component MESSAGE = JobsPlus.translatable("gui.jobs.powerups");

    private final JobsScreenState state;

    public PowerupsButtonWidget(JobsScreenState state) {
        super(186, 188, Minecraft.getInstance().font.width(MESSAGE) + 20, 18, MESSAGE, null,
                button -> Minecraft.getInstance().setScreen(new PowerupsScreen(new PowerupsScreenState(
                        state.getSelectedJob(),
                        state.getCoins()
                ), Minecraft.getInstance().screen))
        );
        this.state = state;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, JobsPlus.getId("jobs/tab_bottom"), this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.alpha));
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + 10,
                this.getY() + 6,
                ARGB.color(this.alpha, isHoveredOrFocused() ? this.state.getSelectedJob().getJobInstance().getColorDecimal() : 0x1E1410),
                false
        );
    }
}
