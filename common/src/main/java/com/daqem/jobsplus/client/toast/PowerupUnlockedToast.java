package com.daqem.jobsplus.client.toast;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PowerupUnlockedToast implements Toast {
    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/recipe");
    private static final long DISPLAY_TIME = 5000L;
    private final List<PowerupInstance> powerups = new ArrayList<>();
    private long lastChanged;
    private boolean changed;
    private Visibility wantedVisibility = Visibility.HIDE;
    private int displayedPowerupIndex;

    @Override
    public @NotNull Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(@NotNull ToastManager toastManager, long l) {
        if (this.changed) {
            this.lastChanged = l;
            this.changed = false;
        }

        if (this.powerups.isEmpty()) {
            this.wantedVisibility = Visibility.HIDE;
        } else {
            this.wantedVisibility = (double)(l - this.lastChanged) >= DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
        }

        this.displayedPowerupIndex = (int)(
                l / Math.max(1.0, DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier() / this.powerups.size()) % this.powerups.size()
        );
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, @NotNull Font font, long l) {
        PowerupInstance entry = this.powerups.get(this.displayedPowerupIndex);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        guiGraphics.text(font, entry.getName(), 30, 7, -11534256, false);
        guiGraphics.text(font, JobsPlus.API.translatable("job.powerup_unlocked.toast"), 30, 18, -16777216, false);
        guiGraphics.fakeItem(entry.getIcon(), 8, 8);
    }

    private void addItem(PowerupInstance powerup) {
        this.powerups.add(powerup);
        this.changed = true;
    }

    public static void addOrUpdate(ToastManager toastManager, PowerupInstance powerup) {
        PowerupUnlockedToast powerupUnlockedToast = toastManager.getToast(PowerupUnlockedToast.class, NO_TOKEN);
        if (powerupUnlockedToast == null) {
            powerupUnlockedToast = new PowerupUnlockedToast();
            toastManager.addToast(powerupUnlockedToast);
        }
        powerupUnlockedToast.addItem(powerup);
    }
}
