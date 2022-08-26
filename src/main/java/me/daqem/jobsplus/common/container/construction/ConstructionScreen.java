package me.daqem.jobsplus.common.container.construction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.SideProxy;
import me.daqem.jobsplus.client.gui.JobsScreen;
import me.daqem.jobsplus.client.renderer.RenderColor;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.packet.PacketConstructionArrows;
import me.daqem.jobsplus.packet.PacketCraftItem;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobItemEntry;
import me.daqem.jobsplus.utils.JobItemEntryHelper;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.daqem.jobsplus.client.SelectionHelper.Construction.*;

public class ConstructionScreen extends AbstractContainerScreen<ConstructionMenu> {

    private static final ResourceLocation TEXTURE = JobsPlus.getId("textures/gui/container/construction_gui.png");
    private static final int menuWidth = 358;
    private static final int menuHeight = 222;
    private final TreeMap<Jobs, int[]> enabledJobsData;
    private final ArrayList<JobItemEntry> recipeList;
    private final Map<ItemStack, Integer> requiredLevelsForStacksList;
    private final Inventory inventory;
    private ArrayList<Object> currentShownItemsArray;
    private ItemStack selectedItemStack;
    private Jobs selectedJob;
    private boolean leftArrowEnabled;
    private boolean rightArrowEnabled;
    private boolean scrollingLeft;
    private boolean scrollingRight;
    private boolean craftable;
    private float scrollOffsetLeft;
    private float scrollOffsetRight;
    private int selectedButtonLeft;
    private int selectedButtonRight;
    private int startIndexLeft;
    private int startIndexRight;
    private int requiredLevelForSelectedStack;
    private int selectedJobLevel;
    private int startX;
    private int startY;

    public ConstructionScreen(ConstructionMenu menu, Inventory inventory, Component ignoredTitle) {
        super(menu, inventory, Component.literal("Construction Table"));

        TreeMap<Jobs, int[]> enabledJobsData = new TreeMap<>();
        for (Jobs job : Jobs.values()) {
            int[] intArray = menu.getDataTag().getIntArray(job.name());
            if (intArray.length != 0) enabledJobsData.put(job, intArray);
        }

        this.enabledJobsData = enabledJobsData;
        this.recipeList = SideProxy.Client.getEntries();
        this.requiredLevelsForStacksList = JobItemEntryHelper.getRequiredLevelsForStacks(recipeList);
        this.inventory = inventory;
        this.leftArrowEnabled = false;
        this.rightArrowEnabled = false;
        this.scrollingLeft = false;
        this.scrollingRight = false;
        this.craftable = false;
        this.scrollOffsetLeft = 0;
        this.scrollOffsetRight = 0;
        this.selectedButtonLeft = 0;
        this.selectedButtonRight = -1;
        this.startIndexLeft = 0;
        this.startIndexRight = 0;
        this.selectedItemStack = ItemStack.EMPTY;
        this.selectedJob = Jobs.getJobFromInt(new ArrayList<>(this.enabledJobsData.entrySet()).get(this.selectedButtonLeft).getKey().get());
        this.selectedJobLevel = this.enabledJobsData.get(selectedJob)[0];

        setCurrentShownItemsArray();
    }

    private static void sendClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int x, int y, float partialTicks) {
        this.startX = (this.width - menuWidth) / 2;
        this.startY = (this.height - menuHeight) / 2;
        this.leftArrowEnabled = this.selectedItemStack != ItemStack.EMPTY && (this.selectedJobLevel >= this.requiredLevelForSelectedStack);
        this.rightArrowEnabled = !this.menu.getConstructingSlots().isEmpty();
        int occurrencesLeft = this.startIndexLeft + 3;
        int occurrencesRight = this.startIndexRight + 10;

        renderBackgrnd(poseStack);
        renderCraftButton(poseStack, x, y);
        renderArrowButton(poseStack, isOnLeftArrowButton(x, y, this.startX, this.startY), this.leftArrowEnabled, 127, 71);
        renderArrowButton(poseStack, isOnRightArrowButton(x, y, this.startX, this.startY), this.rightArrowEnabled, 213, 89);
        renderJobButtons(poseStack, x, y, occurrencesLeft);
        renderCraftableItemButtons(poseStack, x, y, occurrencesRight);
        renderScrollbarLeft(poseStack);
        renderScrollbarRight(poseStack);

        textRenderMain(poseStack);
        textRenderJobButtons(poseStack, occurrencesLeft);
        textRenderCraftableItems(poseStack, occurrencesRight);

        checkAndSetCraftable();

        super.render(poseStack, x, y, partialTicks);
        this.renderItems(poseStack);
        this.renderTooltip(poseStack, x, y);
    }

    @Override
    public boolean mouseClicked(double x, double y, int clickType) {
        this.scrollingLeft = false;
        this.scrollingRight = false;

        if (clickCraftItemButton(x, y)) return true;
        if (clickArrow(isOnLeftArrowButton(x, y, this.startX, this.startY), this.leftArrowEnabled, true)) return true;
        if (clickArrow(isOnRightArrowButton(x, y, this.startX, this.startY), this.rightArrowEnabled, false))
            return true;
        if (clickJobButton(x, y)) return true;
        if (clickCraftableItemButton(x, y)) return true;
        if (clickScrollbarLeft(x, y)) return true;
        if (clickScrollbarRight(x, y)) return true;

        return super.mouseClicked(x, y, clickType);
    }

    private boolean clickArrow(boolean x, boolean flag, boolean left) {
        if (x && flag) {
            sendClickSound();
            ModPacketHandler.INSTANCE.sendToServer(new PacketConstructionArrows(left, selectedJob.name(), selectedButtonRight, hasShiftDown()));
            return true;
        }
        return false;
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
                ItemStack clickedStack;
                try {
                    clickedStack = JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList).get(o);
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
                selectedButtonRight = o;
                selectedItemStack = clickedStack;
                if (selectedItemStack != ItemStack.EMPTY) {
                    int requiredLevel = 100;
                    for (Map.Entry<ItemStack, Integer> entry : requiredLevelsForStacksList.entrySet()) {
                        ItemStack itemStack1 = entry.getKey();
                        int integer = entry.getValue();
                        if (itemStack1.equals(selectedItemStack, true))
                            requiredLevel = integer;
                    }
                    requiredLevelForSelectedStack = requiredLevel;
                }
                sendClickSound();
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
                    sendClickSound();
                    this.selectedJob = Jobs.getJobFromInt(new ArrayList<>(this.enabledJobsData.entrySet()).get(this.selectedButtonLeft).getKey().get());
                    this.scrollOffsetRight = 0;
                    this.startIndexRight = 0;
                    this.craftable = false;
                    this.selectedItemStack = ItemStack.EMPTY;
                    this.selectedButtonRight = -1;
                    setCurrentShownItemsArray();
                    this.selectedJobLevel = this.enabledJobsData.get(selectedJob)[0];
                    return true;
                } catch (IndexOutOfBoundsException ignore) {
                }
            }
        }
        return false;
    }

    private boolean clickCraftItemButton(double x, double y) {
        if (isOnCraftItemButton(x, y, startX, startY) && craftable && (inventory.getFreeSlot() != -1)
                && (selectedJobLevel >= requiredLevelForSelectedStack) && selectedJob != null) {
            sendClickSound();
            if (hasShiftDown())
                ModPacketHandler.INSTANCE.sendToServer(new PacketCraftItem(selectedItemStack, true, Objects.requireNonNull(selectedJob).toString()));
            else
                ModPacketHandler.INSTANCE.sendToServer(new PacketCraftItem(selectedItemStack, false, Objects.requireNonNull(selectedJob).toString()));
            return true;
        }
        return false;
    }

    private void renderBackgrnd(@NotNull PoseStack poseStack) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderColor.normal();
        RenderSystem.setShaderTexture(0, TEXTURE);
        blitThis(poseStack, 0, 0, 0, 0, 358, 222);
    }

    private void textRenderMain(@NotNull PoseStack poseStack) {
        font.draw(poseStack, "Jobs", startX + 8, startY + 6, 4210752);
        font.draw(poseStack, "Craftable Items", startX + 242, startY + 6, 4210752);
        JobsScreen.drawCenteredStringNew(poseStack, font, "Craft Item", this.width / 2, startY + 116, craftable ? 4210752 : 0xcccccc);
    }

    private void textRenderCraftableItems(@NotNull PoseStack poseStack, int occurrencesRight) {
        for (int i = this.startIndexRight; i < occurrencesRight && i < JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList).size(); ++i) {
            int xOffset = startX + 241 + i % 2 * 50;
            int l = i / 2;
            int yOffset = startY + 16 + l * 21 - (startIndexRight / 2 * 21);

            if (i < currentShownItemsArray.size() / 2) {
                ArrayList<Object> currentJobArray = new ArrayList<>(List.of(currentShownItemsArray.get(i * 2), currentShownItemsArray.get(i * 2 + 1)));
                int requiredLevel = (int) currentJobArray.get(1);
                final ItemStack stack = ((ItemStack) currentJobArray.get(0));
                itemRenderer.renderAndDecorateFakeItem(stack, xOffset + 3, yOffset + 3);
                poseStack.pushPose();
                poseStack.scale(0.6F, 0.6F, 0.6F);
                int color = selectedJobLevel >= requiredLevel ? 5635925 : 16711680;
                font.draw(poseStack, "Required", (int) ((xOffset + 21) * (1 / 0.6)), (int) ((yOffset + 6) * (1 / 0.6)), color);
                font.draw(poseStack, "LVL: " + ChatColor.white() + requiredLevel, (int) ((xOffset + 21) * (1 / 0.6)), (int) ((yOffset + 12) * (1 / 0.6)), color);
                poseStack.popPose();
            }
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
                font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.level", ChatColor.reset(), level).getString(), startX + 10, startY + i1 + 14, 16777215);
                font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.exp", ChatColor.reset(), (int) ((double) exp / maxExp * 100), "%").getString(), startX + 10, startY + i1 + 23, 16777215);
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
        for (int i = this.startIndexRight; i < occurrencesRight && i < JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList).size(); ++i) {
            if (i < currentShownItemsArray.size() / 2) {
                ArrayList<Object> currentJobArray = new ArrayList<>(List.of(currentShownItemsArray.get(i * 2), currentShownItemsArray.get(i * 2 + 1)));
                int requiredLevel = (int) currentJobArray.get(1);
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

    private void renderArrowButton(@NotNull PoseStack poseStack, boolean selected, boolean isEnabled, int posX, int startX) {
        if (selected) {
            if (!isEnabled) RenderColor.grayedOutSelected();
            else RenderColor.normalSelected();
        } else {
            if (!isEnabled) RenderColor.grayedOut();
            else RenderColor.normal();
        }
        blitThis(poseStack, posX, 111, startX, 222, 18, 18);
        RenderColor.normal();
    }

    private void renderCraftButton(@NotNull PoseStack poseStack, int x, int y) {
        if (isOnCraftItemButton(x, y, startX, startY)) {
            if (!craftable) RenderColor.grayedOutSelected();
            else if (inventory.getFreeSlot() == -1 || this.selectedJobLevel < this.requiredLevelForSelectedStack)
                RenderColor.redSelected();
            else RenderColor.greenSelected();
        } else {
            if (!craftable) RenderColor.grayedOut();
            else if (inventory.getFreeSlot() == -1 || this.selectedJobLevel < this.requiredLevelForSelectedStack)
                RenderColor.red();
            else RenderColor.green();
        }
        blitThis(poseStack, 147, 111, 7, 222, 64, 18);
        RenderColor.normal();
    }

    private void setCurrentShownItemsArray() {
        this.currentShownItemsArray = new ArrayList<>();
        for (ItemStack itemStack : JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList)) {
            this.currentShownItemsArray.add(itemStack);
            int level = 100;
            for (Map.Entry<ItemStack, Integer> entry : requiredLevelsForStacksList.entrySet()) {
                ItemStack itemStack1 = entry.getKey();
                int integer = entry.getValue();
                if (itemStack1.getItem() == itemStack.getItem()) {
                    if (itemStack1.getItem() == ModItems.EXPERIENCE_BOTTLE.get()) {
                        if ((itemStack.getTag() == null && itemStack1.getTag() == null)) {
                            level = integer;
                        } else if (itemStack.getTag() != null && itemStack1.getTag() != null) {
                            if (itemStack.getTag().getInt("tier") == itemStack1.getTag().getInt("tier")) {
                                level = integer;
                            }
                        }
                    } else level = integer;
                }
            }
            this.currentShownItemsArray.add(level);
        }
    }

    private void renderItems(PoseStack poseStack) {
        int count = 0;
        if (selectedItemStack == ItemStack.EMPTY) return;
        int requiredLevel = 100;
        for (Map.Entry<ItemStack, Integer> entry : requiredLevelsForStacksList.entrySet()) {
            ItemStack itemStack1 = entry.getKey();
            int integer = entry.getValue();
            if (itemStack1.equals(selectedItemStack, true))
                requiredLevel = integer;
        }
        if (selectedJobLevel < requiredLevel)
            GuiComponent.fill(poseStack, startX + 135, startY + 13, startX + 18 * 5 + 133, startY + 18 * 5 + 11, 0x30FF0000);
        for (ItemStack itemStack : getShowRecipe()) {
            if (itemStack != ItemStack.EMPTY) {
                int x1 = startX + (18 * 5) + 45 + count * 18 + (count < 5 ? 0 : count < 10 ? -18 * 5 : count < 15 ? -18 * 10 : count < 20 ? -18 * 15 : -18 * 20);
                int y1 = startY + 13 + (count < 5 ? 0 : count < 10 ? 18 : count < 15 ? 18 * 2 : count < 20 ? 18 * 3 : 18 * 4);
                if (!itemStack.equals(menu.getSlot(count + 36).getItem(), true)) {
                    if (menu.getConstructingSlots().getItem(count) == ItemStack.EMPTY) {
                        itemRenderer.renderAndDecorateFakeItem(itemStack, x1, y1);
                        RenderSystem.depthFunc(516);
                        GuiComponent.fill(poseStack, x1, y1, x1 + 16, y1 + 16, 822083583);
                        RenderSystem.depthFunc(515);
                    }
                }
            }
            ++count;
        }
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    @Override
    protected void renderTooltip(@NotNull PoseStack poseStack, int x, int y) {
        if (isBetween(x, y, 147, 111, 146 + 64, 110 + 18)) {
            if (selectedItemStack == ItemStack.EMPTY)
                super.renderTooltip(poseStack, Component.literal("No crafting recipe selected."), x, y);
            else if (selectedJobLevel < requiredLevelForSelectedStack)
                super.renderTooltip(poseStack, Component.literal("Your job level is too low."), x, y);
            else if (!craftable)
                super.renderTooltip(poseStack, Component.literal("Crafting recipe is incomplete."), x, y);
            else if (inventory.getFreeSlot() == -1)
                super.renderTooltip(poseStack, Component.literal("No empty space in your inventory."), x, y);
        }

        //JOB ITEM LIST
        int l = 241;
        int m = 16;
        int n = this.startIndexRight + 10;

        for (int o = this.startIndexRight; o < n; ++o) {
            int i1 = o - this.startIndexRight;
            double d0 = l + i1 % 2 * 50;
            double d1 = m + i1 / 2 * 21;
            if (isBetween(x, y, (int) d0, (int) d1, (int) d0 + 49, (int) d1 + 20)) {
                ItemStack clickedStack;
                try {
                    clickedStack = JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList).get(o);
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
        return enabledJobsData.size() > 4;
    }

    private boolean isScrollBarRightActive() {
        return JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList).size() > 10;
    }

    protected int getOffscreenRowsLeft() {
        int i = enabledJobsData.size();
        return i <= 3 ? 0 : i == 4 ? 1 : i == 5 ? 2 : i == 6 ? 2 : i == 7 ? 3 : i == 8 ? 3 : i == 9 ? 4 : i == 10 ? 4 : 0;
    }

    protected int getOffscreenRowsRight() {
        return (int) Math.ceil(((double) JobItemEntryHelper.getStacksForJob(Objects.requireNonNull(selectedJob), recipeList).size() - 10D) / 2D);
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

    private ArrayList<ItemStack> getShowRecipe() {
        return JobItemEntryHelper.getRecipe(this.selectedItemStack, this.recipeList);
    }

    private void checkAndSetCraftable() {
        if (this.selectedItemStack != ItemStack.EMPTY) {
            int correct = 0;
            for (int i = 36; i < 36 + 25; i++) {
                final ItemStack inMenu = this.getShowRecipe().get(i - 36);
                final ItemStack fromPlayer = this.menu.getSlot(i).getItem();
                if (inMenu.getItem() == fromPlayer.getItem()) {
                    if (fromPlayer.getItem().getDefaultInstance().equals(inMenu, true)
                            || (inMenu.getTag() == null && fromPlayer.getTag() == null)
                            || fromPlayer.equals(inMenu, true)) {

                        correct++;
                    }
                }
            }
            this.craftable = correct == 25;
        }
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTicks, int x, int y) {
    }
}
