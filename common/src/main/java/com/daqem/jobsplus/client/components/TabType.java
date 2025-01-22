package com.daqem.jobsplus.client.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(value= EnvType.CLIENT)
public enum TabType {
    ABOVE(new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_above_left_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_above_middle_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_above_right_selected")), new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_above_left"), ResourceLocation.withDefaultNamespace("advancements/tab_above_middle"), ResourceLocation.withDefaultNamespace("advancements/tab_above_right")), 28, 32),
    BELOW(new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_below_left_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_below_middle_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_below_right_selected")), new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_below_left"), ResourceLocation.withDefaultNamespace("advancements/tab_below_middle"), ResourceLocation.withDefaultNamespace("advancements/tab_below_right")), 28, 32),
    LEFT(new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_left_top_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_left_middle_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_left_bottom_selected")), new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_left_top"), ResourceLocation.withDefaultNamespace("advancements/tab_left_middle"), ResourceLocation.withDefaultNamespace("advancements/tab_left_bottom")), 32, 28),
    RIGHT(new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_right_top_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_right_middle_selected"), ResourceLocation.withDefaultNamespace("advancements/tab_right_bottom_selected")), new Sprites(ResourceLocation.withDefaultNamespace("advancements/tab_right_top"), ResourceLocation.withDefaultNamespace("advancements/tab_right_middle"), ResourceLocation.withDefaultNamespace("advancements/tab_right_bottom")), 32, 28);

    private final Sprites selectedSprites;
    private final Sprites unselectedSprites;
    private final int width;
    private final int height;

    TabType(Sprites selectedSprites, Sprites unselectedSprites, int width, int height) {
        this.selectedSprites = selectedSprites;
        this.unselectedSprites = unselectedSprites;
        this.width = width;
        this.height = height;
    }

    public ResourceLocation getSprite(TabPosition tabPosition) {
        return switch (tabPosition) {
            case LEFT -> this.unselectedSprites.first();
            case MIDDLE -> this.unselectedSprites.middle();
            case RIGHT -> this.unselectedSprites.last();
        };
    }

    public ResourceLocation getSelectedSprite(TabPosition tabPosition) {
        return switch (tabPosition) {
            case LEFT -> this.selectedSprites.first();
            case MIDDLE -> this.selectedSprites.middle();
            case RIGHT -> this.selectedSprites.last();
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHorizontal() {
        return this == ABOVE || this == BELOW;
    }

    public boolean isVertical() {
        return this == LEFT || this == RIGHT;
    }

    @Environment(value=EnvType.CLIENT)
    record Sprites(ResourceLocation first, ResourceLocation middle, ResourceLocation last) {
    }
}
