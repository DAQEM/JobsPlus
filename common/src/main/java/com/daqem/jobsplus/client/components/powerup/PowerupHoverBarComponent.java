package com.daqem.jobsplus.client.components.powerup;

import com.daqem.jobsplus.client.components.SpriteComponent;
import com.daqem.jobsplus.client.textures.JobsPlusTextures;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import net.minecraft.resources.ResourceLocation;

public class PowerupHoverBarComponent extends SpriteComponent {

    public PowerupHoverBarComponent(int x, int y, int width, PowerupState state) {
        super(getSprite(state), x, y, width, 26);
    }

    private static ResourceLocation getSprite(PowerupState state) {
        return switch (state) {
            case ACTIVE -> JobsPlusTextures.Powerup.POWERUP_HOVER_BAR_ACTIVE;
            case INACTIVE -> JobsPlusTextures.Powerup.POWERUP_HOVER_BAR_INACTIVE;
            case LOCKED -> JobsPlusTextures.Powerup.POWERUP_HOVER_BAR_LOCKED;
            case NOT_OWNED -> JobsPlusTextures.Powerup.POWERUP_HOVER_BAR_NOT_OWNED;
        };
    }

    public void setState(PowerupState powerupState) {
        setSelectedSprite(getSprite(powerupState));
    }
}
