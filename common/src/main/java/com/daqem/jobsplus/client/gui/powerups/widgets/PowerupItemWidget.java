package com.daqem.jobsplus.client.gui.powerups.widgets;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.confimation.ConfirmationScreen;
import com.daqem.jobsplus.client.gui.confimation.ConfirmationScreenState;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreen;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.c2s.ServerboundOpenPowerupsScreenPacket;
import com.daqem.jobsplus.networking.c2s.ServerboundStartPowerupPacket;
import com.daqem.jobsplus.networking.c2s.ServerboundTogglePowerUpPacket;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.daqem.uilib.api.skilltree.ISkillTreeItem;
import com.daqem.uilib.api.widget.skilltree.ISkillTreeItemWidget;
import com.daqem.uilib.gui.component.text.multiline.MultiLineTextComponent;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PowerupItemWidget extends CustomButtonWidget implements ISkillTreeItemWidget {

    private final ISkillTreeItem skillTreeItem;
    private final PowerupsScreenState state;
    private final Powerup powerup;

    public PowerupItemWidget(ISkillTreeItem skillTreeItem, PowerupsScreenState state, Powerup powerup) {
        super(0, 0, 26, 26, powerup != null ? powerup.getPowerupInstance().getName() : state.getJob().getJobInstance().getName(), null, btn -> {
            if (btn instanceof PowerupItemWidget button && button.isActive()) {
                Powerup powerUp = button.getPowerup();
                PowerupInstance powerupInstance = powerUp.getPowerupInstance();
                ResourceLocation location = button.getState().getJob().getJobInstance().getLocation();
                if (powerUp.getState() == PowerupState.ACTIVE || powerUp.getState() == PowerupState.INACTIVE) {
                    NetworkManager.sendToServer(new ServerboundTogglePowerUpPacket(location, powerupInstance.getLocation()));
                    if (Minecraft.getInstance().screen instanceof PowerupsScreen powerupsScreen) {
                        button.getPowerup().setState(powerUp.getState() == PowerupState.ACTIVE ? PowerupState.INACTIVE : PowerupState.ACTIVE);
                    }
                } else if (powerUp.getState() == PowerupState.NOT_OWNED) {
                    Minecraft.getInstance().setScreen(new ConfirmationScreen(Minecraft.getInstance().screen, new ConfirmationScreenState(
                            JobsPlus.translatable("gui.confirmation.purchase_powerup", powerupInstance.getName(), powerupInstance.getPrice()),
                            () -> {
                                NetworkManager.sendToServer(new ServerboundStartPowerupPacket(location, powerupInstance.getLocation()));
                                NetworkManager.sendToServer(new ServerboundOpenPowerupsScreenPacket(location));
                            }
                    )));
                }
            }
        });
        this.skillTreeItem = skillTreeItem;
        this.state = state;
        this.powerup = powerup;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.blitSlot(guiGraphics);
    }

    public PowerupsScreenState getState() {
        return state;
    }

    public Powerup getPowerup() {
        return powerup;
    }

    private ResourceLocation getSprite() {
        ResourceLocation defaultSprite = JobsPlus.getId("powerups/slot_active");
        ResourceLocation lockedSprite = JobsPlus.getId("powerups/slot_locked");
        ResourceLocation notOwnedSprite = JobsPlus.getId("powerups/slot_not_owned");
        if (this.powerup == null) {
            Job job = state.getJob();
            if (job.getLevel() > 0) {
                return defaultSprite;
            }
            if (state.getCoins() >= job.getJobInstance().getPrice()) {
                return notOwnedSprite;
            }
            return lockedSprite;
        }
        if (!hasPowerup() && (!hasEnoughCoins() || !hasRequiredLevel())) {
            return lockedSprite;
        }
        return switch (this.powerup.getState()) {
            case ACTIVE -> defaultSprite;
            case INACTIVE -> JobsPlus.getId("powerups/slot_inactive");
            case NOT_OWNED -> notOwnedSprite;
            case LOCKED -> lockedSprite;
        };
    }

    private void blitSlot(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                this.getSprite(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight()
        );

        ItemStack icon = this.powerup != null ? this.powerup.getPowerupInstance().getIcon() : state.getJob().getJobInstance().getIconItem();
        guiGraphics.renderFakeItem(
                icon,
                this.getX() + 5,
                this.getY() + 5
        );
        guiGraphics.renderItemDecorations(
                Minecraft.getInstance().font,
                icon,
                this.getX() + 5,
                this.getY() + 5
        );
    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isMouseOver(mouseX, mouseY)) {
            Component title = this.powerup != null ? this.powerup.getPowerupInstance().getName() : state.getJob().getJobInstance().getName();
            Component description = this.powerup != null ? this.powerup.getPowerupInstance().getDescription() : state.getJob().getJobInstance().getDescription();
            int titleWidth = Math.max(50, Minecraft.getInstance().font.width(title));
            MultiLineTextComponent descriptionComponent = new MultiLineTextComponent(0, 0, titleWidth + getWidth() + 10, description, 0xFF1E1410);
            if (getX() + getWidth() + titleWidth + 18 > guiGraphics.guiWidth()) {
                guiGraphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        JobsPlus.getId("powerups/text_background"),
                        this.getX() - titleWidth - 18,
                        this.getY() + 7,
                        this.getWidth() + titleWidth + 24,
                        20 + descriptionComponent.getHeight() + 6 + (this.powerup != null && this.powerup.getPowerupInstance().getRequiredLevel() > 0 ? 25 : 13)
                );
                guiGraphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        JobsPlus.getId("powerups/bar"),
                        this.getX() - titleWidth - 18,
                        this.getY() + 3,
                        this.getWidth() + titleWidth + 24,
                        20
                );
                guiGraphics.drawString(
                        Minecraft.getInstance().font,
                        title,
                        this.getX() - titleWidth - 6,
                        this.getY() + 9,
                        0xFF1E1410,
                        false
                );
                if (this.powerup != null) {
                    guiGraphics.blitSprite(
                            RenderPipelines.GUI_TEXTURED,
                            JobsPlus.getId("powerups/line"),
                            this.getX() - titleWidth - 12,
                            this.getY() + 29 + descriptionComponent.getHeight() + 1,
                            30,
                            1
                    );
                    int requiredLevel = this.powerup.getPowerupInstance().getRequiredLevel();
                    if (requiredLevel > 0) {
                        guiGraphics.drawString(
                                Minecraft.getInstance().font,
                                JobsPlus.translatable("gui.powerups.required_level", requiredLevel),
                                this.getX() - titleWidth - 11,
                                this.getY() + 29 + descriptionComponent.getHeight() + 4,
                                0xFF1E1410,
                                false
                        );
                    }
                    MutableComponent price = JobsPlus.translatable("gui.powerups.price", this.powerup.getPowerupInstance().getPrice());
                    guiGraphics.drawString(
                            Minecraft.getInstance().font,
                            price,
                            this.getX() - titleWidth - 11,
                            this.getY() + 29 + descriptionComponent.getHeight() + (requiredLevel > 0 ? 15 : 4),
                            0xFF1E1410,
                            false
                    );
                    guiGraphics.blitSprite(
                            RenderPipelines.GUI_TEXTURED,
                            JobsPlus.getId("jobs/coins"),
                            this.getX() - titleWidth - 11 + Minecraft.getInstance().font.width(price) + 2,
                            this.getY() + 29 + descriptionComponent.getHeight() + (requiredLevel > 0 ? 15 : 4),
                            7,
                            8
                    );
                }
                descriptionComponent.setX(this.getX() - titleWidth - 11);
                descriptionComponent.setY(this.getY() + 29);
                descriptionComponent.renderBase(guiGraphics, mouseX, mouseY,0, 0, 0);
            } else {
                guiGraphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        JobsPlus.getId("powerups/text_background"),
                        this.getX() - 6,
                        this.getY() + 7,
                        this.getWidth() + titleWidth + 24,
                        20 + descriptionComponent.getHeight() + 6 + (this.powerup != null && this.powerup.getPowerupInstance().getRequiredLevel() > 0 ? 25 : 13)
                );
                guiGraphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        JobsPlus.getId("powerups/bar"),
                        this.getX() - 6,
                        this.getY() + 3,
                        this.getWidth() +titleWidth + 24,
                        20
                );
                guiGraphics.drawString(
                        Minecraft.getInstance().font,
                        title,
                        this.getX() + this.getWidth() + 8,
                        this.getY() + 9,
                        0xFF1E1410,
                        false
                );
                if (this.powerup != null) {
                    guiGraphics.blitSprite(
                            RenderPipelines.GUI_TEXTURED,
                            JobsPlus.getId("powerups/line"),
                            this.getX(),
                            this.getY() + 29 + descriptionComponent.getHeight() + 1,
                            30,
                            1
                    );
                    int requiredLevel = this.powerup.getPowerupInstance().getRequiredLevel();
                    if (requiredLevel > 0) {
                        guiGraphics.drawString(
                                Minecraft.getInstance().font,
                                JobsPlus.translatable("gui.powerups.required_level", requiredLevel),
                                this.getX() + 1,
                                this.getY() + 29 + descriptionComponent.getHeight() + 4,
                                0xFF1E1410,
                                false
                        );
                    }
                    MutableComponent price = JobsPlus.translatable("gui.powerups.price", this.powerup.getPowerupInstance().getPrice());
                    guiGraphics.drawString(
                            Minecraft.getInstance().font,
                            price,
                            this.getX() + 1,
                            this.getY() + 29 + descriptionComponent.getHeight() + (requiredLevel > 0 ? 15 : 4),
                            0xFF1E1410,
                            false
                    );
                    guiGraphics.blitSprite(
                            RenderPipelines.GUI_TEXTURED,
                            JobsPlus.getId("jobs/coins"),
                            this.getX() + 1 + Minecraft.getInstance().font.width(price) + 2,
                            this.getY() + 29 + descriptionComponent.getHeight() + (requiredLevel > 0 ? 15 : 4),
                            7,
                            8
                    );
                }
                descriptionComponent.setX(this.getX() + 1);
                descriptionComponent.setY(this.getY() + 29);
                descriptionComponent.renderBase(guiGraphics, mouseX, mouseY,0, 0, 0);
            }
            this.blitSlot(guiGraphics);
        }
    }

    @Override
    public ISkillTreeItem getSkillTreeItem() {
        return this.skillTreeItem;
    }

    @Override
    protected boolean isValidClickButton(MouseButtonInfo mouseButtonInfo) {
        if (hasPowerup()) return true;

        boolean isCorrectPowerupState = this.powerup != null && this.powerup.getState() != PowerupState.LOCKED;
        return isCorrectPowerupState && hasEnoughCoins() && hasRequiredLevel();
    }

    @Override
    public boolean isActive() {
        return isValidClickButton(new MouseButtonInfo(0, 0));
    }

    private boolean hasEnoughCoins() {
        return this.powerup != null && this.state.getCoins() >= this.powerup.getPowerupInstance().getPrice();
    }

    private boolean hasRequiredLevel() {
        return this.powerup != null && this.state.getJob().getLevel() >= 1 && this.state.getJob().getLevel() >= this.powerup.getPowerupInstance().getRequiredLevel();
    }

    private boolean hasPowerup() {
        return this.powerup != null && (this.powerup.getState() == PowerupState.ACTIVE || this.powerup.getState() == PowerupState.INACTIVE);
    }
}
