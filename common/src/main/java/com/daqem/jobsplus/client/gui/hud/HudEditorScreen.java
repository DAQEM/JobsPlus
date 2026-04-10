package com.daqem.jobsplus.client.gui.hud;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.statusbar.JobStatusBarAlignmentHorizontal;
import com.daqem.jobsplus.client.gui.statusbar.JobStatusBarAlignmentVertical;
import com.daqem.jobsplus.client.gui.statusbar.JobsStatusBarsComponent;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HudEditorScreen extends Screen {

    private static final int SNAP_THRESHOLD = 6;

    private JobsStatusBarsComponent statusBarsComponent;
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    // State for visual feedback
    private boolean snappedX = false;
    private boolean snappedY = false;

    private int componentX;
    private int componentY;

    public HudEditorScreen() {
        super(JobsPlus.API.translatable("gui.hud_editor.title"));
    }

    @Override
    protected void init() {
        super.init();
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player instanceof JobsPlayer jobsPlayer) {
            this.statusBarsComponent = new JobsStatusBarsComponent(jobsPlayer);
            // We must calculate the size manually here because the component
            // usually calculates it during render, causing the initial position to be wrong (0,0).
            calculateComponentSize(jobsPlayer);
            this.updateComponentPositionFromConfig();
        }
    }

    /**
     * Simulates the height calculation logic from JobsStatusBarsComponent
     * to ensure the box is positioned correctly immediately upon opening.
     */
    private void calculateComponentSize(JobsPlayer jobsPlayer) {
        List<String> configJobs = JobsPlusClientConfig.jobStatusBarJobs.get();
        int activeCount = 0;

        for (String jobLoc : configJobs) {
            Identifier loc = Identifier.tryParse(jobLoc);
            if (loc != null) {
                Job job = jobsPlayer.jobsplus$getJob(loc);
                // Logic matches JobsStatusBarsComponent: only show if job exists
                if (job != null) {
                    activeCount++;
                }
            }
        }

        int width = JobsPlusClientConfig.jobStatusBarWidth.get() + 6;
        int height = 0;

        if (activeCount > 0) {
            boolean isDetailed = JobsPlusClientConfig.jobStatusBarDetailed.get();
            int lineHeight = Minecraft.getInstance().font.lineHeight;
            int singleBarHeight = isDetailed ? 3 + lineHeight : 3;

            // Height = (bars * height) + (spacing * (bars-1)) + padding(3 top + 3 bottom)
            // Spacing is 2 pixels
            int contentHeight = (activeCount * singleBarHeight) + ((activeCount - 1) * 2);
            height = contentHeight + 6;
        }

        this.statusBarsComponent.setWidth(width);
        this.statusBarsComponent.setHeight(height);
    }

    private void updateComponentPositionFromConfig() {
        if (this.statusBarsComponent == null) return;

        int windowWidth = this.width;
        int windowHeight = this.height;
        int width = this.statusBarsComponent.getWidth();
        int height = this.statusBarsComponent.getHeight();

        JobStatusBarAlignmentHorizontal hAlign = JobsPlusClientConfig.jobStatusBarHorizontalAlignment.get();
        JobStatusBarAlignmentVertical vAlign = JobsPlusClientConfig.jobStatusBarVerticalAlignment.get();
        int offsetX = JobsPlusClientConfig.jobStatusBarXOffset.get();
        int offsetY = JobsPlusClientConfig.jobStatusBarYOffset.get();

        this.componentX = switch (hAlign) {
            case LEFT -> offsetX;
            case CENTER -> (windowWidth - width) / 2 + offsetX;
            case RIGHT -> windowWidth - width - offsetX;
        };

        this.componentY = switch (vAlign) {
            case TOP -> offsetY;
            case CENTER -> (windowHeight - height) / 2 + offsetY;
            case BOTTOM -> windowHeight - height - 1 - offsetY;
        };
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Draw alignment grid
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Vertical Center Line (Green if snapped)
        guiGraphics.verticalLine(centerX, 0, this.height, snappedX ? 0xFF00FF00 : 0x40FFFFFF);
        // Horizontal Center Line (Green if snapped)
        guiGraphics.horizontalLine(0, this.width, centerY, snappedY ? 0xFF00FF00 : 0x40FFFFFF);

        // Instructions
        MutableComponent instructions = JobsPlus.API.translatable("gui.hud_editor.instructions");
        guiGraphics.text(this.font, instructions, centerX - font.width(instructions) / 2, 20, 0xFFFFFFFF, false);

        // Show "Centered" text if snapped
        String statusText = "X: " + this.componentX + ", Y: " + this.componentY;
        if (snappedX && snappedY) statusText = "CENTERED";
        else if (snappedX) statusText += " (Centered X)";
        else if (snappedY) statusText += " (Centered Y)";

        MutableComponent status = Component.literal(statusText);
        guiGraphics.text(this.font, status, centerX - font.width(status) / 2, 35, 0xFFAAAAAA, false);

        if (this.statusBarsComponent != null) {
            // Force position for rendering
            this.statusBarsComponent.setX(this.componentX);
            this.statusBarsComponent.setY(this.componentY);

            // Render a bounding box for better visibility
            // Green outline if dragging, White if idle
            int color = this.isDragging ? 0xFF00FF00 : 0xFFFFFFFF;
            guiGraphics.outline(this.componentX - 1, this.componentY - 1,
                    this.statusBarsComponent.getWidth() + 2, this.statusBarsComponent.getHeight() + 2,
                    color);

            this.statusBarsComponent.extractRenderStateBase(guiGraphics, mouseX, mouseY, partialTick, this.width, this.height);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
        if (event.button() == 0 && this.statusBarsComponent != null) {
            if (event.x() >= this.componentX && event.x() <= this.componentX + this.statusBarsComponent.getWidth() &&
                    event.y() >= this.componentY && event.y() <= this.componentY + this.statusBarsComponent.getHeight()) {
                this.isDragging = true;
                this.dragOffsetX = (int) event.x() - this.componentX;
                this.dragOffsetY = (int) event.y() - this.componentY;
                return true;
            }
        }
        return super.mouseClicked(event, bl);
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double d, double e) {
        if (this.isDragging && this.statusBarsComponent != null) {
            int newX = (int) event.x() - this.dragOffsetX;
            int newY = (int) event.y() - this.dragOffsetY;

            // Snapping Logic
            int windowCenterX = this.width / 2;
            int windowCenterY = this.height / 2;
            int componentCenterX = newX + this.statusBarsComponent.getWidth() / 2;
            int componentCenterY = newY + this.statusBarsComponent.getHeight() / 2;

            // Check X Snap
            if (Math.abs(componentCenterX - windowCenterX) < SNAP_THRESHOLD) {
                newX = windowCenterX - this.statusBarsComponent.getWidth() / 2;
                this.snappedX = true;
            } else {
                this.snappedX = false;
            }

            // Check Y Snap
            if (Math.abs(componentCenterY - windowCenterY) < SNAP_THRESHOLD) {
                newY = windowCenterY - this.statusBarsComponent.getHeight() / 2;
                this.snappedY = true;
            } else {
                this.snappedY = false;
            }

            this.componentX = newX;
            this.componentY = newY;
            return true;
        }
        return super.mouseDragged(event, d, e);
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent mouseButtonEvent) {
        if (this.isDragging) {
            this.isDragging = false;
            this.snappedX = false;
            this.snappedY = false;
            savePosition();
            return true;
        }
        return super.mouseReleased(mouseButtonEvent);
    }

    private void savePosition() {
        if (this.statusBarsComponent == null) return;

        int windowWidth = this.width;
        int windowHeight = this.height;
        int width = this.statusBarsComponent.getWidth();
        int height = this.statusBarsComponent.getHeight();

        // Determine Alignment based on position
        JobStatusBarAlignmentHorizontal hAlign;
        int offsetX;

        // Horizontal Logic
        int centerX = this.componentX + width / 2;
        // If center is exactly middle (snapped), use CENTER
        if (Math.abs(centerX - windowWidth / 2) <= 1) {
            hAlign = JobStatusBarAlignmentHorizontal.CENTER;
            offsetX = this.componentX - (windowWidth - width) / 2; // Should be approx 0
        } else if (this.componentX < windowWidth / 3) {
            hAlign = JobStatusBarAlignmentHorizontal.LEFT;
            offsetX = this.componentX;
        } else if (this.componentX > (windowWidth / 3) * 2) {
            hAlign = JobStatusBarAlignmentHorizontal.RIGHT;
            offsetX = windowWidth - width - this.componentX;
        } else {
            hAlign = JobStatusBarAlignmentHorizontal.CENTER;
            offsetX = this.componentX - (windowWidth - width) / 2;
        }

        JobStatusBarAlignmentVertical vAlign;
        int offsetY;

        // Vertical Logic
        int centerY = this.componentY + height / 2;
        // If center is exactly middle (snapped), use CENTER
        if (Math.abs(centerY - windowHeight / 2) <= 1) {
            vAlign = JobStatusBarAlignmentVertical.CENTER;
            offsetY = this.componentY - (windowHeight - height) / 2; // Should be approx 0
        } else if (this.componentY < windowHeight / 3) {
            vAlign = JobStatusBarAlignmentVertical.TOP;
            offsetY = this.componentY;
        } else if (this.componentY > (windowHeight / 3) * 2) {
            vAlign = JobStatusBarAlignmentVertical.BOTTOM;
            offsetY = windowHeight - height - 1 - this.componentY;
        } else {
            vAlign = JobStatusBarAlignmentVertical.CENTER;
            offsetY = this.componentY - (windowHeight - height) / 2;
        }

        JobsPlusClientConfig.jobStatusBarHorizontalAlignment.set(hAlign);
        JobsPlusClientConfig.jobStatusBarVerticalAlignment.set(vAlign);
        JobsPlusClientConfig.jobStatusBarXOffset.set(offsetX);
        JobsPlusClientConfig.jobStatusBarYOffset.set(offsetY);
        JobsPlusClientConfig.config.save();
    }

    @Override
    public void onClose() {
        savePosition();
        super.onClose();
    }
}