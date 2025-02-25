package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.screen.job.tab.LeftTab;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.api.client.gui.component.IComponent;
import com.daqem.uilib.api.client.gui.component.scroll.ScrollOrientation;
import com.daqem.uilib.client.gui.component.scroll.ScrollBarComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollPanelComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollWheelComponent;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JobsScrollComponent extends ScrollPanelComponent {

    private static final int SCROLL_BAR_WIDTH = 12;
    private static final int SCROLL_BAR_BORDER = 1;
    private static final int SCROLL_BAR_MARGIN = 3;

    private final JobsScreenOptions options;
    private final ScrollContentComponent scrollContent;
    private final List<IComponent<?>> allJobs = new ArrayList<>();
    private final List<IComponent<?>> preformingJobs = new ArrayList<>();
    private final List<IComponent<?>> notPreformingJobs = new ArrayList<>();
    private LeftTab selectedLeftTab;

    public JobsScrollComponent(int x, int y, int width, int height, JobsScreenOptions options) {
        super(Textures.SCROLL_PANE, x, y, width, height, ScrollOrientation.VERTICAL);
        this.options = options;
        this.selectedLeftTab = options.getSelectedLeftTab();
        this.scrollContent = new ScrollContentComponent(0, 0, 0, ScrollOrientation.VERTICAL);

        ScrollWheelComponent scrollWheel = new ScrollWheelComponent(Textures.SCROLL_WHEEL, 0, 0, SCROLL_BAR_WIDTH);
        ScrollBarComponent scrollBar = new ScrollBarComponent(width + SCROLL_BAR_MARGIN + SCROLL_BAR_BORDER, SCROLL_BAR_BORDER, SCROLL_BAR_WIDTH, height - (SCROLL_BAR_BORDER * 2), ScrollOrientation.VERTICAL, scrollWheel);

        for (Job job : options.getJobs()) {
            JobsScrollItemComponent item = new JobsScrollItemComponent(0, 0, width, 35, options, job);
            scrollContent.addChild(item);
            allJobs.add(item);
            if (job.getLevel() > 0) {
                preformingJobs.add(item);
            } else {
                notPreformingJobs.add(item);
            }
        }


        setScrollBar(scrollBar);
        setScrollContentComponent(scrollContent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (options.getSelectedLeftTab() != selectedLeftTab) {
            selectedLeftTab = options.getSelectedLeftTab();
            switch (options.getSelectedLeftTab()) {
                case ALL -> scrollContent.setChildren(allJobs);
                case PREFORMING -> scrollContent.setChildren(preformingJobs);
                case NOT_PREFORMING -> scrollContent.setChildren(notPreformingJobs);
            }
            getScrollBar().ifPresent(s -> s.scroll(this, getTotalX(), getTotalY()));
            getScrollContentComponent().ifPresent(s -> s.scroll(this, getTotalX(), getTotalY()));
        }
        super.render(graphics, mouseX, mouseY, delta);
    }
}
