package com.daqem.jobsplus.client.gui.jobs.tab;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface ITab {
    Component getName();
    ResourceLocation getActiveSprite();
    ResourceLocation getInactiveSprite();
}
