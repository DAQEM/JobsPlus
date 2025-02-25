package com.daqem.jobsplus.client.toast;

import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ItemRestrictionUnlockedToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");
    private static final long DISPLAY_TIME = 5000L;
    private final ItemRestriction itemRestriction;
    private long lastChanged;
    private boolean changed;

    public ItemRestrictionUnlockedToast(ItemRestriction itemRestriction) {
        this.itemRestriction = itemRestriction;
    }

    @Override
    public Toast.@NotNull Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long l) {
        if (this.changed) {
            this.lastChanged = l;
            this.changed = false;
        }

        if (this.itemRestriction == null) {
            return Toast.Visibility.HIDE;
        } else {
            guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
            guiGraphics.drawString(toastComponent.getMinecraft().font, this.itemRestriction.getIcon().getHoverName(), 30, 7, -11534256, false);
            guiGraphics.drawString(toastComponent.getMinecraft().font, JobsPlus.translatable("job.item_unlocked.toast"), 30, 18, -16777216, false);

            guiGraphics.renderFakeItem(this.itemRestriction.getIcon(), 8, 8);
            return (double) (l - this.lastChanged) >= DISPLAY_TIME * toastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }
    }

    public static void add(ToastComponent toastComponent, ItemRestriction itemRestriction) {
        toastComponent.addToast(new ItemRestrictionUnlockedToast(itemRestriction));
    }
}
