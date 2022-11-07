package me.daqem.jobsplus.events.jobs;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.HeadData;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.ModItemUtils;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HunterEvents {

    private static final List<String> horseTypes = Arrays.asList("white", "creamy", "chestnut", "brown", "black", "gray", "dark_brown");
    private static final List<String> llamaTypes = Arrays.asList("creamy", "white", "brown", "gray");
    private static final List<String> parrotTypes = Arrays.asList("red", "blue", "green", "cyan", "gray");
    private static final List<String> rabbitTypes = Arrays.asList("brown", "white", "black", "black_and_white", "gold", "salt_and_pepper");
    @SuppressWarnings("unused")
    private static final List<String> catTypes = Arrays.asList("tabby", "tuxedo", "red", "siamese", "british_shorthair", "calico", "persian", "ragdoll", "white", "jellie", "black");
    private static final List<String> axolotlTypes = Arrays.asList("lucy", "wild", "gold", "cyan", "blue");
    private final Jobs job = Jobs.HUNTER;
    private final HashMap<Player, Integer> furnaceHashmap = new HashMap<>();

    private static int partToDecimalValue(String hex) {
        return Long.valueOf(hex, 16).intValue();
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (!JobGetters.jobIsEnabled(player, job)) return;

        final Entity entity = event.getEntity();

        if (entity instanceof Monster || entity instanceof Animal || entity instanceof WaterAnimal
                || entity instanceof Slime || entity instanceof Villager) {
            if (event.getSource().isProjectile()) ExpHandler.addEXPHigh(player, job);
            else ExpHandler.addEXPMid(player, job);

            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP3.get(), true) && Math.random() * 100 < 5) {
                if (entity instanceof Zombie && !(entity instanceof Drowned) && !(entity instanceof ZombieVillager) && !(entity instanceof ZombifiedPiglin))
                    ItemHandler.addFreshItemEntity(player.level, entity.getOnPos().above(), Items.ZOMBIE_HEAD);
                if (entity instanceof Creeper)
                    ItemHandler.addFreshItemEntity(player.level, entity.getOnPos().above(), Items.CREEPER_HEAD);
                if (entity instanceof Skeleton)
                    ItemHandler.addFreshItemEntity(player.level, entity.getOnPos().above(), Items.SKELETON_SKULL);
                if (entity instanceof WitherSkeleton)
                    ItemHandler.addFreshItemEntity(player.level, entity.getOnPos().above(), Items.WITHER_SKELETON_SKULL);
                else {
                    final String mobName = getName(entity);
                    if (!HeadData.headDataMap.containsKey(mobName)) return;
                    final Pair<String, String> stringPair = HeadData.headDataMap.get(mobName);
                    if (stringPair != null) {
                        String headName = ChatHandler.capitalizeWord(mobName.replace("_", " ")) + " Head";
                        String oldId = stringPair.getFirst();
                        String texture = stringPair.getSecond();

                        ItemStack mobHead = getHead(headName, texture, oldId, 1);
                        ItemHandler.addFreshItemEntity(player.level, entity.getOnPos().above(), mobHead);
                    }
                }
            }
            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60));
            }
        }
    }

    @SubscribeEvent
    public void onHeadMine(BlockEvent.BreakEvent event) {
        Level level = event.getPlayer().getLevel();
        if (level.isClientSide) return;
        BlockState state = event.getState();
        Block block = state.getBlock();
        if (!(block instanceof SkullBlock || block instanceof WallSkullBlock)) return;
        BlockPos pos = event.getPos();
        SkullBlockEntity sbe = (SkullBlockEntity) level.getBlockEntity(pos);
        if (event.getPlayer().isCreative() || sbe == null) return;
        GameProfile profile = sbe.getOwnerProfile();
        if (profile == null) return;
        UUID uuid = profile.getId();
        if (uuid == null) return;
        String headId = uuid.toString();

        String correctHeadName = "";
        String texture = "";
        for (String headName : HeadData.headDataMap.keySet()) {
            String headNameId = HeadData.headDataMap.get(headName).getFirst();
            if (headId.equals(headNameId)) {
                correctHeadName = ChatHandler.capitalizeWord(headName.replace("_", " ")) + " Head";
                texture = HeadData.headDataMap.get(headName).getSecond();
                break;
            }
        }
        ItemStack namedHeadStack = getHead(correctHeadName, texture, headId, 1);
        if (namedHeadStack != null) {
            event.setCanceled(true);
            level.destroyBlock(pos, false);

            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), namedHeadStack));
        }
    }

    @SubscribeEvent
    public void onPlayerTickFurnace(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu instanceof FurnaceMenu || containerMenu instanceof SmokerMenu) {
            if (JobGetters.jobIsEnabled(player, job)) {
                for (Slot slot : containerMenu.slots) {
                    if (!(slot.container instanceof Inventory)) {
                        ItemStack item = slot.getItem();
                        int itemCount = item.getCount();
                        if (slot.getContainerSlot() == 2) {
                            if (furnaceHashmap.containsKey(player)) {
                                if (item.getItem() instanceof AirItem) {
                                    int exp = 0;
                                    for (int i = 0; i < furnaceHashmap.get(player); i++) {
                                        exp += ExpHandler.getEXPLow();
                                    }
                                    ExpHandler.addJobEXP(player, job, exp);
                                    furnaceHashmap.remove(player);
                                } else {
                                    if (itemCount < furnaceHashmap.get(player)) {
                                        int exp = 0;
                                        for (int i = 0; i < furnaceHashmap.get(player) - itemCount; i++) {
                                            exp += ExpHandler.getEXPLow();
                                        }
                                        ExpHandler.addJobEXP(player, job, exp);
                                        furnaceHashmap.put(player, furnaceHashmap.get(player) - itemCount);

                                    }
                                }

                            }
                            ArrayList<String> acceptedItems = new ArrayList<>(List.of("cooked_chicken", "cooked_beef",
                                    "cooked_mutton", "cooked_porkchop", "cooked_rabbit", "porkchop", "beef", "chicken",
                                    "rabbit", "mutton"));
                            if (acceptedItems.contains(item.getItem().getDescriptionId().replace("item.minecraft.", ""))) {
                                furnaceHashmap.put(player, itemCount);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onInventoryClose(PlayerContainerEvent.Close event) {
        furnaceHashmap.remove(event.getEntity());
    }

    @SubscribeEvent
    public void onEat(LivingEntityUseItemEvent.Finish event) {
        if (!event.getItem().isEdible()) return;
        if (event.getEntity() instanceof Player player) {
            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP1.get(), true)) {
                final Item item = event.getItem().getItem();
                if (item == Items.COOKED_BEEF || item == Items.COOKED_CHICKEN || item == Items.COOKED_MUTTON
                        || item == Items.COOKED_RABBIT || item == Items.COOKED_PORKCHOP
                        || item.getDescriptionId().equalsIgnoreCase("item.artifacts.eternal_steak")) {

                    final FoodData foodData = player.getFoodData();
                    foodData.setFoodLevel(foodData.getFoodLevel() + 2);
                    foodData.setSaturation(foodData.getSaturationLevel() + 2);
                }
            }
        }
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (!JobGetters.hasSuperPowerEnabled(player, job, true)) return;

        event.setAmount((float) (event.getAmount() * 1.25));
    }

    @SubscribeEvent
    public void onArrowShoot(ArrowLooseEvent event) {
        if (event.getLevel().isClientSide) return;
        Player player = event.getEntity();
        if (!JobGetters.hasSuperPowerEnabled(player, job, true)) return;
        if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.MULTISHOT, event.getBow()) != 0) return;

        float power = BowItem.getPowerForTime(event.getCharge());
        float[] afloat = getShotPitches(new Random());

        shootProjectile(event.getLevel(), player, event.getBow(), Items.ARROW.getDefaultInstance(), afloat[1], power * 3, -10.0F);
        shootProjectile(event.getLevel(), player, event.getBow(), Items.ARROW.getDefaultInstance(), afloat[2], power * 3, 10.0F);
    }

    private void shootProjectile(Level level, LivingEntity livingEntity, ItemStack bow, ItemStack arrow,
                                 float shotPitch, float power, float pitch) {
        if (livingEntity instanceof Player player) {
            AbstractArrow projectile = getArrow(level, player, bow, arrow);
            if (player.getAbilities().instabuild || pitch != 0.0F) {
                projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            Vector3f vector3f = new Vector3f(player.getViewVector(1.0F));
            vector3f.transform(new Quaternion(new Vector3f(player.getUpVector(1.0F)), pitch, true));
            projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), power, (float) 1.0);

            ModItemUtils.damageItem(1, bow, player);
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, shotPitch);
        }
    }

    private AbstractArrow getArrow(Level p_40915_, LivingEntity p_40916_, ItemStack stack, ItemStack p_40918_) {
        ArrowItem arrowitem = (ArrowItem) (p_40918_.getItem() instanceof ArrowItem ? p_40918_.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(p_40915_, p_40918_, p_40916_);
        if (p_40916_ instanceof Player) {
            abstractarrow.setCritArrow(true);
        }

        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int i = stack.getEnchantmentLevel(Enchantments.PIERCING);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte) i);
        }

        return abstractarrow;
    }

    private float[] getShotPitches(Random p_40924_) {
        boolean flag = p_40924_.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, p_40924_), getRandomShotPitch(!flag, p_40924_)};
    }

    private float getRandomShotPitch(boolean p_150798_, Random p_150799_) {
        float f = p_150798_ ? 0.63F : 0.43F;
        return 1.0F / (p_150799_.nextFloat() * 0.5F + 1.8F) + f;
    }

    @SuppressWarnings("deprecation")
    public String getEntityString(Entity entity) {
        String entityString = entity.getType().getDescriptionId().replace("item.", "");
        if (entityString.contains(".")) entityString = entityString.split("\\.")[1];
        return WordUtils.capitalize(entityString.replace("_", " ")).replace(" ", "").replace("Entity", "");
    }

    public String getName(Entity entity) {
        String entityString = getEntityString(entity);
        String mobName = entityString.split("\\[")[0].replace("Entity", "");
        mobName = String.join("_", mobName.split("(?<=.)(?=\\p{Lu})")).toLowerCase();

        if (entity instanceof Creeper creeper) {
            if (creeper.isPowered()) mobName = "charged_creeper";
        } else if (entity instanceof Cat cat) {
            mobName = cat.getCatVariant().toString();
            mobName = mobName.split("\\/")[3];
            mobName = mobName.replace(".png]", "") + "_cat";
        } else if (entity instanceof Horse horse) {
            int type = horse.getVariant().getId();
            mobName = horseTypes.get(type >= 1024 ? type - 1024 : type >= 768 ? type - 768 : type >= 512 ? type - 512 : type >= 256 ? type - 256 : horse.getVariant().getId()) + "_horse";
        } else if (entity instanceof TraderLlama traderllama) {
            int type = traderllama.getVariant();
            if (type < llamaTypes.size()) mobName = llamaTypes.get(type) + "_trader_" + mobName;
        } else if (entity instanceof Llama llama) {
            int type = llama.getVariant();
            if (type < llamaTypes.size()) mobName = llamaTypes.get(type) + "_" + mobName;
        } else if (entity instanceof Parrot parrot) {
            int type = parrot.getVariant();
            if (type < parrotTypes.size()) mobName = parrotTypes.get(type) + "_parrot";
        } else if (entity instanceof Rabbit rabbit) {
            int type = rabbit.getRabbitType();
            if (type < rabbitTypes.size()) mobName = rabbitTypes.get(type) + "_rabbit";
            else if (type == 99) mobName = "killer_rabbit";
        } else if (entity instanceof Sheep sheep) {
            boolean checkType = true;
            if (sheep.hasCustomName()) {
                if (sheep.getName().getString().equals("jeb_")) {
                    mobName = "jeb_sheep";
                    checkType = false;
                }
            }
            if (checkType) mobName = sheep.getColor().toString().toLowerCase() + "_sheep";
        } else if (entity instanceof MushroomCow mooshroom) {
            if (mooshroom.getMushroomType() == MushroomCow.MushroomType.BROWN) mobName = "brown_mooshroom";
        } else if (entity instanceof Axolotl axolotl) {
            int type = axolotl.getVariant().getId();
            if (type < axolotlTypes.size()) mobName = axolotlTypes.get(type) + "_axolotl";
        } else if (entity instanceof Villager villager) {
            VillagerData d = villager.getVillagerData();
            VillagerProfession prof = d.getProfession();
            if (!prof.toString().equals("none")) mobName = prof.toString();
        } else if (entity instanceof ZombieVillager zombieVillager) {
            VillagerData d = zombieVillager.getVillagerData();
            VillagerProfession prof = d.getProfession();
            if (!prof.toString().equals("none")) {
                mobName = "zombie_" + prof;
            }
        }

        return mobName.toLowerCase();
    }

    public ItemStack getHead(String headName, String texture, String oldId, Integer amount) {
        ItemStack texturedHead = new ItemStack(Items.PLAYER_HEAD, amount);
        String oldIdFull = oldId.replace("-", "");
        List<Integer> parts = new ArrayList<>();
        int len = oldIdFull.length();
        for (int i = 0; i < len; i += 8) {
            parts.add(partToDecimalValue(oldIdFull.substring(i, Math.min(len, i + 8))));
        }
        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putIntArray("Id", parts);
        CompoundTag properties = new CompoundTag();
        ListTag textures = new ListTag();
        CompoundTag tex = new CompoundTag();
        tex.putString("Value", texture);
        textures.add(tex);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        texturedHead.addTagElement("SkullOwner", skullOwner);
        texturedHead.setHoverName(Component.literal(headName));
        return texturedHead;
    }
}
