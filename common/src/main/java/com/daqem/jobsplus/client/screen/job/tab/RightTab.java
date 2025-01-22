package com.daqem.jobsplus.client.screen.job.tab;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.components.TabPosition;
import com.daqem.jobsplus.client.components.TabType;
import com.daqem.jobsplus.client.options.TabIconOptions;
import com.daqem.jobsplus.client.options.TabOptions;
import net.minecraft.network.chat.Component;

public enum RightTab implements ITab {
    INFO(JobsPlus.translatable("gui.tab.right.info"), TabPosition.LEFT, new TabIconOptions(JobsPlus.getId("icon/info"), 7, 10, 14, 14)),
    CRAFTING(JobsPlus.translatable("gui.tab.right.crafting"), TabPosition.MIDDLE, new TabIconOptions(JobsPlus.getId("icon/crafting_table"), 7, 10, 14, 14)),
    POWER_UPS(JobsPlus.translatable("gui.tab.right.power_ups"), TabPosition.MIDDLE, new TabIconOptions(JobsPlus.getId("icon/power_up"), 9, 10, 10, 14)),
    EXP(JobsPlus.translatable("gui.tab.right.exp"), TabPosition.MIDDLE, new TabIconOptions(JobsPlus.getId("icon/experience_orb"), 8, 11, 12, 12));

    private final Component name;
    private final TabPosition tabPosition;
    private final TabIconOptions tabIconOptions;

    RightTab(Component name, TabPosition tabPosition, TabIconOptions tabIconOptions) {
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
