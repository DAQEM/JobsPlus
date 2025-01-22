package com.daqem.jobsplus.client.components;

import com.daqem.uilib.api.client.gui.component.IComponent;
import com.daqem.uilib.api.client.gui.texture.ITexture;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class ModalClickPasserComponent extends AbstractComponent<ModalClickPasserComponent> {

    private final ModalComponent modalComponent;

    public ModalClickPasserComponent(int width, int height, ModalComponent modalComponent) {
        super(null, 0, 0, width, height);
        this.modalComponent = modalComponent;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {

    }

    @Override
    public boolean preformOnClickEvent(double mouseX, double mouseY, int button) {
        if (modalComponent.isVisible()) {
            if (handleClickEvent(modalComponent.getChildren(), mouseX, mouseY, button)) {
                return true;
            }
            if (modalComponent.preformOnClickEvent(mouseX, mouseY, button)) {
                return true;
            }
        }
        return modalComponent.isVisible();
    }

    private boolean handleClickEvent(List<IComponent<?>> components, double mouseX, double mouseY, int button) {
        for (IComponent<?> component : new ArrayList<>(components)) {
            if (handleClickEvent(component.getChildren(), mouseX, mouseY, button)) {
                return true;
            }
            if (component.preformOnClickEvent(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }
}
