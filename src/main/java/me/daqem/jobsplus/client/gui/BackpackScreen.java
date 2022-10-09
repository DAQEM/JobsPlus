package me.daqem.jobsplus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.common.inventory.BackpackMenu;
import me.daqem.jobsplus.utils.enums.Backpack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class BackpackScreen extends AbstractContainerScreen<BackpackMenu> {

    private final ResourceLocation GUI;

    public BackpackScreen(BackpackMenu container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
        Backpack tier = container.getTier();
        this.GUI = tier.texture;
        this.imageWidth = tier.xSize;
        this.imageHeight = tier.ySize;
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.GUI);
        blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), 7, 6, 0x404040);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
        this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
    }
}
