package me.daqem.jobsplus.events;

import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModEffects;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber
public class PotionEvents {

    @SubscribeEvent
    public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
        MobEffectInstance effect = event.getPotionEffect();
        if (event.getEntityLiving() instanceof Player player) {
            Jobs job = Jobs.ALCHEMIST;
            if (JobGetters.jobIsEnabled(player, job)) {
                int duration;
                if (JobGetters.hasSuperPowerEnabled(player, job)) {
                    duration = JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get()) ? effect.getDuration() * 3 : effect.getDuration() * 2;
                } else {
                    duration = JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP2.get()) ? (int) (effect.getDuration() * 1.5) : 0;
                }
                if (duration != 0) {
                    MinecraftForge.EVENT_BUS.register(new Object() {
                        int delay = 1;

                        @SubscribeEvent
                        public void onTick(TickEvent.WorldTickEvent event) {
                            if (delay-- > 0) return;
                            player.forceAddEffect(new MobEffectInstance(effect.getEffect(), duration,
                                    effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()), null);
                            MinecraftForge.EVENT_BUS.unregister(this);
                        }
                    });
                }
                if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP1.get())) {
                    if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        MinecraftForge.EVENT_BUS.register(new Object() {
                            int delay = 1;

                            @SubscribeEvent
                            public void onTick(TickEvent.WorldTickEvent event) {
                                if (delay-- > 0) return;
                                player.removeEffect(effect.getEffect());
                                MinecraftForge.EVENT_BUS.unregister(this);
                            }
                        });
                        if (player instanceof ServerPlayer serverPlayer) {
                            HotbarMessageHandler.sendHotbarMessage(serverPlayer, ChatColor.green() + "Removed bad effect.");
                        }
                    }
                }
            }
            Map<MobEffect, List<List<Integer>>> map = new HashMap<>(Map.of(
                    MobEffects.DIG_SPEED, List.of(List.of(0, 5, 2400), List.of(0, 10, 6000), List.of(1, 20, 2400), List.of(1, 50, 6000)),
                    MobEffects.REGENERATION, List.of(List.of(1, 20, 1200), List.of(1, 50, 2400), List.of(2, 75, 1200)),
                    MobEffects.MOVEMENT_SPEED, List.of(List.of(2, 5, 2400), List.of(2, 10, 6000), List.of(3, 35, 2400), List.of(3, 75, 6000)),
                    MobEffects.DAMAGE_BOOST, List.of(List.of(2, 10, 2400), List.of(2, 20, 6000), List.of(3, 75, 2400), List.of(3, 100, 6000)),
                    MobEffects.LUCK, List.of(List.of(0, 35, 2400), List.of(0, 50, 6000), List.of(1, 75, 2400), List.of(1, 100, 6000)),
                    ModEffects.JESUS.get(), List.of(List.of(0, 35, 2400), List.of(0, 75, 6000)),
                    ModEffects.FLYING.get(), List.of(List.of(0, 50, 6000), List.of(0, 100, 12000))));

            AtomicBoolean pass = new AtomicBoolean(true);
            if (map.containsKey(effect.getEffect())) {
                map.forEach((key, value) -> {
                    if (key == effect.getEffect()) {
                        for (List<Integer> list : value) {
                            if (list.get(0) == effect.getAmplifier()) {
                                pass.set(false);
                                if (JobGetters.getJobLevel(player, job) >= list.get(1)) {
                                    if (list.get(2) >= effect.getDuration()) {
                                        pass.set(true);
                                        return;
                                    }
                                } else {
                                    if (effect.getDuration() <= 310) {
                                        pass.set(true);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                });
            }
            if (!pass.get()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    HotbarMessageHandler.sendHotbarMessage(serverPlayer, ChatColor.red() + "You are not allowed to use this effect yet.");
                    MinecraftForge.EVENT_BUS.register(new Object() {
                        int delay = 1;

                        @SubscribeEvent
                        public void onTick(TickEvent.WorldTickEvent event) {
                            if (delay-- > 0) return;
                            player.removeEffect(effect.getEffect());
                            MinecraftForge.EVENT_BUS.unregister(this);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            Jobs job = Jobs.ALCHEMIST;
            if (JobGetters.jobIsEnabled(player, job)) {
                if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP3.get())) {
                    if (event.getAmount() > 1) {
                        event.setAmount(event.getAmount() * 2);
                    }
                }
            }
        }
    }

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
    }

    @SubscribeEvent
    public static void doJesus(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.hasEffect(ModEffects.JESUS.get())) {
            if (Objects.equals(Objects.requireNonNull(player.level.getBlockState(new BlockPos(player.getX(), player.getBlockY(), player.getZ())).getBlock().getRegistryName()).toString(), "minecraft:water") &&
                    player.level.getBlockState(new BlockPos(player.getX(), player.getBlockY() + 1, player.getZ())).isAir()) {
                Vec3 vec3 = player.getDeltaMovement();
                player.setDeltaMovement(vec3.x, 0.05D, vec3.z);
                if (player.isFallFlying()) {
                    player.setDeltaMovement(vec3.x, vec3.y, vec3.z);
                }
                player.flyingSpeed = 0.05F;
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

    @SubscribeEvent
    public static void removeHarmingEffect(LivingDamageEvent event) {
        Jobs job = Jobs.ALCHEMIST;
        if (event.getEntityLiving() instanceof ServerPlayer player) {
            if (JobGetters.jobIsEnabled(player, job)) {
                if (JobGetters.hasEnabledPowerup(player, job, CapType.POWER_UP1.get())) {
                    if (event.getSource().getLocalizedDeathMessage(event.getEntityLiving()).toString().contains("death.attack.indirectMagic")) {
                        event.setCanceled(true);
                        HotbarMessageHandler.sendHotbarMessage(player, ChatColor.green() + "Removed bad effect.");
                    }
                }
            }
        }
    }
}
