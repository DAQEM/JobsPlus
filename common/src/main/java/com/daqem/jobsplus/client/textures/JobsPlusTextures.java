package com.daqem.jobsplus.client.textures;

import com.daqem.jobsplus.JobsPlus;
import net.minecraft.resources.ResourceLocation;

public class JobsPlusTextures {

    public static class Powerup {
        public static final ResourceLocation POWERUP_TEXT_BOX = getSpriteLocation("text_box");

        public static final ResourceLocation POWERUP_HOVER_BAR_ACTIVE = getSpriteLocation("hover_bar_active");
        public static final ResourceLocation POWERUP_HOVER_BAR_INACTIVE = getSpriteLocation("hover_bar_inactive");
        public static final ResourceLocation POWERUP_HOVER_BAR_LOCKED = getSpriteLocation("hover_bar_locked");
        public static final ResourceLocation POWERUP_HOVER_BAR_NOT_OWNED = getSpriteLocation("hover_bar_not_owned");

        public static final ResourceLocation POWERUP_ICON_BASIC_ACTIVE = getSpriteLocation("basic_active");
        public static final ResourceLocation POWERUP_ICON_ADVANCED_ACTIVE = getSpriteLocation("advanced_active");
        public static final ResourceLocation POWERUP_ICON_MASTERY_ACTIVE = getSpriteLocation("mastery_active");
        public static final ResourceLocation POWERUP_ICON_BASIC_INACTIVE = getSpriteLocation("basic_inactive");
        public static final ResourceLocation POWERUP_ICON_ADVANCED_INACTIVE = getSpriteLocation("advanced_inactive");
        public static final ResourceLocation POWERUP_ICON_MASTERY_INACTIVE = getSpriteLocation("mastery_inactive");
        public static final ResourceLocation POWERUP_ICON_BASIC_LOCKED = getSpriteLocation("basic_locked");
        public static final ResourceLocation POWERUP_ICON_ADVANCED_LOCKED = getSpriteLocation("advanced_locked");
        public static final ResourceLocation POWERUP_ICON_MASTERY_LOCKED = getSpriteLocation("mastery_locked");
        public static final ResourceLocation POWERUP_ICON_BASIC_NOT_OWNED = getSpriteLocation("basic_not_owned");
        public static final ResourceLocation POWERUP_ICON_ADVANCED_NOT_OWNED = getSpriteLocation("advanced_not_owned");
        public static final ResourceLocation POWERUP_ICON_MASTERY_NOT_OWNED = getSpriteLocation("mastery_not_owned");

        private static ResourceLocation getSpriteLocation(String spriteName) {
            return JobsPlus.getId("powerup/" + spriteName);
        }
    }
}
