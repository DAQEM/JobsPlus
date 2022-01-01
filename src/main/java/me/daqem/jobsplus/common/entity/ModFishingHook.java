package me.daqem.jobsplus.common.entity;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.item.RodItem;
import me.daqem.jobsplus.init.ModEntities;
import me.daqem.jobsplus.init.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ModFishingHook extends FishingHook {

    public static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(ModFishingHook.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(ModFishingHook.class, EntityDataSerializers.BOOLEAN);
    public final Random syncronizedRandom = new Random();
    public final int luck;
    public final int lureSpeed;
    public boolean openWater = true;
    public ModFishingHook.FishHookState currentState = ModFishingHook.FishHookState.FLYING;
    public boolean biting;
    public int outOfWaterTime;
    public int life;
    public int nibble;
    public int timeUntilLured;
    public int timeUntilHooked;
    public float fishAngle;
    @Nullable
    public Entity hookedIn;

    public ModFishingHook(EntityType<? extends ModFishingHook> entityType, Level level, int luck, int lureSpeed) {
        super(entityType, level);
        this.noCulling = true;
        this.luck = Math.max(0, luck);
        this.lureSpeed = Math.max(0, lureSpeed);
    }

    public ModFishingHook(Player player, Level level, int luck, int lure) {
        this(ModEntities.FISHING_BOBBER.get(), level, luck, lure);
        this.setOwner(player);
        float f = player.getXRot();
        float f1 = player.getYRot();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        double d0 = player.getX() - (double) f3 * 0.3D;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double) f2 * 0.3D;
        this.moveTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0F, 5.0F), -f2);
        double d3 = vec3.length();
        vec3 = vec3.multiply(0.6D / d3 + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / d3 + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / d3 + 0.5D + this.random.nextGaussian() * 0.0045D);
        this.setDeltaMovement(vec3);
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vec3.y, vec3.horizontalDistance()) * (double) (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public ModFishingHook(EntityType<ModFishingHook> customFishingHookEntityType, Level level) {
        this(customFishingHookEntityType, level, 0, 0);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_HOOKED_ENTITY, 0);
        this.getEntityData().define(DATA_BITING, false);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> entityDataAccessor) {
        if (DATA_HOOKED_ENTITY.equals(entityDataAccessor)) {
            int i = this.getEntityData().get(DATA_HOOKED_ENTITY);
            this.hookedIn = i > 0 ? this.level.getEntity(i - 1) : null;
        }

        if (DATA_BITING.equals(entityDataAccessor)) {
            this.biting = this.getEntityData().get(DATA_BITING);
            if (this.biting) {
                this.setDeltaMovement(this.getDeltaMovement().x, -0.4F * Mth.nextFloat(this.syncronizedRandom, 0.6F, 1.0F), this.getDeltaMovement().z);
            }
        }

        super.onSyncedDataUpdated(entityDataAccessor);
    }

    @Override
    public void tick() {
        this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level.getGameTime());
        Player player = this.getPlayerOwner();
        if (player == null) {
            this.discard();
        } else if (this.level.isClientSide || !this.shouldStopFishing(player)) {
            if (this.onGround) {
                ++this.life;
                if (this.life >= 1200) {
                    this.discard();
                    return;
                }
            } else {
                this.life = 0;
            }

            float f = 0.0F;
            BlockPos blockpos = this.blockPosition();
            FluidState fluidstate = this.level.getFluidState(blockpos);
            if (fluidstate.is(FluidTags.WATER)) {
                f = fluidstate.getHeight(this.level, blockpos);
            }

            boolean flag = f > 0.0F;
            if (this.currentState == ModFishingHook.FishHookState.FLYING) {
                if (this.hookedIn != null) {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.currentState = ModFishingHook.FishHookState.HOOKED_IN_ENTITY;
                    return;
                }

                if (flag) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
                    this.currentState = ModFishingHook.FishHookState.BOBBING;
                    return;
                }

                this.checkCollision();
            } else {
                if (this.currentState == ModFishingHook.FishHookState.HOOKED_IN_ENTITY) {
                    if (this.hookedIn != null) {
                        if (!this.hookedIn.isRemoved() && this.hookedIn.level.dimension() == this.level.dimension()) {
                            this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
                        } else {
                            this.setHookedEntity(null);
                            this.currentState = ModFishingHook.FishHookState.FLYING;
                        }
                    }

                    return;
                }

                if (this.currentState == ModFishingHook.FishHookState.BOBBING) {
                    Vec3 vec3 = this.getDeltaMovement();
                    double d0 = this.getY() + vec3.y - (double) blockpos.getY() - (double) f;
                    if (Math.abs(d0) < 0.01D) {
                        d0 += Math.signum(d0) * 0.1D;
                    }

                    this.setDeltaMovement(vec3.x * 0.9D, vec3.y - d0 * (double) this.random.nextFloat() * 0.2D, vec3.z * 0.9D);
                    if (this.nibble <= 0 && this.timeUntilHooked <= 0) {
                        this.openWater = true;
                    } else {
                        this.openWater = this.isOpenWaterFishing() && this.outOfWaterTime < 10 && this.calculateOpenWater(blockpos);
                    }

                    if (flag) {
                        this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
                        if (this.biting) {
                            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.1D * (double) this.syncronizedRandom.nextFloat() * (double) this.syncronizedRandom.nextFloat(), 0.0D));
                        }

                        if (!this.level.isClientSide) {
                            this.catchingFish(blockpos);
                        }
                    } else {
                        this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
                    }
                }
            }

            if (!fluidstate.is(FluidTags.WATER)) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            this.updateRotation();
            if (this.currentState == ModFishingHook.FishHookState.FLYING && (this.onGround || this.horizontalCollision)) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.92D));
            this.reapplyPosition();
        }
    }

    @Override
    protected boolean shouldStopFishing(Player player) {
        ItemStack itemstack = player.getMainHandItem();
        ItemStack itemstack1 = player.getOffhandItem();
        boolean flag = itemstack.getItem() instanceof RodItem;
        boolean flag1 = itemstack1.getItem() instanceof RodItem;
        if (!player.isRemoved() && player.isAlive() && (flag || flag1) && !(this.distanceToSqr(player) > 1024.0D)) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    public void setHookedEntity(@Nullable Entity p_150158_) {
        this.hookedIn = p_150158_;
        this.getEntityData().set(DATA_HOOKED_ENTITY, p_150158_ == null ? 0 : p_150158_.getId() + 1);
    }

    @Override
    public void catchingFish(BlockPos blockPos) {
        ServerLevel serverlevel = (ServerLevel) this.level;
        int i = 1;
        BlockPos blockpos = blockPos.above();
        if (this.random.nextFloat() < 0.25F && this.level.isRainingAt(blockpos)) {
            ++i;
        }

        if (this.random.nextFloat() < 0.5F && !this.level.canSeeSky(blockpos)) {
            --i;
        }

        if (this.nibble > 0) {
            --this.nibble;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                this.getEntityData().set(DATA_BITING, false);
            }
        } else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= i;
            if (this.timeUntilHooked > 0) {
                this.fishAngle = (float) ((double) this.fishAngle + this.random.nextGaussian() * 4.0D);
                float f = this.fishAngle * ((float) Math.PI / 180F);
                float f1 = Mth.sin(f);
                float f2 = Mth.cos(f);
                double d0 = this.getX() + (double) (f1 * (float) this.timeUntilHooked * 0.1F);
                double d1 = (float) Mth.floor(this.getY()) + 1.0F;
                double d2 = this.getZ() + (double) (f2 * (float) this.timeUntilHooked * 0.1F);
                if (serverlevel.getBlockState(new BlockPos((int) d0, (int) d1 - 1, (int) d2)).getMaterial() == net.minecraft.world.level.material.Material.WATER) {
                    if (this.random.nextFloat() < 0.15F) {
                        serverlevel.sendParticles(ParticleTypes.BUBBLE, d0, d1 - (double) 0.1F, d2, 1, f1, 0.1D, f2, 0.0D);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    serverlevel.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, f4, 0.01D, -f3, 1.0D);
                    serverlevel.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, -f4, 0.01D, f3, 1.0D);
                }
            } else {
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                double d3 = this.getY() + 0.5D;
                serverlevel.sendParticles(ParticleTypes.BUBBLE, this.getX(), d3, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0D, this.getBbWidth(), 0.2F);
                serverlevel.sendParticles(ParticleTypes.FISHING, this.getX(), d3, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0D, this.getBbWidth(), 0.2F);
                this.nibble = Mth.nextInt(this.random, 20, 40);
                this.getEntityData().set(DATA_BITING, true);
            }
        } else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= i;
            float f5 = 0.15F;
            if (this.timeUntilLured < 20) {
                f5 = (float) ((double) f5 + (double) (20 - this.timeUntilLured) * 0.05D);
            } else if (this.timeUntilLured < 40) {
                f5 = (float) ((double) f5 + (double) (40 - this.timeUntilLured) * 0.02D);
            } else if (this.timeUntilLured < 60) {
                f5 = (float) ((double) f5 + (double) (60 - this.timeUntilLured) * 0.01D);
            }

            if (this.random.nextFloat() < f5) {
                float f6 = Mth.nextFloat(this.random, 0.0F, 360.0F) * ((float) Math.PI / 180F);
                float f7 = Mth.nextFloat(this.random, 25.0F, 60.0F);
                double d4 = this.getX() + (double) (Mth.sin(f6) * f7 * 0.1F);
                double d5 = (float) Mth.floor(this.getY()) + 1.0F;
                double d6 = this.getZ() + (double) (Mth.cos(f6) * f7 * 0.1F);
                if (serverlevel.getBlockState(new BlockPos(d4, d5 - 1.0D, d6)).getMaterial() == net.minecraft.world.level.material.Material.WATER) {
                    serverlevel.sendParticles(ParticleTypes.SPLASH, d4, d5, d6, 2 + this.random.nextInt(2), 0.1F, 0.0D, 0.1F, 0.0D);
                }
            }

            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        } else {
            this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
            this.timeUntilLured -= this.lureSpeed * 20 * 5;
        }

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @Override
    public int retrieve(@NotNull ItemStack stack) {
        Player player = this.getPlayerOwner();
        if (!this.level.isClientSide && player != null && !this.shouldStopFishing(player)) {
            int i = 0;
            ItemFishedEvent event = null;
            if (this.hookedIn != null) {
                this.pullEntity(this.hookedIn);
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, Collections.emptyList());
                this.level.broadcastEntityEvent(this, (byte) 31);
                i = this.hookedIn instanceof ItemEntity ? 3 : 5;
            } else {
                try {
                    if (this.nibble > 0) {
                        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.THIS_ENTITY, this).withRandom(this.random).withLuck((float) this.luck + player.getLuck());
                        lootcontext$builder.withParameter(LootContextParams.KILLER_ENTITY, Objects.requireNonNull(this.getOwner())).withParameter(LootContextParams.THIS_ENTITY, this);
                        InteractionHand hand = player.getMainHandItem().getItem() instanceof RodItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                        ItemStack itemStack = player.getItemInHand(hand);
                        LootTable loottable = Objects.requireNonNull(this.level.getServer()).getLootTables().get(JobsPlus.getId("gameplay/fishing_level_1"));
                        if (itemStack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_2.get())
                            loottable = Objects.requireNonNull(this.level.getServer()).getLootTables().get(JobsPlus.getId("gameplay/fishing_level_2"));
                        if (itemStack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_3.get())
                            loottable = Objects.requireNonNull(this.level.getServer()).getLootTables().get(JobsPlus.getId("gameplay/fishing_level_3"));
                        if (itemStack.getItem() == ModItems.FISHERMANS_ROD_LEVEL_4.get())
                            loottable = Objects.requireNonNull(this.level.getServer()).getLootTables().get(JobsPlus.getId("gameplay/fishing_level_4"));
                        List<ItemStack> list = loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.FISHING));
                        event = new ItemFishedEvent(list, this.onGround ? 2 : 1, this);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (event.isCanceled()) {
                            this.discard();
                            return event.getRodDamage();
                        }
                        CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, list);
                        for (ItemStack itemstack : list) {
                            itemstack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(hand));
                            ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack);
                            double d0 = player.getX() - this.getX();
                            double d1 = player.getY() - this.getY();
                            double d2 = player.getZ() - this.getZ();
                            itementity.setDeltaMovement(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
                            this.level.addFreshEntity(itementity);
                            player.level.addFreshEntity(new ExperienceOrb(player.level, player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, this.random.nextInt(6) + 1));
                            if (itemstack.is(ItemTags.FISHES)) {
                                player.awardStat(Stats.FISH_CAUGHT, 1);
                            }
                        }

                        i = 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (this.onGround) {
                i = 2;
            }

            this.discard();
            return event == null ? i : event.getRodDamage();
        } else {
            return 0;
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 31 && this.level.isClientSide && this.hookedIn instanceof Player && ((Player) this.hookedIn).isLocalPlayer()) {
            this.pullEntity(this.hookedIn);
        }

        super.handleEntityEvent(b);
    }

    @Override
    protected void pullEntity(@NotNull Entity entity1) {
        Entity entity = this.getOwner();
        if (entity != null) {
            Vec3 vec3 = (new Vec3(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ())).scale(0.1D);
            entity1.setDeltaMovement(entity1.getDeltaMovement().add(vec3));
        }
    }

    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    @Nullable
    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player) entity : null;
    }

    @Override
    @Nullable
    public Entity getHookedIn() {
        return this.hookedIn;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? this.getId() : entity.getId());
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        if (this.getPlayerOwner() == null) {
            int i = clientboundAddEntityPacket.getData();
            LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.level.getEntity(i), i);
            this.kill();
        }

    }

    enum FishHookState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING
    }

}
