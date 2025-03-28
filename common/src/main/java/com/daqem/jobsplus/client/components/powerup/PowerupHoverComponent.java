package com.daqem.jobsplus.client.components.powerup;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.options.JobsScreenOptions;
import com.daqem.jobsplus.client.textures.JobsPlusTextures;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.daqem.jobsplus.player.job.powerup.PowerupType;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.text.TruncatedText;
import com.daqem.uilib.client.gui.text.multiline.MultiLineText;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class PowerupHoverComponent extends AbstractComponent<PowerupHoverComponent> {

    private static final int MIN_WIDTH = 140;
    private static final int MAX_WIDTH = 260;
    private static final int TITLE_X = 28;
    private static final int TITLE_Y = 8;
    private final MultiLineText descriptionText;
    private final TextComponent requiredLevelComponent;
    private final TextComponent priceComponent;
    private final TextComponent descriptionComponent;
    private final PowerupHoverBarComponent hoverBarComponent;
    private final PowerupIconComponent iconComponent;
    private final boolean hasStaticHeight;

    public PowerupHoverComponent(int x, int y, Font font, ItemStack itemStack, Component title, Component descriptionText, PowerupState state, PowerupType type, int requiredLevel, int price, JobsScreenOptions options) {
        this(x, y, Mth.clamp(TITLE_X + font.width(title), MIN_WIDTH, MAX_WIDTH), font, itemStack, title, descriptionText, state, type, requiredLevel, price, options);
    }

    public PowerupHoverComponent(int x, int y, int width, Font font, ItemStack itemStack, Component title, Component descriptionText, PowerupState state, PowerupType type, int requiredLevel, int price,  JobsScreenOptions options) {
        this(x, y, width, -1, font, itemStack, title, descriptionText, state, type, requiredLevel, price, options);
    }

    public PowerupHoverComponent(int x, int y, int width, int height, Font font, ItemStack itemStack, Component title, Component descriptionText, PowerupState state, PowerupType type, int requiredLevel, int price,  JobsScreenOptions options) {
        super(null, x, y, 0, 0);
        setText(new TruncatedText(font, title, TITLE_X, TITLE_Y, width - TITLE_X - 3, font.lineHeight));
        setWidth(width);
        setHeight(height);

        this.requiredLevelComponent = new TextComponent(0, 0, new Text(font, JobsPlus.translatable("gui.job.powerup.required_level", requiredLevel), 0, 0));
        Objects.requireNonNull(requiredLevelComponent.getText()).setTextColor(ChatFormatting.GRAY);
        this.priceComponent = new TextComponent(0, 0, new Text(font, JobsPlus.translatable("gui.job.powerup.price", price), 0, 0));
        Objects.requireNonNull(priceComponent.getText()).setTextColor(ChatFormatting.GRAY);

        this.descriptionText = new MultiLineText(Minecraft.getInstance().font, descriptionText, 0, 0, getWidth() - 3);
        this.descriptionComponent = new TextComponent(0, 27, this.descriptionText);

        this.hoverBarComponent = new PowerupHoverBarComponent(-4, 0, getWidth(), state);
        this.iconComponent = new PowerupIconComponent(0, 0, itemStack, state, type, options);
        this.hasStaticHeight = height != -1;

        if (!hasStaticHeight) {
            setHeight(32 + getDescriptionHeight());
        }

        addChild(requiredLevelComponent);
        addChild(priceComponent);
        addChild(descriptionComponent);
        addChild(hoverBarComponent);
        addChild(iconComponent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, int color) {
        if (!hasStaticHeight) {
            setHeight(32 + getDescriptionHeight());
        }
        this.requiredLevelComponent.setY(getHeight() + 1);
        this.priceComponent.setY(getHeight() + 11);

        graphics.blitSprite(RenderType::guiTextured, JobsPlusTextures.Powerup.POWERUP_TEXT_BOX, -4, 16, getWidth(), getHeight() + 10);
        graphics.fill(-2, getHeight() - 3, getWidth(), getHeight() - 2, 0x33FFFFFF);
    }

    private int getDescriptionHeight() {
        return this.descriptionText.getLines().size() * Minecraft.getInstance().font.lineHeight;
    }

    public void setState(PowerupState powerupState) {
        this.hoverBarComponent.setState(powerupState);
        this.iconComponent.setState(powerupState);
    }
}
