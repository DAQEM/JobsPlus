package com.daqem.jobsplus.event.triggers;

import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.action.type.JobsPlusActionType;
import com.daqem.jobsplus.integration.arc.data.type.JobsPlusActionDataType;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundLevelUpJobPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockItemRestrictionPacket;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobEvents {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void onJobLevelUp(JobsPlayer player, Job job) {
        if (player instanceof ArcPlayer arcPlayer) {
            new ActionDataBuilder(arcPlayer, JobsPlusActionType.JOB_LEVEL_UP)
                    .withData(JobsPlusActionDataType.ONLY_FOR_JOB, job)
                    .build()
                    .sendToAction();
        }
        if (player.jobsplus$getPlayer() instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, new ClientboundLevelUpJobPacket(job.getJobInstance().getLocation(), job.getLevel()));

            List<ItemRestriction> itemRestrictions = job.getJobInstance().getItemRestrictions()
                    .entrySet().stream()
                    .filter(entry -> entry.getValue() == job.getLevel())
                    .map(Map.Entry::getKey)
                    .toList();

            for (ItemRestriction itemRestriction : itemRestrictions) {
                NetworkManager.sendToPlayer(serverPlayer, new ClientboundUnlockItemRestrictionPacket(itemRestriction.getLocation()));
            }

            triggerLevelUpEffects(serverPlayer);

            player.jobsplus$addCoins(JobsPlusConfig.coinsPerLevelUp.get());
            JobInstance jobInstance = job.getJobInstance();
            serverPlayer.level().getServer().getPlayerList()
                    .broadcastSystemMessage(
                            JobsPlus.translatable("job.level_up",
                                    serverPlayer.getName().copy()
                                            .withStyle(style -> style
                                                    .withColor(jobInstance.getColorDecimal())
                                            ),
                                    JobsPlus.literal(String.valueOf(job.getLevel()))
                                            .withStyle(style -> style
                                                    .withColor(jobInstance.getColorDecimal())
                                            ),
                                    jobInstance.getName().getString()
                            ), false);
        }
    }

    public static void onJobExperience(JobsPlayer player, Job job, int experience) {
        if (player instanceof ArcPlayer arcPlayer) {
            new ActionDataBuilder(arcPlayer, JobsPlusActionType.JOB_EXP)
                    .withData(JobsPlusActionDataType.JOB_EXP, experience)
                    .withData(JobsPlusActionDataType.ONLY_FOR_JOB, job)
                    .build()
                    .sendToAction();
        }
    }

    private static void schedule(Runnable task, long delayInMillis) {
        scheduler.schedule(task, delayInMillis, TimeUnit.MILLISECONDS);
    }

    public static void triggerLevelUpEffects(ServerPlayer player) {

        // Play first sound after 250 ms (5 ticks)
        schedule(() -> {
            playLevelUpSound(player, 0.5F, 2F);
            playEXPOrbPickupSound(player);
        }, 250);

        // Play second sound after 450 ms (9 ticks)
        schedule(() -> {
            playLevelUpSound(player, 1F, 2F);
            playEXPOrbPickupSound(player);
        }, 450);

        // Play final sound after 550 ms (11 ticks)
        schedule(() -> {
            playLevelUpSound(player, 0.5F, 1.5F);
            playEXPOrbPickupSound(player);
        }, 550);
    }

    public static void playLevelUpSound(ServerPlayer player, float volume, float pitch) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, volume, pitch);
    }

    public static void playEXPOrbPickupSound(ServerPlayer player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 1F, 1F);
    }
}
