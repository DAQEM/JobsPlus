package com.daqem.jobsplus.client.components;

import com.daqem.uilib.client.gui.component.AbstractSpriteComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class SpriteComponent extends AbstractSpriteComponent<SpriteComponent> {

    private ResourceLocation sprite;

    public SpriteComponent(ResourceLocation sprites, int x, int y, int width, int height) {
        super(new LinkedList<>(List.of(sprites)), x, y, width, height);
        this.sprite = sprites;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.blitSprite(sprite, 0, 0, getWidth(), getHeight());
    }

    public ResourceLocation getSelectedSprite() {
        return sprite;
    }

    public void setSelectedSprite(ResourceLocation selectedSprite) {
        this.sprite = selectedSprite;
    }
}
