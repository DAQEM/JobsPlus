package me.daqem.jobsplus.common.inventory;

import me.daqem.jobsplus.SideProxy;
import me.daqem.jobsplus.common.crafting.ConstructionRecipe;
import me.daqem.jobsplus.common.item.*;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModBlocks;
import me.daqem.jobsplus.init.ModMenuTypes;
import me.daqem.jobsplus.init.ModRecipes;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ConstructionMenu extends RecipeBookMenu<CraftingContainer> {

    private final CraftingContainer craftSlots = new CraftingContainer(this, 5, 5);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private CompoundTag enabledJobsTag;
    private Player player;

    //Doesn't do Server side.
    public ConstructionMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
        this.enabledJobsTag = buf.readNbt();
        this.player = inventory.player;
        if (enabledJobsTag == null || enabledJobsTag.isEmpty())
            HotbarMessageHandler.sendHotBarMessageClient(ChatColor.red() + "Press '" + SideProxy.Client.OPEN_GUI_KEYBIND.getKey().getDisplayName().getString().toUpperCase() + "' first, and start a job.");
    }

    //Does Server and Client side.
    public ConstructionMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
        super(ModMenuTypes.CONSTRUCTION.get(), containerId);
        this.access = access;
        this.player = inventory.player;
        this.addSlot(new ResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, -40, 130));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlot(new Slot(craftSlots, j + i * 5, 44 + j * 18, 5 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 112 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 170));
        }


    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (!level.isClientSide && level.getServer() != null) {
            ServerPlayer serverplayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ConstructionRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(ModRecipes.CONSTRUCTION_TYPE.get(), craftingContainer, level);
            if (optional.isPresent()) {
                ConstructionRecipe constructionRecipe = optional.get();
                if (resultContainer.setRecipeUsed(level, serverplayer, constructionRecipe)) {
                    if (JobGetters.getJobLevel(player, constructionRecipe.getJob()) >= constructionRecipe.getRequiredLevel()) {
                        int i = countItems(craftingContainer);
                        if (i <= 1) itemstack = constructionRecipe.assemble(craftingContainer);
                        if (i == 1) itemstack.setTag(getItemTag(craftingContainer));
                    }
                }
            }

            resultContainer.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }
    }

    public static int countItems(CraftingContainer craftingContainer) {
        int i = 0;

        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            Item item = craftingContainer.getItem(j).getItem();
            if (item instanceof BackpackItem || item instanceof ExcavatorItem || item instanceof FarmersHoeItem ||
                    item instanceof RodItem || item instanceof HunterSwordItem || item instanceof HunterBowItem ||
                    item instanceof LumberAxeItem || item instanceof HammerItem) {
                ++i;
            }
        }
        return i;
    }

    public static CompoundTag getItemTag(CraftingContainer craftingContainer) {
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            Item item = craftingContainer.getItem(j).getItem();
            if (item instanceof BackpackItem || item instanceof ExcavatorItem || item instanceof FarmersHoeItem ||
                    item instanceof RodItem || item instanceof HunterSwordItem || item instanceof HunterBowItem ||
                    item instanceof LumberAxeItem || item instanceof HammerItem) {
                return craftingContainer.getItem(j).getTag();
            }
        }
        return new CompoundTag();
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        this.access.execute((p_39386_, p_39387_) -> slotChangedCraftingGrid(this, p_39386_, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (i <= 35) {
                if (!this.moveItemStackTo(itemStack1, 36, 61, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (i <= 60) {
                if (!this.moveItemStackTo(itemStack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
            if (i == 0) {
                player.drop(itemStack1, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.CONSTRUCTION_TABLE.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((p_39575_, p_39576_) -> this.clearContainer(player, this.craftSlots));
    }

    public CompoundTag getDataTag() {
        return enabledJobsTag;
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents stackedContents) {
        this.craftSlots.fillStackedContents(stackedContents);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(@NotNull Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftSlots, this.player.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 26;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int i) {
        return i != this.getResultSlotIndex();
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    public Player getPlayer() {
        return player;
    }
}
