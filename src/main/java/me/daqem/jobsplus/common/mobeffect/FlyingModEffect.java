package me.daqem.jobsplus.common.mobeffect;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.init.ModEffects;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class FlyingModEffect extends MobEffect {

    public static HashMap<UUID, Boolean> array = new HashMap<>();

    public FlyingModEffect() {
        super(MobEffectCategory.BENEFICIAL, 4183275);
    }

    public static void performEffectClient(Player player) {
        Abilities abilities = player.getAbilities();
        if (player instanceof ServerPlayer) {
            if (player.hasEffect(ModEffects.FLYING.get())) {
                if (!array.containsKey(player.getUUID())) {
                    array.put(player.getUUID(), true);
                }
                abilities.mayfly = true;
            } else {
                if (array.containsKey(player.getUUID())) {
                    abilities.mayfly = false;
                    abilities.flying = false;
                    array.put(player.getUUID(), false);
                    ((ServerPlayer) player).gameMode.getGameModeForPlayer().updatePlayerAbilities(player.getAbilities());
                }
            }
        }
        if (player instanceof LocalPlayer) {
            if (array.get(player.getUUID()) != null) {
                if (!array.get(player.getUUID())) {
                    if (!player.isCreative()) {
                        abilities.mayfly = false;
                        abilities.flying = false;
                    }
                    array.remove(player.getUUID());
                } else {
                    abilities.mayfly = true;
                }
            }
        }
    }

    public static void performEffectServer(ServerPlayer player) {
        Abilities abilities = player.getAbilities();
        JobsPlus.LOGGER.info(abilities.mayfly);
        if (player.hasEffect(ModEffects.FLYING.get())) {
            JobsPlus.LOGGER.info("1");
            if (!array.containsKey(player.getUUID())) {
                array.put(player.getUUID(), true);
            }
            abilities.mayfly = true;
        } else {
            if (array.containsKey(player.getUUID())) {
                JobsPlus.LOGGER.info("2");
                abilities.mayfly = false;
                abilities.flying = false;
                array.put(player.getUUID(), false);
                player.gameMode.getGameModeForPlayer().updatePlayerAbilities(player.getAbilities());
            }
        }
        JobsPlus.LOGGER.info(array);
    }
}
