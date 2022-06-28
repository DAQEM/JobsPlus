package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.container.backpack.BackpackContainer;
import me.daqem.jobsplus.common.container.backpack.BackpackHandler;
import me.daqem.jobsplus.common.container.backpack.BackpackSavedData;
import me.daqem.jobsplus.handlers.BackpackItemHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.TranslatableString;
import me.daqem.jobsplus.utils.enums.Backpack;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BackpackItem extends Item {

    private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");
    final String name;
    final Backpack tier;


    public BackpackItem(String name, Backpack tier) {
        super(new Item.Properties().stacksTo(1).tab(JobsPlus.TAB).fireResistant());
        this.name = name;
        this.tier = tier;
    }

    public static BackpackSavedData getData(ItemStack stack) {
        if (!(stack.getItem() instanceof BackpackItem))
            return null;
        UUID uuid;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("UUID")) {
            uuid = UUID.randomUUID();
            tag.putUUID("UUID", uuid);
        } else
            uuid = tag.getUUID("UUID");
        return BackpackHandler.get().getOrCreateBackpack(uuid, ((BackpackItem) stack.getItem()).tier);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack backpack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer && backpack.getItem() instanceof BackpackItem) {
            Jobs job = Jobs.BUILDER;
            BackpackSavedData data = BackpackItem.getData(backpack);
            Backpack itemTier = ((BackpackItem) backpack.getItem()).tier;
            UUID uuid = Objects.requireNonNull(data).getUuid();

            boolean canOpenBackpack = (backpack.getItem() == ModItems.SMALL_BACKPACK.get() && JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_SMALL_BACKPACK.get())
                    || (backpack.getItem() == ModItems.MEDIUM_BACKPACK.get() && JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_MEDIUM_BACKPACK.get())
                    || (backpack.getItem() == ModItems.LARGE_BACKPACK.get() && JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_LARGE_BACKPACK.get())
                    || (backpack.getItem() == ModItems.HUGE_BACKPACK.get() && JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_HUGE_BACKPACK.get())
                    || (backpack.getItem() == ModItems.ENDER_BACKPACK.get() && JobGetters.getJobLevel(player, job) >= Config.REQUIRED_LEVEL_ENDER_BACKPACK.get());
            if (!canOpenBackpack) {
                HotbarMessageHandler.sendHotbarMessage((ServerPlayer) player, TranslatableString.get("error.magic"));
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            if (backpack.getOrCreateTag().contains("Inventory")) {
                ((BackpackItemHandler) Objects.requireNonNull(data).getHandler()).deserializeNBT(Objects.requireNonNull(backpack.getTag()).getCompound("Inventory"));
                backpack.getTag().remove("Inventory");
            }
            if (data.getTier().ordinal() < itemTier.ordinal()) data.upgrade(itemTier);
            if (!backpack.is(ModItems.ENDER_BACKPACK.get())) {
                NetworkHooks.openGui(((ServerPlayer) player), new SimpleMenuProvider((windowId, playerInventory, playerEntity) -> new BackpackContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()), backpack.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
            } else {
                PlayerEnderChestContainer playerenderchestcontainer = player.getEnderChestInventory();
                NetworkHooks.openGui(((ServerPlayer) player), new SimpleMenuProvider((p_53124_, p_53125_, p_53126_) ->
                        ChestMenu.threeRows(p_53124_, p_53125_, playerenderchestcontainer), CONTAINER_TITLE));
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return this.tier.rarity;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (Screen.hasShiftDown()) {
            int level = 0;
            final Item item = stack.getItem();
            if (item == ModItems.SMALL_BACKPACK.get()) level = Config.REQUIRED_LEVEL_SMALL_BACKPACK.get();
            if (item == ModItems.MEDIUM_BACKPACK.get()) level = Config.REQUIRED_LEVEL_MEDIUM_BACKPACK.get();
            if (item == ModItems.LARGE_BACKPACK.get()) level = Config.REQUIRED_LEVEL_LARGE_BACKPACK.get();
            if (item == ModItems.HUGE_BACKPACK.get()) level = Config.REQUIRED_LEVEL_HUGE_BACKPACK.get();
            if (item == ModItems.ENDER_BACKPACK.get()) level = Config.REQUIRED_LEVEL_ENDER_BACKPACK.get();
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"));
            tooltip.add(Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Builder"));
            tooltip.add(Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + level));
        } else {
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
    }

    @Nullable
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

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if (!this.optional.isPresent())
                    this.optional = BackpackHandler.get().getCapability(this.stack);
                return this.optional.cast();
            } else
                return LazyOptional.empty();
        }
    }
}
