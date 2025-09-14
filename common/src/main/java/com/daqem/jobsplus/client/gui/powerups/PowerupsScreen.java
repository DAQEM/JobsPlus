package com.daqem.jobsplus.client.gui.powerups;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.powerups.components.PowerupsComponent;
import com.daqem.uilib.gui.AbstractScreen;
import net.minecraft.client.gui.screens.Screen;

public class PowerupsScreen extends AbstractScreen {

    private final PowerupsScreenState state;
    private final Screen previousScreen;

    public PowerupsScreen(PowerupsScreenState state, Screen previousScreen) {
        super(JobsPlus.translatable("gui.title.powerups"));
        this.state = state;
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        PowerupsComponent powerupsComponent = new PowerupsComponent(this.state);
        powerupsComponent.center();

        this.addComponent(powerupsComponent);

        super.init();
    }

    public Screen getPreviousScreen() {
        return previousScreen;
    }

    public PowerupsScreenState getState() {
        return state;
    }

    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(previousScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
