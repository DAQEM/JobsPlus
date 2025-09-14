package com.daqem.jobsplus.client.gui.jobs.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreenState;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TextAlign;
import com.daqem.uilib.gui.component.text.TextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;

public class CoinsComponent extends SpriteComponent {

    public CoinsComponent(JobsScreenState state) {
        super(0, 27, 0, 16, JobsPlus.getId("jobs/tab_coins"));

        MutableComponent coinsText = JobsPlus.literal(state.getCoins() + "");
        int coinsTextWidth = Minecraft.getInstance().font.width(coinsText);

        this.setX(-coinsTextWidth + 2);
        this.setWidth(coinsTextWidth + 16);

        TextComponent coinsTextComponent = new TextComponent(getWidth() - 11, 5, coinsText, 0xFF1E1410);
        coinsTextComponent.setTextAlign(TextAlign.RIGHT);
        SpriteComponent coinsIconComponent = new SpriteComponent(getWidth() - 10, 4, 7, 8, JobsPlus.getId("jobs/coins"));

        this.addComponent(coinsTextComponent);
        this.addComponent(coinsIconComponent);
    }
}
