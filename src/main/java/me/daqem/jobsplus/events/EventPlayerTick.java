package me.daqem.jobsplus.events;

import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.packet.PacketSendMainMenuData;
import me.daqem.jobsplus.packet.PacketUserSettingsClient;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.JobSetters;
import me.daqem.jobsplus.utils.enums.CapType;
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
    public static Map<UUID, String> settingsMap = new HashMap<>();

    public static ArrayList<UUID> setHotBarEXPSetting = new ArrayList<>();
    public static ArrayList<UUID> setLevelUpSound = new ArrayList<>();
    public static ArrayList<UUID> setLevelUpChat = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            Player player = event.player;
            final UUID uuid = player.getUUID();

            if (doubleJumpList.contains(uuid)) {
                DoubleJumpEvents.attemptPlayerJump(player, new int[]{JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER) ? 1 : 0, JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWER_UP3.get()) ? 1 : 0});
                doubleJumpList.remove(uuid);
            }

            if (settingsMap.containsKey(uuid)) {
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), settingsMap.get(uuid)));
                settingsMap.remove(uuid);
            }

            if (setHotBarEXPSetting.contains(uuid)) {
                JobSetters.setEXPHotBarSetting(player, JobGetters.getEXPHotBarSetting(player) == 0 ? 1 : 0);
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), "HOTBAR"));
                setHotBarEXPSetting.remove(uuid);
            }

            if (setLevelUpSound.contains(uuid)) {
                JobSetters.setLevelUpSoundSetting(player, JobGetters.getLevelUpSoundSetting(player) == 0 ? 1 : 0);
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), "LEVEL_UP"));
                setLevelUpSound.remove(uuid);
            }

            if (setLevelUpChat.contains(uuid)) {
                JobSetters.setLevelUpChatSetting(player, JobGetters.getLevelUpChatSetting(player) == 0 ? 1 : JobGetters.getLevelUpChatSetting(player) == 1 ? 2 : 0);
                ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new PacketUserSettingsClient(JobGetters.getAllSettings(player), "LEVEL_UP"));
                setLevelUpChat.remove(uuid);
            }

            if (openMenuMap.containsKey(uuid)) {
                AtomicReference<Float> scrollOffset = new AtomicReference<>((float) 0);
                final Map<Float, int[]> map = openMenuMap.get(uuid);
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
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.ALCHEMIST, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.BUILDER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.DIGGER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.ENCHANTER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.FARMER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.FISHERMAN, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.HUNTER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.LUMBERJACK, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.MINER, CapType.POWER_UP3.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWER_UP1.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWER_UP2.get()),
                        JobGetters.getPowerup(player, Jobs.SMITH, CapType.POWER_UP3.get()),
                        JobGetters.getSuperPower(player, Jobs.ALCHEMIST), JobGetters.getSuperPower(player, Jobs.BUILDER),
                        JobGetters.getSuperPower(player, Jobs.DIGGER), JobGetters.getSuperPower(player, Jobs.ENCHANTER),
                        JobGetters.getSuperPower(player, Jobs.FARMER), JobGetters.getSuperPower(player, Jobs.FISHERMAN),
                        JobGetters.getSuperPower(player, Jobs.HUNTER), JobGetters.getSuperPower(player, Jobs.LUMBERJACK),
                        JobGetters.getSuperPower(player, Jobs.MINER), JobGetters.getSuperPower(player, Jobs.SMITH),
                        JobGetters.getDisplay(player), JobGetters.getActiveBossBar(player)
                }, array[0], array[1], array[2], array[3], scrollOffset.get(), array[4]));
                openMenuMap.remove(uuid);
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
