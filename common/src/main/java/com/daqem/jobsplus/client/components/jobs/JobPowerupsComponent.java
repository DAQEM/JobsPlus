package com.daqem.jobsplus.client.components.jobs;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.components.powerup.PowerupTreeContainerComponent;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.texture.NineSlicedTextureComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.Map;

public class JobPowerupsComponent extends AbstractComponent<JobPowerupsComponent> {

    private final JobsScreenOptions options;
    private final Map<Job, PowerupTreeContainerComponent> powerupTreeContainerComponents = new HashMap<>();
    private Job cachedJob;

    public JobPowerupsComponent(int x, int y, int width, int height, JobsScreenOptions options) {
        super(null, x, y, width, height);
        this.options = options;
        this.cachedJob = options.getSelectedJob();

        Font font = Minecraft.getInstance().font;
        Text titleText = new Text(font, JobsPlus.translatable("gui.tab.right.power_ups"), 7, 0);

        titleText.setTextColor(ChatFormatting.DARK_GRAY);

        TextComponent titleComponent = new TextComponent(titleText);
        PowerupTreeContainerComponent powerupTreeContainerComponent = new PowerupTreeContainerComponent(8, font.lineHeight + 1, width - 16, height - 2, options);
        this.powerupTreeContainerComponents.put(options.getSelectedJob(), powerupTreeContainerComponent);
        NineSlicedTextureComponent background = new NineSlicedTextureComponent(Textures.SCROLL_BAR_BACKGROUND, 7, font.lineHeight, width - 14, height);

        this.addChild(background);
        this.addChild(titleComponent);
        this.addChild(powerupTreeContainerComponent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        if (isVisible()) {
            if (cachedJob != options.getSelectedJob()) {
                this.removeChild(powerupTreeContainerComponents.get(cachedJob));
                cachedJob = options.getSelectedJob();
                if (powerupTreeContainerComponents.containsKey(cachedJob)) {
                    this.addChild(powerupTreeContainerComponents.get(cachedJob));
                } else {
                    PowerupTreeContainerComponent powerupTreeContainerComponent = new PowerupTreeContainerComponent(8, Minecraft.getInstance().font.lineHeight + 1, getWidth() - 16, getHeight() - 2, options);
                    this.powerupTreeContainerComponents.put(cachedJob, powerupTreeContainerComponent);
                    this.addChild(powerupTreeContainerComponent);
                }
            }
        }
    }
}
