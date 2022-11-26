package me.daqem.jobsplus.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHandler {

    @SuppressWarnings("deprecation")
    public static void destroyBlock(ServerPlayer player, BlockPos blockPos) {
        BlockState blockstate = player.level.getBlockState(blockPos);
        Block block = blockstate.getBlock();
        if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
            player.level.sendBlockUpdated(blockPos, blockstate, blockstate, 3);
        } else {
            if (player.isCreative()) {
                removeBlock(player, blockPos, false);
            } else {
                ItemStack itemStack = player.getMainHandItem();
                ItemStack itemStackCopy = itemStack.copy();
                boolean flag1 = blockstate.canHarvestBlock(player.level, blockPos, player);
                itemStack.mineBlock(player.level, blockstate, blockPos, player);
                if (itemStack.isEmpty() && !itemStackCopy.isEmpty())
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemStackCopy, InteractionHand.MAIN_HAND);
                boolean flag = removeBlock(player, blockPos, flag1);

                if (flag && flag1) {
                    block.playerDestroy(player.level, player, blockPos, blockstate, player.level.getBlockEntity(blockPos), itemStackCopy);
                }

                if (flag) {
                    int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
                    int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
                    int exp = blockstate.getExpDrop(player.level, player.level.random, blockPos, fortuneLevel, silkTouchLevel);
                    blockstate.getBlock().popExperience(player.getLevel(), blockPos, exp);
                }
            }
        }
    }

    private static boolean removeBlock(ServerPlayer player, BlockPos blockPos, boolean canHarvest) {
        BlockState state = player.level.getBlockState(blockPos);
        boolean removed = state.onDestroyedByPlayer(player.level, blockPos, player, canHarvest, player.level.getFluidState(blockPos));
        if (removed)
            state.getBlock().destroy(player.level, blockPos, state);
        return removed;
    }

    @SuppressWarnings("deprecation")
    public static boolean isOre(Block block) {
        return ((block instanceof DropExperienceBlock && block != Blocks.SCULK)
                || block instanceof RedStoneOreBlock
                || block.builtInRegistryHolder().containsTag(new TagKey<>(Registry.BLOCK_REGISTRY, new ResourceLocation("forge", "ores")))
                || block.getDescriptionId().endsWith("_ore")
                || block == Blocks.ANCIENT_DEBRIS);
    }
}
