package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerProfileComponent extends AbstractComponent {

    private final static SpriteComponent SEPARATOR_LINE = new SpriteComponent(21, 47, 113, 7, JobsPlus.API.getId("jobs/separator_line"));

    private final JobsScreenState state;
    private @Nullable UUID cachedUUID;

    public PlayerProfileComponent(JobsScreenState state) {
        super(0, 0, 138, 186);
        this.cachedUUID = null;
        this.state = state;

        CustomButtonWidget closeButton = new CustomButtonWidget(-2, 27, 20, 16, JobsPlus.API.translatable("gui.jobs.back"), new WidgetSprites(JobsPlus.API.getId("jobs/player_back"), JobsPlus.API.getId("jobs/player_back_hovered")), button -> state.stopViewingPlayer()) {
            @Override
            protected void extractDefaultLabel(@NotNull ActiveTextCollector activeTextCollector) {
            }
        };
        this.addWidget(closeButton);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        LeaderboardPlayer viewingPlayer = state.getViewingPlayer();
        if (viewingPlayer != null && !viewingPlayer.getUuid().equals(cachedUUID) && !state.getViewingPlayerJobs().isEmpty()) {
            cachedUUID = viewingPlayer.getUuid();
            this.clearComponents();
            this.addComponent(new JobSelectionComponent(state));
            this.addComponent(SEPARATOR_LINE);
            this.updateParentPosition(getParentX(), getParentY(), parentWidth, parentHeight);
        }

        if (viewingPlayer != null) {
            Minecraft minecraft = Minecraft.getInstance();
            PlayerInfo playerInfo = minecraft.getConnection() != null ? minecraft.getConnection().getPlayerInfo(viewingPlayer.getUuid()) : null;
            if (playerInfo == null) {
                playerInfo = minecraft.getConnection() != null ? minecraft.getConnection().getSeenPlayers().get(viewingPlayer.getUuid()) : null;
            }

            Identifier skinLocation;
            if (playerInfo != null) {
                skinLocation = playerInfo.getSkin().body().texturePath();
            } else {
                skinLocation = DefaultPlayerSkin.get(viewingPlayer.getUuid()).body().texturePath();
            }

            PlayerFaceExtractor.extractRenderState(guiGraphics, skinLocation, getTotalX() + 21, getTotalY() + 20, 24, true, false, -1);

            TruncatedTextComponent nameComponent = new TruncatedTextComponent(getTotalX() + 48, getTotalY() + 20, getWidth() - 48, JobsPlus.API.literal(viewingPlayer.getPlayerName()), 0xFF333333);
            nameComponent.extractRenderState(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);

            boolean isOnline = minecraft.getConnection() != null && minecraft.getConnection().getOnlinePlayers().stream().anyMatch(player -> player.getProfile().id().equals(viewingPlayer.getUuid()));
            TruncatedTextComponent statusComponent = new TruncatedTextComponent(getTotalX() + 48, getTotalY() + 20 + 9 + 2, getWidth() - 48, JobsPlus.API.literal(isOnline ? "Online" : "Offline"), isOnline ? 0xFF55AA55 : 0xFFAA5555);
            statusComponent.extractRenderState(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
        }
    }
}
