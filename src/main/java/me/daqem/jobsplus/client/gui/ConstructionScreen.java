package me.daqem.jobsplus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.renderer.RenderColor;
import me.daqem.jobsplus.common.crafting.ModGhostRecipe;
import me.daqem.jobsplus.common.crafting.ModPlaceRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionCraftingRecipe;
import me.daqem.jobsplus.common.crafting.construction.ConstructionRecipeType;
import me.daqem.jobsplus.common.inventory.construction.ConstructionMenu;
import me.daqem.jobsplus.common.packet.PacketMoveConstructionRecipe;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.init.ModPackets;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConstructionScreen extends AbstractContainerScreen<ConstructionMenu> implements RecipeUpdateListener, ModPlaceRecipe<Ingredient> {

    private static final ResourceLocation BACKGROUND = JobsPlus.getId("textures/gui/container/construction_gui.png");
    private static final ResourceLocation NONE = JobsPlus.getId("none");
    private static final int menuWidth = 358;
    private static final int menuHeight = 222;
    private final TreeMap<Jobs, int[]> enabledJobsData;
    private final List<ConstructionCraftingRecipe> recipes;
    private final ModGhostRecipe ghostRecipe = new ModGhostRecipe();
    private ResourceLocation selectedItemStackID;
    private Recipe<?> selectedRecipe;
    private Jobs selectedJob;
    private boolean scrollingLeft;
    private boolean scrollingRight;
    private float scrollOffsetLeft;
    private float scrollOffsetRight;
    private int selectedButtonLeft;
    private int selectedButtonRight;
    private int startIndexLeft;
    private int startIndexRight;
    private int selectedJobLevel;
    private int startX;
    private int startY;

    public ConstructionScreen(ConstructionMenu menu, Inventory inventory, Component ignoredTitle) {
        super(menu, inventory, JobsPlus.literal("Construction Table"));
        this.recipes = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor(ConstructionRecipeType.INSTANCE);
        this.enabledJobsData = generateJobsDataArray(menu);
        this.scrollingLeft = false;
        this.scrollingRight = false;
        this.scrollOffsetLeft = 0;
        this.scrollOffsetRight = 0;
        this.selectedButtonLeft = 0;
        this.selectedButtonRight = -1;
        this.startIndexLeft = 0;
        this.startIndexRight = 0;
        this.selectedItemStackID = NONE;
        this.selectedRecipe = null;
        this.selectedJob = Jobs.getJobFromInt(new ArrayList<>(this.enabledJobsData.entrySet()).get(this.selectedButtonLeft).getKey().get());
        this.selectedJobLevel = this.enabledJobsData.get(selectedJob)[0];
    }

    @NotNull
    private static TreeMap<Jobs, int[]> generateJobsDataArray(ConstructionMenu menu) {
        TreeMap<Jobs, int[]> enabledJobsData = new TreeMap<>();
        for (Jobs job : Jobs.values()) {
            int[] intArray = menu.getDataTag().getIntArray(job.name());
            if (intArray.length != 0) enabledJobsData.put(job, intArray);
        }
        return enabledJobsData;
    }

    private static void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int x, int y, float partialTicks) {
        this.startX = (this.width - menuWidth) / 2;
        this.startY = (this.height - menuHeight) / 2;
        int occurrencesLeft = this.startIndexLeft + 3;
        int occurrencesRight = this.startIndexRight + 10;

        renderBackgrnd(poseStack);
        renderJobButtons(poseStack, x, y, occurrencesLeft);
        renderCraftableItemButtons(poseStack, x, y, occurrencesRight);
        renderScrollbarLeft(poseStack);
        renderScrollbarRight(poseStack);

        textRenderMain(poseStack);
        textRenderJobButtons(poseStack, occurrencesLeft);
        textRenderCraftableItems(poseStack, occurrencesRight);

        setupGhostRecipe(poseStack, partialTicks);

        super.render(poseStack, x, y, partialTicks);
        this.renderTooltip(poseStack, x, y);
    }

    private void setupGhostRecipe(@NotNull PoseStack poseStack, float partialTicks) {
        if (selectedRecipe != null) {
            this.setupGhostRecipe(selectedRecipe, menu.slots);
            if (this.minecraft != null) {
                this.ghostRecipe.render(poseStack, this.minecraft, leftPos, topPos, true, partialTicks);
            }
        }
    }

    private Recipe<?> getRecipe() {
        Recipe<?> recipe = recipes.get(0);
        if (!selectedItemStackID.equals(JobsPlus.getId("none"))) {
            for (ConstructionCraftingRecipe constructionRecipe : recipes) {
                if (constructionRecipe.getId() == selectedItemStackID) {
                    recipe = constructionRecipe;
                    break;
                }
            }
        }
        return recipe;
    }

    private void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        ItemStack itemstack = recipe.getResultItem();
        this.ghostRecipe.setRecipe(recipe);
        this.ghostRecipe.clearIngredients();
        this.ghostRecipe.addIngredient(Ingredient.of(itemstack), (slots.get(0)).x, (slots.get(0)).y, slots.get(0));
        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);
    }

    @Override
    public boolean mouseClicked(double x, double y, int clickType) {
        this.scrollingLeft = false;
        this.scrollingRight = false;

        if (clickJobButton(x, y)) return true;
        if (clickCraftableItemButton(x, y)) return true;
        if (clickScrollbarLeft(x, y)) return true;
        if (clickScrollbarRight(x, y)) return true;

        return super.mouseClicked(x, y, clickType);
    }

    private boolean clickScrollbarRight(double x, double y) {
        if (isBetween(x, y, 345, 17, 351, 119)) {
            this.scrollingRight = true;
            return true;
        }
        return false;
    }

    private boolean clickScrollbarLeft(double x, double y) {
        if (isBetween(x, y, 345 - 234, 17, 351 - 234, 119)) {
            this.scrollingLeft = true;
            return true;
        }
        return false;
    }

    private boolean clickCraftableItemButton(double x, double y) {
        int l = 241;
        int m = 16;
        int n = this.startIndexRight + 10;
        for (int o = this.startIndexRight; o < n; ++o) {
            int i1 = o - this.startIndexRight;
            double d0 = x - (double) (l + i1 % 2 * 50);
            double d1 = y - (double) (m + i1 / 2 * 21);
            if (isBetween(d0, d1, 0, 0, 50, 21)) {
                ResourceLocation clickedStack;
                selectedButtonRight = o;
                try {
                    clickedStack = getRecipesForJob(selectedJob).get(selectedButtonRight).getId();
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
                selectedItemStackID = clickedStack;
                Recipe<?> recipe = getRecipe();
                if (selectedRecipe == recipe) {
                    CompoundTag recipeData = new CompoundTag();
                    for (int i = 0; i < this.ghostRecipe.getIngredients().size(); i++) {
                        if (i != 0) {
                            ModGhostRecipe.GhostIngredient ghostIngredient = this.ghostRecipe.get(i);
                            recipeData.putString(String.valueOf(ghostIngredient.getSlot().index), ghostIngredient.getItem().getDescriptionId());
                        }
                    }
                    ModPackets.INSTANCE.sendToServer(new PacketMoveConstructionRecipe(hasShiftDown(), recipeData));
                } else {
                    selectedRecipe = recipe;
                }
                playClickSound();
                return true;
            }
        }
        return false;
    }

    private boolean clickJobButton(double x, double y) {
        int i = 7;
        int j = 16;
        int k = this.startIndexLeft + 3;

        if (isBetween(x, y, i, j, 100 + i, 35 * 3 + j)) {
            for (int l = this.startIndexLeft; l < k; ++l) {
                try {
                    this.selectedButtonLeft = (int) (y - j - startY) / 35 + l;
                    playClickSound();
                    this.selectedJob = Jobs.getJobFromInt(new ArrayList<>(this.enabledJobsData.entrySet()).get(this.selectedButtonLeft).getKey().get());
                    this.scrollOffsetRight = 0;
                    this.startIndexRight = 0;
                    this.selectedItemStackID = NONE;
                    this.selectedRecipe = null;
                    this.selectedButtonRight = -1;
                    this.selectedJobLevel = this.enabledJobsData.get(selectedJob)[0];
                    return true;
                } catch (IndexOutOfBoundsException ignore) {
                }
            }
        }
        return false;
    }

    private void renderBackgrnd(@NotNull PoseStack poseStack) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderColor.normal();
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blitThis(poseStack, 0, 0, 0, 0, 358, 222);
    }

    private void textRenderMain(@NotNull PoseStack poseStack) {
        font.draw(poseStack, "Jobs", startX + 8, startY + 6, 4210752);
        font.draw(poseStack, "Craftable Items", startX + 242, startY + 6, 4210752);
    }

    private void textRenderCraftableItems(@NotNull PoseStack poseStack, int occurrencesRight) {
        for (int i = this.startIndexRight; i < occurrencesRight && i < getRecipesForJob(selectedJob).size(); ++i) {
            int xOffset = startX + 241 + i % 2 * 50;
            int l = i / 2;
            int yOffset = startY + 16 + l * 21 - (startIndexRight / 2 * 21);
            int requiredLevel = getRequiredLevelForID(getRecipesForJob(selectedJob).get(i).getId());
            final ItemStack stack = getRecipesForJob(selectedJob).get(i).getResultItem();
            itemRenderer.renderAndDecorateFakeItem(stack, xOffset + 3, yOffset + 3);
            poseStack.pushPose();
            poseStack.scale(0.6F, 0.6F, 0.6F);
            int color = selectedJobLevel >= requiredLevel ? 5635925 : 16711680;
            font.draw(poseStack, "Required", (int) ((xOffset + 21) * (1 / 0.6)), (int) ((yOffset + 6) * (1 / 0.6)), color);
            font.draw(poseStack, "LVL: " + ChatColor.white() + requiredLevel, (int) ((xOffset + 21) * (1 / 0.6)), (int) ((yOffset + 12) * (1 / 0.6)), color);
            poseStack.popPose();
        }
    }

    private void textRenderJobButtons(@NotNull PoseStack poseStack, int occurrencesLeft) {
        ArrayList<Integer> ints = new ArrayList<>();
        for (Map.Entry<Jobs, int[]> jobsEntry : enabledJobsData.entrySet()) {
            ints.add(jobsEntry.getKey().get());
            ints.add(jobsEntry.getValue()[0]);
            ints.add(jobsEntry.getValue()[1]);
        }
        for (int i = this.startIndexLeft; i < occurrencesLeft && i < enabledJobsData.size(); ++i) {
            int i1 = 16 + (i - this.startIndexLeft) * 35;
            if (i < ints.size() / 3) {
                int[] currentJobArray = new int[]{ints.get(i * 3), ints.get(i * 3 + 1), ints.get(i * 3 + 2)};
                int level = currentJobArray[1];
                int exp = currentJobArray[2];
                int maxExp = LevelHandler.calcExp(level);
                font.draw(poseStack, ChatColor.boldGreen() + Jobs.getString(currentJobArray[0]), startX + 10, startY + i1 + 3, 16777215);
                font.draw(poseStack, ChatColor.aqua() + JobsPlus.translatable("gui.level", ChatColor.reset(), level).getString(), startX + 10, startY + i1 + 14, 16777215);
                font.draw(poseStack, ChatColor.aqua() + JobsPlus.translatable("gui.exp", ChatColor.reset(), (int) ((double) exp / maxExp * 100), "%").getString(), startX + 10, startY + i1 + 23, 16777215);
            }
        }
    }

    private void renderScrollbarRight(@NotNull PoseStack poseStack) {
        blitThis(poseStack, 345, (int) (17 + (88.0F * this.scrollOffsetRight)), 0, 222, 7, 15);
    }

    private void renderScrollbarLeft(@NotNull PoseStack poseStack) {
        if (getOffscreenRowsLeft() <= 4) {
            blitThis(poseStack, 111, (int) (17 + (41.0F * this.scrollOffsetLeft)), 0, 222, 7, 15);
            blitThis(poseStack, 111, (int) (17 + 11 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
            blitThis(poseStack, 111, (int) (17 + 17 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
        }
        if (getOffscreenRowsLeft() <= 3) {
            blitThis(poseStack, 111, (int) (17 + 19 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
        }
        if (getOffscreenRowsLeft() <= 2) {
            blitThis(poseStack, 111, (int) (17 + 29 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
        }
        if (getOffscreenRowsLeft() <= 1) {
            blitThis(poseStack, 111, (int) (17 + 41 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
            blitThis(poseStack, 111, (int) (17 + 47 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
        }
        if (getOffscreenRowsLeft() <= 0) {
            blitThis(poseStack, 111, (int) (17 + 59 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
            blitThis(poseStack, 111, (int) (17 + 71 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
            blitThis(poseStack, 111, (int) (17 + 83 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
            blitThis(poseStack, 111, (int) (17 + 89 + (41.0F * this.scrollOffsetLeft)), 0, 223, 7, 15);
        }
    }

    private void renderCraftableItemButtons(@NotNull PoseStack poseStack, int x, int y, int occurrencesRight) {
        for (int i = this.startIndexRight; i < occurrencesRight && i < getRecipesForJob(selectedJob).size(); ++i) {
            int requiredLevel = getRequiredLevelForID(getRecipesForJob(selectedJob).get(i).getId());
            int xOffset = 241 + i % 2 * 50;
            int l = i / 2;
            int yOffset = 16 + l * 21 - (startIndexRight / 2 * 21);
            if (isBetween(x, y, xOffset, yOffset, xOffset + 49, yOffset + 20)) {
                RenderSystem.setShaderColor(0.85F, 0.85F, 0.85F, 1.0F);
                if (selectedJobLevel < requiredLevel)
                    RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
            } else if (selectedJobLevel < requiredLevel)
                RenderSystem.setShaderColor(0.65F, 0.65F, 0.65F, 1.0F);
            if (i == selectedButtonRight) {
                RenderSystem.setShaderColor(0.7F, 1F, 0.7F, 1);
                if (selectedJobLevel < requiredLevel) {
                    RenderSystem.setShaderColor(1F, 0.7F, 0.7F, 1);
                }
            }
            blitSmallSquare(poseStack, xOffset, yOffset);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1);
        }
        RenderColor.normal();
    }

    private void renderJobButtons(@NotNull PoseStack poseStack, int x, int y, int occurrencesLeft) {
        for (int i = this.startIndexLeft; i < occurrencesLeft && i < enabledJobsData.size(); ++i) {
            int i1 = 16 + (i - this.startIndexLeft) * 35;
            if (isBetween(x, y, 7, i1, 106, i1 + 35)) {
                RenderSystem.setShaderColor(0.85F, 0.85F, 0.85F, 1.0F);
            } else RenderColor.normal();
            if (i == this.selectedButtonLeft) RenderColor.grayedOutSelected();
            blitThis(poseStack, 7, i1, 0, 240, 100, 35);
        }
        RenderColor.normal();
    }

    @Override
    protected void renderTooltip(@NotNull PoseStack poseStack, int x, int y) {
        //JOB ITEM LIST
        int l = 241;
        int m = 16;
        int n = this.startIndexRight + 10;

        for (int o = this.startIndexRight; o < n; ++o) {
            int i1 = o - this.startIndexRight;
            double d0 = l + i1 % 2 * 50;
            //noinspection IntegerDivisionInFloatingPointContext
            double d1 = m + i1 / 2 * 21;
            if (isBetween(x, y, (int) d0, (int) d1, (int) d0 + 49, (int) d1 + 20)) {
                ItemStack clickedStack;
                try {
                    clickedStack = getRecipesForJob(selectedJob).get(o).getResultItem();
                } catch (IndexOutOfBoundsException e) {
                    super.renderTooltip(poseStack, x, y);
                    return;
                }
                super.renderTooltip(poseStack, clickedStack.copy(), x, y);
                return;
            }
        }
        super.renderTooltip(poseStack, x, y);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double p_94688_) {
        if (isBetween(x, y, -startX, -startY, -startX + width / 2, -startY + height)) {
            if (isScrollBarLeftActive()) {
                int i = this.getOffscreenRowsLeft();
                float f = (float) p_94688_ / (float) i;
                this.scrollOffsetLeft = Mth.clamp(this.scrollOffsetLeft - f, 0.0F, i == 1 ? 1.03F : i == 2 ? 1.47F : i == 3 ? 1.73F : 1.78F);
                this.startIndexLeft = (int) ((double) (this.scrollOffsetLeft * (float) i) + 0.5D);
            }
        } else {
            if (this.isScrollBarRightActive()) {
                int i = this.getOffscreenRowsRight();
                float f = (float) p_94688_ / (float) i;
                this.scrollOffsetRight = Mth.clamp(this.scrollOffsetRight - f, 0.0F, 1.0F);
                this.startIndexRight = (int) ((double) (this.scrollOffsetRight * (float) i) + 0.5D) * 2;

            }
        }
        return super.mouseScrolled(x, y, p_94688_);
    }

    @Override
    public boolean mouseDragged(double x, double y, int p_97754_, double p_97755_, double p_97756_) {
        if (isBetween(x, y, -startX, -startY, -startX + width / 2, -startY + height)) {
            if (this.scrollingLeft && this.isScrollBarLeftActive()) {
                int i = startY + 17;
                int j = i + 102;
                this.scrollOffsetLeft = ((float) y - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
                this.scrollOffsetLeft = Mth.clamp(this.scrollOffsetLeft, 0.0F, i == 1 ? 1.03F : i == 2 ? 1.47F : i == 3 ? 1.73F : 1.78F);
                this.startIndexLeft = (int) ((double) (this.scrollOffsetLeft * (float) this.getOffscreenRowsLeft()) + 0.5D);
                return true;
            } else {
                return super.mouseDragged(x, y, p_97754_, p_97755_, p_97756_);
            }
        } else {
            if (this.scrollingRight && this.isScrollBarRightActive()) {
                int i = startY + 17;
                int j = i + 102;
                this.scrollOffsetRight = ((float) y - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
                this.scrollOffsetRight = Mth.clamp(this.scrollOffsetRight, 0.0F, 1.0F);
                this.startIndexRight = (int) ((double) (this.scrollOffsetRight * (float) this.getOffscreenRowsRight()) + 0.5D) * 2;
                return true;
            } else {
                return super.mouseDragged(x, y, p_97754_, p_97755_, p_97756_);
            }
        }
    }

    private boolean isScrollBarLeftActive() {
        return enabledJobsData.size() > 3;
    }

    private boolean isScrollBarRightActive() {
        return getRecipesForJob(selectedJob).size() > 10;
    }

    protected int getOffscreenRowsLeft() {
        int i = enabledJobsData.size();
        return i <= 3 ? 0 : i == 4 ? 1 : i == 5 ? 2 : i == 6 ? 2 : i == 7 ? 3 : i == 8 ? 3 : i == 9 ? 4 : i == 10 ? 4 : 0;
    }

    protected int getOffscreenRowsRight() {
        return (int) Math.ceil(((double) getRecipesForJob(selectedJob).size() - 10D) / 2D);
    }

    public boolean isBetween(double mouseX, double mouseY, int mouseXTop, int mouseYTop, int mouseXBottom, int mouseYBottom) {
        return mouseX >= startX + mouseXTop && mouseY >= startY + mouseYTop && mouseX <= startX + mouseXBottom && mouseY <= startY + mouseYBottom;
    }

    public void blitThis(PoseStack poseStack, int posX, int posY, int startX, int startY, int stopX, int stopY) {
        blit(poseStack, this.startX + posX, this.startY + posY, startX, startY, stopX, stopY, 358, 280);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int x, int y) {
    }

    private void blitSmallSquare(PoseStack poseStack, int x, int y) {
        blitThis(poseStack, x, y, 0, 240, 25, 11);
        blitThis(poseStack, x + 25, y, 75, 240, 25, 11);
        blitThis(poseStack, x, y + 10, 0, 240 + 24, 25, 11);
        blitThis(poseStack, x + 25, y + 10, 75, 240 + 24, 25, 11);
    }

    private int getRequiredLevelForID(ResourceLocation id) {
        for (ConstructionCraftingRecipe recipe : this.recipes) {
            if (recipe.getId().equals(id)) return recipe.getRequiredLevel();
        }
        return 100;
    }

    private ArrayList<ConstructionCraftingRecipe> getRecipesForJob(Jobs job) {
        ArrayList<ConstructionCraftingRecipe> list = new ArrayList<>();
        for (ConstructionCraftingRecipe recipe : recipes) {
            if (recipe.getJob().equals(job)) list.add(recipe);
        }
        list.sort(Comparator.comparing(ConstructionCraftingRecipe::getId));
        list.sort(Comparator.comparing(ConstructionCraftingRecipe::getRequiredLevel));
        return list;
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTicks, int x, int y) {
    }

    @Override
    public void addItemToSlot(Iterator<Ingredient> p_100338_, int p_100339_, int p_100340_, int p_100341_, int p_100342_) {
        Ingredient ingredient = p_100338_.next();
        if (!ingredient.isEmpty()) {
            Slot slot = this.menu.slots.get(p_100339_);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y, slot);
        }
    }

    @Override
    public void recipesUpdated() {
        new RecipeBookComponent().recipesUpdated();
    }

    @Override
    public @NotNull RecipeBookComponent getRecipeBookComponent() {
        return new RecipeBookComponent();
    }
}
