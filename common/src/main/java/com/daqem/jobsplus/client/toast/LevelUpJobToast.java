package com.daqem.jobsplus.client.toast;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class LevelUpJobToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");
    private static final long DISPLAY_TIME = 5000L;
    private final List<Entry> jobInstances = new ArrayList<>();
    private long lastChanged;
    private boolean changed;
    private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
    private int displayedJobInstanceIndex;

    @Override
    public @NotNull Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager toastManager, long l) {
        if (this.changed) {
            this.lastChanged = l;
            this.changed = false;
        }

        if (this.jobInstances.isEmpty()) {
            this.wantedVisibility = Toast.Visibility.HIDE;
        } else {
            this.wantedVisibility = l - this.lastChanged >= DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }

        this.displayedJobInstanceIndex = (int)(
                l / Math.max(1.0, DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier() / this.jobInstances.size()) % this.jobInstances.size()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long l) {
        Entry entry = this.jobInstances.get(this.displayedJobInstanceIndex);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        guiGraphics.drawString(font, entry.jobInstance.getName(), 30, 7, -11534256, false);
        guiGraphics.drawString(font, JobsPlus.translatable("job.level_up.toast", entry.level), 30, 18, -16777216, false);
        guiGraphics.renderFakeItem(entry.jobInstance.getIconItem(), 8, 8);
    }

    private void addItem(JobInstance jobInstance, int level) {
        this.jobInstances.add(new Entry(jobInstance, level));
        this.changed = true;
    }

    public static void addOrUpdate(ToastManager toastManager, JobInstance jobInstance, int level) {
        LevelUpJobToast levelUpJobToast = toastManager.getToast(LevelUpJobToast.class, NO_TOKEN);
        if (levelUpJobToast == null) {
            levelUpJobToast = new LevelUpJobToast();
            toastManager.addToast(levelUpJobToast);
        }

        ContextMap contextMap = SlotDisplayContext.fromLevel(Objects.requireNonNull(toastManager.getMinecraft().level));
        levelUpJobToast.addItem(jobInstance, level);
    }

    @Environment(EnvType.CLIENT)
    record Entry(JobInstance jobInstance, int level) {
    }
}
