package com.daqem.jobsplus.client.networking;

import com.daqem.jobsplus.client.gui.hud.HudEditorScreen;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenHudEditorPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundOpenHudEditorPacketHandler {

    public static void handleClientSide(ClientboundOpenHudEditorPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().setScreen(new HudEditorScreen());
    }
}