package me.daqem.jobsplus.common.packet;

import me.daqem.jobsplus.client.gui.SettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketUserSettingsClient(int[] array, String settingsType) {

    public static void encode(PacketUserSettingsClient msg, FriendlyByteBuf buf) {
        buf.writeVarIntArray(msg.array());
        buf.writeUtf(msg.settingsType());
    }

    public static PacketUserSettingsClient decode(FriendlyByteBuf buf) {
        return new PacketUserSettingsClient(buf.readVarIntArray(), buf.readUtf());
    }

    public static void handle(PacketUserSettingsClient msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> clientWork(msg));
            context.get().setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientWork(PacketUserSettingsClient msg) {
        int[] array = msg.array();
        String settingsType = msg.settingsType();
        Minecraft.getInstance().setScreen(new SettingsScreen(array, SettingsScreen.SettingsType.valueOf(settingsType)));
    }
}
