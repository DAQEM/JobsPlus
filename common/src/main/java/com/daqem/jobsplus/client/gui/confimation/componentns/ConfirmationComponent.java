package com.daqem.jobsplus.client.gui.confimation.componentns;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.confimation.ConfirmationScreenState;
import com.daqem.jobsplus.client.gui.confimation.widgets.ConfirmationButtonWidget;
import com.daqem.uilib.gui.component.EmptyComponent;
import net.minecraft.client.Minecraft;

public class ConfirmationComponent extends EmptyComponent {

    public ConfirmationComponent(ConfirmationScreenState state) {
        super(0, 0, 0, 0);

        ConfirmationTextComponent confirmationTextComponent = new ConfirmationTextComponent(state);
        int buttonWidth = 50;

        ConfirmationButtonWidget cancelButton = new ConfirmationButtonWidget(
                confirmationTextComponent.getWidth() / 2 - buttonWidth - 3
                , confirmationTextComponent.getHeight(), buttonWidth, 30, JobsPlus.translatable("gui.confirmation.cancel"), button -> {
            assert Minecraft.getInstance().screen != null;
            Minecraft.getInstance().screen.onClose();
        });
        ConfirmationButtonWidget yesButton = new ConfirmationButtonWidget(
                confirmationTextComponent.getWidth() / 2 + 3
                , confirmationTextComponent.getHeight(), buttonWidth, 30, JobsPlus.translatable("gui.confirmation.yes"), button -> state.getOnConfirm().onConfirm());

        this.setWidth(confirmationTextComponent.getWidth());
        this.setHeight(confirmationTextComponent.getHeight() + yesButton.getHeight() + 5);

        this.addComponent(confirmationTextComponent);
        this.addWidget(yesButton);
        this.addWidget(cancelButton);
    }
}
