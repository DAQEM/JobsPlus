package com.daqem.jobsplus.client.gui.powerups.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreenState;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

public class CoinsComponent extends SpriteComponent {

    private final PowerupsScreenState state;
    private int cachedCoins;

    public CoinsComponent(PowerupsScreenState state) {
        super(0, 24, 0, 15, JobsPlus.getId("powerups/coins_background"));
        this.state = state;
        this.cachedCoins = state.getCoins();

        MutableComponent coinsText = JobsPlus.literal(this.cachedCoins + "");
        int coinsTextWidth = Minecraft.getInstance().font.width(coinsText);
        TextComponent coinsTextComponent = new TextComponent(6, 4, coinsText, 0xFFEAF0FF);
        SpriteComponent coinIcon = new SpriteComponent(6 + coinsTextWidth + 2, 4, 7, 8, JobsPlus.getId("jobs/coins"));

        this.addComponent(coinsTextComponent);
        this.addComponent(coinIcon);
        this.setWidth(6 + coinsTextWidth + 2 + 7 + 6);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        if (this.state.getCoins() != this.cachedCoins) {

        }
        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
    }
}
