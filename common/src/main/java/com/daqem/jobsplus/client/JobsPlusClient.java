package com.daqem.jobsplus.client;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.event.EventKeyPressed;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class JobsPlusClient {

    private static final KeyMapping.Category JOBSPLUS_CATEGORY = new KeyMapping.Category(JobsPlus.getId("category"));
    public static final KeyMapping OPEN_MENU = new KeyMapping("key.jobsplus.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, JOBSPLUS_CATEGORY);

    public static void init() {
        registerEvents();
    }

    private static void registerEvents() {
        EventKeyPressed.registerEvent();
    }
}
