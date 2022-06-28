package me.daqem.jobsplus.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.packet.PacketOpenMenu;
import me.daqem.jobsplus.packet.PacketUserSettingsServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingsScreen extends Screen {

    int[] settings;
    SettingsType settingsType;

    public SettingsScreen(int[] settings, SettingsType settingsType) {
        super(Component.translatable("jobsplus.gui.settings." + settingsType.name().toLowerCase()));
        this.settingsType = settingsType;
        this.settings = settings;
    }

    @Override
    protected void init() {
        if (settingsType == SettingsType.MAIN) {
            this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, Component.translatable("jobsplus.gui.settings.main.hotbar"), (p_96276_) -> Minecraft.getInstance().setScreen(new SettingsScreen(settings, SettingsType.HOTBAR))));
            this.addRenderableWidget(new Button(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, Component.translatable("jobsplus.gui.settings.main.level_up"), (p_96274_) -> Minecraft.getInstance().setScreen(new SettingsScreen(settings, SettingsType.LEVEL_UP))));
            this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, (p_96257_) -> ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(-1, 0, 0, -1, 0, 0))));
        } else if (settingsType == SettingsType.HOTBAR) {
            this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, Component.translatable("jobsplus.gui.settings.hotbar.exp", settings[0] == 0 ? Component.translatable("jobsplus.gui.settings.on") : Component.translatable("jobsplus.gui.settings.off")), (p_96276_) -> ModPacketHandler.INSTANCE.sendToServer(new PacketUserSettingsServer("switch_hotbar_exp")), (button, poseStack, mouseX, mouseY) -> {
                List<Component> componentList = new ArrayList<>(List.of(Component.translatable("jobsplus.gui.settings.hotbar.exp"), Component.translatable("jobsplus.gui.settings.hotbar.exp.on"), Component.translatable("jobsplus.gui.settings.hotbar.exp.off")));
                renderComponentTooltip(poseStack, componentList, mouseX, mouseY);
            }));
            this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, (p_96257_) -> Minecraft.getInstance().setScreen(new SettingsScreen(settings, SettingsType.MAIN))));
        } else if (settingsType == SettingsType.LEVEL_UP) {
            this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, Component.translatable("jobsplus.gui.settings.level_up.sound", settings[1] == 0 ? Component.translatable("jobsplus.gui.settings.on") : Component.translatable("jobsplus.gui.settings.off")), (p_96276_) -> ModPacketHandler.INSTANCE.sendToServer(new PacketUserSettingsServer("switch_level_up_sound")), (button, poseStack, mouseX, mouseY) -> {
                List<Component> componentList = new ArrayList<>(List.of(Component.translatable("jobsplus.gui.settings.level_up.sound"), Component.translatable("jobsplus.gui.settings.level_up.sound.on"), Component.translatable("jobsplus.gui.settings.level_up.sound.off")));
                renderComponentTooltip(poseStack, componentList, mouseX, mouseY);
            }));
            this.addRenderableWidget(new Button(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, Component.translatable("jobsplus.gui.settings.level_up.chat", settings[2] == 0 ? Component.translatable("jobsplus.gui.settings.level_up.everyone") : settings[2] == 1 ? Component.translatable("jobsplus.gui.settings.level_up.self") : Component.translatable("jobsplus.gui.settings.off")), (p_96274_) -> ModPacketHandler.INSTANCE.sendToServer(new PacketUserSettingsServer("switch_level_up_chat")), (button, poseStack, mouseX, mouseY) -> {
                List<Component> componentList = new ArrayList<>(List.of(Component.translatable("jobsplus.gui.settings.level_up.chat"), Component.translatable("jobsplus.gui.settings.level_up.chat.everyone"), Component.translatable("jobsplus.gui.settings.level_up.chat.self"), Component.translatable("jobsplus.gui.settings.level_up.chat.off")));
                renderComponentTooltip(poseStack, componentList, mouseX, mouseY);
            }));
            this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, (p_96257_) -> Minecraft.getInstance().setScreen(new SettingsScreen(settings, SettingsType.MAIN))));
        }
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, font, title, width / 2, 15, 16777215);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public enum SettingsType {
        MAIN,
        LEVEL_UP,
        HOTBAR
    }
}
