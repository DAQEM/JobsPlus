package com.daqem.jobsplus.client.components;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.text.multiline.MultiLineText;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ModalComponent extends AbstractComponent<ModalComponent> {

    private static final int GAP = 2;
    private static final int BORDER = 6;

    private final SpriteComponent background;

    private final TextComponent title;
    private final TextComponent description;

    private final JobsButtonComponent cancelButton;
    private final JobsButtonComponent confirmButton;

    public ModalComponent(int width) {
        super(null, 0, 0, width, 0);
        setVisible(false);
        setZ(100);
        Font font = Minecraft.getInstance().font;

        Text titleText = new Text(font, JobsPlus.literal(""), 0, 0, width - (BORDER * 2), font.lineHeight);
        MultiLineText descriptionText = new MultiLineText(font, JobsPlus.literal(""), 0, 0, width - (BORDER * 2));

        titleText.setBold(true);
        titleText.setTextColor(ChatFormatting.DARK_GRAY);
        descriptionText.setTextColor(ChatFormatting.DARK_GRAY);

        this.title = new TextComponent(BORDER, BORDER, titleText);
        this.description = new TextComponent(BORDER, BORDER + font.lineHeight + GAP + GAP, descriptionText);
        this.cancelButton = new JobsButtonComponent(0, 0, (width - GAP) / 2, 20, JobsPlus.translatable("gui.cancel"), (clickedObject, screen, mouseX, mouseY, button) -> {
            if (!isVisible()) return false;
            setVisible(false);
            return true;
        });
        this.confirmButton = new JobsButtonComponent((width + GAP) / 2, 0, (width - GAP) / 2, 20, JobsPlus.translatable("gui.confirm"), (clickedObject, screen, mouseX, mouseY, button) -> {
            if (!isVisible()) return false;
            setVisible(false);
            return true;
        });

        this.background = new SpriteComponent(JobsPlus.getId("widget/background"), 0, 0, width, 0);

        addChild(background);
        addChild(title);
        addChild(description);
        addChild(cancelButton);
        addChild(confirmButton);
    }

    @Override
    public void startRenderable() {
        super.startRenderable();
        center();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBlurredBackground(delta);
        this.background.setHeight(getBackgroundHeight());
        this.cancelButton.setY(getBackgroundHeight() + GAP);
        this.confirmButton.setY(getBackgroundHeight() + GAP);
    }

    @Override
    public int getHeight() {
        return getBackgroundHeight() + GAP + cancelButton.getHeight();
    }

    public void close() {
        setVisible(false);
        getConfirmButton().setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            if (!isVisible()) return false;
            setVisible(false);
            return true;
        });
    }

    private int getBackgroundHeight() {
        if (description.getText() == null) return 0;
        int descriptionHeight = ((MultiLineText) description.getText()).getLines().size() * Minecraft.getInstance().font.lineHeight;
        return BORDER + title.getHeight() + GAP + GAP + descriptionHeight + BORDER;
    }

    @Override
    public boolean preformOnClickEvent(double mouseX, double mouseY, int button) {
        return false;
    }

    private void renderBlurredBackground(float delta) {
        Minecraft.getInstance().gameRenderer.processBlurEffect(delta);
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    public TextComponent getTitle() {
        return title;
    }

    public TextComponent getDescription() {
        return description;
    }

    public JobsButtonComponent getConfirmButton() {
        return confirmButton;
    }

    public JobsButtonComponent getCancelButton() {
        return cancelButton;
    }
}
