package com.daqem.jobsplus.client.gui.confimation;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.confimation.componentns.ConfirmationComponent;
import com.daqem.uilib.gui.AbstractScreen;
import net.minecraft.client.gui.screens.Screen;

public class ConfirmationScreen extends AbstractScreen {

    private final Screen previousScreen;
    private final ConfirmationScreenState state;

    public ConfirmationScreen(Screen previousScreen, ConfirmationScreenState state) {
        super(JobsPlus.translatable("gui.confirmation.title"));
        this.previousScreen = previousScreen;
        this.state = state;
    }

    @Override
    protected void init() {
        ConfirmationComponent confirmationComponent = new ConfirmationComponent(this.state);
        confirmationComponent.center();
        this.addComponent(confirmationComponent);

        super.init();
    }

    public Screen getPreviousScreen() {
        return previousScreen;
    }

    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(this.previousScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }
}
