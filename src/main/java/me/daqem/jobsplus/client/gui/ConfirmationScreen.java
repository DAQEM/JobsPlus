package me.daqem.jobsplus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.renderer.RenderColor;
import me.daqem.jobsplus.common.packet.PacketJobStartStop;
import me.daqem.jobsplus.common.packet.PacketMenuPowerUp;
import me.daqem.jobsplus.common.packet.PacketOpenMenu;
import me.daqem.jobsplus.init.ModPackets;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConfirmationScreen extends Screen {

    private static final Component TITLE = JobsPlus.literal("Confirmation");
    private static final ResourceLocation BACKGROUND = JobsPlus.getId("textures/gui/confirmation_screen.png");
    private static final List<String> showBackButtonList = List.of("not_enough_coins_stop", "not_enough_coins_start", "not_enough_coins_powerup", "job_not_enabled", "must_be_level_100");
    private final Screen lastScreen;
    private final int imageWidth = 300;
    private final int imageHeight = 50;
    private final Component confirmationText;
    private final String action;
    private final Jobs job;
    private final int powerUp;
    private int startX;
    private int startY;

    protected ConfirmationScreen(Component confirmationText, String action, Jobs job, int powerUp, Screen lastScreen) {
        super(TITLE);

        this.confirmationText = confirmationText;
        this.action = action;
        this.job = job;
        this.powerUp = powerUp;
        this.lastScreen = lastScreen;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        startX = (this.width - imageWidth) / 2;
        startY = (this.height - imageHeight) / 2;

        this.renderBackgrnd(poseStack);
        this.renderButtons(poseStack, mouseX, mouseY);
        this.renderTexts(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    private void renderTexts(@NotNull PoseStack poseStack) {
        drawNewCenteredString(poseStack, confirmationText, width / 2, startY + 15);
        if (showBackButtonList.contains(action)) {
            drawNewCenteredString(poseStack, "confirm.back", width / 2, startY + 33);
        } else {
            drawNewCenteredString(poseStack, "confirm.yes", width / 2 - 40, startY + 33);
            drawNewCenteredString(poseStack, "confirm.cancel", width / 2 + 40, startY + 33);
        }
    }

    private void renderButtons(PoseStack poseStack, int mouseX, int mouseY) {
        if (showBackButtonList.contains(action)) {
            renderButton(poseStack, mouseX, mouseY, 38); // BACK BUTTON if NOT ENOUGH COINS
        } else {
            renderButton(poseStack, mouseX, mouseY, 78); // YES BUTTON
            renderButton(poseStack, mouseX, mouseY, -3); // CANCEL BUTTON
        }
    }

    private void renderButton(PoseStack poseStack, int mouseX, int mouseY, int xOffset) {
        if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - xOffset, imageHeight - 22, imageWidth / 2 - xOffset + 74, imageHeight - 5)) {
            RenderColor.buttonHover();
        }
        blitThis(poseStack, imageWidth / 2 - xOffset, imageHeight - 22, 0, imageHeight, 75, 18);
        RenderColor.normal();
    }


    private void renderBackgrnd(@NotNull PoseStack poseStack) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderColor.normal();
        RenderSystem.setShaderTexture(0, BACKGROUND);

        int boxWidth = font.width(confirmationText) + 16;
        blit(poseStack, (this.width - boxWidth) / 2, (this.height - 30) / 2, 0, 0, boxWidth, 30, boxWidth, 300 / 5 * 3);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94697_) {
        if (showBackButtonList.contains(action)) {
            // BACK BUTTON if NOT ENOUGH COINS
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 38, imageHeight - 22, imageWidth / 2 + 36, imageHeight - 5)) {
                if (lastScreen instanceof JobsScreen jobsScreen) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    openNewJobsScreen(jobsScreen);
                }
            }
        } else {
            // YES BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 78, imageHeight - 22, imageWidth / 2 - 4, imageHeight - 5)) {
                if (lastScreen instanceof JobsScreen jobsScreen) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    if (action.equals("start") || action.equals("start_paid"))
                        ModPackets.INSTANCE.sendToServer(new PacketJobStartStop(true, job));
                    if (action.equals("stop") || action.equals("stop_free"))
                        ModPackets.INSTANCE.sendToServer(new PacketJobStartStop(false, job));
                    if (action.equals("powerup"))
                        ModPackets.INSTANCE.sendToServer(new PacketMenuPowerUp(false, job, powerUp + 1));
                    openNewJobsScreen(jobsScreen);
                }
            }
            // CANCEL BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 + 3, imageHeight - 22, imageWidth / 2 + 77, imageHeight - 5)) {
                if (lastScreen instanceof JobsScreen jobsScreen) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    openNewJobsScreen(jobsScreen);
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, p_94697_);
    }

    private void openNewJobsScreen(JobsScreen jobsScreen) {
        ModPackets.INSTANCE.sendToServer(new PacketOpenMenu(Jobs.getJobInt(job), jobsScreen.getActiveLeftButton(), jobsScreen.getActiveRightButton(), jobsScreen.getSelectedButton(), jobsScreen.getScrollOffs(), jobsScreen.getStartIndex()));
    }

    public void blitThis(PoseStack poseStack, int posX, int posY, int startX, int startY, int stopX, int stopY) {
        blit(poseStack, this.startX + posX, this.startY + posY, startX, startY, stopX, stopY, 300, 300);
    }

    public boolean isBetween(double mouseX, double mouseY, int mouseXTop, int mouseYTop, int mouseXBottom, int mouseYBottom) {
        return mouseX >= mouseXTop && mouseY >= mouseYTop && mouseX <= mouseXBottom && mouseY <= mouseYBottom;
    }

    public void drawNewCenteredString(PoseStack poseStack, String text, int x, int y) {
        Component component = JobsPlus.translatable(text);
        font.draw(poseStack, component, (float) (x - font.width(component) / 2), (float) y, 16777215);
    }

    public void drawNewCenteredString(PoseStack poseStack, Component component, int x, int y) {
        font.draw(poseStack, component.getString(), (float) (x - font.width(component) / 2), (float) y, 16777215);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
