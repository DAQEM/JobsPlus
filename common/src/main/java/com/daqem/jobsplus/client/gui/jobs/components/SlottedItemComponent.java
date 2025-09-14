package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class SlottedItemComponent extends SpriteComponent {

    private static final int[] SPRITE_IDS = new int[] {4, 1, 2, 3, 3, 1, 4, 3, 2, 4, 2, 4, 3, 1, 2, 1};

    private final ItemStack itemStack;
    private final Supplier<ScreenRectangle> parentBounds;

    public SlottedItemComponent(int x, int y, int index, ItemStack itemStack, Supplier<ScreenRectangle> parentBounds) {
        super(x, y, 24, 24, JobsPlus.getId("jobs/item_slot_" + SPRITE_IDS[index % SPRITE_IDS.length]));
        this.itemStack = itemStack;
        this.parentBounds = parentBounds;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
        guiGraphics.renderFakeItem(this.itemStack, this.getTotalX() + 4, this.getTotalY() + 4);
        if (this.parentBounds.get().containsPoint(mouseX, mouseY) && getRectangle().containsPoint(mouseX, mouseY)) {
            Minecraft minecraft = Minecraft.getInstance();
            guiGraphics.setTooltipForNextFrame(
                    minecraft.font,
                    this.itemStack.getTooltipLines(
                            Item.TooltipContext.of(minecraft.level), minecraft.player, TooltipFlag.NORMAL
                    ),
                    Optional.empty(),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.getTotalX(), this.getTotalY(), this.getWidth(), this.getHeight());
    }
}
