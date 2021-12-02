package me.daqem.jobsplus.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerTick {

    public static HashMap<Player, Integer> levelUpHashMap = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            if (!levelUpHashMap.isEmpty()) {
                int tick = levelUpHashMap.get(player);
                if (levelUpHashMap.containsKey(player)) {
                    if (tick == 0) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            serverPlayer.sendMessage(new KeybindComponent(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "LEVEL UP!"), ChatType.GAME_INFO, player.getUUID());
                        }
                        levelUpHashMap.put(player, tick + 1);
                    } else if (tick == 5) {
                        player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 0.5F, 2F);
                        player.level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 1F, 1F);
                        levelUpHashMap.put(player, tick + 1);
                    } else if (tick == 9) {
                        player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 1F, 2F);
                        player.level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 1F, 1F);
                        levelUpHashMap.put(player, tick + 1);
                    } else if (tick >= 11) {
                        player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, 0.5F, 1.5F);
                        player.level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 1F, 1F);
                        levelUpHashMap.remove(player);
                    } else {
                        levelUpHashMap.put(player, tick + 1);
                    }
                }
            }
        }
    }
}
