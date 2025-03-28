package com.daqem.jobsplus.client.components;

import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.options.TabOptions;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class TabGroupComponent extends AbstractComponent<TabGroupComponent> {

    private static final int TAB_GAP = 4;

    public TabGroupComponent(List<TabOptions> tabOptionsList, int x, int y, JobsScreenOptions options) {
        super(null, x, y, 0, 0);

        for (int i = 0; i < tabOptionsList.size(); i++) {
            TabOptions tabOptions = tabOptionsList.get(i);
            TabType tabType = tabOptions.tabType();
            this.addChild(
                    new TabComponent(
                            tabOptions,
                            tabType.isHorizontal() ? (tabType.getWidth() + TAB_GAP) * i : 0,
                            tabType.isVertical() ? (tabType.getHeight() + TAB_GAP) * i : 0,
                            options
                    )
            );

            if (tabType.isHorizontal()) {
                this.setWidth(this.getWidth() + tabType.getWidth() + TAB_GAP);
            } else if (tabType.isVertical()) {
                this.setHeight(this.getHeight() + tabType.getHeight() + TAB_GAP);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
    }
}
