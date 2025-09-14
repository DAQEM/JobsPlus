package com.daqem.jobsplus.client.gui.confimation;

import net.minecraft.network.chat.Component;

public class ConfirmationScreenState {

    private final Component message;
    private final OnConfirm onConfirm;

    public ConfirmationScreenState(Component message, OnConfirm onConfirm) {
        this.message = message;
        this.onConfirm = onConfirm;
    }

    public Component getMessage() {
        return message;
    }

    public OnConfirm getOnConfirm() {
        return onConfirm;
    }
}
