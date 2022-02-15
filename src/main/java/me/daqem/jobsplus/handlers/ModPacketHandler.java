package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.packet.PacketPowerups;
import me.daqem.jobsplus.packet.PacketSendMainMenuData;
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
    }

    public static void sendPowerupPacket(String str, LocalPlayer player) {
        ModPacketHandler.INSTANCE.sendToServer(new PacketPowerups(str, player.getUUID()));
    }
}