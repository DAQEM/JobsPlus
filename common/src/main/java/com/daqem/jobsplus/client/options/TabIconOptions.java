package com.daqem.jobsplus.client.options;

import net.minecraft.resources.ResourceLocation;

public record TabIconOptions(
        ResourceLocation icon,
        int x,
        int y,
        int width,
        int height
) {
}
