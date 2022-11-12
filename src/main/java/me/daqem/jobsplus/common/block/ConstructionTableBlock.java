package me.daqem.jobsplus.common.block;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.inventory.construction.ConstructionMenu;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConstructionTableBlock extends CraftingTableBlock {

    private static final Component CONTAINER_TITLE = JobsPlus.translatable("container.construction");

    public ConstructionTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else {
            CompoundTag tag = new CompoundTag();
            for (Jobs enabledJob : getEnabledJobs(player)) {
                if (enabledJob != Jobs.ALCHEMIST) {
                    tag.putIntArray(enabledJob.name(), new int[]{JobGetters.getJobLevel(player, enabledJob), JobGetters.getJobEXP(player, enabledJob)});
                }
            }
            if (tag.isEmpty()) {
                HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.red() + "Open the Jobs+ screen to start a job first.");
            } else {
                NetworkHooks.openGui((ServerPlayer) player, getMenuProvider(state, level, pos), (buffer -> buffer.writeNbt(tag)));
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider((i, inventory, player) -> new ConstructionMenu(i, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter blockGetter, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        components.add(JobsPlus.literal(ChatColor.green() + "Used to craft Jobs+ items."));
        super.appendHoverText(stack, blockGetter, components, flag);
    }

    public ArrayList<Jobs> getEnabledJobs(Player player) {
        ArrayList<Jobs> list = new ArrayList<>();
        for (Jobs job : Jobs.values()) {
            if (JobGetters.jobIsEnabled(player, job)) {
                list.add(job);
            }
        }
        return list;
    }
}
