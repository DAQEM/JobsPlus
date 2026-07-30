package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.toast.PowerupUnlockedToast;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockPowerupPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundUnlockPowerupPacketHandler {

    public static void handle(@NotNull ClientboundUnlockPowerupPacket packet, ClientboundContext context) {
        PowerupInstance powerupInstance = PowerupInstance.of(packet.powerupLocation());
        if (powerupInstance == null) return;
        if (JobsPlusClientConfig.showPowerupUnlockToastMessage.get()) {
            PowerupUnlockedToast.addOrUpdate(Minecraft.getInstance().gui.toastManager(), powerupInstance);
        }
    }
}
