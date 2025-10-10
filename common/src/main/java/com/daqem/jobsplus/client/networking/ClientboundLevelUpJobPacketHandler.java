package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.toast.LevelUpJobToast;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.s2c.ClientboundLevelUpJobPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundLevelUpJobPacketHandler {

    public static void handleClientSide(ClientboundLevelUpJobPacket packet, NetworkManager.PacketContext context) {
        LevelUpJobToast.addOrUpdate(Minecraft.getInstance().getToastManager(), JobInstance.of(packet.getJobLocation()), packet.getLevel());
    }
}
