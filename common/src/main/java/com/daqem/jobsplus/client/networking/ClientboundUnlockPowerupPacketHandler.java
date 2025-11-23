package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.toast.PowerupUnlockedToast;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockPowerupPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundUnlockPowerupPacketHandler {

    public static void handleClientSide(ClientboundUnlockPowerupPacket packet, NetworkManager.PacketContext context) {
        PowerupInstance powerupInstance = PowerupInstance.of(packet.getPowerupLocation());
        if (powerupInstance == null) return;
        if (JobsPlusClientConfig.showPowerupUnlockToastMessage.get()) {
            PowerupUnlockedToast.addOrUpdate(Minecraft.getInstance().getToastManager(), powerupInstance);
        }
    }
}
