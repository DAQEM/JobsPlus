package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.sound.JobsPlusSoundManager;
import com.daqem.jobsplus.client.toast.LevelUpJobToast;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundLevelUpJobPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundLevelUpJobPacketHandler {

    public static void handleClientSide(ClientboundLevelUpJobPacket packet, NetworkManager.PacketContext context) {
        JobInstance jobInstance = JobInstance.of(packet.getJobLocation());
        if (jobInstance == null) return;
        if (JobsPlusClientConfig.showYourLevelUpToastMessages.get()) {
            LevelUpJobToast.addOrUpdate(Minecraft.getInstance().getToastManager(), jobInstance, packet.getLevel());
        }
        JobsPlusSoundManager.playLevelUpSound();
    }
}
