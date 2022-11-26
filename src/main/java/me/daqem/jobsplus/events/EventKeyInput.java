package me.daqem.jobsplus.events;

import me.daqem.jobsplus.SideProxy.Client;
import me.daqem.jobsplus.client.gui.JobsScreen;
import me.daqem.jobsplus.common.packet.PacketOpenMenu;
import me.daqem.jobsplus.init.ModPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventKeyInput {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyPress(InputEvent.Key event) {
        final int key = event.getKey();
        final int action = event.getAction();
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (key == Client.OPEN_GUI_KEYBIND.getKey().getValue() && action == 1) {
                Screen screen = Minecraft.getInstance().screen;
                if (screen instanceof JobsScreen) screen.onClose();
                if (screen == null)
                    ModPackets.INSTANCE.sendToServer(new PacketOpenMenu(-1, 0, 0, -1, 0, 0));
            }
            if (key == Client.VEIN_MINER_KEYBIND.getKey().getValue()) {
                if (action == 1) ModPackets.sendPowerUpPacket("enable_veinminer");
                if (action == 0) ModPackets.sendPowerUpPacket("disable_veinminer");
            }
            if (key == Client.DOUBLE_JUMP_KEYBIND.getKey().getValue() && action == 1 && DoubleJumpEvents.isPlayerAllowedToDoubleJump(player)) {
                ModPackets.sendPowerUpPacket("doublejump");
            }
        }
    }
}
