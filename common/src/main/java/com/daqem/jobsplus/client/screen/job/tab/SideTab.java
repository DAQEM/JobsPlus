package com.daqem.jobsplus.client.screen.job.tab;

import com.daqem.jobsplus.client.components.TabPosition;
import com.daqem.jobsplus.client.components.TabType;
import com.daqem.jobsplus.client.options.TabIconOptions;
import com.daqem.jobsplus.client.options.TabOptions;
import net.minecraft.network.chat.Component;

public enum SideTab implements ITab {
    ;

    private final Component name;
    private final TabPosition tabPosition;
    private final TabIconOptions tabIconOptions;

    SideTab(Component name, TabPosition tabPosition, TabIconOptions tabIconOptions) {
        this.name = name;
        this.tabPosition = tabPosition;
        this.tabIconOptions = tabIconOptions;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public TabOptions getOptions() {
        return new TabOptions(
                this,
                TabType.RIGHT,
                tabPosition,
                tabIconOptions
        );
    }
}
