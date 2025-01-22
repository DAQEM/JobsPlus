package com.daqem.jobsplus.client.screen.job.tab;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.components.TabPosition;
import com.daqem.jobsplus.client.components.TabType;
import com.daqem.jobsplus.client.options.TabIconOptions;
import com.daqem.jobsplus.client.options.TabOptions;
import net.minecraft.network.chat.Component;

public enum LeftTab implements ITab {
    ALL(JobsPlus.translatable("gui.tab.left.all"), TabPosition.LEFT, new TabIconOptions(JobsPlus.getId("icon/book"), 4, 10, 20, 13)),
    PREFORMING(JobsPlus.translatable("gui.tab.left.performing"), TabPosition.MIDDLE, new TabIconOptions(JobsPlus.getId("icon/checkmark"), 6, 10, 16, 13)),
    NOT_PREFORMING(JobsPlus.translatable("gui.tab.left.not_performing"), TabPosition.MIDDLE, new TabIconOptions(JobsPlus.getId("icon/cross"), 7, 10, 14, 14));

    private final Component name;
    private final TabPosition tabPosition;
    private final TabIconOptions tabIconOptions;

    LeftTab(Component name, TabPosition tabPosition, TabIconOptions tabIconOptions) {
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
                TabType.ABOVE,
                tabPosition,
                tabIconOptions
        );
    }
}
