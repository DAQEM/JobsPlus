package com.daqem.jobsplus;

import com.daqem.knot.Knot;

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

import java.text.DecimalFormat;

public class JobsPlus {
    public static final String MOD_ID = "jobsplus";
    public static final Knot API = new Knot(MOD_ID);

    public static void init() {
        JobsPlusConfig.init();
        JobsPlusNetworking.init();

        registerEvents();
        initRegistry();
        Knot.RELOAD_REGISTRY.registerData(API.getId("jobs"), new JobManager());
        Knot.RELOAD_REGISTRY.registerData(API.getId("powerups"), new PowerupManager());
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
