package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.tooltip.TooltipBuilder;
import me.daqem.jobsplus.common.crafting.ModRecipeManager;
import me.daqem.jobsplus.common.data.BackpackSavedData;
import me.daqem.jobsplus.common.inventory.backpack.BackpackMenu;
import me.daqem.jobsplus.handlers.BackpackHandler;
import me.daqem.jobsplus.handlers.BackpackItemHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Backpack;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BackpackItem extends Item {

    private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");
    private static final String UUID_NBT = "UUID";
    private static final String INVENTORY_NBT = "Inventory";
    final String name;
    final Backpack tier;


    public BackpackItem(String name, Backpack tier) {
        super(new Item.Properties().stacksTo(1).tab(JobsPlus.TAB).fireResistant());
        this.name = name;
        this.tier = tier;
    }

    public static BackpackSavedData getData(ItemStack stack) {
        if (!(stack.getItem() instanceof BackpackItem backpackItem)) return null;
        CompoundTag tag = stack.getOrCreateTag();
        UUID uuid = tag.contains(UUID_NBT) ? tag.getUUID(UUID_NBT) : UUID.randomUUID();
        if (!tag.contains(UUID_NBT)) tag.putUUID(UUID_NBT, uuid);
        return BackpackHandler.get().getOrCreateBackpack(uuid, backpackItem.tier);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @Nonnull InteractionHand hand) {
        if (level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(hand));

        ItemStack backpack = player.getItemInHand(hand);
        BackpackSavedData data = BackpackItem.getData(backpack);
        Backpack itemTier = ((BackpackItem) backpack.getItem()).tier;
        UUID uuid = Objects.requireNonNull(data).getUuid();

        if (!(JobGetters.getJobLevel(player, ModRecipeManager.getJob(backpack)) >= ModRecipeManager.getRequiredJobLevel(backpack))) {
            HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, JobsPlus.translatable("error.magic").withStyle(ChatFormatting.RED));
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        if (backpack.getOrCreateTag().contains(INVENTORY_NBT)) {
            ((BackpackItemHandler) Objects.requireNonNull(data).getHandler()).deserializeNBT(Objects.requireNonNull(backpack.getTag()).getCompound(INVENTORY_NBT));
            backpack.getTag().remove(INVENTORY_NBT);
        }
        if (data.getTier().ordinal() < itemTier.ordinal()) data.upgrade(itemTier);
        if (!backpack.is(ModItems.ENDER_BACKPACK.get())) {
            NetworkHooks.openScreen(((ServerPlayer) player), new SimpleMenuProvider((windowId, playerInventory, playerEntity) -> new BackpackMenu(windowId, playerInventory, uuid, data.getTier(), data.getHandler()), backpack.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
        } else {
            NetworkHooks.openScreen(((ServerPlayer) player), new SimpleMenuProvider((containerId, inventory, player1) ->
                    ChestMenu.threeRows(containerId, inventory, player.getEnderChestInventory()), CONTAINER_TITLE));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public @NotNull Rarity getRarity(@Nonnull ItemStack stack) {
        return this.tier.rarity;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.addAll(new TooltipBuilder()
                .withRequirement(stack)
                .withHoldShift()
                .build());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BackpackCaps(stack);
    }

    static class BackpackCaps implements ICapabilityProvider {
        private final ItemStack stack;
        private LazyOptional<IItemHandler> optional = LazyOptional.empty();

        public BackpackCaps(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                if (!this.optional.isPresent())
                    this.optional = BackpackHandler.get().getCapability(this.stack);
                return this.optional.cast();
            } else
                return LazyOptional.empty();
        }
    }
}
