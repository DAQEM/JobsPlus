package me.daqem.jobsplus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.packet.PacketJobStartStop;
import me.daqem.jobsplus.packet.PacketMenuPowerUp;
import me.daqem.jobsplus.packet.PacketOpenMenu;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ConfirmationScreen extends Screen {

    private static final Component TITLE = Component.literal("Confirmation");
    private static final ResourceLocation BACKGROUND = JobsPlus.getId("textures/gui/confirmation_screen.png");
    private static final String[] backButton = new String[]{"not_enough_coins_stop", "not_enough_coins_start", "not_enough_coins_powerup", "job_not_enabled", "must_be_level_100"};
    private final int imageWidth = 300;
    private final int imageHeight = 50;
    private final Component confirmationText;
    private final String action;
    private final Jobs job;
    private final int activeLeftButton;
    private final int activeRightButton;
    private final int selectedButton;
    private final float scrollOffs;
    private final int startIndex;
    private int startX;
    private int startY;
    private int powerUp;

    protected ConfirmationScreen(Component confirmationText, String action, Jobs job, int activeLeftButton, int activeRightButton, int selectedButton, float scrollOffs, int startIndex) {
        super(TITLE);

        this.confirmationText = confirmationText;
        this.action = action;
        this.job = job;
        this.activeLeftButton = activeLeftButton;
        this.activeRightButton = activeRightButton;
        this.selectedButton = selectedButton;
        this.scrollOffs = scrollOffs;
        this.startIndex = startIndex;
    }

    protected ConfirmationScreen(Component confirmationText, String action, Jobs job, int powerUp, int activeLeftButton, int activeRightButton, int selectedButton, float scrollOffs, int startIndex) {
        super(TITLE);

        this.confirmationText = confirmationText;
        this.action = action;
        this.job = job;
        this.powerUp = powerUp;
        this.activeLeftButton = activeLeftButton;
        this.activeRightButton = activeRightButton;
        this.selectedButton = selectedButton;
        this.scrollOffs = scrollOffs;
        this.startIndex = startIndex;
    }

    public static void drawCenteredString(@NotNull PoseStack p_93209_, Font p_93210_, @NotNull Component p_93211_, int p_93212_, int p_93213_, int p_93214_) {
        p_93210_.draw(p_93209_, p_93211_, (float) (p_93212_ - p_93210_.width(p_93211_) / 2), (float) p_93213_, p_93214_);
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
        int boxWidth = font.width(confirmationText) + 16;
        blit(poseStack, (this.width - boxWidth) / 2, (this.height - 30) / 2, 0, 0, boxWidth, 30, boxWidth, 300 / 5 * 3);
        renderButtons(poseStack, mouseX, mouseY);
        poseStack.popPose();
        drawCenteredString(poseStack, font, confirmationText, width / 2, startY + 15, 16777215);
        if (!Arrays.stream(backButton).toList().contains(action)) {
            // YES AND CANCEL BUTTON TEXT
            drawCenteredString(poseStack, font, Component.translatable("confirm.yes"), startX + (imageWidth / 2) - (75 / 2) - 3, startY + 33, 16777215);
            drawCenteredString(poseStack, font, Component.translatable("confirm.cancel"), startX + (imageWidth / 2) + 3 + (75 / 2), startY + 33, 16777215);
        } else {
            // BACK BUTTON TEXT
            drawCenteredString(poseStack, font, Component.translatable("confirm.back"), startX + (imageWidth / 2), startY + 33, 16777215);
        }
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    private void renderButtons(PoseStack poseStack, int mouseX, int mouseY) {
        if (!Arrays.stream(backButton).toList().contains(action)) {
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
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return super.mouseClicked(mouseX, mouseY, p_94697_);
        if (!Arrays.stream(backButton).toList().contains(action)) {
            // YES BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 78, imageHeight - 22, imageWidth / 2 - 4, imageHeight - 5)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                if (action.equals("start") || action.equals("start_paid"))
                    ModPacketHandler.INSTANCE.sendToServer(new PacketJobStartStop(true, job));
                if (action.equals("stop") || action.equals("stop_free"))
                    ModPacketHandler.INSTANCE.sendToServer(new PacketJobStartStop(false, job));
                if (action.equals("powerup"))
                    ModPacketHandler.INSTANCE.sendToServer(new PacketMenuPowerUp(false, job, powerUp + 1));
                ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(Jobs.getJobInt(job), activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
            }
            // CANCEL BUTTON
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 + 3, imageHeight - 22, imageWidth / 2 + 77, imageHeight - 5)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(Jobs.getJobInt(job), activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
            }
        } else {
            // BACK BUTTON if NOT ENOUGH COINS
            if (isBetween(mouseX - startX, mouseY - startY, imageWidth / 2 - 38, imageHeight - 22, imageWidth / 2 + 36, imageHeight - 5)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(Jobs.getJobInt(job), activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
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
