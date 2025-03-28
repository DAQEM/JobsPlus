package com.daqem.jobsplus.client.components;

import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.options.TabOptions;
import com.daqem.jobsplus.client.screen.job.tab.LeftTab;
import com.daqem.jobsplus.client.screen.job.tab.RightTab;
import com.daqem.jobsplus.client.screen.job.tab.SideTab;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.util.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class TabComponent extends AbstractComponent<TabComponent> {

    private final TabOptions tabOptions;
    private final JobsScreenOptions screenOptions;

    public TabComponent(TabOptions tabOptions, int x, int y, JobsScreenOptions screenOptions) {
        super(null, x, y, tabOptions.tabType().getWidth(), tabOptions.tabType().getHeight());
        this.tabOptions = tabOptions;
        this.screenOptions = screenOptions;

        setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            boolean success = false;
            if (this.tabOptions.tab() instanceof LeftTab leftTab) {
                screenOptions.setSelectedLeftTab(leftTab);
                success = true;
            } else if (this.tabOptions.tab() instanceof RightTab rightTab) {
                screenOptions.setSelectedRightTab(rightTab);
                success = true;
            } else if (this.tabOptions.tab() instanceof SideTab sideTab) {
                screenOptions.setSelectedSideTab(sideTab);
                success = true;
            }
            if (success) SoundManager.playUIClick();
            return success;
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        ResourceLocation selectedSprite = isSelected() ?
                tabOptions.tabType().getSelectedSprite(tabOptions.tabPosition()) :
                tabOptions.tabType().getSprite(tabOptions.tabPosition());
        graphics.blitSprite(RenderType::guiTextured, selectedSprite, 0, 0, getWidth(), getHeight());
        graphics.blitSprite(RenderType::guiTextured, tabOptions.tabIconOptions().icon(), tabOptions.tabIconOptions().x(), tabOptions.tabIconOptions().y(), tabOptions.tabIconOptions().width(), tabOptions.tabIconOptions().height());
    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (isTotalHovered(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, tabOptions.tab().getName(), mouseX, mouseY);
        }
        super.renderTooltips(guiGraphics, mouseX, mouseY, delta);
    }

    private boolean isSelected() {
        if (tabOptions.tab() instanceof LeftTab leftTab) {
            return screenOptions.getSelectedLeftTab() == leftTab;
        } else if (tabOptions.tab() instanceof RightTab rightTab) {
            return screenOptions.getSelectedRightTab() == rightTab;
        } else if (tabOptions.tab() instanceof SideTab sideTab) {
            return screenOptions.getSelectedSideTab() == sideTab;
        }
        return false;
    }
}
