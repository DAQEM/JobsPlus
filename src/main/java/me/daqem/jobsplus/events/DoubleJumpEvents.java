package me.daqem.jobsplus.events;

import me.daqem.jobsplus.common.event.DoubleJumpEvent;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DoubleJumpEvents {

    private static final String DOUBLE_JUMP_TAG = "jobsplus:double_jump";
    private static final String JUMPS = "jobsplus.jumps";

    public static void attemptPlayerJump(Player player, int[] builderInfo) {
        if (isPlayerAllowedToDoubleJump(player)) {
            CompoundTag compound = getCompound(player);
            int jumps = compound.getInt(JUMPS);
            boolean hasSuperPowerEnabled = builderInfo[0] == 1;
            boolean hasPowerUpEnabled = builderInfo[1] == 1;

            if (jumps == 0 && (hasSuperPowerEnabled || hasPowerUpEnabled) || (jumps == 1 && hasSuperPowerEnabled)) {
                MinecraftForge.EVENT_BUS.post(new DoubleJumpEvent.MultiJump.Post(player));

                Vec3 vec3 = player.getDeltaMovement();
                player.setDeltaMovement(vec3.x, 0.5, vec3.z);
                if (player.isSprinting()) {
                    float f = player.getYRot() * ((float) Math.PI / 180F);
                    player.setDeltaMovement(player.getDeltaMovement().add(-Mth.sin(f) * 0.275F, 0.25D, Mth.cos(f) * 0.275F));
                }
                player.hurtMarked = true;
                compound.putInt(JUMPS, jumps + 1);
            }
        }
    }

    private static CompoundTag getCompound(Player player) {
        final CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(DOUBLE_JUMP_TAG)) persistentData.put(DOUBLE_JUMP_TAG, new CompoundTag());
        return persistentData.getCompound(DOUBLE_JUMP_TAG);
    }

    public static boolean isPlayerAllowedToDoubleJump(Player player) {
        return player.isAlive() && !player.isOnGround() && !player.isSwimming() && !player.isVisuallySwimming() && !player.isSleeping() && !player.isPassenger() && !player.getAbilities().mayfly && !player.isInWater() && !player.isInLava() && !player.isInPowderSnow && !player.isInWall();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void multiJump(DoubleJumpEvent.MultiJump.Post event) {
        Player player = event.getEntity();
        if (event.getEntity().getLevel() instanceof ServerLevel serverWorld && player instanceof ServerPlayer serverPlayer) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    serverWorld.sendParticles(serverPlayer, ParticleTypes.CLOUD, false, player.getX() + (x < 0 ? x + 0.65 : x > 0 ? x - 0.65 : 0), player.getY(), player.getZ() + (z < 0 ? z + 0.65 : z > 0 ? z - 0.65 : 0), 1, 0, 0, 0, 0);
                }
            }
            player.level.playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.4F, 0.75F);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getLevel().isClientSide) return;
            if (JobGetters.hasSuperPowerEnabled(player, Jobs.BUILDER)) {
                event.setCanceled(true);
            } else if (JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWER_UP3.get())) {
                event.setDistance(event.getDistance() - 3F);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        final Player player = event.player;
        final CompoundTag compound = getCompound(player);

        if (!isPlayerAllowedToDoubleJump(player)) {
            int jumps = compound.getInt(JUMPS);
            if (jumps != 0) {
                compound.putInt(JUMPS, 0);
            }
        }
    }
}
