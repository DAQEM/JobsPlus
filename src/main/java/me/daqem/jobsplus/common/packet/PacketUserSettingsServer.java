package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.init.ModPackets;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record PacketUserSettingsServer(String settingsType) {

    public static void encode(PacketUserSettingsServer msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.settingsType());
    }

    public static PacketUserSettingsServer decode(FriendlyByteBuf buf) {
        return new PacketUserSettingsServer(buf.readUtf());
    }

    public static void handle(PacketUserSettingsServer msg, Supplier<NetworkEvent.Context> context) {
        String settingsType = msg.settingsType();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (settingsType.contains("switch")) {
                    if (settingsType.equals("switch_hotbar_exp")) {
                        JobSetters.setEXPHotBarSetting(player, JobGetters.getEXPHotBarSetting(player) == 0 ? 1 : 0);
                        ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), "HOTBAR"));
                    }
                    if (settingsType.equals("switch_level_up_sound")) {
                        JobSetters.setLevelUpSoundSetting(player, JobGetters.getLevelUpSoundSetting(player) == 0 ? 1 : 0);
                        ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), "LEVEL_UP"));
                    }
                    if (settingsType.equals("switch_level_up_chat")) {
                        JobSetters.setLevelUpChatSetting(player, JobGetters.getLevelUpChatSetting(player) == 0 ? 1 : JobGetters.getLevelUpChatSetting(player) == 1 ? 2 : 0);
                        ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), "LEVEL_UP"));
                    }
                } else {
                    ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), settingsType));
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
