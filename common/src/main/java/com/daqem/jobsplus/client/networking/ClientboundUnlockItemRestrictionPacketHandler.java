package com.daqem.jobsplus.client.networking;

import com.daqem.itemrestrictions.data.ItemRestrictionManager;
import com.daqem.jobsplus.client.toast.ItemRestrictionUnlockedToast;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockItemRestrictionPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundUnlockItemRestrictionPacketHandler {

    public static void handleClientSide(ClientboundUnlockItemRestrictionPacket packet, NetworkManager.PacketContext context) {
        ItemRestrictionUnlockedToast.addOrUpdate(Minecraft.getInstance().getToastManager(), ItemRestrictionManager.getInstance().getItemRestriction(packet.getItemRestrictionLocation()));
    }
}
