package com.daqem.jobsplus.event.triggers;

import java.util.List;
import java.util.Map;

import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.action.type.JobsPlusActionType;
import com.daqem.jobsplus.integration.arc.data.type.JobsPlusActionDataType;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundLevelUpJobPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockItemRestrictionPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockPowerupPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;

import com.daqem.knot.Knot;
import net.minecraft.server.level.ServerPlayer;

public class JobEvents {

    public static void onJobLevelUp(JobsPlayer player, Job job) {
        if (player instanceof ArcPlayer arcPlayer) {
            new ActionDataBuilder(arcPlayer, JobsPlusActionType.JOB_LEVEL_UP)
                    .withData(JobsPlusActionDataType.ONLY_FOR_JOB, job)
                    .build()
                    .sendToAction();
        }
        if (player.jobsplus$getPlayer() instanceof ServerPlayer serverPlayer) {
            Knot.NETWORKING.sendToPlayer(serverPlayer, new ClientboundLevelUpJobPacket(job.getJobInstance().getIdentifier(), job.getLevel()));

            List<ItemRestriction> itemRestrictions = job.getJobInstance().getItemRestrictions()
                    .entrySet().stream()
                    .filter(entry -> entry.getValue() == job.getLevel())
                    .map(Map.Entry::getKey)
                    .toList();

            for (ItemRestriction itemRestriction : itemRestrictions) {
                if (itemRestriction.getIcon() != null && !itemRestriction.getIcon().isEmpty()) {
                    Knot.NETWORKING.sendToPlayer(serverPlayer, new ClientboundUnlockItemRestrictionPacket(itemRestriction.getIdentifier()));
                }
            }

            List<PowerupInstance> powerupInstances = job.getJobInstance().getPowerups()
                    .stream()
                    .filter(entry -> entry.getRequiredLevel() == job.getLevel())
                    .toList();

            for (PowerupInstance powerupInstance : powerupInstances) {
                if (powerupInstance.getIcon() != null && !powerupInstance.getIcon().isEmpty()) {
                    Knot.NETWORKING .sendToPlayer(serverPlayer, new ClientboundUnlockPowerupPacket(powerupInstance.getIdentifier()));
                }
            }

            player.jobsplus$addCoins(JobsPlusConfig.coinsPerLevelUp.get());
            JobInstance jobInstance = job.getJobInstance();
            if (JobsPlusConfig.sendJobLevelUpMessages.get()) {
                serverPlayer.level().getServer().getPlayerList()
                        .broadcastSystemMessage(
                                JobsPlus.API.translatable("job.level_up",
                                        serverPlayer.getName().copy()
                                                .withStyle(style -> style
                                                        .withColor(jobInstance.getColorDecimal())
                                                ),
                                        JobsPlus.API.literal(String.valueOf(job.getLevel()))
                                                .withStyle(style -> style
                                                        .withColor(jobInstance.getColorDecimal())
                                                ),
                                        jobInstance.getName().getString()
                                ), false);
            }
        }
    }

    public static void onJobExperience(JobsPlayer player, Job job, double experience) {
        if (player instanceof ArcPlayer arcPlayer) {
            new ActionDataBuilder(arcPlayer, JobsPlusActionType.JOB_EXP)
                    .withData(JobsPlusActionDataType.JOB_EXP, experience)
                    .withData(JobsPlusActionDataType.ONLY_FOR_JOB, job)
                    .build()
                    .sendToAction();
        }
    }
}
