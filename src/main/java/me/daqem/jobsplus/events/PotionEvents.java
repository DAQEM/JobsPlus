package me.daqem.jobsplus.events;

import me.daqem.jobsplus.init.ModEffects;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PotionEvents {

    @SubscribeEvent
    public static void doFlying(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        CompoundTag tag = player.getPersistentData();
        boolean mayfly = tag.getBoolean("mayfly");
        if (!player.isCreative() && !player.isSpectator()) {
            if (player.hasEffect(ModEffects.FLYING.get()) && !player.hasEffect(MobEffects.LEVITATION)) {
                if (!(player.getAbilities()).mayfly) {
                    (player.getAbilities()).mayfly = true;
                    tag.putBoolean("mayfly", true);
                    player.onUpdateAbilities();
                }
            } else if ((mayfly && !player.hasEffect((ModEffects.FLYING.get())) || player.hasEffect(MobEffects.LEVITATION))) {
                (player.getAbilities()).mayfly = false;
                (player.getAbilities()).flying = false;
                tag.putBoolean("mayfly", false);
                player.onUpdateAbilities();
            }
        }
        //TODO Rewrite this with ArrayList and separate method.
        Jobs job = Jobs.ALCHEMIST;
        if (!player.getActiveEffects().isEmpty() && JobGetters.jobIsEnabled(player, job)) {
            MobEffect notAllowedEffect = null;
            for (MobEffectInstance activeEffect : player.getActiveEffects()) {
                if (activeEffect.getEffect() == MobEffects.DIG_SPEED) {
                    if (activeEffect.getAmplifier() == 0) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 5)) notAllowedEffect = MobEffects.DIG_SPEED;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 10)) notAllowedEffect = MobEffects.DIG_SPEED;
                        }
                    }
                    if (activeEffect.getAmplifier() == 1) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 20)) notAllowedEffect = MobEffects.DIG_SPEED;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 50)) notAllowedEffect = MobEffects.DIG_SPEED;
                        }
                    }
                }
                if (activeEffect.getEffect() == MobEffects.REGENERATION) {
                    if (activeEffect.getAmplifier() == 1) {
                        if (activeEffect.getDuration() <= 1200) {
                            if (!(JobGetters.getJobLevel(player, job) >= 20)) notAllowedEffect = MobEffects.REGENERATION;
                        }
                        if (activeEffect.getDuration() <= 2400 && activeEffect.getDuration() >= 1200) {
                            if (!(JobGetters.getJobLevel(player, job) >= 75)) notAllowedEffect = MobEffects.REGENERATION;
                        }
                    }
                    if (activeEffect.getAmplifier() == 2) {
                        if (activeEffect.getDuration() <= 1200) {
                            if (!(JobGetters.getJobLevel(player, job) >= 75))
                                notAllowedEffect = MobEffects.REGENERATION;
                        }
                    }
                }
                if (activeEffect.getEffect() == MobEffects.MOVEMENT_SPEED) {
                    if (activeEffect.getAmplifier() == 2) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 5)) notAllowedEffect = MobEffects.MOVEMENT_SPEED;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 10)) notAllowedEffect = MobEffects.MOVEMENT_SPEED;
                        }
                    }
                    if (activeEffect.getAmplifier() == 3) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 35)) notAllowedEffect = MobEffects.MOVEMENT_SPEED;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 75)) notAllowedEffect = MobEffects.MOVEMENT_SPEED;
                        }
                    }
                }
                if (activeEffect.getEffect() == MobEffects.DAMAGE_BOOST) {
                    if (activeEffect.getAmplifier() == 2) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 10)) notAllowedEffect = MobEffects.DAMAGE_BOOST;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 20)) notAllowedEffect = MobEffects.DAMAGE_BOOST;
                        }
                    }
                    if (activeEffect.getAmplifier() == 3) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 75)) notAllowedEffect = MobEffects.DAMAGE_BOOST;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 100)) notAllowedEffect = MobEffects.DAMAGE_BOOST;
                        }
                    }
                }
                if (activeEffect.getEffect() == MobEffects.LUCK) {
                    if (activeEffect.getAmplifier() == 0) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 35)) notAllowedEffect = MobEffects.LUCK;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 50)) notAllowedEffect = MobEffects.LUCK;
                        }
                    }
                    if (activeEffect.getAmplifier() == 1) {
                        if (activeEffect.getDuration() <= 2400) {
                            if (!(JobGetters.getJobLevel(player, job) >= 75)) notAllowedEffect = MobEffects.LUCK;
                        } else {
                            if (!(JobGetters.getJobLevel(player, job) >= 100)) notAllowedEffect = MobEffects.LUCK;
                        }
                    }
                }
                if (activeEffect.getEffect() == ModEffects.JESUS.get()) {
                    if (activeEffect.getDuration() <= 2400) {
                        if (!(JobGetters.getJobLevel(player, job) >= 35)) notAllowedEffect = ModEffects.JESUS.get();
                    } else {
                        if (!(JobGetters.getJobLevel(player, job) >= 75)) notAllowedEffect = ModEffects.JESUS.get();
                    }
                }
                if (activeEffect.getEffect() == ModEffects.FLYING.get()) {
                    if (activeEffect.getDuration() <= 6000) {
                        if (!(JobGetters.getJobLevel(player, job) >= 50)) notAllowedEffect = ModEffects.JESUS.get();
                    } else {
                        if (!(JobGetters.getJobLevel(player, job) >= 100)) notAllowedEffect = ModEffects.JESUS.get();
                    }
                }
            }
            if (notAllowedEffect != null) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendMessage(new KeybindComponent(ChatColor.boldRed() + "You are not allowed to use this effect yet."), ChatType.GAME_INFO, player.getUUID());
                }
                player.removeEffect(notAllowedEffect);
            }
        }
    }

    @SubscribeEvent
    public static void addNBTData(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        CompoundTag tag = player.getPersistentData();
        Tag mayfly = tag.get("mayfly");
        if (mayfly == null)
            tag.putBoolean("mayfly", false);
    }
}
