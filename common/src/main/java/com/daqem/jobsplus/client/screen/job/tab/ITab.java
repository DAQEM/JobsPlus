package com.daqem.jobsplus.client.screen.job.tab;

import com.daqem.jobsplus.client.options.TabOptions;
import net.minecraft.network.chat.Component;

public interface ITab {
    Component getName();
    TabOptions getOptions();
}
