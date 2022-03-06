package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.container.backpack.BackpackSavedData;
import me.daqem.jobsplus.common.item.BackpackItem;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuilderEvents {

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isClientSide()) {
            if (event.getEntity() instanceof Player player) {
                if (player.isCreative()) return;
                if (player.getMainHandItem().getDescriptionId().contains("structurize")
                        || player.getOffhandItem().getDescriptionId().contains("structurize")) return;
                if (JobGetters.jobIsEnabled(player, Jobs.BUILDER)) {
                    Block block = event.getPlacedBlock().getBlock();
                    ArrayList<String> bannedBlocks = new ArrayList<>(List.of("cobblestone", "farmland"));
                    if (!bannedBlocks.contains(block.getDescriptionId().replace("block.minecraft.", ""))
                            && event.getPlacedBlock().getMaterial().isSolid()
                            && !block.getDescriptionId().endsWith(".twig")) {
                        float destroySpeed = event.getPlacedBlock().getDestroySpeed(player.level, event.getPos());
                        if (destroySpeed <= 2.5) {
                            ExpHandler.addEXPLowest(player, Jobs.BUILDER);
                        }
                        if (destroySpeed <= 5) {
                            ExpHandler.addEXPLow(player, Jobs.BUILDER);
                        } else {
                            ExpHandler.addEXPMid(player, Jobs.BUILDER);
                        }
                    }
                    if (JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWERUP2.get())) {
                        for (ItemStack item : player.getInventory().items) {
                            if (!Objects.equals(item.getItem(), Items.AIR)) {
                                if (item.getItem() instanceof BackpackItem) {
                                    BackpackSavedData data = BackpackItem.getData(item);
                                    for (int i = 0; i < Objects.requireNonNull(data).getHandler().getSlots(); ++i) {
                                        ItemStack itemInBackpack = data.getHandler().getStackInSlot(i);

                                        if (itemInBackpack.getItem() == player.getInventory().getSelected().getItem()) {
                                            if (!giveBlockBack(player, block)) {
                                                itemInBackpack.setCount(itemInBackpack.getCount() - 1);
                                            }
                                            giveItemBack(player, block);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        giveBlockBack(player, block);
                    }
                }
            }
        }
    }

    public boolean giveBlockBack(Player player, Block block) {
        if (JobGetters.hasEnabledPowerup(player, Jobs.BUILDER, CapType.POWERUP1.get())) {
            if (Math.random() * 100 < 5) {
                if (BlockTags.LOGS.contains(block) || BlockTags.PLANKS.contains(block) || BlockTags.WOOL.contains(block)
                        || BlockTags.STONE_BRICKS.contains(block) || BlockTags.BUTTONS.contains(block) || BlockTags.CARPETS.contains(block)
                        || BlockTags.DOORS.contains(block) || BlockTags.SAND.contains(block) || BlockTags.STAIRS.contains(block)
                        || BlockTags.SLABS.contains(block) || BlockTags.WALLS.contains(block) || BlockTags.RAILS.contains(block)
                        || BlockTags.LEAVES.contains(block) || BlockTags.TRAPDOORS.contains(block) || BlockTags.FENCES.contains(block)
                        || BlockTags.CANDLES.contains(block) || BlockTags.DIRT.contains(block) || BlockTags.TERRACOTTA.contains(block)
                        || BlockTags.FLOWER_POTS.contains(block) || BlockTags.ENDERMAN_HOLDABLE.contains(block) || BlockTags.ICE.contains(block)
                        || BlockTags.IMPERMEABLE.contains(block) || BlockTags.CORALS.contains(block) || BlockTags.SIGNS.contains(block)
                        || BlockTags.NYLIUM.contains(block) || BlockTags.CLIMBABLE.contains(block) || BlockTags.FENCE_GATES.contains(block)
                        || BlockTags.SNOW.contains(block) || block == Blocks.STONE || block == Blocks.COBBLESTONE || block == Blocks.GRANITE
                        || block == Blocks.POLISHED_GRANITE || block == Blocks.DIORITE || block == Blocks.POLISHED_DIORITE || block == Blocks.ANDESITE
                        || block == Blocks.POLISHED_ANDESITE || block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE || block == Blocks.POLISHED_DEEPSLATE
                        || block == Blocks.CALCITE || block == Blocks.TUFF || block == Blocks.DRIPSTONE_BLOCK || block == Blocks.POLISHED_BASALT
                        || block == Blocks.BASALT || block == Blocks.BLACKSTONE || block == Blocks.POLISHED_BLACKSTONE || block == Blocks.GILDED_BLACKSTONE
                        || block == Blocks.CHISELED_POLISHED_BLACKSTONE || block == Blocks.POLISHED_BLACKSTONE_BRICKS) {
                    giveItemBack(player, block);
                    return true;
                }
            }
        }
        return false;
    }

    public void giveItemBack(Player player, Block block) {
        MinecraftForge.EVENT_BUS.register(new Object() {
            final Item item = block.asItem();
            int delay = 1;

            @SubscribeEvent
            public void onTick(TickEvent.WorldTickEvent event) {
                if (delay-- > 0) return;
                player.getInventory().add(new ItemStack(item, 1));
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        });
    }
}

