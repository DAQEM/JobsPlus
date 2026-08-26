package com.daqem.jobsplus.client.sound;

import com.daqem.jobsplus.config.JobsPlusClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobsPlusSoundManager {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("JobsPlus-ClientSound-Scheduler");
        return thread;
    });

    public static void playLevelUpSound() {
        if (!JobsPlusClientConfig.playLevelUpSound.get()) return;

        float volumeMultiplier = JobsPlusClientConfig.soundVolume.get().floatValue();
        if (volumeMultiplier <= 0) return;

        schedule(() -> {
            play(SoundEvents.PLAYER_LEVELUP, 0.5F, 2F, volumeMultiplier);
            play(SoundEvents.EXPERIENCE_ORB_PICKUP, 1F, 1F, volumeMultiplier);
        }, 250);

        schedule(() -> {
            play(SoundEvents.PLAYER_LEVELUP, 1F, 2F, volumeMultiplier);
            play(SoundEvents.EXPERIENCE_ORB_PICKUP, 1F, 1F, volumeMultiplier);
        }, 450);

        schedule(() -> {
            play(SoundEvents.PLAYER_LEVELUP, 0.5F, 1.5F, volumeMultiplier);
            play(SoundEvents.EXPERIENCE_ORB_PICKUP, 1F, 1F, volumeMultiplier);
        }, 550);
    }

    private static void schedule(Runnable task, long delayInMillis) {
        scheduler.schedule(() -> {
            Minecraft.getInstance().execute(task);
        }, delayInMillis, TimeUnit.MILLISECONDS);
    }

    private static void play(SoundEvent sound, float volume, float pitch, float configMultiplier) {
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(
                        sound,
                        pitch,
                        volume * configMultiplier
                )
        );
    }
}