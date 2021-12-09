package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.mobeffect.FlyingModEffect;
import me.daqem.jobsplus.common.mobeffect.JesusModEffect;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.init.ModEffects;
import me.daqem.jobsplus.utils.enums.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
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
                            serverPlayer.sendMessage(new KeybindComponent(ChatColor.boldGreen() + "LEVEL UP!"), ChatType.GAME_INFO, player.getUUID());
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
