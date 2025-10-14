package com.daqem.jobsplus.test;

import com.daqem.itemrestrictions.config.ItemRestrictionsConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class TestMod {
    public static final String MOD_ID = "jobsplus_test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
    }

    public static ResourceLocation getId(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static MutableComponent translatable(String str) {
        return Component.translatable(MOD_ID + "." + str);
    }

    public static MutableComponent translatable(String str, Object... objects) {
        return Component.translatable(MOD_ID + "." + str, objects);
    }

    public static MutableComponent literal(String str) {
        return Component.literal(str);
    }

    public static boolean isDebugEnvironment() {
        return ItemRestrictionsConfig.isDebug.get();
    }
}
