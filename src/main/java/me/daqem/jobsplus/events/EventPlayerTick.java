package me.daqem.jobsplus.events;

import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.utils.ChatColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerTick {

    public static HashMap<ServerPlayer, Integer> levelUpHashMap = new HashMap<>();
    public static HashMap<ServerPlayer, ArrayList<MobEffect>> removeEffect = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER) return;
        if (event.phase != TickEvent.Phase.START) return;
        if (levelUpHashMap.isEmpty() && removeEffect.isEmpty()) return;

        if (event.player instanceof ServerPlayer serverPlayer) {
            if (levelUpHashMap.containsKey(serverPlayer)) {
                int tick = levelUpHashMap.get(serverPlayer);
                levelUpHashMap.put(serverPlayer, tick + 1);
                if (tick == 0) {
                    HotbarMessageHandler.sendHotbarMessageServer(serverPlayer, ChatColor.boldGreen() + "LEVEL UP!");
                }
                if (tick == 5) {
                    SoundHandler.playLevelUpSound(serverPlayer, 0.5F, 2F);
                    SoundHandler.playEXPOrbPickupSound(serverPlayer);
                }
                if (tick == 9) {
                    SoundHandler.playLevelUpSound(serverPlayer, 1F, 2F);
                    SoundHandler.playEXPOrbPickupSound(serverPlayer);
                }
                if (tick >= 11) {
                    SoundHandler.playLevelUpSound(serverPlayer, 0.5F, 1.5F);
                    SoundHandler.playEXPOrbPickupSound(serverPlayer);
                    levelUpHashMap.remove(serverPlayer);
                }
            }
            if (removeEffect.containsKey(serverPlayer)) {
                removeEffect.get(serverPlayer).forEach(serverPlayer::removeEffect);
                removeEffect.remove(serverPlayer);
            }
        }
    }

    public static void addRemoveEffect(ServerPlayer player, MobEffect effect) {
        if (removeEffect.containsKey(player)) {
            removeEffect.get(player).add(effect);
        } else {
            removeEffect.put(player, new ArrayList<>(List.of(effect)));
        }
    }
}
