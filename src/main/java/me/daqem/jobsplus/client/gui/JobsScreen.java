package me.daqem.jobsplus.client.gui;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.init.ModEffects;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModPotions;
import me.daqem.jobsplus.jei.JobsPlusJeiPlugin;
import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JobsScreen extends Screen {

    public static final ResourceLocation BACKGROUND = JobsPlus.getId("textures/gui/jobs_screen.png");
    private final int[] array;
    private final int imageWidth = 326;
    private final int imageHeight = 166;
    ArrayList<Integer> ints = new ArrayList<>();
    private int activeRightButton;
    private int activeLeftButton;
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private int startX;
    private int startY;
    private int jobId;
    private int selectedButton;

    public JobsScreen(int[] array, int jobId, int activeLeftButton, int activeRightButton, int selectedButton, float scrollOffs, int startIndex) {
        super(new KeybindComponent("Jobs"));
        this.array = array;
        this.jobId = jobId;
        this.activeLeftButton = activeLeftButton;
        this.activeRightButton = activeRightButton;
        this.selectedButton = selectedButton;
        this.scrollOffs = scrollOffs;
        this.startIndex = startIndex;
    }

//.RRRRRRRRRR........EEEEEEEEEEE...... NNN...NNNN....... DDDDDDDD......... EEEEEEEEEE...... RRRRRRRRR....
//.RRRRRRRRRRR.......EEEEEEEEEEE...... NNNN..NNNN....... DDDDDDDDD........ EEEEEEEEEE...... RRRRRRRRRR...
//.RRRRRRRRRRR.......EEEEEEEEEEE...... NNNN..NNNN....... DDDDDDDDDD....... EEEEEEEEEE...... RRRRRRRRRR...
//.RRRR...RRRRR......EEEE............. NNNNN.NNNN....... DDD...DDDD....... EEE............. RRR...RRRRR..
//.RRRR...RRRRR......EEEE............. NNNNN.NNNN....... DDD....DDDD...... EEE............. RRR...RRRRR..
//.RRRRRRRRRRR.......EEEEEEEEEE....... NNNNNNNNNN....... DDD....DDDD...... EEEEEEEEE....... RRRRRRRRRR...
//.RRRRRRRRRRR.......EEEEEEEEEE....... NNNNNNNNNN....... DDD....DDDD...... EEEEEEEEE....... RRRRRRRRRR...
//.RRRRRRRR..........EEEEEEEEEE....... NNNNNNNNNN....... DDD....DDDD...... EEEEEEEEE....... RRRRRRR......
//.RRRR.RRRR.........EEEE............. NNNNNNNNNN....... DDD....DDDD...... EEE............. RRR.RRRR.....
//.RRRR..RRRR........EEEE............. NNN.NNNNNN....... DDD...DDDDD...... EEE............. RRR..RRRR....
//.RRRR..RRRRR.......EEEEEEEEEEE...... NNN..NNNNN....... DDDDDDDDDD....... EEEEEEEEEE...... RRR..RRRRR...
//.RRRR...RRRRR......EEEEEEEEEEE...... NNN..NNNNN....... DDDDDDDDD........ EEEEEEEEEE...... RRR...RRRRR..
//.RRRR....RRRR......EEEEEEEEEEE...... NNN...NNNN....... DDDDDDDD......... EEEEEEEEEE...... RRR....RRRR..

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        startX = (this.width - imageWidth) / 2;
        startY = (this.height - imageHeight) / 2;
        blitThis(poseStack, 0, 0, 0, 0, imageWidth, imageHeight);
        int k = (int) (41.0F * this.scrollOffs);
        blitThis(poseStack, 127, 17 + k, 0, 207, 12, 15);
        int l = this.startX + 7;
        int i1 = this.startY + 16;
        int j1 = this.startIndex + 4;
        this.renderButtons(poseStack, mouseX, mouseY, l, i1, j1);
        this.renderTooltip(poseStack, mouseX, mouseY);
        this.renderItems(startX, startY);
        drawTexts(poseStack, i1, j1);
        poseStack.popPose();
        font.draw(poseStack, new KeybindComponent(ChatColor.darkGray() + "Jobs               Coins: " + array[20]).copy(),
                startX + 7, startY + 6, 16777215);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

//.TTTTTTTTTTT.......OOOOOOO............OOOOOOO..........LLLL............TTTTTTTTTTT.... IIII......PPPPPPPPP.........SSSSSSS.....
//.TTTTTTTTTTT......OOOOOOOOOO.........OOOOOOOOOO........LLLL............TTTTTTTTTTT.... IIII......PPPPPPPPPP....... SSSSSSSS....
//.TTTTTTTTTTT.....OOOOOOOOOOOO.......OOOOOOOOOOOO.......LLLL............TTTTTTTTTTT.... IIII......PPPPPPPPPPP...... SSSSSSSSS...
//....TTTT.........OOOOO..OOOOO.......OOOOO..OOOOO.......LLLL...............TTTT........ IIII......PPPP...PPPP..... SSS..SSSS...
//....TTTT........ OOOO....OOOOO..... OOOO....OOOOO......LLLL...............TTTT........ IIII......PPPP...PPPP..... SSS.........
//....TTTT........ OOO......OOOO..... OOO......OOOO......LLLL...............TTTT........ IIII......PPPPPPPPPPP...... SSSSSS......
//....TTTT........ OOO......OOOO..... OOO......OOOO......LLLL...............TTTT........ IIII......PPPPPPPPPP........SSSSSSSSS...
//....TTTT........ OOO......OOOO..... OOO......OOOO......LLLL...............TTTT........ IIII......PPPPPPPPP...........SSSSSSS...
//....TTTT........ OOOO....OOOOO..... OOOO....OOOOO......LLLL...............TTTT........ IIII......PPPP...................SSSSS..
//....TTTT.........OOOOO..OOOOO.......OOOOO..OOOOO.......LLLL...............TTTT........ IIII......PPPP............ SS....SSSS..
//....TTTT.........OOOOOOOOOOOO.......OOOOOOOOOOOO.......LLLLLLLLLL.........TTTT........ IIII......PPPP............ SSSSSSSSSS..
//....TTTT..........OOOOOOOOOO.........OOOOOOOOOO........LLLLLLLLLL.........TTTT........ IIII......PPPP............. SSSSSSSSS...
//....TTTT............OOOOOO.............OOOOOO..........LLLLLLLLLL.........TTTT........ IIII......PPPP..............SSSSSSSS....

    protected void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        mouseX = mouseX - startX;
        mouseY = mouseY - startY;
        if (isBetween(mouseX, mouseY, 6, -22, 32, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.all_jobs"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28, -22, 32 + 28, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.performing_jobs"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28, -22, 32 + 28 + 28, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.not_performing_jobs"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 150, -22, 32 + 150, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.job_info"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 150, -22, 32 + 28 + 150, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.job_crafting_recipes"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 150, -22, 32 + 28 + 28 + 150, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.job_powerups"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 28 + 150, -22, 32 + 28 + 28 + 28 + 150, 0)) {
            super.renderTooltip(poseStack, new TranslatableComponent("jobsplus.gui.job_how_to_get_exp"), mouseX + startX, mouseY + startY);
        }
    }

//.BBBBBBBBBB........UUUU...UUUU.......TTTTTTTTTTT.....TTTTTTTTTTT.......OOOOOOO..........NNNN...NNNN........SSSSSSS.....
//.BBBBBBBBBBB.......UUUU...UUUU.......TTTTTTTTTTT.....TTTTTTTTTTT......OOOOOOOOOO........NNNNN..NNNN.......SSSSSSSSS....
//.BBBBBBBBBBB.......UUUU...UUUU.......TTTTTTTTTTT.....TTTTTTTTTTT.....OOOOOOOOOOOO.......NNNNN..NNNN.......SSSSSSSSSS...
//.BBBB...BBBB.......UUUU...UUUU..........TTTT............TTTT.........OOOOO..OOOOO.......NNNNNN.NNNN...... SSSS..SSSS...
//.BBBB...BBBB.......UUUU...UUUU..........TTTT............TTTT........OOOOO....OOOOO......NNNNNN.NNNN...... SSSS.........
//.BBBBBBBBBBB.......UUUU...UUUU..........TTTT............TTTT........OOOO......OOOO......NNNNNNNNNNN.......SSSSSSS......
//.BBBBBBBBBB........UUUU...UUUU..........TTTT............TTTT........OOOO......OOOO......NNNNNNNNNNN........SSSSSSSSS...
//.BBBBBBBBBBB.......UUUU...UUUU..........TTTT............TTTT........OOOO......OOOO......NNNNNNNNNNN..........SSSSSSS...
//.BBBB....BBBB......UUUU...UUUU..........TTTT............TTTT........OOOOO....OOOOO......NNNNNNNNNNN.............SSSSS..
//.BBBB....BBBB......UUUU...UUUU..........TTTT............TTTT.........OOOOO..OOOOO.......NNNN.NNNNNN...... SSS....SSSS..
//.BBBBBBBBBBBB......UUUUUUUUUUU..........TTTT............TTTT.........OOOOOOOOOOOO.......NNNN..NNNNN...... SSSSSSSSSSS..
//.BBBBBBBBBBB........UUUUUUUUU...........TTTT............TTTT..........OOOOOOOOOO........NNNN..NNNNN.......SSSSSSSSSS...
//.BBBBBBBBBB..........UUUUUUU............TTTT............TTTT............OOOOOO..........NNNN...NNNN........SSSSSSSS....

    public void renderButtons(PoseStack poseStack, int mouseX, int mouseY, int p_99345_, int something, int occurrences) {
        // JOB BUTTONS
        if (activeLeftButton == 0) {
            for (int i = this.startIndex; i < occurrences && i < Jobs.values().length; ++i) {
                int j = i - this.startIndex;
                int k = p_99345_ + j % 1 * (16 * 4);
                int i1 = something + j * 35;
                int j1 = this.imageHeight;
                if (mouseX >= k && mouseY >= i1 && mouseX < k + 116 && mouseY < i1 + 35) {
                    if (i != this.selectedButton) j1 += 35;
                }
                if (i == this.selectedButton) blitThis(poseStack, 7, i1 - startY, 26, j1 + 35 * 2, 116, 35);
                else blitThis(poseStack, 7, i1 - startY, 26, j1, 116, 35);
            }
        }
        //JOB BUTTONS
        if (activeLeftButton == 1 || activeLeftButton == 2) {
            Map<Jobs, int[]> map = new HashMap<>();
            for (int i = 0; i < Jobs.values().length; ++i) {
                int level = array[i * 2];
                int exp = array[i * 2 + 1];
                if (activeLeftButton == 1) {
                    if (level != 0) {
                        map.put(Jobs.valueOf(Jobs.getString(i)), new int[]{level, exp});
                    }
                }
                if (activeLeftButton == 2) {
                    if (level == 0) {
                        map.put(Jobs.valueOf(Jobs.getString(i)), new int[]{level, exp});
                    }
                }
            }
            for (int i = this.startIndex; i < occurrences && i < map.size(); ++i) {
                int j = i - this.startIndex;
                int k = p_99345_ + j % 1 * (16 * 4);
                int i1 = something + j * 35;
                int j1 = this.imageHeight;
                if (mouseX >= k && mouseY >= i1 && mouseX < k + 116 && mouseY < i1 + 35) {
                    if (i != this.selectedButton) j1 += 35;
                }
                if (i == this.selectedButton) blitThis(poseStack, 7, i1 - startY, 26, j1 + 35 * 2, 116, 35);
                else blitThis(poseStack, 7, i1 - startY, 26, j1, 116, 35);
            }
        }
        // LEFT BUTTONS
        for (int i = 0; i < 3; ++i) {
            int posX = 6 + (28 * i);
            if (activeLeftButton != i && isBetween(mouseX - startX, mouseY - startY, posX, -19, posX + 25, 0)) {
                poseStack.pushPose();
                RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                blitThis(poseStack, posX, -22, 142, activeLeftButton == i ? imageHeight + 22 : imageHeight, 26, 22);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                poseStack.popPose();
            } else {
                blitThis(poseStack, posX, -22, 142, activeLeftButton == i ? imageHeight + 22 : imageHeight, 26, 22);
            }
            blitThis(poseStack, posX + 3, -21, 0, imageHeight + 56 + (20 * i), 20, 20);
        }
        //RIGHT BUTTONS
        for (int i = 0; i < 4; ++i) {
            int posX = 156 + (28 * i);
            if (activeRightButton != i && isBetween(mouseX - startX, mouseY - startY, posX, -19, posX + 25, 0)) {
                poseStack.pushPose();
                RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                blitThis(poseStack, posX, -22, 142, activeRightButton == i ? imageHeight + 22 : imageHeight, 26, 22);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                poseStack.popPose();
            } else {
                blitThis(poseStack, posX, -22, 142, activeRightButton == i ? imageHeight + 22 : imageHeight, 26, 22);
            }
            blitThis(poseStack, posX + 3, -21, 0, imageHeight + 56 + 60 + (20 * i), 20, 20);
        }
        //JOB BUTTONS
        if (activeLeftButton == 0) {
            for (int i = this.startIndex; i < occurrences && i < Jobs.values().length; ++i) {
                int level = array[i * 2];
                int j = i - this.startIndex;
                int i1 = something - startY + j * 35 - 1;

                if (level >= 75) blitThis(poseStack, 7 + 5, i1 + 5, 168 + (27 * 3), imageHeight, 27, 27);
                else if (level >= 50) blitThis(poseStack, 7 + 5, i1 + 5, 168 + (27 * 2), imageHeight, 27, 27);
                else if (level >= 25) blitThis(poseStack, 7 + 5, i1 + 5, 168 + 27, imageHeight, 27, 27);
                else blitThis(poseStack, 7 + 5, i1 + 5, 168, imageHeight, 27, 27);

                blitThis(poseStack, 7 + 3, i1 + 3, imageWidth, (i * 31), 31, 27);
            }
        }
        //JOB BUTTONS
        if (activeLeftButton == 1 || activeLeftButton == 2) {
            ints = new ArrayList<>();
            for (int i = 0; i < Jobs.values().length; ++i) {
                int level = array[i * 2];
                int exp = array[i * 2 + 1];
                if (activeLeftButton == 1) {
                    if (level != 0) {
                        ints.add(level);
                        ints.add(exp);
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getString(i))));
                    }
                }
                if (activeLeftButton == 2) {
                    if (level == 0) {
                        ints.add(level);
                        ints.add(exp);
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getString(i))));
                    }
                }
            }
            for (int i = this.startIndex; i < occurrences; ++i) {
                if (i < ints.size() / 3) {
                    int[] currentJobArray = new int[]{ints.get(i * 3), ints.get(i * 3 + 1), ints.get(i * 3 + 2)};

                    int level = currentJobArray[0];
                    int j = i - this.startIndex;
                    int i1 = something - startY + j * 35 - 1;

                    if (level >= 75) blitThis(poseStack, 7 + 5, i1 + 5, 168 + (27 * 3), imageHeight, 27, 27);
                    else if (level >= 50) blitThis(poseStack, 7 + 5, i1 + 5, 168 + (27 * 2), imageHeight, 27, 27);
                    else if (level >= 25) blitThis(poseStack, 7 + 5, i1 + 5, 168 + 27, imageHeight, 27, 27);
                    else blitThis(poseStack, 7 + 5, i1 + 5, 168, imageHeight, 27, 27);

                    blitThis(poseStack, 7 + 3, i1 + 3, imageWidth, (currentJobArray[2] * 31), 31, 27);
                }
            }
        }
        // JOB START AND STOP BUTTONS
        if (activeRightButton == 0) {
            if (getSelectedJobLevel() == 0) {
                if (isBetween(mouseX - startX, mouseY - startY, 169, 132, 169 + 138, 132 + 17)) {
                    poseStack.pushPose();
                    RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    blitThis(poseStack, 150 + 19, 132, 26, imageHeight + 105, 139, 18);
                    RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                    poseStack.popPose();
                } else {
                    RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1);
                    blitThis(poseStack, 150 + 19, 132, 26, imageHeight + 105, 139, 18);
                    RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                }
            } else if (jobId != -1) {
                if (isBetween(mouseX - startX, mouseY - startY, 169, 132, 169 + 138, 132 + 17)) {
                    poseStack.pushPose();
                    RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    blitThis(poseStack, 150 + 19, 132, 26, imageHeight + 105, 139, 18);
                    RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                    poseStack.popPose();
                } else {
                    RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1);
                    blitThis(poseStack, 150 + 19, 132, 26, imageHeight + 105, 139, 18);
                    RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                }
            }
            // CRAFTING RECIPE BUTTONS
        } else if (activeRightButton == 1) {
            if (jobId != -1) {
                ArrayList<Integer> jobItemAmounts = new ArrayList<>(Arrays.asList(23, 5, 4, 4, 4, 3, 9, 4, 4, 16));
                int itemsAmount = jobItemAmounts.get(jobId);
                for (int i = 0; i < itemsAmount; ++i) {
                    int xOffset = 172 + i % 5 * 27;
                    int l = i / 5;
                    int yOffset = 16 + l * 27 + 2;
                    if (isBetween(mouseX - startX, mouseY - startY, xOffset, yOffset, xOffset + 23, yOffset + 23)) {
                        poseStack.pushPose();
                        RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                        blitThis(poseStack, xOffset, yOffset, 142, 210, 24, 24);
                        RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                        poseStack.popPose();
                    } else {
                        blitThis(poseStack, xOffset, yOffset, 142, 210, 24, 24);
                    }
                }
            }
        } else if (activeRightButton == 2 && (jobId == 0 || jobId == 1 || jobId == 8)) {
            if (jobId != -1) {
                //POWER-UP 1
                if (array[21 + (jobId * 3) + 1] == 1) {
                    RenderSystem.setShaderColor(0.5F, 1F, 0.5F, 1);
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27, 169 + 138, 27 + 17)) {
                        RenderSystem.setShaderColor(0.3F, 0.9F, 0.3F, 1);
                    }
                } else if (array[21 + (jobId * 3) + 1] == 2) {
                    RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1);
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27, 169 + 138, 27 + 17)) {
                        RenderSystem.setShaderColor(0.9F, 0.3F, 0.3F, 1);
                    }
                } else {
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27, 169 + 138, 27 + 17)) {
                        RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    }
                }
                blitThis(poseStack, 150 + 19, 27, 26, imageHeight + 105, 139, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                //POWER-UP 2
                if (array[21 + (jobId * 3) + 2] == 1) {
                    RenderSystem.setShaderColor(0.5F, 1F, 0.5F, 1);
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 25, 169 + 138, 27 + 17 + 25)) {
                        RenderSystem.setShaderColor(0.3F, 0.9F, 0.3F, 1);
                    }
                } else if (array[21 + (jobId * 3) + 2] == 2) {
                    RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1);
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 25, 169 + 138, 27 + 17 + 25)) {
                        RenderSystem.setShaderColor(0.9F, 0.3F, 0.3F, 1);
                    }
                } else {
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 25, 169 + 138, 27 + 17 + 25)) {
                        RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    }
                }
                blitThis(poseStack, 150 + 19, 52, 26, imageHeight + 105, 139, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                //POWER-UP 3
                if (array[21 + (jobId * 3) + 3] == 1) {
                    RenderSystem.setShaderColor(0.5F, 1F, 0.5F, 1);
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 50, 169 + 138, 27 + 17 + 50)) {
                        RenderSystem.setShaderColor(0.3F, 0.9F, 0.3F, 1);
                    }
                } else if (array[21 + (jobId * 3) + 3] == 2) {
                    RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1);
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 50, 169 + 138, 27 + 17 + 50)) {
                        RenderSystem.setShaderColor(0.9F, 0.3F, 0.3F, 1);
                    }
                } else {
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 50, 169 + 138, 27 + 17 + 50)) {
                        RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    }
                }
                blitThis(poseStack, 150 + 19, 77, 26, imageHeight + 105, 139, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
                //SUPERPOWER
                if (array[(jobId * 2)] == 100) {
                    if (array[52 + jobId] == 0) {
                        RenderSystem.setShaderColor(0.5F, 1F, 0.5F, 1);
                        if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 103, 169 + 138, 27 + 17 + 103)) {
                            RenderSystem.setShaderColor(0.3F, 0.9F, 0.3F, 1);
                        }
                    } else {
                        RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1);
                        if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 103, 169 + 138, 27 + 17 + 103)) {
                            RenderSystem.setShaderColor(0.9F, 0.3F, 0.3F, 1);
                        }
                    }
                } else {
                    if (isBetween(mouseX - startX, mouseY - startY, 169, 27 + 103, 169 + 138, 27 + 17 + 103)) {
                        RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    }
                }
                blitThis(poseStack, 150 + 19, 130, 26, imageHeight + 105, 139, 18);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1);
            }
        }
    }

//.TTTTTTTTTTT...EEEEEEEEEEEE...EXXX..XXXXX...XTTTTTTTTT.....SSSSSSS.....
//.TTTTTTTTTTT...EEEEEEEEEEE....EXXX..XXXX....XTTTTTTTTT....TSSSSSSSS....
//.TTTTTTTTTTT...EEEEEEEEEEE....EXXXXXXXXX....XTTTTTTTTT...TTSSSSSSSSS...
//....TTTT.......EEEE............XXXXXXXX........TTTT......TTSSS..SSSS...
//....TTTT.......EEEE.............XXXXXX.........TTTT......TTSSS.........
//....TTTT.......EEEEEEEEEE.......XXXXXX.........TTTT.......TSSSSSS......
//....TTTT.......EEEEEEEEEE.......XXXXX..........TTTT........SSSSSSSSS...
//....TTTT.......EEEEEEEEEE.......XXXXXX.........TTTT..........SSSSSSS...
//....TTTT.......EEEE............XXXXXXXX........TTTT.............SSSSS..
//....TTTT.......EEEE............XXXXXXXX........TTTT......TTSS....SSSS..
//....TTTT.......EEEEEEEEEEE....EXXX.XXXXX.......TTTT......TTSSSSSSSSSS..
//....TTTT.......EEEEEEEEEEEE...EXXX..XXXXX......TTTT.......TSSSSSSSSS...
//....TTTT.......EEEEEEEEEEEE...EXX....XXXX......TTTT........SSSSSSSS....

    public void renderItems(int x, int y) {
        if (activeRightButton == 1) {
            ArrayList<ItemStack> jobItemsArray = new ArrayList<>();
            if (jobId == 0) {
                final ItemStack SPEED_POTION = Items.POTION.getDefaultInstance();
                final ItemStack HASTE_POTION = Items.POTION.getDefaultInstance();
                final ItemStack STRENGTH_POTION = Items.POTION.getDefaultInstance();
                final ItemStack REGENERATION_POTION = Items.POTION.getDefaultInstance();
                final ItemStack LUCK_POTION = Items.POTION.getDefaultInstance();
                final ItemStack JESUS_POTION = Items.POTION.getDefaultInstance();
                final ItemStack FLYING_POTION = Items.POTION.getDefaultInstance();
                Set<MobEffectInstance> speed = Sets.newHashSet(new MobEffectInstance(MobEffects.MOVEMENT_SPEED));
                Set<MobEffectInstance> haste = Sets.newHashSet(new MobEffectInstance(MobEffects.DIG_SPEED));
                Set<MobEffectInstance> strength = Sets.newHashSet(new MobEffectInstance(MobEffects.DAMAGE_BOOST));
                Set<MobEffectInstance> regeneration = Sets.newHashSet(new MobEffectInstance(MobEffects.REGENERATION));
                Set<MobEffectInstance> luck = Sets.newHashSet(new MobEffectInstance(MobEffects.LUCK));
                Set<MobEffectInstance> jesus = Sets.newHashSet(new MobEffectInstance(ModEffects.JESUS.get()));
                Set<MobEffectInstance> flying = Sets.newHashSet(new MobEffectInstance(ModEffects.FLYING.get()));
                jobItemsArray.addAll(Arrays.asList(PotionUtils.setCustomEffects(SPEED_POTION, speed), PotionUtils.setCustomEffects(SPEED_POTION, speed), PotionUtils.setCustomEffects(SPEED_POTION, speed), PotionUtils.setCustomEffects(SPEED_POTION, speed), PotionUtils.setCustomEffects(HASTE_POTION, haste), PotionUtils.setCustomEffects(HASTE_POTION, haste), PotionUtils.setCustomEffects(HASTE_POTION, haste), PotionUtils.setCustomEffects(HASTE_POTION, haste), PotionUtils.setCustomEffects(STRENGTH_POTION, strength), PotionUtils.setCustomEffects(STRENGTH_POTION, strength), PotionUtils.setCustomEffects(STRENGTH_POTION, strength), PotionUtils.setCustomEffects(STRENGTH_POTION, strength), PotionUtils.setCustomEffects(REGENERATION_POTION, regeneration), PotionUtils.setCustomEffects(REGENERATION_POTION, regeneration), PotionUtils.setCustomEffects(REGENERATION_POTION, regeneration), PotionUtils.setCustomEffects(LUCK_POTION, luck), PotionUtils.setCustomEffects(LUCK_POTION, luck), PotionUtils.setCustomEffects(LUCK_POTION, luck), PotionUtils.setCustomEffects(LUCK_POTION, luck), PotionUtils.setCustomEffects(JESUS_POTION, jesus), PotionUtils.setCustomEffects(JESUS_POTION, jesus), PotionUtils.setCustomEffects(FLYING_POTION, flying), PotionUtils.setCustomEffects(FLYING_POTION, flying)));
            } else if (jobId == 1) {
                jobItemsArray.addAll(Arrays.asList(ModItems.SMALL_BACKPACK.get().getDefaultInstance(), ModItems.MEDIUM_BACKPACK.get().getDefaultInstance(), ModItems.LARGE_BACKPACK.get().getDefaultInstance(), ModItems.HUGE_BACKPACK.get().getDefaultInstance(), ModItems.ENDER_BACKPACK.get().getDefaultInstance()));
            } else if (jobId == 2) {
                jobItemsArray.addAll(Arrays.asList(ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get().getDefaultInstance(), ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get().getDefaultInstance(), ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get().getDefaultInstance(), ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 3) {
                jobItemsArray.addAll(Arrays.asList(ModItems.FARMERS_HOE_LEVEL_1.get().getDefaultInstance(), ModItems.FARMERS_HOE_LEVEL_2.get().getDefaultInstance(), ModItems.FARMERS_HOE_LEVEL_3.get().getDefaultInstance(), ModItems.FARMERS_HOE_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 4) {
                jobItemsArray.addAll(Arrays.asList(ModItems.FISHERMANS_ROD_LEVEL_1.get().getDefaultInstance(), ModItems.FISHERMANS_ROD_LEVEL_2.get().getDefaultInstance(), ModItems.FISHERMANS_ROD_LEVEL_3.get().getDefaultInstance(), ModItems.FISHERMANS_ROD_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 5) {
                jobItemsArray.addAll(Arrays.asList(ModItems.EXP_JAR.get().getDefaultInstance(), ModItems.EXPERIENCE_BOTTLE.get().getDefaultInstance(), ModItems.CURSE_BREAKER.get().getDefaultInstance()));
            } else if (jobId == 6) {
                jobItemsArray.addAll(Arrays.asList(Items.WHITE_WOOL.getDefaultInstance(), ModItems.HUNTERS_SWORD_LEVEL_1.get().getDefaultInstance(), ModItems.HUNTERS_SWORD_LEVEL_2.get().getDefaultInstance(), ModItems.HUNTERS_SWORD_LEVEL_3.get().getDefaultInstance(), ModItems.HUNTERS_SWORD_LEVEL_4.get().getDefaultInstance(), ModItems.HUNTERS_BOW_LEVEL_1.get().getDefaultInstance(), ModItems.HUNTERS_BOW_LEVEL_2.get().getDefaultInstance(), ModItems.HUNTERS_BOW_LEVEL_3.get().getDefaultInstance(), ModItems.HUNTERS_BOW_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 7) {
                jobItemsArray.addAll(Arrays.asList(ModItems.LUMBERJACK_AXE_LEVEL_1.get().getDefaultInstance(), ModItems.LUMBERJACK_AXE_LEVEL_2.get().getDefaultInstance(), ModItems.LUMBERJACK_AXE_LEVEL_3.get().getDefaultInstance(), ModItems.LUMBERJACK_AXE_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 8) {
                jobItemsArray.addAll(Arrays.asList(ModItems.MINERS_HAMMER_LEVEL_1.get().getDefaultInstance(), ModItems.MINERS_HAMMER_LEVEL_2.get().getDefaultInstance(), ModItems.MINERS_HAMMER_LEVEL_3.get().getDefaultInstance(), ModItems.MINERS_HAMMER_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 9) {
                jobItemsArray.addAll(Arrays.asList(ModItems.REINFORCED_IRON_HELMET.get().getDefaultInstance(), ModItems.REINFORCED_IRON_CHESTPLATE.get().getDefaultInstance(), ModItems.REINFORCED_IRON_LEGGINGS.get().getDefaultInstance(), ModItems.REINFORCED_IRON_BOOTS.get().getDefaultInstance(), ModItems.OBSIDIAN_HELMET.get().getDefaultInstance(), ModItems.OBSIDIAN_CHESTPLATE.get().getDefaultInstance(), ModItems.OBSIDIAN_LEGGINGS.get().getDefaultInstance(), ModItems.OBSIDIAN_BOOTS.get().getDefaultInstance(), ModItems.REINFORCED_DIAMOND_HELMET.get().getDefaultInstance(), ModItems.REINFORCED_DIAMOND_CHESTPLATE.get().getDefaultInstance(), ModItems.REINFORCED_DIAMOND_LEGGINGS.get().getDefaultInstance(), ModItems.REINFORCED_DIAMOND_BOOTS.get().getDefaultInstance(), ModItems.REINFORCED_NETHERITE_HELMET.get().getDefaultInstance(), ModItems.REINFORCED_NETHERITE_CHESTPLATE.get().getDefaultInstance(), ModItems.REINFORCED_NETHERITE_LEGGINGS.get().getDefaultInstance(), ModItems.REINFORCED_NETHERITE_BOOTS.get().getDefaultInstance()));
            }
            for (int i = 0; i < jobItemsArray.size(); i++) {
                int xOffset = x + 176 + i % 5 * 27;
                int l = i / 5;
                int yOffset = y + 20 + l * 27 + 2;
                minecraft.getItemRenderer().renderAndDecorateItem(jobItemsArray.get(i), xOffset, yOffset);
            }
        }
    }

    public void drawTexts(PoseStack poseStack, int something, int occurrences) {
        if (activeLeftButton == 0) {
            for (int i = this.startIndex; i < occurrences && i < Jobs.values().length; ++i) {
                int j = i - this.startIndex;
                int i1 = something + j * 35;
                int level = array[i * 2];
                int exp = array[i * 2 + 1];
                if (level != 0) {
                    int maxExp = LevelHandler.calcExp(level);
                    font.draw(poseStack, ChatColor.boldGreen() + Jobs.getString(i), startX + 7 + 3 + 35, i1 + 3, 16777215);
                    font.draw(poseStack, ChatColor.aqua() + "Level: " + ChatColor.white() + level, startX + 7 + 3 + 35, i1 + 14, 16777215);
                    font.draw(poseStack, ChatColor.aqua() + "EXP: " + ChatColor.white() + (int) ((double) exp / maxExp * 100) + "%", startX + 7 + 3 + 35, i1 + 23, 16777215);
                } else {
                    font.draw(poseStack, ChatColor.boldRed() + Jobs.getString(i), startX + 7 + 3 + 35, i1 + 3, 16777215);
                    font.draw(poseStack, ChatColor.aqua() + "Want this job?", startX + 7 + 3 + 35, i1 + 14, 16777215);
                    if (array[21] < 2) {
                        font.draw(poseStack, ChatColor.aqua() + "Cost: " + ChatColor.reset() + 0 + " coins", startX + 7 + 3 + 35, i1 + 23, 16777215);
                    } else {
                        font.draw(poseStack, ChatColor.aqua() + "Cost: " + ChatColor.reset() + 10 + " coins", startX + 7 + 3 + 35, i1 + 23, 16777215);
                    }
                }
            }
        }
        if (activeLeftButton == 1 || activeLeftButton == 2) {
            ints = new ArrayList<>();
            for (int i = 0; i < Jobs.values().length; ++i) {
                int level = array[i * 2];
                int exp = array[i * 2 + 1];
                if (activeLeftButton == 1) {
                    if (level != 0) {
                        ints.add(level);
                        ints.add(exp);
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getString(i))));
                    }
                }
                if (activeLeftButton == 2) {
                    if (level == 0) {
                        ints.add(level);
                        ints.add(exp);
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getString(i))));
                    }
                }
            }
            for (int i = this.startIndex; i < occurrences; ++i) {
                if (i < ints.size() / 3) {
                    int[] currentJobArray = new int[]{ints.get(i * 3), ints.get(i * 3 + 1), ints.get(i * 3 + 2)};
                    int j = i - this.startIndex;
                    int i1 = something + j * 35;
                    int level = currentJobArray[0];
                    int exp = currentJobArray[1];
                    if (level != 0) {
                        int maxExp = LevelHandler.calcExp(level);
                        font.draw(poseStack, ChatColor.boldGreen() + Jobs.getString(currentJobArray[2]), startX + 7 + 3 + 35, i1 + 3, 16777215);
                        font.draw(poseStack, ChatColor.aqua() + "Level: " + ChatColor.white() + level, startX + 7 + 3 + 35, i1 + 14, 16777215);
                        font.draw(poseStack, ChatColor.aqua() + "EXP: " + ChatColor.white() + (int) ((double) exp / maxExp * 100) + "%", startX + 7 + 3 + 35, i1 + 23, 16777215);
                    } else {
                        font.draw(poseStack, ChatColor.boldRed() + Jobs.getString(currentJobArray[2]), startX + 7 + 3 + 35, i1 + 3, 16777215);
                        font.draw(poseStack, ChatColor.aqua() + "Want this job?", startX + 7 + 3 + 35, i1 + 14, 16777215);
                        if (array[21] < 2) {
                            font.draw(poseStack, ChatColor.aqua() + "Cost: " + ChatColor.reset() + 0 + " coins", startX + 7 + 3 + 35, i1 + 23, 16777215);
                        } else {
                            font.draw(poseStack, ChatColor.aqua() + "Cost: " + ChatColor.reset() + 10 + " coins", startX + 7 + 3 + 35, i1 + 23, 16777215);
                        }
                    }
                }
            }
        }
        if (jobId == -1) {
            if (activeRightButton == 0) {
                font.draw(poseStack, ChatColor.darkGray() + "Select a job for more info.", startX + 168, startY + 60, 16777215);
            } else if (activeRightButton == 1) {
                font.draw(poseStack, ChatColor.darkGray() + "Select a job to view", startX + 183, startY + 60, 16777215);
                font.draw(poseStack, ChatColor.darkGray() + "its crafting recipes.", startX + 184, startY + 60 + 9, 16777215);
            } else if (activeRightButton == 2) {
                font.draw(poseStack, ChatColor.darkGray() + "Select a job to view", startX + 183, startY + 60, 16777215);
                font.draw(poseStack, ChatColor.darkGray() + "its powerups.", startX + 199, startY + 60 + 9, 16777215);
            } else if (activeRightButton == 3) {
                font.draw(poseStack, ChatColor.darkGray() + "Select a job to view", startX + 183, startY + 60, 16777215);
                font.draw(poseStack, ChatColor.darkGray() + "the possible ways to", startX + 182, startY + 60 + 9, 16777215);
                font.draw(poseStack, ChatColor.darkGray() + "get EXP for the job.", startX + 183, startY + 60 + 18, 16777215);
            }
        } else {
            if (activeRightButton == 0) {
                poseStack.pushPose();
                poseStack.scale(2F, 2F, 2F);
                font.draw(poseStack, getSelectedJobLevel() != 0 ? ChatColor.boldGreen() + Jobs.getString(jobId) : ChatColor.boldRed() + Jobs.getString(jobId), (startX + 156) / 2, (startY + 5) / 2, 16777215);
                poseStack.popPose();
                if (getSelectedJobLevel() != 0) {
                    font.draw(poseStack, ChatColor.darkGray() + "Level: " + ChatColor.white() + getSelectedJobLevel(), startX + 156, startY + 22, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "EXP: " + ChatColor.white() + "[" + getSelectedJobEXP() + "/" + getSelectedJobMaxEXP() + "]", startX + 216, startY + 22, 16777215);
                    font.draw(poseStack, ChatFormatting.STRIKETHROUGH + "                                        ", startX + 156, startY + 28, 16777215);
                } else {
                    font.draw(poseStack, ChatColor.darkGray() + "Want this job?", startX + 156, startY + 22, 16777215);
                    if (array[21] < 2) {
                        font.draw(poseStack, ChatColor.darkGray() + "Cost: " + ChatColor.reset() + 0 + " coins", startX + 236, startY + 22, 16777215);
                    } else {
                        font.draw(poseStack, ChatColor.darkGray() + "Cost: " + ChatColor.reset() + 10 + " coins", startX + 236, startY + 22, 16777215);
                    }
                    font.draw(poseStack, ChatFormatting.STRIKETHROUGH + "                                        ", startX + 156, startY + 28, 16777215);
                }
                if (jobId == 0) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Alchemist job adds 2", startX + 173, startY + 42, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "custom potion effects:", startX + 180, startY + 42 + 9, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Jesus " + ChatColor.darkGray() + "and " + ChatColor.boldWhite() + "Flying", startX + 187, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Jesus allows you to walk on", startX + 166, startY + 42 + 36, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "water and flying allows you", startX + 167, startY + 42 + 36 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "to fly. It also increases the", startX + 166, startY + 42 + 36 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "duration and strength of", startX + 172, startY + 42 + 36 + 27, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "some existing potions.", startX + 182, startY + 42 + 36 + 36, 16777215);
                } else if (jobId == 1) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Builder job adds 5", startX + 177, startY + 42 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "different types of backpacks:", startX + 162, startY + 42 + 9 + 9, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Small" + ChatColor.darkGray() + ", 9 slots. " + ChatColor.boldWhite() + "Medium" + ChatColor.darkGray() + ", 18 slots.", startX + 157, startY + 42 + 18 + 9, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Large" + ChatColor.darkGray() + ", 36 slots. " + ChatColor.boldWhite() + "Huge" + ChatColor.darkGray() + ", 54 slots.", startX + 157, startY + 42 + 27 + 9, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Ender" + ChatColor.darkGray() + ", opens your enderchest.", startX + 158, startY + 42 + 36 + 9, 16777215);
                } else if (jobId == 2) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Digger job adds a new", startX + 169, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "tool called the " + ChatColor.boldWhite() + "Excavator" + ChatColor.darkGray() + ".", startX + 168, startY + 42 + 9 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "The " + ChatColor.boldWhite() + "Excavator" + ChatColor.darkGray() + " can mine", startX + 173, startY + 42 + 18 + 9 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "multiple blocks at once.", startX + 180, startY + 42 + 27 + 9 + 18, 16777215);
                } else if (jobId == 3) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Farmer job adds a new", startX + 168, startY + 42 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "tool called the " + ChatColor.boldWhite() + "Farmers Hoe" + ChatColor.darkGray() + ".", startX + 162, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "The " + ChatColor.boldWhite() + "Farmers Hoe" + ChatColor.darkGray() + " can harvest", startX + 157, startY + 42 + 18 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "multiple crops at once. Including", startX + 157, startY + 42 + 27 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "crops like sugarcane and cactus.", startX + 154, startY + 42 + 27 + 27, 16777215);
                } else if (jobId == 4) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Fisherman job adds a new", startX + 161, startY + 42 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "fishing rod called the", startX + 182, startY + 42 + 9 + 9, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Fishermans Rod" + ChatColor.darkGray() + ".", startX + 189, startY + 42 + 18 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "The " + ChatColor.boldWhite() + "Fishermans Rod" + ChatColor.darkGray() + " can", startX + 169, startY + 42 + 18 + 18 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "catch multiple fish or", startX + 183, startY + 42 + 27 + 18 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "treasures at once.", startX + 188, startY + 42 + 27 + 18 + 18, 16777215);
                } else if (jobId == 5) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Enchanter job adds 2", startX + 169, startY + 42, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "new items. The " + ChatColor.boldWhite() + "EXP jar" + ChatColor.darkGray() + " is a", startX + 166, startY + 42 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "jar that can store EXP. The", startX + 167, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Curse Breaker" + ChatColor.darkGray() + " is a book that", startX + 158, startY + 42 + 18 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "removes curses from an item.", startX + 166, startY + 42 + 27 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "It also adds the ability", startX + 179, startY + 42 + 27 + 27, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "to craft and upgrade", startX + 182, startY + 42 + 27 + 36, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Bottles 'o Enchanting" + ChatColor.darkGray() + ".", startX + 173, startY + 42 + 36 + 36, 16777215);
                } else if (jobId == 6) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Hunter job adds 2 new", startX + 168, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "weapons a " + ChatColor.boldWhite() + "Hunters Sword", startX + 166, startY + 42 + 9 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "and a " + ChatColor.boldWhite() + "Hunters Bow" + ChatColor.darkGray() + ".", startX + 183, startY + 42 + 18 + 18, 16777215);
                } else if (jobId == 7) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Lumberjack job adds", startX + 172, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "a new tool called the ", startX + 185, startY + 42 + 9 + 18, 16777215);
                    font.draw(poseStack, ChatColor.boldWhite() + "Lumberjack Axe" + ChatColor.darkGray() + ".", startX + 190, startY + 42 + 18 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "The " + ChatColor.boldWhite() + "Lumberjack Axe" + ChatColor.darkGray() + " can chop", startX + 155, startY + 42 + 18 + 18 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "an entire tree down at once.", startX + 165, startY + 42 + 27 + 18 + 18, 16777215);
                } else if (jobId == 8) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Miner job adds a new", startX + 171, startY + 42 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "tool called the " + ChatColor.boldWhite() + "Hammer" + ChatColor.darkGray() + ".", startX + 176, startY + 42 + 9 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "The " + ChatColor.boldWhite() + "Hammer" + ChatColor.darkGray() + " can mine", startX + 182, startY + 42 + 18 + 9 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "multiple blocks at once.", startX + 181, startY + 42 + 27 + 9 + 18, 16777215);
                } else if (jobId == 9) {
                    font.draw(poseStack, ChatColor.darkGray() + "The Smith job adds 4", startX + 181, startY + 42 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "new sets of armor:", startX + 186, startY + 42 + 9 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "- " + ChatColor.boldWhite() + "Reinforced Iron", startX + 182, startY + 42 + 18 + 9, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "- " + ChatColor.boldWhite() + "Obsidian", startX + 202, startY + 42 + 18 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "- " + ChatColor.boldWhite() + "Reinforced Diamond", startX + 173, startY + 42 + 27 + 18, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "- " + ChatColor.boldWhite() + "Reinforced Netherite", startX + 168, startY + 42 + 36 + 18, 16777215);
                }

                if (getSelectedJobLevel() == 0) {
                    font.draw(poseStack, ChatColor.white() + "Start performing this job.", startX + 174, startY + 137, 16777215);
                } else {
                    font.draw(poseStack, ChatColor.white() + "Stop performing this job.", startX + 175, startY + 137, 16777215);
                }
            } else if (activeRightButton == 1) {
                font.draw(poseStack, ChatColor.darkGray() + "Available Crafting Recipes", startX + 172, startY + 6, 16777215);
            } else if (activeRightButton == 2 && (jobId == 0 || jobId == 1 || jobId == 8)) {
                font.draw(poseStack, ChatColor.darkGray() + "Power-ups", startX + 209, startY + 6, 16777215);
                font.draw(poseStack, ChatColor.gray() + "10 Coins Each", startX + 200, startY + 16, 16777215);
                font.draw(poseStack, ChatColor.darkGray() + "Superpower", startX + 207, startY + 110, 16777215);
                font.draw(poseStack, ChatColor.gray() + "Unlocked at Level 100", startX + 181, startY + 120, 16777215);
                poseStack.pushPose();
                poseStack.scale(0.72F, 0.72F, 0.72F);
                if (jobId == 0) {
                    font.draw(poseStack, ChatColor.darkGray() + "Harmful potion immunity", (startX + 197) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "1.5x Potion duration", (startX + 202) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Double Health with Potion of Healing", (startX + 175) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "2x Potion duration", (startX + 205) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 1) {
                    font.draw(poseStack, ChatColor.darkGray() + "A chance you get your block back", (startX + 176) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Use blocks in backpack first", (startX + 186) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Double jump and less fall damage", (startX + 178) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Triple jump and no fall damage", (startX + 181) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 2) {
                    font.draw(poseStack, ChatColor.darkGray() + "Blocks mined go into your inventory", (startX + 174) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "A Change to dig up minerals", (startX + 188) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Haste I when digging", (startX + 200) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Haste II and Speed I when digging", (startX + 177) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 3) {
                    font.draw(poseStack, ChatColor.darkGray() + "A chance to get double drops", (startX + 184) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "A Chance to get better drops", (startX + 184) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "+1 Random color wool when shearing", (startX + 172) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Double crop and wool drops", (startX + 187) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 4) {
                    font.draw(poseStack, ChatColor.darkGray() + "Double job-exp", (startX + 207) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "A chance to get 2 extra drops", (startX + 182) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "A Change to catch minerals", (startX + 188) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "A chance to get 5 extra drops", (startX + 182) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 5) {
                    font.draw(poseStack, ChatColor.darkGray() + "Remove curses using a grinder", (startX + 182) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "+1 Enchantment level on enchanting", (startX + 174) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Double experience", (startX + 203) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Enchantments in an anvil add up", (startX + 181) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 6) {
                    font.draw(poseStack, ChatColor.darkGray() + "Meat give one extra hunger bar", (startX + 180) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Strength II and Speed I on kill", (startX + 182) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Chance to drop the head", (startX + 192) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Shoot 3 arrows & 25% more damage", (startX + 175) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 7) {
                    font.draw(poseStack, ChatColor.darkGray() + "A change to get a double log drop", (startX + 176) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "A chance to get Haste II on harvest", (startX + 173) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Get better apples from leaves", (startX + 183) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Logs go into your inventory", (startX + 186) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 8) {
                    font.draw(poseStack, ChatColor.darkGray() + "Ore vein miner", (startX + 210) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Automatically smelt ores", (startX + 194) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Haste I when mining", (startX + 203) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Haste II and Speed I when mining", (startX + 179) / 0.72F, (startY + 136) / 0.72F, 16777215);
                } else if (jobId == 9) {
                    font.draw(poseStack, ChatColor.darkGray() + "Mobs can freeze when they hit you", (startX + 175) / 0.72F, (startY + 33) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Enchantments from items to books", (startX + 177) / 0.72F, (startY + 33 + 25) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Jobs+ armor gives Fire Protection", (startX + 176) / 0.72F, (startY + 33 + 50) / 0.72F, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "Crafted items are unbreakable", (startX + 181) / 0.72F, (startY + 136) / 0.72F, 16777215);
                }
                poseStack.popPose();
            } else {
                font.draw(poseStack, ChatColor.darkGray() + "Coming soon!", startX + 203, startY + 69, 16777215);
            }
        }
    }

    @Override
    protected void init() {
        startX = (this.width - imageWidth) / 2;
        startY = (this.height - imageHeight) / 2;
        super.init();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int p_99320_) {
        this.scrolling = false;
        int i = this.startX + 7;
        int j = this.startY + 16;
        int k = this.startIndex + 4;
        //JOBS LIST
        for (int l = this.startIndex; l < k; ++l) {
            if (isBetween(mouseX - i, mouseY - j, 0, 0, 116, 35 * 4)) {
                try {
                    selectedButton = (int) (mouseY - j) / 35 + l;
                    if (activeLeftButton != 0) jobId = ints.get(selectedButton * 3 + 2);
                    else jobId = selectedButton;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                } catch (IndexOutOfBoundsException ignore) {
                }
            }
        }
        i = startX + 127;
        j = startY + 17;
        //SCROLL BAR
        if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 140)) {
            this.scrolling = true;
        }
        mouseX = mouseX - startX;
        mouseY = mouseY - startY;
        //ALL JOBS BUTTON
        if (isBetween(mouseX, mouseY, 6, -22, 32, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (activeLeftButton != 0) {
                selectedButton = -1;
                jobId = -1;
                startIndex = 0;
                scrollOffs = 0;
            }
            activeLeftButton = 0;
            //PERFORMING JOBS BUTTON
        } else if (isBetween(mouseX, mouseY, 6 + 28, -22, 32 + 28, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (activeLeftButton != 1) {
                selectedButton = -1;
                jobId = -1;
                startIndex = 0;
                scrollOffs = 0;
            }
            activeLeftButton = 1;
            //NOT PERFORMING JOBS BUTTON
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28, -22, 32 + 28 + 28, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (activeLeftButton != 2) {
                selectedButton = -1;
                jobId = -1;
                startIndex = 0;
                scrollOffs = 0;
            }
            activeLeftButton = 2;
            //JOB INFO BUTTON
        } else if (isBetween(mouseX, mouseY, 6 + 150, -22, 32 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 0;
            //CRAFTING RECIPES BUTTON
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 150, -22, 32 + 28 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 1;
            // POWERUPS BUTTON
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 150, -22, 32 + 28 + 28 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 2;
            // HOW TO GET EXP BUTTON
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 28 + 150, -22, 32 + 28 + 28 + 28 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 3;
        }
        // JOB START STOP BUTTON
        if (jobId != -1) {
            if (activeRightButton == 0) {
                if (isBetween(mouseX, mouseY, 169, 132, 169 + 138, 132 + 18)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    if (getSelectedJobLevel() == 0) {
                        if (array[21] <= 2) {
                            Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to start performing this job?", "start", Jobs.getJobFromInt(jobId)));
                        } else {
                            if (array[20] >= 10) {
                                Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to start performing this job for " + 10 + " coins?", "start_paid", Jobs.getJobFromInt(jobId)));
                            } else {
                                Minecraft.getInstance().setScreen(new ConfirmationScreen("You do not have enough coins to start performing this job.", "not_enough_coins_start", Jobs.getJobFromInt(jobId)));
                            }
                        }
                    } else if (getSelectedJobLevel() == 1) {
                        Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to stop performing this job?", "stop_free", Jobs.getJobFromInt(jobId)));
                    } else {
                        if (array[20] >= 5) {
                            Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to stop performing this job for " + 5 + " coins?", "stop", Jobs.getJobFromInt(jobId)));
                        } else {
                            Minecraft.getInstance().setScreen(new ConfirmationScreen("You do not have enough coins to stop performing this job.", "not_enough_coins_stop", Jobs.getJobFromInt(jobId)));
                        }
                    }
                }
            }
            if (activeRightButton == 1) {
                ItemStack itemStack = ItemStack.EMPTY;
                mouseX = mouseX - 150D;
                if (jobId == 0) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.EXTRA_STRONG_SWIFTNESS.get());
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_EXTRA_STRONG_SWIFTNESS.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.SUPER_STRONG_SWIFTNESS.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_SUPER_STRONG_SWIFTNESS.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18, 46 + (27 * 4), 42)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.HASTE.get());
                    } else if (isBetween(mouseX, mouseY, 22, 18 + 27, 46, 42 + 27)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_HASTE.get());
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + 27, 46 + 27, 42 + 27)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.STRONG_HASTE.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + 27, 46 + (27 * 2), 42 + 27)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_STRONG_HASTE.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18 + 27, 46 + (27 * 3), 42 + 27)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.EXTRA_STRONG_STRENGTH.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18 + 27, 46 + (27 * 4), 42 + 27)) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_EXTRA_STRONG_STRENGTH.get());
                    } else if (isBetween(mouseX, mouseY, 22, 18 + (27 * 2), 46, 42 + (27 * 2))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.SUPER_STRONG_STRENGTH.get());
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + (27 * 2), 46 + 27, 42 + (27 * 2))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_SUPER_STRONG_STRENGTH.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + (27 * 2), 46 + (27 * 2), 42 + (27 * 2))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_STRONG_REGENERATION.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18 + (27 * 2), 46 + (27 * 3), 42 + (27 * 2))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.EXTRA_LONG_STRONG_REGENERATION.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18 + (27 * 2), 46 + (27 * 4), 42 + (27 * 2))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.EXTRA_STRONG_REGENERATION.get());
                    } else if (isBetween(mouseX, mouseY, 22, 18 + (27 * 3), 46, 42 + (27 * 3))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LUCK.get());
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + (27 * 3), 46 + 27, 42 + (27 * 3))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_LUCK.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + (27 * 3), 46 + (27 * 2), 42 + (27 * 3))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.STRONG_LUCK.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18 + (27 * 3), 46 + (27 * 3), 42 + (27 * 3))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_STRONG_LUCK.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18 + (27 * 3), 46 + (27 * 4), 42 + (27 * 3))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.JESUS.get());
                    } else if (isBetween(mouseX, mouseY, 22, 18 + (27 * 4), 46, 42 + (27 * 4))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_JESUS.get());
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + (27 * 4), 46 + 27, 42 + (27 * 4))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.FLYING.get());
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + (27 * 4), 46 + (27 * 2), 42 + (27 * 4))) {
                        itemStack = PotionUtils.setPotion(Items.POTION.getDefaultInstance(), ModPotions.LONG_FLYING.get());
                    }
                } else if (jobId == 1) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.SMALL_BACKPACK.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.MEDIUM_BACKPACK.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.LARGE_BACKPACK.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.HUGE_BACKPACK.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18, 46 + (27 * 4), 42)) {
                        itemStack = ModItems.ENDER_BACKPACK.get().getDefaultInstance();
                    }
                } else if (jobId == 2) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get().getDefaultInstance();
                    }
                } else if (jobId == 3) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.FARMERS_HOE_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.FARMERS_HOE_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.FARMERS_HOE_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.FARMERS_HOE_LEVEL_4.get().getDefaultInstance();
                    }
                } else if (jobId == 4) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.FISHERMANS_ROD_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.FISHERMANS_ROD_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.FISHERMANS_ROD_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.FISHERMANS_ROD_LEVEL_4.get().getDefaultInstance();
                    }
                } else if (jobId == 5) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.EXP_JAR.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.EXPERIENCE_BOTTLE.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.CURSE_BREAKER.get().getDefaultInstance();
                    }
                } else if (jobId == 6) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = Items.WHITE_WOOL.getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.HUNTERS_SWORD_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.HUNTERS_SWORD_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.HUNTERS_SWORD_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18, 46 + (27 * 4), 42)) {
                        itemStack = ModItems.HUNTERS_SWORD_LEVEL_4.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22, 18 + 27, 46, 42 + 27)) {
                        itemStack = ModItems.HUNTERS_BOW_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + 27, 46 + 27, 42 + 27)) {
                        itemStack = ModItems.HUNTERS_BOW_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + 27, 46 + (27 * 2), 42 + 27)) {
                        itemStack = ModItems.HUNTERS_BOW_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18 + 27, 46 + (27 * 3), 42 + 27)) {
                        itemStack = ModItems.HUNTERS_BOW_LEVEL_4.get().getDefaultInstance();
                    }
                } else if (jobId == 7) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.LUMBERJACK_AXE_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.LUMBERJACK_AXE_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.LUMBERJACK_AXE_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.LUMBERJACK_AXE_LEVEL_4.get().getDefaultInstance();
                    }
                } else if (jobId == 8) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.MINERS_HAMMER_LEVEL_1.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.MINERS_HAMMER_LEVEL_2.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.MINERS_HAMMER_LEVEL_3.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.MINERS_HAMMER_LEVEL_4.get().getDefaultInstance();
                    }
                } else if (jobId == 9) {
                    if (isBetween(mouseX, mouseY, 22, 18, 46, 42)) {
                        itemStack = ModItems.REINFORCED_IRON_HELMET.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18, 46 + 27, 42)) {
                        itemStack = ModItems.REINFORCED_IRON_CHESTPLATE.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18, 46 + (27 * 2), 42)) {
                        itemStack = ModItems.REINFORCED_IRON_LEGGINGS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18, 46 + (27 * 3), 42)) {
                        itemStack = ModItems.REINFORCED_IRON_BOOTS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18, 46 + (27 * 4), 42)) {
                        itemStack = ModItems.OBSIDIAN_HELMET.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22, 18 + 27, 46, 42 + 27)) {
                        itemStack = ModItems.OBSIDIAN_CHESTPLATE.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + 27, 46 + 27, 42 + 27)) {
                        itemStack = ModItems.OBSIDIAN_LEGGINGS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + 27, 46 + (27 * 2), 42 + 27)) {
                        itemStack = ModItems.OBSIDIAN_BOOTS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18 + 27, 46 + (27 * 3), 42 + 27)) {
                        itemStack = ModItems.REINFORCED_DIAMOND_HELMET.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18 + 27, 46 + (27 * 4), 42 + 27)) {
                        itemStack = ModItems.REINFORCED_DIAMOND_CHESTPLATE.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22, 18 + (27 * 2), 46, 42 + (27 * 2))) {
                        itemStack = ModItems.REINFORCED_DIAMOND_LEGGINGS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + 27, 18 + (27 * 2), 46 + 27, 42 + (27 * 2))) {
                        itemStack = ModItems.REINFORCED_DIAMOND_BOOTS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 2), 18 + (27 * 2), 46 + (27 * 2), 42 + (27 * 2))) {
                        itemStack = ModItems.REINFORCED_NETHERITE_HELMET.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 3), 18 + (27 * 2), 46 + (27 * 3), 42 + (27 * 2))) {
                        itemStack = ModItems.REINFORCED_NETHERITE_CHESTPLATE.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22 + (27 * 4), 18 + (27 * 2), 46 + (27 * 4), 42 + (27 * 2))) {
                        itemStack = ModItems.REINFORCED_NETHERITE_LEGGINGS.get().getDefaultInstance();
                    } else if (isBetween(mouseX, mouseY, 22, 18 + (27 * 3), 46, 42 + (27 * 3))) {
                        itemStack = ModItems.REINFORCED_NETHERITE_BOOTS.get().getDefaultInstance();
                    }
                }
                if (itemStack != ItemStack.EMPTY) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    ItemStack finalItemStack = itemStack;
                    JobsPlusJeiPlugin.getJeiRuntime().ifPresent(jeiRuntime -> {
                        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();
                        IFocus<ItemStack> focus = recipeManager.createFocus(IFocus.Mode.OUTPUT, finalItemStack);

                        IRecipesGui recipesGui = jeiRuntime.getRecipesGui();
                        recipesGui.show(focus);
                    });
                }
            }
            if (activeRightButton == 2) {
                if (Minecraft.getInstance().player != null) {
                    String menuCommand = "/jobs menu " + jobId + " " + activeLeftButton + " " + activeRightButton + " " + selectedButton + " " + scrollOffs + " " + startIndex;
                    String powerupSwitchCommand = "/job powerups switch " + Jobs.getString(jobId);
                    if (isBetween(mouseX, mouseY, 169, 27, 169 + 139, 27 + 18)) {
                        if (array[21 + (jobId * 3) + 1] == 1 || array[21 + (jobId * 3) + 1] == 2) {
                            Minecraft.getInstance().player.chat(powerupSwitchCommand + " 1");
                            Minecraft.getInstance().player.chat(menuCommand);
                        } else {
                            Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to buy this power-up for 10 coins?", "powerup", Jobs.getJobFromInt(jobId), 1));
                        }
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    if (isBetween(mouseX, mouseY, 169, 27 + 25, 169 + 139, 27 + 18 + 25)) {
                        if (array[21 + (jobId * 3) + 2] == 1 || array[21 + (jobId * 3) + 2] == 2) {
                            Minecraft.getInstance().player.chat(powerupSwitchCommand + " 2");
                            Minecraft.getInstance().player.chat(menuCommand);
                        } else {
                            Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to buy this power-up for 10 coins?", "powerup", Jobs.getJobFromInt(jobId), 2));
                        }
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    if (isBetween(mouseX, mouseY, 169, 27 + 50, 169 + 139, 27 + 18 + 50)) {
                        if (array[21 + (jobId * 3) + 3] == 1 || array[21 + (jobId * 3) + 3] == 2) {
                            Minecraft.getInstance().player.chat(powerupSwitchCommand + " 3");
                            Minecraft.getInstance().player.chat(menuCommand);
                        } else {
                            Minecraft.getInstance().setScreen(new ConfirmationScreen("Are you sure you want to buy this power-up for 10 coins?", "powerup", Jobs.getJobFromInt(jobId), 3));
                        }
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    if (isBetween(mouseX, mouseY, 169, 27 + 103, 169 + 139, 27 + 18 + 103)) {
                        Minecraft.getInstance().player.chat("/job superpower switch " + Jobs.getString(jobId));
                        Minecraft.getInstance().player.chat(menuCommand);
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, p_99320_);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int p_99324_, double p_99325_, double p_99326_) {
        if (this.scrolling) {
            int i = startY + 14;
            int j = i + 54;
            this.scrollOffs = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 3F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, p_99324_, p_99325_, p_99326_);
        }
    }

    public boolean mouseScrolled(double p_99314_, double p_99315_, double p_99316_) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            this.scrollOffs = (float) ((double) this.scrollOffs - p_99316_ / (double) i);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 3F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) i) + 0.5D);
        }

        return true;
    }

    protected int getOffscreenRows() {
        return 2;
    }

    private boolean isScrollBarActive() {
        return true;
    }

    public void blitThis(PoseStack poseStack, int posX, int posY, int startX, int startY, int stopX, int stopY) {
        blit(poseStack, this.startX + posX, this.startY + posY, startX, startY, stopX, stopY, 362, 362);
    }

    public boolean isBetween(double mouseX, double mouseY, int mouseXTop, int mouseYTop, int mouseXBottom, int mouseYBottom) {
        return mouseX >= mouseXTop && mouseY >= mouseYTop && mouseX <= mouseXBottom && mouseY <= mouseYBottom;
    }

    private int getSelectedJobLevel() {
        if (jobId != -1) return array[jobId * 2];
        return -1;
    }

    private int getSelectedJobEXP() {
        if (jobId != -1) return array[jobId * 2 + 1];
        return -1;
    }

    private int getSelectedJobMaxEXP() {
        if (getSelectedJobLevel() == 0) return 0;
        return LevelHandler.calcExp(getSelectedJobLevel());
    }

}
