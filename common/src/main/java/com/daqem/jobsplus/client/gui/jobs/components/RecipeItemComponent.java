package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.itemrestrictions.ItemRestrictions;
import com.daqem.itemrestrictions.data.RestrictionType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.gui.component.item.ItemComponent;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeItemComponent extends SpriteComponent {

    private static final int[] SPRITE_IDS = new int[] {4, 1, 2, 3, 3, 1, 4, 3, 2, 4, 2, 4, 3, 1, 2, 1};

    private final List<Component> tooltip;

    public RecipeItemComponent(int x, int y, int index, List<RestrictionType> restrictionTypes, int requiredLevel, ItemStack itemStack) {
        super(x, y, 24, 24, JobsPlus.getId("jobs/item_slot_" + SPRITE_IDS[index % SPRITE_IDS.length]));
        this.tooltip = restrictionTypes.stream()
                .map(restrictionType -> (Component) ItemRestrictions.translatable(restrictionType.getTranslationKey()))
                .sorted((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getString(), c2.getString()))
                .collect(Collectors.toList());
        MutableComponent title = JobsPlus.translatable("gui.jobs.restriction_types", requiredLevel);
        List<Component> titleLines = getTitleLines(title).reversed();
        titleLines.forEach(this.tooltip::addFirst);

        this.tooltip.add(Component.empty());
        this.tooltip.add(JobsPlus.translatable("gui.jobs.item_info").withStyle(ChatFormatting.GOLD));

        //add item tooltip info to the end of the tooltip
        Minecraft minecraft = Minecraft.getInstance();
        List<Component> itemTooltips = itemStack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, TooltipFlag.NORMAL);
        this.tooltip.addAll(itemTooltips);

        ItemComponent iconComponent = new ItemComponent(4, 4, itemStack.copyWithCount(requiredLevel), true);

        this.addComponent(iconComponent);
    }

    private List<Component> getTitleLines(MutableComponent title) {
        List<Component> lines = new ArrayList<>();
        String titleString = title.getString();
        int width = Minecraft.getInstance().font.width(titleString);
        int maxWidth = 140;
        if (width <= maxWidth) {
            lines.add(title.withStyle(ChatFormatting.GOLD));
        } else {
            String[] words = titleString.split(" ");
            StringBuilder currentLine = new StringBuilder();
            for (String word : words) {
                int wordWidth = Minecraft.getInstance().font.width(word + " ");
                if (Minecraft.getInstance().font.width(currentLine.toString()) + wordWidth <= maxWidth) {
                    currentLine.append(word).append(" ");
                } else {
                    lines.add(Component.literal(currentLine.toString().trim()).withStyle(ChatFormatting.GOLD));
                    currentLine = new StringBuilder(word).append(" ");
                }
            }
            if (!currentLine.isEmpty()) {
                lines.add(Component.literal(currentLine.toString().trim()).withStyle(ChatFormatting.GOLD));
            }
        }
        return lines;
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.getTotalX(), this.getTotalY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
        if (this.getRectangle().containsPoint(mouseX, mouseY)) {
            guiGraphics.setTooltipForNextFrame(
                    Minecraft.getInstance().font,
                    this.tooltip,
                    Optional.empty(),
                    mouseX,
                    mouseY
            );

        }
    }
}
