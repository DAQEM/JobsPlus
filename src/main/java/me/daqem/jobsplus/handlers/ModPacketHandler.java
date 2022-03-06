package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.packet.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            JobsPlus.getId("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(++id, PacketSendMainMenuData.class, PacketSendMainMenuData::encode, PacketSendMainMenuData::decode, PacketSendMainMenuData::handle);
        INSTANCE.registerMessage(++id, PacketPowerups.class, PacketPowerups::encode, PacketPowerups::decode, PacketPowerups::handle);
        INSTANCE.registerMessage(++id, PacketOpenMenu.class, PacketOpenMenu::encode, PacketOpenMenu::decode, PacketOpenMenu::handle);
        INSTANCE.registerMessage(++id, PacketUserSettingsServer.class, PacketUserSettingsServer::encode, PacketUserSettingsServer::decode, PacketUserSettingsServer::handle);
        INSTANCE.registerMessage(++id, PacketUserSettingsClient.class, PacketUserSettingsClient::encode, PacketUserSettingsClient::decode, PacketUserSettingsClient::handle);
    }

    public static void sendPowerupPacket(String str, LocalPlayer player) {
        ModPacketHandler.INSTANCE.sendToServer(new PacketPowerups(str, player.getUUID()));
    }
}
