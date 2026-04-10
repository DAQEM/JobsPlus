package com.daqem.jobsplus.client.gui.jobs.widgets;

import com.daqem.arc.api.action.IAction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActionsPaginationArrowLeftWidget extends CustomButtonWidget {

    public ActionsPaginationArrowLeftWidget(int x, int y, List<IAction> actions, JobsScreenState state) {
        super(x, y, 10, 10,
                JobsPlus.API.translatable("gui.jobs.experience.previous"),
                new WidgetSprites(
                        JobsPlus.API.getId("jobs/pagination_arrow_left"),
                        JobsPlus.API.getId("jobs/pagination_arrow_left_hovered")
                ), button -> {
                    int currentIndex = actions.indexOf(state.getActiveAction());
                    int newIndex = (currentIndex - 1 + actions.size()) % actions.size();
                    state.setActiveAction(actions.get(newIndex));
                });
    }

    @Override
    protected void extractDefaultLabel(@NotNull ActiveTextCollector activeTextCollector) {
    }
}
