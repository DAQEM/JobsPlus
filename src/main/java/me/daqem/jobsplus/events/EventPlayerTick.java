package me.daqem.jobsplus.events;

import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.packet.PacketSendMainMenuData;
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
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventPlayerTick {

    public static HashMap<Player, Integer> levelUpHashMap = new HashMap<>();
    public static ArrayList<UUID> doubleJumpList = new ArrayList<>();
    public static Map<UUID, Map<Float, int[]>> openMenuMap = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if (doubleJumpList.contains(player.getUUID())) {
                DoubleJumpEvents.attemptPlayerJump(player, new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER) ? 1 : 0, JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWERUP3.get()) ? 1 : 0});
                doubleJumpList.remove(player.getUUID());
            }

            if (openMenuMap.containsKey(player.getUUID())) {
                AtomicReference<Float> scrollOffset = new AtomicReference<>((float) 0);
                final Map<Float, int[]> map = openMenuMap.get(player.getUUID());
                map.forEach((key, value) -> scrollOffset.set(key));
                int[] array = map.get(scrollOffset.get());
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketSendMainMenuData(new int[]{
                        JobGetters.getJobLevel(player, Jobs.ALCHEMIST), JobGetters.getJobEXP(player, Jobs.ALCHEMIST),
                        JobGetters.getJobLevel(player, Jobs.BUILDER), JobGetters.getJobEXP(player, Jobs.BUILDER),
                        JobGetters.getJobLevel(player, Jobs.DIGGER), JobGetters.getJobEXP(player, Jobs.DIGGER),
                        JobGetters.getJobLevel(player, Jobs.ENCHANTER), JobGetters.getJobEXP(player, Jobs.ENCHANTER),
                        JobGetters.getJobLevel(player, Jobs.FARMER), JobGetters.getJobEXP(player, Jobs.FARMER),
                        JobGetters.getJobLevel(player, Jobs.FISHERMAN), JobGetters.getJobEXP(player, Jobs.FISHERMAN),
                        JobGetters.getJobLevel(player, Jobs.HUNTER), JobGetters.getJobEXP(player, Jobs.HUNTER),
                        JobGetters.getJobLevel(player, Jobs.LUMBERJACK), JobGetters.getJobEXP(player, Jobs.LUMBERJACK),
                        JobGetters.getJobLevel(player, Jobs.MINER), JobGetters.getJobEXP(player, Jobs.MINER),
                        JobGetters.getJobLevel(player, Jobs.SMITH), JobGetters.getJobEXP(player, Jobs.SMITH),
                        JobGetters.getCoins(player), JobGetters.getAmountOfEnabledJobs(player),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWERUP3.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWERUP1.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWERUP2.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWERUP3.get()),
                        JobGetters.getSuperPower(player, Jobs.ALCHEMIST), JobGetters.getSuperPower(player, Jobs.BUILDER),
                        JobGetters.getSuperPower(player, Jobs.DIGGER), JobGetters.getSuperPower(player, Jobs.ENCHANTER),
                        JobGetters.getSuperPower(player, Jobs.FARMER), JobGetters.getSuperPower(player, Jobs.FISHERMAN),
                        JobGetters.getSuperPower(player, Jobs.HUNTER), JobGetters.getSuperPower(player, Jobs.LUMBERJACK),
                        JobGetters.getSuperPower(player, Jobs.MINER), JobGetters.getSuperPower(player, Jobs.SMITH),
                        JobGetters.getDisplay(player), JobGetters.getActiveBossBar(player)
                }, array[0], array[1], array[2], array[3], scrollOffset.get(), array[4]));
                openMenuMap.remove(player.getUUID());
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
