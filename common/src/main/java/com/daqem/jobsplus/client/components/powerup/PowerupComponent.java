package com.daqem.jobsplus.client.components.powerup;

import com.daqem.jobsplus.client.components.jobs.JobsComponent;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.daqem.uilib.api.client.gui.component.IComponent;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PowerupComponent extends AbstractComponent<PowerupComponent> {

    public static final int SIZE = 26;
    public static final int SPACING = 2;

    private final PowerupComponent previousSibling;
    private final @Nullable PowerupComponent powerupComponentParent;
    private final PowerupTreeComponent treeComponent;
    private final Powerup powerup;
    private final JobsScreenOptions options;

    private final int childIndex;
    private PowerupComponent ancestor;
    private @Nullable PowerupComponent thread;

    private final PowerupIconComponent iconComponent;
    private final PowerupHoverComponent hoverComponent;

    private float mod;
    private float change;
    private float shift;

    private float posX;
    private float posY;

    public PowerupComponent(Powerup powerup, PowerupTreeComponent parent, @Nullable PowerupComponent powerupComponentParent, PowerupComponent previousSibling, int childIndex, int x, JobsScreenOptions options) {
        super(null, x, -1, SIZE, SIZE);
        this.powerupComponentParent = powerupComponentParent;
        this.powerup = powerup;
        this.options = options;
        this.previousSibling = previousSibling;
        this.treeComponent = parent;

        this.childIndex = childIndex;
        this.ancestor = this;

        this.iconComponent = new PowerupIconComponent(0, 0,
                powerup.getPowerupInstance().getIcon(),
                powerup.getState(),
                powerup.getPowerupInstance().getPowerupType(),
                options
        );

        this.hoverComponent = new PowerupHoverComponent(0, 0, Minecraft.getInstance().font,
                powerup.getPowerupInstance().getIcon(),
                powerup.getPowerupInstance().getName(),
                powerup.getPowerupInstance().getDescription(),
                powerup.getState(),
                powerup.getPowerupInstance().getPowerupType(),
                powerup.getPowerupInstance().getRequiredLevel(),
                powerup.getPowerupInstance().getPrice(),
                options
        );

        this.addChildren(getPowerupChildren(powerup));

        addChildren(iconComponent);
    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        //noinspection DataFlowIssue
        if (this.iconComponent.isTotalHovered(mouseX, mouseY)
                && this.treeComponent.getParent().isTotalHovered(mouseX, mouseY)
                && this.treeComponent.getParent().getParent().isVisible()
                && this.treeComponent.getParent().getParent().getParent() instanceof JobsComponent jobsComponent
                && !jobsComponent.getModalComponent().isVisible()
        ) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(getTotalX(), getTotalY(), 0);
            this.hoverComponent.renderBase(guiGraphics, mouseX, mouseY, delta);
            guiGraphics.pose().popPose();
        }
    }

    public PowerupHoverComponent getHoverComponent() {
        return hoverComponent;
    }

    public PowerupIconComponent getIconComponent() {
        return iconComponent;
    }

    public PowerupTreeComponent getTreeComponent() {
        return treeComponent;
    }

    public Powerup getPowerup() {
        return powerup;
    }

    private List<IComponent<?>> getPowerupChildren(Powerup powerup) {
        List<IComponent<?>> children = new ArrayList<>();
        PowerupComponent previousSibling = null;

        ResourceLocation parentLocation = this.powerupComponentParent == null ? null : powerup.getPowerupInstance().getLocation();
        List<PowerupInstance> allPowerupInstances = this.options.getSelectedJob().getJobInstance().getPowerups();
        List<Powerup> allPowerups = this.options.getSelectedJob().getPowerupManager().getAllPowerups();

        Map<ResourceLocation, PowerupInstance> powerupInstances = allPowerupInstances.stream()
                .filter(x -> parentLocation == null ? x.getParentLocation() == null : parentLocation.equals(x.getParentLocation()))
                .collect(HashMap::new, (map, powerup1) -> map.put(powerup1.getLocation(), powerup1), HashMap::putAll);
        Map<ResourceLocation, Powerup> powerups = allPowerups.stream()
                .filter(x -> parentLocation == null ? x.getPowerupInstance().getParentLocation() == null : parentLocation.equals(x.getPowerupInstance().getParentLocation()))
                .collect(HashMap::new, (map, powerup1) -> map.put(powerup1.getPowerupInstance().getLocation(), powerup1), HashMap::putAll);

        for (Map.Entry<ResourceLocation, PowerupInstance> entry : powerupInstances.entrySet()) {
            if (!powerups.containsKey(entry.getKey())) {
                PowerupState state = PowerupState.NOT_OWNED;
                if (this.powerup.getState() == PowerupState.LOCKED || this.powerup.getState() == PowerupState.NOT_OWNED) {
                    state = PowerupState.LOCKED;
                }
                Powerup powerup1 = new Powerup(entry.getValue(), state);
                powerups.put(entry.getKey(), powerup1);
            }
        }

        for (Powerup child : powerups.values()) {
            PowerupComponent powerupComponent = new PowerupComponent(child, this.treeComponent, this, previousSibling, children.size(), (int) (this.posX + 1), this.options);
            previousSibling = powerupComponent;
            children.add(powerupComponent);
        }

        return children;
    }

    private List<PowerupComponent> getPowerupChildren() {
        return this.getChildren().stream()
                .filter(PowerupComponent.class::isInstance)
                .map(PowerupComponent.class::cast)
                .toList();
    }

    public List<PowerupComponent> getAllPowerupChildren() {
        List<PowerupComponent> powerupComponents = new ArrayList<>();
        powerupComponents.add(this);
        for (PowerupComponent powerupComponent : getPowerupChildren()) {
            powerupComponents.addAll(powerupComponent.getAllPowerupChildren());
        }
        return powerupComponents;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        for (PowerupComponent powerupChild : this.getPowerupChildren()) {
            this.drawConnectivity(graphics,
                    1,
                    0,
                    powerupChild.getX() + 1,
                    powerupChild.getY(),
                    false);
        }

        for (PowerupComponent powerupChild : this.getPowerupChildren()) {
            this.drawConnectivity(graphics,
                    1,
                    0,
                    powerupChild.getX() + 1,
                    powerupChild.getY(),
                    true);
        }
    }

    public void firstWalk() {
        List<PowerupComponent> powerupChildren = this.getPowerupChildren();
        if (powerupChildren.isEmpty()) {
            if (this.previousSibling != null) {
                this.posY = this.previousSibling.posY + 1.0F;
            } else {
                this.posY = 0.0F;
            }

        } else {
            PowerupComponent treeNodePosition = null;

            PowerupComponent treeNodePosition2;
            for (Iterator<PowerupComponent> var2 = powerupChildren.iterator(); var2.hasNext(); treeNodePosition = treeNodePosition2.apportion(treeNodePosition == null ? treeNodePosition2 : treeNodePosition)) {
                treeNodePosition2 = var2.next();
                treeNodePosition2.firstWalk();
            }

            this.executeShifts();
            float f = (powerupChildren.getFirst().posY + powerupChildren.getLast().posY) / 2.0F;
            if (this.previousSibling != null) {
                this.posY = this.previousSibling.posY + 1.0F;
                this.mod = this.posY - f;
            } else {
                this.posY = f;
            }

        }
    }

    public float secondWalk(float f, int i, float g) {
        this.posY += f;
        this.posX = i;
        if (this.posY < g) {
            g = this.posY;
        }

        PowerupComponent treeNodePosition;
        for (Iterator<PowerupComponent> var4 = this.getPowerupChildren().iterator(); var4.hasNext(); g = treeNodePosition.secondWalk(f + this.mod, i + 1, g)) {
            treeNodePosition = var4.next();
        }

        return g;
    }

    public void thirdWalk(float f) {
        this.posY += f;

        for (PowerupComponent treeNodePosition : this.getPowerupChildren()) {
            treeNodePosition.thirdWalk(f);
        }
    }

    public void finalizePosition() {
        this.posY = this.posY - (this.powerupComponentParent == null ? 0 : getTotalPosY() - this.posY);
        this.setX(SIZE + SPACING);
        this.setY((int) (this.posY * (float) (SIZE + SPACING)));

        if (this.powerupComponentParent == null) {
            this.setX(this.getX() - SIZE - SPACING);
        }

        if (!this.getPowerupChildren().isEmpty()) {
            for (PowerupComponent treeNodePosition : this.getPowerupChildren()) {
                treeNodePosition.finalizePosition();
            }
        }
    }

    private float getTotalPosY() {
        return this.posY + (this.powerupComponentParent == null ? 0 : powerupComponentParent.getTotalPosY());
    }

    private void executeShifts() {
        float f = 0.0F;
        float g = 0.0F;

        for (int i = this.getPowerupChildren().size() - 1; i >= 0; --i) {
            PowerupComponent treeNodePosition = this.getPowerupChildren().get(i);
            treeNodePosition.posY += f;
            treeNodePosition.mod += f;
            g += treeNodePosition.change;
            f += treeNodePosition.shift + g;
        }
    }

    private PowerupComponent apportion(PowerupComponent treeNodePosition) {
        if (this.previousSibling != null && this.powerupComponentParent != null) {
            PowerupComponent treeNodePosition2 = this;
            PowerupComponent treeNodePosition3 = this;
            PowerupComponent treeNodePosition4 = this.previousSibling;
            PowerupComponent treeNodePosition5 = powerupComponentParent.getPowerupChildren().getFirst();
            float f = this.mod;
            float g = this.mod;
            float h = treeNodePosition4.mod;

            float i;
            for (i = treeNodePosition5.mod; treeNodePosition4.nextOrThread() != null && treeNodePosition2.previousOrThread() != null; g += treeNodePosition3.mod) {
                treeNodePosition4 = treeNodePosition4.nextOrThread();
                treeNodePosition2 = treeNodePosition2.previousOrThread();
                treeNodePosition5 = treeNodePosition5.previousOrThread();
                treeNodePosition3 = treeNodePosition3.nextOrThread();
                Objects.requireNonNull(treeNodePosition3).ancestor = this;
                float j = Objects.requireNonNull(treeNodePosition4).posY + h - (Objects.requireNonNull(treeNodePosition2).posY + f) + 1.0F;
                if (j > 0.0F) {
                    treeNodePosition4.getAncestor(this, treeNodePosition).moveSubtree(this, j);
                    f += j;
                    g += j;
                }

                h += treeNodePosition4.mod;
                f += treeNodePosition2.mod;
                i += Objects.requireNonNull(treeNodePosition5).mod;
            }

            if (treeNodePosition4.nextOrThread() != null && treeNodePosition3.nextOrThread() == null) {
                treeNodePosition3.thread = treeNodePosition4.nextOrThread();
                treeNodePosition3.mod += h - g;
            } else {
                if (treeNodePosition2.previousOrThread() != null && treeNodePosition5.previousOrThread() == null) {
                    treeNodePosition5.thread = treeNodePosition2.previousOrThread();
                    treeNodePosition5.mod += f - i;
                }

                treeNodePosition = this;
            }

        }
        return treeNodePosition;
    }

    @Nullable
    private PowerupComponent previousOrThread() {
        if (this.thread != null) {
            return this.thread;
        } else {
            List<PowerupComponent> powerupChildren = this.getPowerupChildren();
            return !powerupChildren.isEmpty() ? powerupChildren.getFirst() : null;
        }
    }

    @Nullable
    private PowerupComponent nextOrThread() {
        if (this.thread != null) {
            return this.thread;
        } else {
            List<PowerupComponent> powerupChildren = this.getPowerupChildren();
            return !powerupChildren.isEmpty() ? powerupChildren.getLast() : null;
        }
    }

    private PowerupComponent getAncestor(PowerupComponent treeNodePosition, PowerupComponent treeNodePosition2) {
        return this.ancestor != null && Objects.requireNonNull(treeNodePosition.powerupComponentParent).getPowerupChildren().contains(this.ancestor) ? this.ancestor : treeNodePosition2;
    }

    private void moveSubtree(PowerupComponent treeNodePosition, float f) {
        float g = (float) (treeNodePosition.childIndex - this.childIndex);
        if (g != 0.0F) {
            treeNodePosition.change -= f / g;
            this.change += f / g;
        }

        treeNodePosition.shift += f;
        treeNodePosition.posY += f;
        treeNodePosition.mod += f;
    }

    private void drawConnectivity(GuiGraphics guiGraphics, int xFrom, int yFrom, int xTo, int yTo, boolean isWhite) {
        int spacing = 2;
        int centerHeight = 24 / 2;
        int xOffset = 24;
        int xFromOffset = xFrom + xOffset;
        int xToOffset = xTo - 2;
        int yFromCenter = yFrom + centerHeight;
        int yToCenter = yTo + centerHeight;
        int color = isWhite ? 0xFFFFFFFF : 0xFF000000;

        if (isWhite) {
            guiGraphics.hLine(xFromOffset, xFromOffset + spacing, yFromCenter, color);
            guiGraphics.vLine(xFromOffset + spacing, yFromCenter, yToCenter, color);
            guiGraphics.hLine(xToOffset, xToOffset + spacing, yToCenter, color);
        } else {
            guiGraphics.hLine(xFromOffset, xToOffset + 1, yFromCenter - 1, color);
            guiGraphics.hLine(xFromOffset, xToOffset + 1, yFromCenter, color);
            guiGraphics.hLine(xFromOffset, xToOffset + 1, yFromCenter + 1, color);

            guiGraphics.vLine(xToOffset - 1, yToCenter, yFromCenter, color);
            guiGraphics.vLine(xToOffset + 1, yToCenter, yFromCenter, color);

            guiGraphics.hLine(xToOffset - 1, xToOffset + spacing, yToCenter - 1, color);
            guiGraphics.hLine(xToOffset - 1, xToOffset + spacing, yToCenter, color);
            guiGraphics.hLine(xToOffset - 1, xToOffset + spacing, yToCenter + 1, color);
        }
    }

    public void setState(PowerupState powerupState) {
        this.iconComponent.setState(powerupState);
        this.hoverComponent.setState(powerupState);
    }
}
