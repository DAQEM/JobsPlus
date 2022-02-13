package me.daqem.jobsplus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class ConfirmationScreen extends Screen {

    private static final Component TITLE = new KeybindComponent("Confirmation");
    private static final ResourceLocation BACKGROUND = JobsPlus.getId("textures/gui/confirmation_screen.png");
    private final int imageWidth = 300;
    private final int imageHeight = 50;
    private final String confirmationText;
    private final String action;
    private final Jobs job;
    private int startX;
    private int startY;
    private int powerUp;


    protected ConfirmationScreen(String confirmationText, String action, Jobs job) {
        super(TITLE);

        this.confirmationText = confirmationText;
        this.action = action;
        this.job = job;
    }

    protected ConfirmationScreen(String confirmationText, String action, Jobs job, int powerUp) {
        super(TITLE);

        this.confirmationText = confirmationText;
        this.action = action;
        this.job = job;
        this.powerUp = powerUp;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        startX = (this.width - imageWidth) / 2;
        startY = (this.height - imageHeight) / 2;
        this.renderBackground(poseStack);
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        if (action.equals("stop_free") || action.equals("start"))
            blit(poseStack, (this.width - 300) / 2, (this.height - 30) / 2, 0, 0, 300, 30, 300, 300 / 5 * 3);
        else blit(poseStack, (this.width - 350) / 2, (this.height - 30) / 2, 0, 0, 350, 30, 350, 300 / 5 * 3);
        renderButtons(poseStack, mouseX, mouseY);
        poseStack.popPose();
        switch (action) {
            case "start" -> font.draw(poseStack, confirmationText, startX + 15, startY + 15, 16777215);
            case "stop" -> font.draw(poseStack, confirmationText, startX - 13, startY + 15, 16777215);
            case "stop_free" -> font.draw(poseStack, confirmationText, startX + 16, startY + 15, 16777215);
            case "not_enough_coins_stop" -> font.draw(poseStack, confirmationText, startX, startY + 15, 16777215);
            case "start_paid" -> font.draw(poseStack, confirmationText, startX - 18, startY + 15, 16777215);
            case "not_enough_coins_start" -> font.draw(poseStack, confirmationText, startX - 1, startY + 15, 16777215);
            case "powerup" -> font.draw(poseStack, confirmationText, startX, startY + 15, 16777215);
        }
        if (!action.equals("not_enough_coins_stop") && !action.equals("not_enough_coins_start") && !action.equals("not_enough_coins_powerup")) {
            font.draw(poseStack, "Yes", startX + 102, startY + 33, 16777215);
            font.draw(poseStack, "Cancel", startX + 174, startY + 33, 16777215);
        } else {
            // BACK BUTTON TEXT if NOT ENOUGH COINS
            font.draw(poseStack, "Back", startX + 139, startY + 33, 16777215);
        }
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    private void renderButtons(PoseStack poseStack, int mouseX, int mouseY) {
        if (!action.equals("not_enough_coins_stop") && !action.equals("not_enough_coins_start") && !action.equals("not_enough_coins_powerup")) {
            // YES BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 78, imageHeight - 22, imageWidth / 2 - 4, imageHeight - 5)) {
                RenderSystem.setShaderColor(0.6F, 0.6F, 1F, 1);
                blitThis(poseStack, (imageWidth / 2) - 75 - 3, imageHeight - 4 - 18, 0, imageHeight, 75, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
            } else {
                blitThis(poseStack, (imageWidth / 2) - 75 - 3, imageHeight - 4 - 18, 0, imageHeight, 75, 18);
            }
            // CANCEL BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 + 3, imageHeight - 22, imageWidth / 2 + 77, imageHeight - 5)) {
                RenderSystem.setShaderColor(0.6F, 0.6F, 1F, 1);
                blitThis(poseStack, (imageWidth / 2) + 3, imageHeight - 4 - 18, 0, imageHeight, 75, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
            } else {
                blitThis(poseStack, (imageWidth / 2) + 3, imageHeight - 4 - 18, 0, imageHeight, 75, 18);
            }
        } else {
            // BACK BUTTON if NOT ENOUGH COINS
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 38, imageHeight - 22, imageWidth / 2 + 36, imageHeight - 5)) {
                RenderSystem.setShaderColor(0.6F, 0.6F, 1F, 1);
                blitThis(poseStack, (imageWidth / 2) - 38, imageHeight - 4 - 18, 0, imageHeight, 75, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
            } else {
                blitThis(poseStack, (imageWidth / 2) - 38, imageHeight - 4 - 18, 0, imageHeight, 75, 18);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94697_) {
        if (!action.equals("not_enough_coins_stop") && !action.equals("not_enough_coins_start") && !action.equals("not_enough_coins_powerup")) {
            int activeRightButton = 0;
            if (action.equals("powerup")) activeRightButton = 2;
            // YES BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 78, imageHeight - 22, imageWidth / 2 - 4, imageHeight - 5)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                if (action.equals("start") || action.equals("start_paid"))
                    Minecraft.getInstance().player.chat("/job start " + job.name() + " force");
                if (action.equals("stop") || action.equals("stop_free"))
                    Minecraft.getInstance().player.chat("/job stop " + job.name() + " force");
                if (action.equals("powerup"))
                    Minecraft.getInstance().player.chat("/job powerups buy " + Jobs.getString(Jobs.getJobInt(job)) + " " + powerUp);
                Minecraft.getInstance().player.chat("/jobs menu " + Jobs.getJobInt(job) + " 0 " + activeRightButton + " -1 0 0");
            }
            // CANCEL BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 + 3, imageHeight - 22, imageWidth / 2 + 77, imageHeight - 5)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Minecraft.getInstance().player.chat("/jobs menu " + Jobs.getJobInt(job) + " 0 " + activeRightButton + " -1 0 0");
            }
        } else {
            // BACK BUTTON if NOT ENOUGH COINS
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 38, imageHeight - 22, imageWidth / 2 + 36, imageHeight - 5)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Minecraft.getInstance().player.chat("/jobs menu -1 0 0 -1 0 0");
            }
        }
        return super.mouseClicked(mouseX, mouseY, p_94697_);
    }

    public void blitThis(PoseStack poseStack, int posX, int posY, int startX, int startY, int stopX, int stopY) {
        blit(poseStack, this.startX + posX, this.startY + posY, startX, startY, stopX, stopY, 300, 300);
    }

    public boolean isBetween(double mouseX, double mouseY, int mouseXTop, int mouseYTop, int mouseXBottom, int mouseYBottom) {
        return mouseX >= mouseXTop && mouseY >= mouseYTop && mouseX <= mouseXBottom && mouseY <= mouseYBottom;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
