package com.daqem.jobsplus.client.components.powerup;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.components.ModalComponent;
import com.daqem.jobsplus.client.components.SpriteComponent;
import com.daqem.jobsplus.client.components.jobs.JobsComponent;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.screen.job.tab.RightTab;
import com.daqem.jobsplus.client.textures.JobsPlusTextures;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.c2s.ServerboundStartPowerupPacket;
import com.daqem.jobsplus.networking.c2s.ServerboundTogglePowerUpPacket;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.daqem.jobsplus.player.job.powerup.PowerupType;
import com.daqem.uilib.client.gui.component.ItemComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

public class PowerupIconComponent extends SpriteComponent {

    private final PowerupType type;
    private PowerupState state;
    private JobsScreenOptions options;

    private boolean isClicked = false;
    private long clickedAt;
    private double mouseX;
    private double mouseY;

    @SuppressWarnings("DataFlowIssue")
    public PowerupIconComponent(int x, int y, ItemStack itemStack, PowerupState state, PowerupType type, JobsScreenOptions options) {
        super(getSprite(state, type), x, y, 26, 26);
        this.state = state;
        this.type = type;
        this.options = options;

        ItemComponent itemComponent = new ItemComponent(5, 5, itemStack, true);

        addChildren(itemComponent);

        setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            isClicked = true;
            clickedAt = System.currentTimeMillis();
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            return true;
        });

        setOnMouseReleaseEvent((mouseObject, screen, mouseX, mouseY, button) -> {
            if (isClicked &&
                    Math.abs(this.mouseX - mouseX) <= 2 &&
                    Math.abs(this.mouseY - mouseY) <= 2 &&
                    System.currentTimeMillis() - clickedAt < 500) {
                PowerupComponent powerupComponent = (PowerupComponent) this.getParent();
                if (!powerupComponent.getTreeComponent().getRootPowerupComponent().equals(powerupComponent)) {
                    JobInstance jobInstance = options.getSelectedJob().getJobInstance();
                    PowerupInstance powerupInstance = powerupComponent.getPowerup().getPowerupInstance();
                    if (getState() == PowerupState.NOT_OWNED) {
                        JobsComponent jobsComponent = (JobsComponent) (((PowerupComponent) this.getParent()).getTreeComponent().getParent().getParent().getParent());
                        if (options.getCoins() < powerupInstance.getPrice()) {
                            jobsComponent.openModal(
                                    JobsPlus.translatable("gui.jobs.not_enough_coins.title"),
                                    JobsPlus.translatable("gui.jobs.not_enough_coins.description"),
                                    (clickedObject, screen1, mouseX1, mouseY1, button1) -> {
                                        ModalComponent modal = (ModalComponent) clickedObject.getParent();
                                        if (modal != null) {
                                            modal.close();
                                        }
                                        return true;
                                    }, true
                            );
                        } else if (options.getSelectedJob().getLevel() < powerupInstance.getRequiredLevel()) {
                            jobsComponent.openModal(
                                    JobsPlus.translatable("gui.jobs.not_high_enough_level.title"),
                                    JobsPlus.translatable("gui.jobs.not_high_enough_level.description"),
                                    (clickedObject, screen1, mouseX1, mouseY1, button1) -> {
                                        ModalComponent modal = (ModalComponent) clickedObject.getParent();
                                        if (modal != null) {
                                            modal.close();
                                        }
                                        return true;
                                    }, true
                            );
                        } else {
                            jobsComponent.openModal(
                                    JobsPlus.translatable("gui.job.powerup.start"),
                                    JobsPlus.translatable("gui.job.powerup.start.description",
                                            powerupInstance.getName().withColor(jobInstance.getColorDecimal()),
                                            JobsPlus.literal(String.valueOf(powerupInstance.getPrice())).withColor(jobInstance.getColorDecimal())),
                                    (clickedObject, screen1, mouseX1, mouseY1, button1) -> {
                                        ModalComponent modal = (ModalComponent) clickedObject.getParent();
                                        if (modal != null) {
                                            NetworkManager.sendToServer(new ServerboundStartPowerupPacket(jobInstance, powerupInstance));
                                            options.setCoins(options.getCoins() - powerupInstance.getPrice());
                                            powerupComponent.setState(PowerupState.ACTIVE);
                                            powerupComponent.getChildren().stream()
                                                    .filter(child -> child instanceof PowerupComponent)
                                                    .map(child -> (PowerupComponent) child)
                                                    .forEach(child -> child.setState(PowerupState.NOT_OWNED));
                                            modal.close();
                                        }
                                        return true;
                                    }, false
                            );
                        }
                    } else if (getState() == PowerupState.ACTIVE) {
                        powerupComponent.setState(PowerupState.INACTIVE);
                        NetworkManager.sendToServer(new ServerboundTogglePowerUpPacket(jobInstance, powerupInstance));
                    } else if (getState() == PowerupState.INACTIVE) {
                        powerupComponent.setState(PowerupState.ACTIVE);
                        NetworkManager.sendToServer(new ServerboundTogglePowerUpPacket(jobInstance, powerupInstance));
                    }
                }
            }
            isClicked = false;
            return true;
        });
    }

    private static ResourceLocation getSprite(PowerupState state, PowerupType type) {
        return switch (state) {
            case ACTIVE -> switch (type) {
                case BASIC -> JobsPlusTextures.Powerup.POWERUP_ICON_BASIC_ACTIVE;
                case ADVANCED -> JobsPlusTextures.Powerup.POWERUP_ICON_ADVANCED_ACTIVE;
                case MASTERY -> JobsPlusTextures.Powerup.POWERUP_ICON_MASTERY_ACTIVE;
            };
            case INACTIVE -> switch (type) {
                case BASIC -> JobsPlusTextures.Powerup.POWERUP_ICON_BASIC_INACTIVE;
                case ADVANCED -> JobsPlusTextures.Powerup.POWERUP_ICON_ADVANCED_INACTIVE;
                case MASTERY -> JobsPlusTextures.Powerup.POWERUP_ICON_MASTERY_INACTIVE;
            };
            case LOCKED -> switch (type) {
                case BASIC -> JobsPlusTextures.Powerup.POWERUP_ICON_BASIC_LOCKED;
                case ADVANCED -> JobsPlusTextures.Powerup.POWERUP_ICON_ADVANCED_LOCKED;
                case MASTERY -> JobsPlusTextures.Powerup.POWERUP_ICON_MASTERY_LOCKED;
            };
            case NOT_OWNED -> switch (type) {
                case BASIC -> JobsPlusTextures.Powerup.POWERUP_ICON_BASIC_NOT_OWNED;
                case ADVANCED -> JobsPlusTextures.Powerup.POWERUP_ICON_ADVANCED_NOT_OWNED;
                case MASTERY -> JobsPlusTextures.Powerup.POWERUP_ICON_MASTERY_NOT_OWNED;
            };
        };
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        if (state != PowerupState.LOCKED && isTotalHovered(mouseX, mouseY)) {
            color = ARGB.colorFromFloat(0.85F, 0.85F, 0.85F, 1.0F);
        }
        super.render(graphics, mouseX, mouseY, delta, color);
    }

    public PowerupState getState() {
        return state;
    }

    public void setState(PowerupState powerupState) {
        this.state = powerupState;
        setSelectedSprite(getSprite(powerupState, this.type));
    }

    @Override
    public boolean preformOnClickEvent(double mouseX, double mouseY, int button) {
        if (options.getSelectedRightTab() == RightTab.POWER_UPS) {
            return super.preformOnClickEvent(mouseX, mouseY, button);
        } else {
            return false;
        }
    }

    @Override
    public boolean preformOnMouseReleaseEvent(double mouseX, double mouseY, int button) {
        if (options.getSelectedRightTab() == RightTab.POWER_UPS) {
            return super.preformOnMouseReleaseEvent(mouseX, mouseY, button);
        } else {
            return false;
        }
    }
}
