package com.daqem.jobsplus.client.components.jobs;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.client.gui.action.ActionScreen;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.reward.rewards.job.JobExpReward;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.texture.NineSlicedTextureComponent;
import com.daqem.uilib.client.gui.text.multiline.MultiLineText;
import com.daqem.uilib.client.gui.texture.Textures;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class JobExperienceItemComponent extends NineSlicedTextureComponent {

    public JobExperienceItemComponent(int x, int y, int width, Job job, List<IAction> actions, IAction action) {
        super(Textures.SCROLL_BAR_BACKGROUND, x, y, width, 0);

        Component title = action.getName();
        Component description = action.getDescription();

        Font font = Minecraft.getInstance().font;
        MultiLineText titleText = new MultiLineText(font, title, 6, 4, width - 12);
        MultiLineText descriptionText = new MultiLineText(font, description, 6, 5 + (font.lineHeight * titleText.getLines().size()), width - 12);

        titleText.setTextColor(ChatFormatting.AQUA);

        TextComponent titleComponent = new TextComponent(titleText);
        TextComponent descriptionComponent = new TextComponent(descriptionText);

        addChild(titleComponent);
        addChild(descriptionComponent);

        setHeight(4 + (font.lineHeight * (titleText.getLines().size() + descriptionText.getLines().size())) + 4);

        setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            //noinspection DataFlowIssue
            if (getParent().getParent().getParent().isVisible()) {
                Minecraft.getInstance().setScreen(new ActionScreen(actions, action));
                return false;
            }
            return false;
        });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (isTotalHovered(mouseX, mouseY)) {
            RenderSystem.setShaderColor(0.85F, 0.85F, 0.85F, 1.0F);
        }
        super.render(graphics, mouseX, mouseY, delta);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
