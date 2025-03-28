package com.daqem.jobsplus.client.components.jobs;

import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.api.client.gui.component.scroll.ScrollOrientation;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollBarComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollPanelComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollWheelComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobItemRestrictionsComponent extends AbstractComponent<JobItemRestrictionsComponent> {

    private static final int SCROLL_BAR_WIDTH = 12;
    private static final int SCROLL_BAR_BORDER = 1;
    private static final int SCROLL_BAR_MARGIN = 3;

    private final JobsScreenOptions options;
    private final ScrollPanelComponent scrollPanelComponent;
    private final ScrollContentComponent scrollContentComponent;
    private final ScrollBarComponent scrollBarComponent;
    private final Map<Job, List<JobItemRestrictionsItemComponent>> itemRestrictionsMap = new HashMap<>();
    private final TextComponent noRestrictionsComponent;
    private final int scrollPanelWidth;
    private Job cachedJob = null;

    public JobItemRestrictionsComponent(int x, int y, int width, int height, JobsScreenOptions options) {
        super(null, x, y, width, height);
        this.options = options;

        Font font = Minecraft.getInstance().font;
        Text noRestrictions = new Text(font, JobsPlus.translatable("gui.item_restriction.no_restrictions"), 0, 0);
        Text title = new Text(font, JobsPlus.translatable("gui.tab.right.crafting"), 7, 0);
        this.scrollPanelWidth = width - (7 * 2) - SCROLL_BAR_WIDTH - SCROLL_BAR_MARGIN - SCROLL_BAR_BORDER;

        title.setTextColor(ChatFormatting.DARK_GRAY);

        this.scrollContentComponent = new ScrollContentComponent(0, 0, 0, ScrollOrientation.VERTICAL);
        this.noRestrictionsComponent = new TextComponent(7 + (this.scrollPanelWidth / 2) - (noRestrictions.getWidth() / 2), (this.scrollPanelWidth / 2) , noRestrictions);
        TextComponent titleComponent = new TextComponent(title);
        ScrollWheelComponent scrollWheelComponent = new ScrollWheelComponent(Textures.SCROLL_WHEEL, 0, 0, SCROLL_BAR_WIDTH);
        this.scrollBarComponent = new ScrollBarComponent(this.scrollPanelWidth + SCROLL_BAR_MARGIN + SCROLL_BAR_BORDER, SCROLL_BAR_BORDER, SCROLL_BAR_WIDTH, height - (SCROLL_BAR_BORDER * 2), ScrollOrientation.VERTICAL, scrollWheelComponent);
        this.scrollPanelComponent = new ScrollPanelComponent(Textures.SCROLL_PANE, 7, font.lineHeight, this.scrollPanelWidth, height, ScrollOrientation.VERTICAL, this.scrollContentComponent, this.scrollBarComponent);

        addChild(titleComponent);
        addChild(this.scrollPanelComponent);
        addChild(this.noRestrictionsComponent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        if (isVisible()) {
            if (!this.options.getSelectedJob().equals(this.cachedJob)) {
                this.noRestrictionsComponent.setVisible(false);
                this.cachedJob = this.options.getSelectedJob();
                this.scrollContentComponent.getChildren().clear();
                if (itemRestrictionsMap.containsKey(this.cachedJob)) {
                    List<JobItemRestrictionsItemComponent> jobItemRestrictionsItemComponents = this.itemRestrictionsMap.get(this.cachedJob);
                    if (jobItemRestrictionsItemComponents.isEmpty()) {
                        this.noRestrictionsComponent.setVisible(true);
                    } else {
                        this.scrollContentComponent.addChildren(new ArrayList<>(jobItemRestrictionsItemComponents));
                    }
                } else {
                    Map<ItemRestriction, Integer> itemRestrictions = this.cachedJob.getJobInstance().getItemRestrictions();
                    List<JobItemRestrictionsItemComponent> components = new ArrayList<>();
                    for (Map.Entry<ItemRestriction, Integer> entry : itemRestrictions.entrySet()) {
                        ItemRestriction itemRestriction = entry.getKey();
                        int requiredLevel = entry.getValue();
                        components.add(new JobItemRestrictionsItemComponent(0, 0, this.scrollPanelWidth, 24, this.cachedJob, itemRestriction, requiredLevel));
                    }
                    if (components.isEmpty()) {
                        this.noRestrictionsComponent.setVisible(true);
                    } else {
                        components.sort((o1, o2) -> {
                            int level1 = o1.getRequiredLevel();
                            int level2 = o2.getRequiredLevel();
                            return Integer.compare(level1, level2);
                        });
                        this.itemRestrictionsMap.put(this.cachedJob, components);
                        this.scrollContentComponent.addChildren(new ArrayList<>(components));
                    }
                }
            }
        }
    }

    public void resetScroll() {
        scrollContentComponent.scroll(scrollPanelComponent, scrollPanelComponent.getTotalX(), scrollPanelComponent.getTotalY());
        scrollBarComponent.scroll(scrollPanelComponent, scrollPanelComponent.getTotalX(), scrollPanelComponent.getTotalY());
    }
}
