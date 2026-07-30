package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.sound.JobsPlusSoundManager;
import com.daqem.jobsplus.client.toast.LevelUpJobToast;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundLevelUpJobPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundLevelUpJobPacketHandler {

    public static void handle(@NotNull ClientboundLevelUpJobPacket packet, ClientboundContext context) {
        JobInstance jobInstance = JobInstance.of(packet.jobLocation());
        if (jobInstance == null) return;
        if (JobsPlusClientConfig.showYourLevelUpToastMessages.get()) {
            LevelUpJobToast.addOrUpdate(Minecraft.getInstance().gui.toastManager(), jobInstance, packet.level());
        }
        JobsPlusSoundManager.playLevelUpSound();
    }
}
