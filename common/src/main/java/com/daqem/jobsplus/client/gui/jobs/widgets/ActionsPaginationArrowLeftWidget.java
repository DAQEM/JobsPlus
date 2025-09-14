package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;

import java.util.List;

public class ActionsPaginationArrowLeftWidget extends CustomButtonWidget {

    public ActionsPaginationArrowLeftWidget(int x, int y, List<IAction> actions, JobsScreenState state) {
        super(x, y, 10, 10,
                JobsPlus.translatable("gui.jobs.experience.previous"),
                new WidgetSprites(
                        JobsPlus.getId("jobs/pagination_arrow_left"),
                        JobsPlus.getId("jobs/pagination_arrow_left_hovered")
                ), button -> {
                    int currentIndex = actions.indexOf(state.getActiveAction());
                    int newIndex = (currentIndex - 1 + actions.size()) % actions.size();
                    state.setActiveAction(actions.get(newIndex));
                });
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int i) {
    }
}
