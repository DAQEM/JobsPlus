package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.packet.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {

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
        INSTANCE.registerMessage(++id, PacketPowerUps.class, PacketPowerUps::encode, PacketPowerUps::decode, PacketPowerUps::handle);
        INSTANCE.registerMessage(++id, PacketOpenMenu.class, PacketOpenMenu::encode, PacketOpenMenu::decode, PacketOpenMenu::handle);
        INSTANCE.registerMessage(++id, PacketUserSettingsServer.class, PacketUserSettingsServer::encode, PacketUserSettingsServer::decode, PacketUserSettingsServer::handle);
        INSTANCE.registerMessage(++id, PacketUserSettingsClient.class, PacketUserSettingsClient::encode, PacketUserSettingsClient::decode, PacketUserSettingsClient::handle);
        INSTANCE.registerMessage(++id, PacketJobDisplay.class, PacketJobDisplay::encode, PacketJobDisplay::decode, PacketJobDisplay::handle);
        INSTANCE.registerMessage(++id, PacketJobStartStop.class, PacketJobStartStop::encode, PacketJobStartStop::decode, PacketJobStartStop::handle);
        INSTANCE.registerMessage(++id, PacketBossBarr.class, PacketBossBarr::encode, PacketBossBarr::decode, PacketBossBarr::handle);
        INSTANCE.registerMessage(++id, PacketSwitchSuperpower.class, PacketSwitchSuperpower::encode, PacketSwitchSuperpower::decode, PacketSwitchSuperpower::handle);
        INSTANCE.registerMessage(++id, PacketMenuPowerUp.class, PacketMenuPowerUp::encode, PacketMenuPowerUp::decode, PacketMenuPowerUp::handle);
        INSTANCE.registerMessage(++id, PacketMoveConstructionRecipe.class, PacketMoveConstructionRecipe::encode, PacketMoveConstructionRecipe::decode, PacketMoveConstructionRecipe::handle);
    }

    public static void sendPowerUpPacket(String str, LocalPlayer player) {
        ModPackets.INSTANCE.sendToServer(new PacketPowerUps(str, player.getUUID()));
    }
}
