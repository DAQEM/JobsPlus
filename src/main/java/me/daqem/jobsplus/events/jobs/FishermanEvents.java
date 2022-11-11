package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.entity.ModFishingHook;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.ItemHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
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

    private static final ArrayList<Item> TREASURE = new ArrayList<>(List.of(Items.BOW, Items.FISHING_ROD, Items.NAME_TAG, Items.ENCHANTED_BOOK, Items.NAUTILUS_SHELL, Items.SADDLE));
    private static final Jobs JOB = Jobs.FISHERMAN;

    @SubscribeEvent
    public void onItemFished(ItemFishedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!JobGetters.jobIsEnabled(player, JOB)) return;
            int exp = 0;
            for (ItemStack itemStack : event.getDrops()) {
                exp += calculateEXPForDrop(itemStack);
            }
            ItemStack usedRodStack = player.getMainHandItem().getItem() instanceof FishingRodItem ? player.getMainHandItem() : player.getOffhandItem();
            int luck = usedRodStack.getEnchantmentLevel(Enchantments.FISHING_LUCK);
            int lure = usedRodStack.getEnchantmentLevel(Enchantments.FISHING_SPEED);

            ModFishingHook modFishingHook = new ModFishingHook(player, player.level, luck, lure);
            LootContext.Builder builder = (new LootContext.Builder((ServerLevel) player.level))
                    .withParameter(LootContextParams.ORIGIN, player.getPosition(player.getOnPos().asLong()))
                    .withParameter(LootContextParams.TOOL, usedRodStack)
                    .withParameter(LootContextParams.THIS_ENTITY, modFishingHook)
                    .withParameter(LootContextParams.KILLER_ENTITY, player)
                    .withParameter(LootContextParams.THIS_ENTITY, modFishingHook)
                    .withRandom(RandomSource.create())
                    .withLuck((float) luck + player.getLuck());

            if (JobGetters.hasPowerupEnabled(player, JOB, CapType.POWER_UP2.get(), true)) {
                exp += giveFishedItem(player, builder, 2);
            }
            if (JobGetters.hasSuperPowerEnabled(player, JOB, true)) {
                exp += giveFishedItem(player, builder, 5);
            }
            if (exp != 0) {
                if (JobGetters.hasPowerupEnabled(player, JOB, CapType.POWER_UP1.get(), true)) exp *= 2;
                ExpHandler.addJobEXP(player, JOB, exp);
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

    private int giveFishedItem(Player player, LootContext.Builder builder, int amount) {
        int exp = 0;
        if (Math.random() * 100 < 20 && amount != 0) {
            for (int i = 0; i < amount; ++i) {
                List<ItemStack> list = Objects.requireNonNull(player.level.getServer()).getLootTables().get(new ResourceLocation("gameplay/fishing")).getRandomItems(builder.create(LootContextParamSets.FISHING));
                for (ItemStack itemStack : list) {
                    exp += calculateEXPForDrop(itemStack);
                    ItemHandler.addItemsToInventoryOrDrop(itemStack, player, player.level, player.getOnPos(), 0);
                }
            }
        }
        return exp;
    }

    private int calculateEXPForDrop(ItemStack itemStack) {
        return itemStack.is(Items.TROPICAL_FISH) ?
                25 :
                itemStack.is(ItemTags.FISHES) ?
                        ExpHandler.getEXPHigh() :
                        TREASURE.contains(itemStack.getItem()) ?
                                ExpHandler.getEXPFishing() :
                                ExpHandler.getEXPLow();
    }
}
