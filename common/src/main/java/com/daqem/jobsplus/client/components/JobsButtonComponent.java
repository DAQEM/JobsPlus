package com.daqem.jobsplus.client.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.api.client.gui.component.event.OnClickEvent;
import com.daqem.uilib.api.client.gui.text.IText;
import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JobsButtonComponent extends ButtonComponent {

    private static final LinkedList<ResourceLocation> DEFAULT_SPRITES = new LinkedList<>(List.of(
            JobsPlus.getId("widget/button"),
            JobsPlus.getId("widget/button_disabled"),
            JobsPlus.getId("widget/button_highlighted")
    ));

    public JobsButtonComponent(int x, int y, int width, int height, Component component, OnClickEvent<ButtonComponent> onClickEvent) {
        super(DEFAULT_SPRITES, x, y, width, height, component, onClickEvent);
        Objects.requireNonNull(getText()).setShadow(false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        if (this.isEnabled()) Objects.requireNonNull(getText()).setTextColor(ChatFormatting.DARK_GRAY);
    }
}
