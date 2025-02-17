package com.daqem.jobsplus.client.components.jobs;

import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.client.gui.component.ItemComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.texture.NineSlicedTextureComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobItemRestrictionsItemComponent extends NineSlicedTextureComponent {

    private final ItemRestriction itemRestriction;
    private final int requiredLevel;

    private final List<Component> tooltips;

    public JobItemRestrictionsItemComponent(int x, int y, int width, int height, Job job, ItemRestriction itemRestriction, int requiredLevel) {
        super(Textures.SCROLL_BAR_BACKGROUND, x, y, width, height);
        this.itemRestriction = itemRestriction;
        this.requiredLevel = requiredLevel;
        this.tooltips = generateTooltips();

        Text titleText = new Text(Minecraft.getInstance().font, itemRestriction.getIcon().getHoverName(), 28, 4);
        Text requiredLevelText = new Text(Minecraft.getInstance().font, JobsPlus.translatable("gui.item_restriction.required_level", JobsPlus.literal(String.valueOf(this.requiredLevel)).withStyle(ChatFormatting.WHITE)), 28, 13);

        titleText.setTextColor(ChatFormatting.WHITE);
        requiredLevelText.setTextColor(job.getLevel() >= requiredLevel ? ChatFormatting.GREEN : ChatFormatting.RED);

        ItemComponent itemComponent = new ItemComponent(6, 3, itemRestriction.getIcon(), true);
        TextComponent titleComponent = new TextComponent(titleText);
        TextComponent requiredLevelComponent = new TextComponent(requiredLevelText);

        addChild(itemComponent);
        addChild(titleComponent);
        addChild(requiredLevelComponent);

    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderTooltips(guiGraphics, mouseX, mouseY, delta);
        //noinspection DataFlowIssue
        if (getParent().getParent().getParent().isVisible() && isTotalHovered(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltips, Optional.empty(), mouseX, mouseY);
        }
    }

    private List<Component> generateTooltips() {
        List<Component> tooltips = new ArrayList<>(List.of(JobsPlus.translatable("restrictions")));

        tooltips.addAll(itemRestriction.getRestrictionTypes().stream()
                .map(restrictionType -> JobsPlus.translatable(restrictionType.getTranslationKey()).getString())
                .map(s -> JobsPlus.literal(s).withStyle(ChatFormatting.GRAY))
                .toList());

        return tooltips;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }
}
