package com.daqem.jobsplus.client.gui.jobs.widgets.leaderboard;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.LeaderboardPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.uilib.gui.widget.CustomButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class LeaderboardItemWidget extends CustomButtonWidget {

    private static final WidgetSprites SPRITES = new WidgetSprites(
            JobsPlus.API.getId("jobs/job_button"),
            JobsPlus.API.getId("jobs/job_button_hovered")
    );

    private final LeaderboardPlayer player;
    private final TruncatedTextComponent nameComponent;

    public LeaderboardItemWidget(LeaderboardPlayer player, JobsScreenState state) {
        super(0, 0, 99, 19, JobsPlus.API.literal(player.getPlayerName()), SPRITES, button -> {
            state.fetchViewingPlayerJobs(player);
        });
        this.player = player;

        this.nameComponent = new TruncatedTextComponent(0, 0, 0, JobsPlus.API.literal(player.getPlayerName()), 0);
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        JobInstance jobInstance = JobInstance.of(player.getJobLocation());
        if (jobInstance == null) return;

        // Render background
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.active, this.isHovered()), this.getX(), this.getY(), this.getWidth(), this.getHeight());

        PlayerInfo playerInfo = minecraft.getConnection() != null ? minecraft.getConnection().getPlayerInfo(player.getUuid()) : null;
        if (playerInfo == null) {
            playerInfo = minecraft.getConnection() != null ? minecraft.getConnection().getSeenPlayers().get(player.getUuid()) : null;
        }

        Identifier skinLocation;
        if (playerInfo != null) {
            skinLocation = playerInfo.getSkin().body().texturePath();
        } else {
            skinLocation = DefaultPlayerSkin.get(player.getUuid()).body().texturePath();
        }

        int headX = this.getX() + 4;
        int headY = this.getY() + 3;
        int headSize = 12;
        PlayerFaceExtractor.extractRenderState(guiGraphics, skinLocation, headX, headY, headSize, true, false, -1);

        String levelString = String.valueOf(player.getLevel());
        int levelWidth = minecraft.font.width(levelString);
        guiGraphics.text(minecraft.font, levelString, this.getX() + this.getWidth() - 3 - levelWidth, this.getY() + 3, getColor(player), false);

        this.nameComponent.setX(this.getX() + 20);
        this.nameComponent.setY(this.getY() + 3);
        this.nameComponent.setMaxWidth(this.getWidth() - 19 - levelWidth - 3 - 2);
        this.nameComponent.setColor(getColor(player));
        this.nameComponent.extractRenderStateBase(guiGraphics, mouseX, mouseY, partialTick, this.getWidth(), this.getHeight());

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, JobsPlus.API.getId("jobs/exp_bar"), getX() + 20, getY() + 13, 76, 3);
        double expPercentage = player.getExperience() / Job.getExperienceToLevelUp(player.getLevel()) * 100;
        int expWidth = (int) Mth.clamp(expPercentage / 100 * 75, 1, 75);
        guiGraphics.fill(getX() + 21, getY() + 14, getX() + 21 + expWidth, getY() + 15, jobInstance.getColorDecimal() | 0xFF000000);
    }

    private static int getColor(LeaderboardPlayer leaderboardPlayer) {
        return switch (leaderboardPlayer.getRank()) {
            case 1 -> 0xFFD4AF37;
            case 2 -> 0xFFC0C0C0;
            case 3 -> 0xFFCD7F32;
            default -> Minecraft.getInstance().player.getUUID().equals(leaderboardPlayer.getUuid()) ? 0xFF1E1410 : 0xFFD8BF96;
        };
    }
}