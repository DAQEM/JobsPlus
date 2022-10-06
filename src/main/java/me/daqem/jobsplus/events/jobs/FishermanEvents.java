package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FishermanEvents {


    @SubscribeEvent
    public void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        Jobs job = Jobs.FISHERMAN;
        if (JobGetters.jobIsEnabled(player, job)) {
            int exp = 0;
            for (ItemStack itemStack : event.getDrops()) {
                Item item = itemStack.getItem();
                if (item == Items.TROPICAL_FISH) {
                    exp += 25;
                } else {
                    ArrayList<Item> treasure = new ArrayList<>(List.of(Items.BOW, Items.FISHING_ROD, Items.NAME_TAG, Items.ENCHANTED_BOOK, Items.NAUTILUS_SHELL, Items.SADDLE));
                    if (itemStack.is(ItemTags.FISHES)) {
                        exp += ExpHandler.getEXPHigh();
                    } else if (treasure.contains(item)) {
                        exp += ExpHandler.getEXPFishing();
                    } else {
                        exp += ExpHandler.getEXPLow();
                    }
                }
            }
            int luck = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FISHING_LUCK, player.getMainHandItem());
            int lure = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FISHING_SPEED, player.getMainHandItem());
            InteractionHand hand = player.getMainHandItem().getItem() instanceof FishingRodItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            Item usedItem = player.getItemInHand(hand).getItem();
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) player.level)).withParameter(LootContextParams.ORIGIN, player.getPosition(player.getOnPos().asLong())).withParameter(LootContextParams.TOOL, usedItem.getDefaultInstance()).withParameter(LootContextParams.THIS_ENTITY, new ModFishingHook(player, player.level, luck, lure)).withRandom(RandomSource.create()).withLuck((float) luck + player.getLuck());
            lootcontext$builder.withParameter(LootContextParams.KILLER_ENTITY, player).withParameter(LootContextParams.THIS_ENTITY, new ModFishingHook(player, player.level, luck, lure));

            if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP2.get(), true)) {
                if (Math.random() * 100 < 20) {
                    for (int i = 0; i < 2; ++i) {
                        LootTable loottable = Objects.requireNonNull(player.level.getServer()).getLootTables().get(new ResourceLocation("gameplay/fishing"));
                        List<ItemStack> list = loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.FISHING));
                        for (ItemStack itemStack : list) {
                            ItemHandler.addItemsToInventoryOrDrop(itemStack, player, player.level, player.getOnPos(), 0);
                        }
                    }
                }
            }

            if (JobGetters.hasSuperPowerEnabled(player, job, true)) {
                if (Math.random() * 100 < 20) {
                    for (int i = 0; i < 5; ++i) {
                        LootTable loottable = Objects.requireNonNull(player.level.getServer()).getLootTables().get(new ResourceLocation("gameplay/fishing"));
                        List<ItemStack> list = loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.FISHING));
                        for (ItemStack itemStack : list) {
                            ItemHandler.addItemsToInventoryOrDrop(itemStack, player, player.level, player.getOnPos(), 0);
                        }
                    }
                }
            }

            if (exp != 0) {
                if (JobGetters.hasPowerupEnabled(player, job, CapType.POWER_UP1.get(), true)) exp = exp * 2;
                ExpHandler.addJobEXP(player, job, exp);
            }
        }
    }

    @SubscribeEvent
    public void onFallFisherman(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getTags().contains("cancelFallDamageForFisherman")) {
                player.removeTag("cancelFallDamageForFisherman");
                event.setDistance(event.getDistance() / 5);
            }
        }
    }
}
