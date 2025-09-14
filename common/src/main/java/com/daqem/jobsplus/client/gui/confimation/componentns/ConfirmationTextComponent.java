package com.daqem.jobsplus.client.gui.confimation.componentns;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.confimation.ConfirmationScreenState;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import net.minecraft.client.Minecraft;

public class ConfirmationTextComponent extends SpriteComponent {

    public ConfirmationTextComponent(ConfirmationScreenState state) {
        super(0, 0, 0, 0, JobsPlus.getId("confirmation/background"));

        int borderHorizontal = 14;
        int borderVertical = 10;

        int maxWidth = 200;
        int minWidth = 103;
        int textWidth = Math.clamp(Minecraft.getInstance().font.width(state.getMessage()), minWidth, maxWidth);
        MultiLineTextComponent textComponent = new MultiLineTextComponent(borderHorizontal, borderVertical, textWidth, state.getMessage(), 0xFFEAF0FF);

        this.setWidth(Math.max(textComponent.getWidth(), minWidth) + borderHorizontal * 2);
        this.setHeight(textComponent.getHeight() + borderVertical * 2);
        this.addComponent(textComponent);
    }
}
