package com.daqem.jobsplus.client.toast;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class PowerupUnlockedToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");
    private static final long DISPLAY_TIME = 5000L;
    private final PowerupInstance powerup;
    private long lastChanged;
    private boolean changed;

    public PowerupUnlockedToast(PowerupInstance powerup) {
        this.powerup = powerup;
    }

    @Override
    public Toast.@NotNull Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long l) {
        if (this.changed) {
            this.lastChanged = l;
            this.changed = false;
        }

        if (this.powerup == null) {
            return Toast.Visibility.HIDE;
        } else {
            guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
            guiGraphics.drawString(toastComponent.getMinecraft().font, this.powerup.getName(), 30, 7, -11534256, false);
            guiGraphics.drawString(toastComponent.getMinecraft().font, JobsPlus.translatable("job.powerup_unlocked.toast"), 30, 18, -16777216, false);

            guiGraphics.renderFakeItem(this.powerup.getIcon(), 8, 8);
            return (double) (l - this.lastChanged) >= DISPLAY_TIME * toastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }
    }

    public static void add(ToastComponent toastComponent, PowerupInstance powerup) {
        toastComponent.addToast(new PowerupUnlockedToast(powerup));
    }
}