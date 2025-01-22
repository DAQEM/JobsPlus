package com.daqem.jobsplus.client.options;

import com.daqem.jobsplus.client.components.TabPosition;
import com.daqem.jobsplus.client.components.TabType;
import com.daqem.jobsplus.client.screen.job.tab.ITab;

public record TabOptions(
        ITab tab,
        TabType tabType,
        TabPosition tabPosition,
        TabIconOptions tabIconOptions
) {
}
