package me.daqem.jobsplus.packet;

import me.daqem.jobsplus.events.EventPlayerTick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record PacketUserSettingsServer(UUID uuid, String settingsType) {

    public static void encode(PacketUserSettingsServer msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.uuid());
        buf.writeUtf(msg.settingsType());
    }

    public static PacketUserSettingsServer decode(FriendlyByteBuf buf) {
        return new PacketUserSettingsServer(buf.readUUID(), buf.readUtf());
    }

    public static void handle(PacketUserSettingsServer msg, Supplier<NetworkEvent.Context> context) {
        UUID uuid = msg.uuid();
        String settingsType = msg.settingsType();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            if (settingsType.contains("switch")) {
                if (settingsType.equals("switch_hotbar_exp")) {
                    EventPlayerTick.setHotBarEXPSetting.add(uuid);
                }
                if (settingsType.equals("switch_level_up_sound")) {
                    EventPlayerTick.setLevelUpSound.add(uuid);
                }
                if (settingsType.equals("switch_level_up_chat")) {
                    EventPlayerTick.setLevelUpChat.add(uuid);
                }
            } else {
                EventPlayerTick.settingsMap.put(uuid, settingsType);
            }
        }
        context.get().setPacketHandled(true);
    }
}
