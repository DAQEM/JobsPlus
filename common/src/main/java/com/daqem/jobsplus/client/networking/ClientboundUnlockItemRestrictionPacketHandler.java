package com.daqem.jobsplus.client.networking;

import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.itemrestrictions.data.ItemRestrictionManager;
import com.daqem.jobsplus.client.toast.ItemRestrictionUnlockedToast;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockItemRestrictionPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundUnlockItemRestrictionPacketHandler {

    public static void handle(@NotNull ClientboundUnlockItemRestrictionPacket packet, ClientboundContext context) {
        ItemRestriction itemRestriction = ItemRestrictionManager.getInstance().getItemRestriction(packet.itemRestrictionLocation());
        if (itemRestriction == null) return;
        if (JobsPlusClientConfig.showRestrictionUnlockToastMessage.get()) {
            ItemRestrictionUnlockedToast.addOrUpdate(Minecraft.getInstance().getToastManager(), itemRestriction);
        }
    }
}
