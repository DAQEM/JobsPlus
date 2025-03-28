package com.daqem.jobsplus.client.components.powerup;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.daqem.jobsplus.player.job.powerup.PowerupType;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class PowerupTreeComponent extends AbstractComponent<PowerupTreeComponent> {

    public static final int PADDING = 30;

    private final JobsScreenOptions options;
    private PowerupComponent rootPowerupComponent;

    public PowerupTreeComponent(int x, int y, int width, int height, JobsScreenOptions options) {
        super(null, x, y, width, height);
        this.options = options;

        run();

        int maxWidth = getAllPowerupComponents().stream().mapToInt(PowerupComponent::getTotalX).max().orElse(0) + PowerupComponent.SIZE;
        int maxHeight = getAllPowerupComponents().stream().mapToInt(PowerupComponent::getTotalY).max().orElse(0) + PowerupComponent.SIZE;

        setWidth(maxWidth);
        setHeight(maxHeight);

        setOnDragEvent((draggedObject, screen, mouseX, mouseY, button, dragX, dragY) -> {
            if (getParent() != null) {
                int parentWidth = getParent().getWidth();
                int parentHeight = getParent().getHeight();

                int newX = getX() + (int) dragX;
                int newY = getY() + (int) dragY;

                if ((newX - PADDING) <= 0 && (newX + getWidth() + PADDING) >= parentWidth) {
                    setX(newX);
                }

                if ((newY - PADDING) <= 0 && (newY + getHeight() + PADDING) >= parentHeight) {
                    setY(newY);
                }
            }
            return false;
        });
    }

    public List<PowerupComponent> getAllPowerupComponents() {
        return rootPowerupComponent.getAllPowerupChildren();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
    }

    public void run() {
        PowerupInstance rootPowerupInstance = new PowerupInstance(JobsPlus.getId(options.getSelectedJob().getJobInstance().getLocation().getPath() + "/root"), options.getSelectedJob().getJobInstance().getLocation(), null, options.getSelectedJob().getJobInstance().getIconItem(), 0, 0, PowerupType.BASIC);
        Powerup rootPowerup = new Powerup(rootPowerupInstance, options.getSelectedJob().getLevel() > 0 ? PowerupState.ACTIVE : PowerupState.NOT_OWNED);

        rootPowerupComponent = new PowerupComponent(rootPowerup, this, null, null, 0, this.getX(), this.options);
        this.addChild(rootPowerupComponent);
        rootPowerupComponent.firstWalk();
        float f = rootPowerupComponent.secondWalk(0.0F, 0, rootPowerupComponent.getY());
        if (f < 0.0F) {
            rootPowerupComponent.thirdWalk(-f);
        }
        rootPowerupComponent.finalizePosition();
    }

    public PowerupComponent getRootPowerupComponent() {
        return rootPowerupComponent;
    }
}
