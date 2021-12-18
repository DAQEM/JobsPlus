package me.daqem.jobsplus.events;

import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.utils.enums.ChatColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerTick {

    public static HashMap<Player, Integer> levelUpHashMap = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            if (!levelUpHashMap.isEmpty()) {
                Player player = event.player;
                if (player instanceof ServerPlayer serverPlayer) {
                    if (levelUpHashMap.containsKey(player)) {
                        int tick = levelUpHashMap.get(player);
                        levelUpHashMap.put(player, tick + 1);
                        if (tick == 0) {
                            HotbarMessageHandler.sendHotbarMessage(serverPlayer, ChatColor.boldGreen() + "LEVEL UP!");
                        }
                        if (tick == 5) {
                            SoundHandler.playLevelUpSound(player, 0.5F, 2F);
                            SoundHandler.playEXPOrbPickupSound(player, 1F, 1F);
                        }
                        if (tick == 9) {
                            SoundHandler.playLevelUpSound(player, 1F, 2F);
                            SoundHandler.playEXPOrbPickupSound(player, 1F, 1F);
                        }
                        if (tick >= 11) {
                            SoundHandler.playLevelUpSound(player, 0.5F, 1.5F);
                            SoundHandler.playEXPOrbPickupSound(player, 1F, 1F);
                            levelUpHashMap.remove(player);
                        }
                    }
                }
            }
        }
    }
}
