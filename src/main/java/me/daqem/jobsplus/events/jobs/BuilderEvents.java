package me.daqem.jobsplus.events.jobs;

import me.daqem.jobsplus.common.data.BackpackSavedData;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuilderEvents {

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof Player player) {
                if (player.isCreative()) return;
                if (player.getMainHandItem().getDescriptionId().contains("structurize")
                        || player.getOffhandItem().getDescriptionId().contains("structurize")) return;
                if (player.getMainHandItem().getDescriptionId().contains("constructionwand")
                        || player.getOffhandItem().getDescriptionId().contains("constructionwand")) return;
                if (JobGetters.jobIsEnabled(player, Jobs.BUILDER)) {
                    BlockState state = event.getPlacedBlock();
                    Block block = state.getBlock();
                    ArrayList<String> bannedBlocks = new ArrayList<>(List.of("farmland", "jukebox"));
                    if (!bannedBlocks.contains(block.getDescriptionId().replace("block.minecraft.", ""))
                            && state.getMaterial().isSolid()
                            && !block.getDescriptionId().endsWith(".twig")) {
                        float destroySpeed = state.getDestroySpeed(player.level, event.getPos());
                        if (destroySpeed <= 2.5) ExpHandler.addEXPLowest(player, Jobs.BUILDER);
                        if (destroySpeed <= 5) ExpHandler.addEXPLow(player, Jobs.BUILDER);
                        else ExpHandler.addEXPMid(player, Jobs.BUILDER);
                    }
                    if (JobGetters.hasPowerupEnabled(player, Jobs.BUILDER, CapType.POWER_UP2.get(), true)) {
                        for (ItemStack item : player.getInventory().items) {
                            if (item.getItem() != Items.AIR) {
                                if (item.getItem() instanceof BackpackItem) {
                                    BackpackSavedData data = BackpackItem.getData(item);
                                    for (int i = 0; i < Objects.requireNonNull(data).getHandler().getSlots(); ++i) {
                                        ItemStack itemInBackpack = data.getHandler().getStackInSlot(i);

                                        Item item1 = player.getInventory().getSelected().getItem();
                                        if (item1 == Items.AIR) item1 = player.getOffhandItem().getItem();
                                        if (itemInBackpack.getItem() == item1) {
                                            if (!giveBlockBack(player, state)) {
                                                data.getHandler().extractItem(i, 1, false);
                                            }
                                            giveItemBack(player, block);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        giveBlockBack(player, state);
                    }
                }
            }
        }
    }

    public boolean giveBlockBack(Player player, BlockState state) {
        if (JobGetters.hasPowerupEnabled(player, Jobs.BUILDER, CapType.POWER_UP1.get(), true)) {
            if (Math.random() * 100 < 5) {
                Block block = state.getBlock();
                if (state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS) || state.is(BlockTags.WOOL)
                        || state.is(BlockTags.STONE_BRICKS) || state.is(BlockTags.BUTTONS) || state.is(BlockTags.WOOL_CARPETS)
                        || state.is(BlockTags.DOORS) || state.is(BlockTags.SAND) || state.is(BlockTags.STAIRS)
                        || state.is(BlockTags.SLABS) || state.is(BlockTags.WALLS) || state.is(BlockTags.RAILS)
                        || state.is(BlockTags.LEAVES) || state.is(BlockTags.TRAPDOORS) || state.is(BlockTags.FENCES)
                        || state.is(BlockTags.CANDLES) || state.is(BlockTags.DIRT) || state.is(BlockTags.TERRACOTTA)
                        || state.is(BlockTags.FLOWER_POTS) || state.is(BlockTags.ENDERMAN_HOLDABLE) || state.is(BlockTags.ICE)
                        || state.is(BlockTags.IMPERMEABLE) || state.is(BlockTags.CORALS) || state.is(BlockTags.SIGNS)
                        || state.is(BlockTags.NYLIUM) || state.is(BlockTags.CLIMBABLE) || state.is(BlockTags.FENCE_GATES)
                        || state.is(BlockTags.SNOW) || block == Blocks.STONE || block == Blocks.COBBLESTONE || block == Blocks.GRANITE
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
            public void onTick(TickEvent.LevelTickEvent event) {
                if (delay-- > 0) return;
                player.getInventory().add(new ItemStack(item, 1));
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        });
    }
}

