package me.daqem.jobsplus.common.inventory.construction;

import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.common.inventory.construction.container.ConstructionCraftingContainer;
import me.daqem.jobsplus.common.inventory.construction.container.ConstructionResultContainer;
import me.daqem.jobsplus.common.inventory.construction.slot.ConstructionResultSlot;
import me.daqem.jobsplus.common.item.*;
import me.daqem.jobsplus.init.ModBlocks;
import me.daqem.jobsplus.init.ModMenuTypes;
import me.daqem.jobsplus.init.ModRecipes;
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

import java.util.Optional;

public class ConstructionMenu extends RecipeBookMenu<ConstructionCraftingContainer> {
    public final Player player;
    private final ConstructionCraftingContainer craftSlots = new ConstructionCraftingContainer(this, getGridWidth(), getGridHeight());
    private final ConstructionResultContainer resultSlots = new ConstructionResultContainer();
    private final ContainerLevelAccess access;
    private CompoundTag enabledJobsTag;

    public ConstructionMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory);
        this.enabledJobsTag = buf.readNbt();
    }

    public ConstructionMenu(int p_39353_, Inventory p_39354_) {
        this(p_39353_, p_39354_, ContainerLevelAccess.NULL);
    }

    public ConstructionMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenuTypes.CONSTRUCTION.get(), containerId);
        this.access = access;
        this.player = inventory.player;
        this.addSlot(new ConstructionResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, -31, 116));

        for (int i = 0; i < getGridHeight(); ++i) {
            for (int j = 0; j < getGridWidth(); ++j) {
                this.addSlot(new Slot(craftSlots, j + i * getGridHeight(), 44 + j * 18, 5 + i * 18));
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

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, ConstructionCraftingContainer craftingContainer, ConstructionResultContainer resultContainer) {
        if (!level.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ConstructionCraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(ModRecipes.CONSTRUCTION_TYPE.get(), craftingContainer, level);
            if (optional.isPresent()) {
                ConstructionCraftingRecipe craftingRecipe = optional.get();
                if (resultContainer.setRecipeUsed(level, serverplayer, craftingRecipe)) {
                    if (JobGetters.getJobLevel(serverplayer, craftingRecipe.getJob()) >= craftingRecipe.getRequiredLevel()) {
                        int i = countItems(craftingContainer);
                        if (i <= 1) itemstack = craftingRecipe.assemble(craftingContainer);
                        if (i == 1) itemstack.setTag(getItemTag(craftingContainer));
                    }
                }
            }

            resultContainer.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }
    }

    public static int countItems(ConstructionCraftingContainer craftingContainer) {
        int i = 0;

        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            Item item = craftingContainer.getItem(j).getItem();
            if (item instanceof BackpackItem || item instanceof ExcavatorItem || item instanceof FarmersHoeItem ||
                    item instanceof RodItem || item instanceof HunterSwordItem || item instanceof HunterBowItem ||
                    item instanceof LumberAxeItem || item instanceof HammerItem || item instanceof ModArmorItem ||
                    item instanceof EXPJarItem) {
                ++i;
            }
        }
        return i;
    }

    public static CompoundTag getItemTag(ConstructionCraftingContainer craftingContainer) {
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            Item item = craftingContainer.getItem(j).getItem();
            if (item instanceof BackpackItem || item instanceof ExcavatorItem || item instanceof FarmersHoeItem ||
                    item instanceof RodItem || item instanceof HunterSwordItem || item instanceof HunterBowItem ||
                    item instanceof LumberAxeItem || item instanceof HammerItem || item instanceof ModArmorItem ||
                    item instanceof EXPJarItem) {
                return craftingContainer.getItem(j).getTag();
            }
        }
        return new CompoundTag();
    }

    public void slotsChanged(Container container) {
        this.access.execute((level, blockPos) -> {
            slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots);
        });
    }

    public void fillCraftSlotsStackedContents(StackedContents p_39374_) {
        this.craftSlots.fillStackedContents(p_39374_);
    }

    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public boolean recipeMatches(Recipe<? super ConstructionCraftingContainer> p_39384_) {
        return p_39384_.matches(this.craftSlots, this.player.level);
    }

    public void removed(Player p_39389_) {
        super.removed(p_39389_);
        this.access.execute((p_39371_, p_39372_) -> {
            this.clearContainer(p_39389_, this.craftSlots);
        });
    }

    public boolean stillValid(Player p_39368_) {
        return stillValid(this.access, p_39368_, ModBlocks.CONSTRUCTION_TABLE.get());
    }

    public ItemStack quickMoveStack(Player p_39391_, int p_39392_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_39392_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_39392_ == 0) {
                this.access.execute((p_39378_, p_39379_) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, p_39391_);
                });
                if (!this.moveItemStackTo(itemstack1, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (p_39392_ >= 26 && p_39392_ < 62) {
                if (!this.moveItemStackTo(itemstack1, 1, 26, false)) {
                    if (p_39392_ < 37) {
                        if (!this.moveItemStackTo(itemstack1, 53, 62, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 26, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 26, 62, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_39391_, itemstack1);
            if (p_39392_ == 0) {
                p_39391_.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack p_39381_, Slot p_39382_) {
        return p_39382_.container != this.resultSlots && super.canTakeItemForPickAll(p_39381_, p_39382_);
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getGridWidth() {
        return 5;
    }

    public int getGridHeight() {
        return 5;
    }

    public int getSize() {
        return 10;
    }

    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    public boolean shouldMoveToInventory(int p_150553_) {
        return p_150553_ != this.getResultSlotIndex();
    }

    public CompoundTag getDataTag() {
        return enabledJobsTag;
    }
}
