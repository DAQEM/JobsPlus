package com.daqem.jobsplus.client.gui.jobs.tab;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface ITab {
    Component getName();
    Identifier getActiveSprite();
    Identifier getInactiveSprite();
}
