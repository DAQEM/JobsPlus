package me.daqem.jobsplus.events;

import me.daqem.jobsplus.SideProxy;
import me.daqem.jobsplus.SideProxy.Client;
import me.daqem.jobsplus.client.gui.JobsScreen;
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
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (event.getKey() == Client.OPEN_GUI_KEYBIND.getKey().getValue()) {
            if (event.getAction() == 1) {
                Screen screen = Minecraft.getInstance().screen;
                if (screen instanceof JobsScreen) {
                    screen.onClose();
                }
                if (screen == null) {
                    if (Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.chat("/jobs menu -1 0 0 -1 0 0");
                    }
                }
            }
        }
        if (event.getKey() == Client.VEIN_MINER_KEYBIND.getKey().getValue()) {
            if (event.getAction() == 2 || event.getAction() == 1) {
                Minecraft.getInstance().player.chat("/sendjobspacket veinminer enable");
            }
            if (event.getAction() == 0) {
                Minecraft.getInstance().player.chat("/sendjobspacket veinminer disable");
            }
        }
        if (event.getKey() == SideProxy.Client.DOUBLE_JUMP_KEYBIND.getKey().getValue()) {
            if (event.getAction() == 1) {
                final LocalPlayer player = Minecraft.getInstance().player;
                if (player != null && player.isAlive() && DoubleJumpEvents.isPlayerInAir(player)) {
                    player.chat("/sendjobspacket doublejump");
                }
            }
        }
    }
}
