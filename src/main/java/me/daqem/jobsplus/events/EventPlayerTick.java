package me.daqem.jobsplus.events;

import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerTick {

    public static HashMap<Player, Integer> levelUpHashMap = new HashMap<>();
    public static ArrayList<UUID> doublejumpList = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if (doublejumpList.contains(player.getUUID())) {
                DoubleJumpEvents.attemptPlayerJump(player, new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER) ? 1 : 0, JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWERUP3.get()) ? 1 : 0});
                doublejumpList.remove(player.getUUID());
            }

            if (event.phase == TickEvent.Phase.START) {
                if (!levelUpHashMap.isEmpty()) {
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
}
