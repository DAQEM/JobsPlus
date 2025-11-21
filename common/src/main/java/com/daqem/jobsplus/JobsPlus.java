package com.daqem.jobsplus;

import org.slf4j.Logger;

import com.daqem.arc.registry.ArcRegistry;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.event.command.EventRegisterCommands;
import com.daqem.jobsplus.integration.arc.action.type.JobsPlusActionType;
import com.daqem.jobsplus.integration.arc.condition.type.JobsPlusConditionType;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobManager;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupManager;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.daqem.jobsplus.integration.arc.reward.type.JobsPlusRewardType;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import com.mojang.logging.LogUtils;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.text.DecimalFormat;

public class JobsPlus {
    public static final String MOD_ID = "jobsplus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        JobsPlusConfig.init();
        JobsPlusNetworking.init();

        registerEvents();
        initRegistry();
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new JobManager(), getId("jobs"));
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new PowerupManager(), getId("powerups"));
    }

    private static void initRegistry() {
        ArcRegistry.init();

        JobsPlusActionType.init();
        JobsPlusRewardType.init();
        JobsPlusConditionType.init();
        JobsPlusActionHolderType.init();
    }

    private static void registerEvents() {
        EventRegisterCommands.registerEvent();
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

    public static String formatCoin(double coins) {
        return formatNumber(coins, JobsPlusConfig.coinFormat.get());
    }

    public static String formatExp(double exp) {
        return formatNumber(exp, JobsPlusConfig.expFormat.get());
    }

    public static String formatNumber(double number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        df.setMinimumFractionDigits(0);
        return df.format(number);
    }
}
