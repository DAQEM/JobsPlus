package me.daqem.jobsplus.client.gui;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.daqem.jobsplus.Config;
import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.client.renderer.RenderColor;
import me.daqem.jobsplus.common.packet.*;
import me.daqem.jobsplus.handlers.ChatHandler;
import me.daqem.jobsplus.handlers.LevelHandler;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.init.ModEffects;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.init.ModPotions;
import me.daqem.jobsplus.jei.JobsPlusJeiPlugin;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
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
        super(Component.literal("Jobs"));
        this.array = array;
        this.jobId = jobId;
        this.activeLeftButton = activeLeftButton;
        this.activeRightButton = activeRightButton;
        this.selectedButton = selectedButton;
        this.scrollOffs = scrollOffs;
        this.startIndex = startIndex;
    }

    public static void drawRightAlignedString(@NotNull PoseStack poseStack, Font font, @NotNull String text, int posX, int posY, int color) {
        font.draw(poseStack, text, (float) posX - font.width(text), (float) posY, color);
    }

    public static void drawCenteredStringNew(@NotNull PoseStack poseStack, Font font, @NotNull String text, int posX, int posY, int color) {
        font.draw(poseStack, text, (float) (posX - font.width(text) / 2), (float) posY, color);
    }

    public static void drawCenteredPowerupsString(@NotNull PoseStack poseStack, Font font, @NotNull String text, int posX, int posY, int color) {
        font.draw(poseStack, text, (float) (posX - font.width(text) / 2), (float) posY, color);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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
        font.draw(poseStack, ChatColor.darkGray() + Component.translatable("jobsplus.gui.jobs").getString(), startX + 7, startY + 6, 16777215);
        drawRightAlignedString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.coins.top", array[20]).getString(), startX + 140, startY + 6, 16777215);
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
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.all_jobs"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28, -22, 32 + 28, 0)) {
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.performing_jobs"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28, -22, 32 + 28 + 28, 0)) {
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.not_performing_jobs"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 150, -22, 32 + 150, 0)) {
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.job_info"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 150, -22, 32 + 28 + 150, 0)) {
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.job_crafting_recipes"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 150, -22, 32 + 28 + 28 + 150, 0)) {
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.job_powerups"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 28 + 150, -22, 32 + 28 + 28 + 28 + 150, 0)) {
            super.renderTooltip(poseStack, Component.translatable("jobsplus.gui.job_how_to_get_exp"), mouseX + startX, mouseY + startY);
        } else if (isBetween(mouseX, mouseY, imageWidth, 6, imageWidth + 21, 31)) {
            if (jobId >= 0 && array[64] == 1) {
                if (getSelectedJobLevel() != 0) {
                    List<Component> list;
                    final int displayId = array[62];

                    if (displayId != 0 && Jobs.getJobFromInt(displayId - 1) != null) {
                        list = List.of(Component.translatable("jobsplus.gui.toggle_prefix"),
                                Component.translatable("jobsplus.gui.active", ChatHandler.ColorizedJobName(Objects.requireNonNull(Jobs.getJobFromInt(displayId - 1))).replace(" ", "")));
                    } else {
                        list = List.of(Component.translatable("jobsplus.gui.toggle_prefix"),
                                Component.translatable("jobsplus.gui.active", ChatColor.boldBlue() + Component.translatable("job.none").getString()));
                    }
                    super.renderTooltip(poseStack, list, Optional.empty(), mouseX + startX, mouseY + startY + 17);
                }
            }
        } else if (isBetween(mouseX, mouseY, imageWidth, 35, imageWidth + 21, 60)) {
            if (jobId >= 0) {
                if (getSelectedJobLevel() != 0) {
                    List<Component> list;
                    final int bossBarId = array[63];

                    if (bossBarId != -1 && Jobs.getJobFromInt(bossBarId) != null) {
                        list = List.of(Component.translatable("jobsplus.gui.toggle_boss_bar"),
                                Component.translatable("jobsplus.gui.active", ChatHandler.ColorizedJobName(Objects.requireNonNull(Jobs.getJobFromInt(bossBarId))).replace(" ", "")));
                    } else {
                        list = List.of(Component.translatable("jobsplus.gui.toggle_boss_bar"),
                                Component.translatable("jobsplus.gui.active", ChatColor.boldBlue() + Component.translatable("job.none").getString()));
                    }
                    super.renderTooltip(poseStack, list, Optional.empty(), mouseX + startX, mouseY + startY + 17);
                }
            }
        }
        if (activeRightButton == 1) {
            if (getItemStack(mouseX, mouseY) != ItemStack.EMPTY) {
                super.renderTooltip(poseStack, getItemStack(mouseX, mouseY).copy(), mouseX + startX, mouseY + startY);
            }
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
        //SETTINGS
        if (isBetween(mouseX, mouseY, 3, height - 20, 18, height - 4))
            RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1);
        blit(poseStack, 3, height - 20, 276, 166, 16, 16, 362, 362);
        RenderColor.normal();

        //DISCORD
        if (isBetween(mouseX, mouseY, width - 19, height - 20, width - 4, height - 5))
            RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1);
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        blit(poseStack, width * 2 - 38, height * 2 - 40, 276 + 16, 166, 32, 32, 362, 362);
        RenderColor.normal();
        poseStack.popPose();

        // JOB BUTTONS
        if (activeLeftButton == 0) {
            for (int i = this.startIndex; i < occurrences && i < Jobs.values().length; ++i) {
                int j = i - this.startIndex;
                int i1 = something + j * 35;
                int j1 = this.imageHeight;
                if (mouseX >= p_99345_ && mouseY >= i1 && mouseX < p_99345_ + 116 && mouseY < i1 + 35) {
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
                        map.put(Jobs.valueOf(Jobs.getEnglishString(i)), new int[]{level, exp});
                    }
                }
                if (activeLeftButton == 2) {
                    if (level == 0) {
                        map.put(Jobs.valueOf(Jobs.getEnglishString(i)), new int[]{level, exp});
                    }
                }
            }
            for (int i = this.startIndex; i < occurrences && i < map.size(); ++i) {
                int j = i - this.startIndex;
                int i1 = something + j * 35;
                int j1 = this.imageHeight;
                if (mouseX >= p_99345_ && mouseY >= i1 && mouseX < p_99345_ + 116 && mouseY < i1 + 35) {
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
                RenderColor.normal();
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
                RenderColor.normal();
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
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getEnglishString(i))));
                    }
                }
                if (activeLeftButton == 2) {
                    if (level == 0) {
                        ints.add(level);
                        ints.add(exp);
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getEnglishString(i))));
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
            if (getSelectedJobLevel() == 0 || jobId != -1) {
                if (isBetween(mouseX - startX, mouseY - startY, 169, 132, 169 + 138, 132 + 17)) {
                    RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                } else {
                    RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1);
                }
                blitThis(poseStack, 150 + 19, 132, 26, imageHeight + 105, 139, 18);
                RenderColor.normal();

            }
            // CRAFTING RECIPE BUTTONS
        } else if (activeRightButton == 1) {
            if (jobId != -1) {
                for (int i = 0; i < Arrays.asList(23, 5, 4, 3, 4, 4, 9, 4, 4, 16).get(jobId); ++i) {
                    int xOffset = 172 + i % 5 * 27;
                    int yOffset = 18 + (i / 5) * 27;
                    if (isBetween(mouseX - startX, mouseY - startY, xOffset, yOffset, xOffset + 23, yOffset + 23)) {
                        RenderSystem.setShaderColor(0.7F, 0.7F, 1F, 1);
                    }
                    blitThis(poseStack, xOffset, yOffset, 142, 210, 24, 24);
                    RenderColor.normal();
                }
                //CONSTRUCTION TABLE
                if (jobId != 0) {
                    blitThis(poseStack, 158, imageHeight, 150, 136, 100, 30);
                    blitThis(poseStack, 218, imageHeight, 226, 136, 100, 30);
                    blitThis(poseStack, 162, 167, 142, 210, 24, 24);
                }
            }
        } else if (activeRightButton == 2) {
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
                RenderColor.normal();
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
                RenderColor.normal();
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
                RenderColor.normal();
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
                RenderColor.normal();
            }
        }
        if (jobId != -1) {
            if (getSelectedJobLevel() != 0) {
                //Job Display Background
                if (array[64] == 1) {
                    if (array[62] == jobId + 1) blitThis(poseStack, imageWidth, 6, 142, 234, 22, 26);
                    else blitThis(poseStack, imageWidth, 6, 164, 234, 19, 26);
                }
                //Boss Bar Background
                if (array[63] == jobId) blitThis(poseStack, imageWidth, 9 + 26, 142, 234, 22, 26);
                else blitThis(poseStack, imageWidth, 9 + 26, 164, 234, 19, 26);
                // Boss Bar Icon
                if (array[63] == jobId) blitThis(poseStack, imageWidth + 2, 9 + 32, 164 + 19, 234, 15, 14);
                else blitThis(poseStack, imageWidth + 1, 9 + 32, 164 + 19, 234, 15, 14);
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
                    font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.level", ChatColor.reset(), level).getString(), startX + 7 + 3 + 35, i1 + 14, 16777215);
                    if (level != 100)
                        font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.exp", ChatColor.reset(), (int) ((double) exp / maxExp * 100), "%").getString(), startX + 7 + 3 + 35, i1 + 23, 16777215);
                } else {
                    font.draw(poseStack, ChatColor.boldRed() + Jobs.getString(i), startX + 7 + 3 + 35, i1 + 3, 16777215);
                    font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.want_this_job").getString(), startX + 7 + 3 + 35, i1 + 14, 16777215);
                    if (array[21] < 2) {
                        font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.cost", ChatColor.reset(), 0).getString(), startX + 7 + 3 + 35, i1 + 23, 16777215);
                    } else {
                        font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.cost", ChatColor.reset(), 10).getString(), startX + 7 + 3 + 35, i1 + 23, 16777215);
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
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getEnglishString(i))));
                    }
                }
                if (activeLeftButton == 2) {
                    if (level == 0) {
                        ints.add(level);
                        ints.add(exp);
                        ints.add(Jobs.getJobInt(Jobs.valueOf(Jobs.getEnglishString(i))));
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
                        font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.level", ChatColor.reset(), level).getString(), startX + 7 + 3 + 35, i1 + 14, 16777215);
                        font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.exp", ChatColor.reset(), (int) ((double) exp / maxExp * 100), "%").getString(), startX + 7 + 3 + 35, i1 + 23, 16777215);
                    } else {
                        font.draw(poseStack, ChatColor.boldRed() + Jobs.getString(currentJobArray[2]), startX + 7 + 3 + 35, i1 + 3, 16777215);
                        font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.want_this_job").getString(), startX + 7 + 3 + 35, i1 + 14, 16777215);
                        if (array[21] < 2) {
                            font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.cost", ChatColor.reset(), 0).getString(), startX + 7 + 3 + 35, i1 + 23, 16777215);
                        } else {
                            font.draw(poseStack, ChatColor.aqua() + Component.translatable("jobsplus.gui.cost", ChatColor.reset(), 10).getString(), startX + 7 + 3 + 35, i1 + 23, 16777215);
                        }
                    }
                }
            }
        }
        if (jobId == -1) {
            if (activeRightButton == 0) drawNoJobSelected(poseStack, "info");
            else if (activeRightButton == 1) drawNoJobSelected(poseStack, "crafting");
            else if (activeRightButton == 2) drawNoJobSelected(poseStack, "powerups");
            else if (activeRightButton == 3) drawNoJobSelected(poseStack, "how_to_get_exp");
        } else {
            int centerR = startX + (imageWidth + 150) / 2;
            if (activeRightButton == 0) {
                drawnBigJobTitle(poseStack);
                drawJobInfo(poseStack);
                if (getSelectedJobLevel() == 0) {
                    drawCenteredStringNew(poseStack, font, ChatColor.white() + Component.translatable("jobsplus.gui.job.start").getString(), centerR, startY + 137, 16777215);
                } else {
                    drawCenteredStringNew(poseStack, font, ChatColor.white() + Component.translatable("jobsplus.gui.job.stop").getString(), centerR, startY + 137, 16777215);
                }
            } else if (activeRightButton == 1) {
                drawCenteredStringNew(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.crafting").getString(), centerR, startY + 6, 16777215);
                if (jobId != 0) {
                    font.draw(poseStack, ChatColor.darkGray() + "Craft the items using", startX + 189, startY + 171, 16777215);
                    font.draw(poseStack, ChatColor.darkGray() + "the Construction Table.", startX + 189, startY + 181, 16777215);
                }
            } else if (activeRightButton == 2) {
                drawCenteredStringNew(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.powerups.powerups").getString(), centerR, startY + 6, 16777215);
                drawCenteredStringNew(poseStack, font, ChatColor.gray() + Component.translatable("jobsplus.gui.powerups.cost").getString(), centerR, startY + 16, 16777215);
                drawCenteredStringNew(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.powerups.superpowers").getString(), centerR, startY + 110, 16777215);
                drawCenteredStringNew(poseStack, font, ChatColor.gray() + Component.translatable("jobsplus.gui.powerups.superpowers.cost").getString(), centerR, startY + 120, 16777215);
                poseStack.pushPose();
                poseStack.scale(0.72F, 0.72F, 0.72F);
                drawCenteredPowerupsString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.powerups." + Jobs.getEnglishString(jobId).toLowerCase() + ".1").getString(), (int) ((startX + (imageWidth + 152) / 2) / 0.72F), (int) ((startY + 33) / 0.72F), 16777215);
                drawCenteredPowerupsString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.powerups." + Jobs.getEnglishString(jobId).toLowerCase() + ".2").getString(), (int) ((startX + (imageWidth + 152) / 2) / 0.72F), (int) ((startY + 58) / 0.72F), 16777215);
                drawCenteredPowerupsString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.powerups." + Jobs.getEnglishString(jobId).toLowerCase() + ".3").getString(), (int) ((startX + (imageWidth + 152) / 2) / 0.72F), (int) ((startY + 83) / 0.72F), 16777215);
                drawCenteredPowerupsString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.powerups." + Jobs.getEnglishString(jobId).toLowerCase() + ".superpower").getString(), (int) ((startX + (imageWidth + 152) / 2) / 0.72F), (int) ((startY + 137) / 0.72F), 16777215);
                poseStack.popPose();
            } else if (activeRightButton == 3) {
                drawnBigJobTitle(poseStack);
                drawCenteredStringNew(poseStack, font, ChatFormatting.STRIKETHROUGH + "             ", centerR, startY + 47, 16777215);
                for (int i = 1; i < 10; ++i) {
                    drawCenteredStringNew(poseStack, font, getTCText(i), centerR, startY + 50 + (i * 10), getTCTextColor(i));
                }
                poseStack.pushPose();
                poseStack.scale(1.2F, 1.2F, 1.2F);
                drawCenteredStringNew(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.exp.title").getString(), (int) ((centerR) / 1.2), (int) ((startY + 37) / 1.2), 16777215);
            }
        }
    }

    public void renderItems(int x, int y) {
        if (minecraft == null) return;
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
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
                jobItemsArray.addAll(Arrays.asList(ModItems.EXP_JAR.get().getDefaultInstance(), ModItems.EXPERIENCE_BOTTLE.get().getDefaultInstance(), ModItems.CURSE_BREAKER.get().getDefaultInstance()));
            } else if (jobId == 4) {
                jobItemsArray.addAll(Arrays.asList(ModItems.FARMERS_HOE_LEVEL_1.get().getDefaultInstance(), ModItems.FARMERS_HOE_LEVEL_2.get().getDefaultInstance(), ModItems.FARMERS_HOE_LEVEL_3.get().getDefaultInstance(), ModItems.FARMERS_HOE_LEVEL_4.get().getDefaultInstance()));
            } else if (jobId == 5) {
                jobItemsArray.addAll(Arrays.asList(ModItems.FISHERMANS_ROD_LEVEL_1.get().getDefaultInstance(), ModItems.FISHERMANS_ROD_LEVEL_2.get().getDefaultInstance(), ModItems.FISHERMANS_ROD_LEVEL_3.get().getDefaultInstance(), ModItems.FISHERMANS_ROD_LEVEL_4.get().getDefaultInstance()));
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
                itemRenderer.renderAndDecorateItem(jobItemsArray.get(i), xOffset, yOffset);
            }
        }
        if (jobId >= 0) {
            if (getSelectedJobLevel() != 0 && Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()) {
                if (array[62] == jobId + 1)
                    itemRenderer.renderAndDecorateItem(Items.NAME_TAG.getDefaultInstance(), startX + 328, startY + 11);
                else
                    itemRenderer.renderAndDecorateItem(Items.NAME_TAG.getDefaultInstance(), startX + 327, startY + 11);
            }
            //CONSTRUCTION TABLE
            if (jobId != 0 && activeRightButton == 1) {
                itemRenderer.renderAndDecorateItem(ModItems.CONSTRUCTION_TABLE.get().getDefaultInstance(), startX + 166, startY + 171);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;

        //SETTINGS
        if (isBetween(mouseX, mouseY, 3, height - 20, 19, height - 4)) {
            ModPacketHandler.INSTANCE.sendToServer(new PacketUserSettingsServer("MAIN"));
        }

        //DISCORD
        if (isBetween(mouseX, mouseY, width - 19, height - 20, width - 4, height - 5)) {
            try {
                Util.getPlatform().openUri(new URI("https://daqem.com/discord"));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        //CONSTRUCTION TABLE
        if (jobId != 0 && activeRightButton == 1) {
            if (isBetween(mouseX, mouseY, startX + 162, startY + 168, startX + 162 + 24, startY + 168 + 23)) {
                JobsPlusJeiPlugin.getJeiRuntime().ifPresent(jeiRuntime ->
                        jeiRuntime.getRecipesGui().show(jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(
                                RecipeIngredientRole.OUTPUT,
                                jeiRuntime.getIngredientManager().getIngredientType(ModItems.CONSTRUCTION_TABLE.get().getDefaultInstance()),
                                ModItems.CONSTRUCTION_TABLE.get().getDefaultInstance())));
            }
        }

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

        //DISPLAY AND BOSSBAR
        if (jobId >= 0) {
            if (getSelectedJobLevel() != 0) {
                //DISPLAY
                if (Config.DISPLAY_JOB_IN_CHAT_AND_TAB.get()) {
                    if (isBetween(mouseX, mouseY, imageWidth, 6, imageWidth + 21, 31)) {
                        String job;
                        if (jobId + 1 == array[62])
                            job = "NONE";
                        else
                            job = Jobs.getEnglishString(jobId);
                        ModPacketHandler.INSTANCE.sendToServer(new PacketJobDisplay(job));
                        ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(jobId, activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
                    }
                }
                //BOSSBAR
                if (isBetween(mouseX, mouseY, imageWidth, 35, imageWidth + 21, 60)) {
                    if (jobId == array[63]) ModPacketHandler.INSTANCE.sendToServer(new PacketBossBarr("NONE"));
                    else ModPacketHandler.INSTANCE.sendToServer(new PacketBossBarr(Jobs.getEnglishString(jobId)));
                    ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(jobId, activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
                }
            }
        }
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
        }
        //PERFORMING JOBS BUTTON
        else if (isBetween(mouseX, mouseY, 6 + 28, -22, 32 + 28, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (activeLeftButton != 1) {
                selectedButton = -1;
                jobId = -1;
                startIndex = 0;
                scrollOffs = 0;
            }
            activeLeftButton = 1;
        }
        //NOT PERFORMING JOBS BUTTON
        else if (isBetween(mouseX, mouseY, 6 + 28 + 28, -22, 32 + 28 + 28, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (activeLeftButton != 2) {
                selectedButton = -1;
                jobId = -1;
                startIndex = 0;
                scrollOffs = 0;
            }
            activeLeftButton = 2;
        }
        //JOB INFO BUTTON
        else if (isBetween(mouseX, mouseY, 6 + 150, -22, 32 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 0;
        }
        //CRAFTING RECIPES BUTTON
        else if (isBetween(mouseX, mouseY, 6 + 28 + 150, -22, 32 + 28 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 1;
        }
        // POWERUPS BUTTON
        else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 150, -22, 32 + 28 + 28 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 2;
        }
        // HOW TO GET EXP BUTTON
        else if (isBetween(mouseX, mouseY, 6 + 28 + 28 + 28 + 150, -22, 32 + 28 + 28 + 28 + 150, 0)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            activeRightButton = 3;
        }
        // RIGHT BUTTONS
        if (jobId != -1) {
            // JOB START STOP BUTTON
            if (activeRightButton == 0) {
                if (isBetween(mouseX, mouseY, 169, 132, 169 + 138, 132 + 18)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    if (getSelectedJobLevel() == 0) {
                        if (array[21] < Config.AMOUNT_OF_FREE_JOBS.get()) {
                            openConfirmScreen(Component.translatable("confirm.start"), "start", 0);
                        } else {
                            if (array[20] >= Config.JOB_START_COST.get())
                                openConfirmScreen(Component.translatable("confirm.start_paid", Config.JOB_START_COST.get()), "start_paid", 0);
                            else
                                openConfirmScreen(Component.translatable("confirm.not_enough_coins_start"), "not_enough_coins_start", 0);

                        }
                    } else if (getSelectedJobLevel() == Config.JOB_LEVEL_TO_STOP_JOB_FOR_FREE.get()) {
                        openConfirmScreen(Component.translatable("confirm.stop_free"), "stop_free", 0);
                    } else {
                        if (array[20] >= Config.JOB_STOP_COST.get())
                            openConfirmScreen(Component.translatable("confirm.stop", Config.JOB_STOP_COST.get()), "stop", 0);
                        else
                            openConfirmScreen(Component.translatable("confirm.not_enough_coins_stop"), "not_enough_coins_stop", 0);

                    }
                }
            }
            // CRAFTING RECIPES
            if (activeRightButton == 1) {
                ItemStack itemStack = getItemStack(mouseX, mouseY);
                if (itemStack != ItemStack.EMPTY) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    JobsPlusJeiPlugin.getJeiRuntime().ifPresent(jeiRuntime -> {
                        IFocus<ItemStack> focus = jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.OUTPUT, jeiRuntime.getIngredientManager().getIngredientType(itemStack), itemStack);

                        IRecipesGui recipesGui = jeiRuntime.getRecipesGui();
                        recipesGui.show(focus);
                    });
                }
            }
            // POWERUPS
            if (activeRightButton == 2) {
                int clicked = 0;
                if (isBetween(mouseX, mouseY, 169, 27, 169 + 139, 27 + 18)) clicked = 1;
                if (isBetween(mouseX, mouseY, 169, 27 + 25, 169 + 139, 27 + 18 + 25)) clicked = 2;
                if (isBetween(mouseX, mouseY, 169, 27 + 50, 169 + 139, 27 + 18 + 50)) clicked = 3;
                if (clicked != 0) {
                    if (array[21 + (jobId * 3) + clicked] == 1 || array[21 + (jobId * 3) + clicked] == 2) {
                        ModPacketHandler.INSTANCE.sendToServer(new PacketMenuPowerUp(true, Jobs.getJobFromInt(jobId), clicked + 1));
                        ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(jobId, activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
                    } else {
                        //Check if player has enough coins to buy a power-up
                        if (array[20] >= Config.POWERUP_COST.get()) {
                            //Check is the player has the job.
                            if (getSelectedJobLevel() != 0)
                                openConfirmScreen(Component.translatable("confirm.powerup"), "powerup", clicked);
                            else {
                                openConfirmScreen(Component.translatable("confirm.job_not_enabled"), "job_not_enabled", clicked);
                            }
                        } else {
                            openConfirmScreen(Component.translatable("confirm.not_enough_coins_powerup"), "not_enough_coins_powerup", clicked);
                        }
                    }
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                if (isBetween(mouseX, mouseY, 169, 27 + 103, 169 + 139, 27 + 18 + 103)) {
                    if (getSelectedJobLevel() == 100) {
                        ModPacketHandler.INSTANCE.sendToServer(new PacketSwitchSuperpower(Jobs.getJobFromInt(jobId)));
                        ModPacketHandler.INSTANCE.sendToServer(new PacketOpenMenu(jobId, activeLeftButton, activeRightButton, selectedButton, scrollOffs, startIndex));
                    } else {
                        openConfirmScreen(Component.translatable("error.level.must_be_100"), "must_be_level_100", clicked);
                    }
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, clickType);
    }

    private ItemStack getItemStack(double mouseX, double mouseY) {
        ItemStack itemStack = ItemStack.EMPTY;
        mouseX = mouseX - 150D;
        int COL_1_XT = 22, COL_1_XB = 46, ROW_1_YT = 18, ROW_1_YB = 42,
                COL_2_XT = 49, COL_2_XB = 73, ROW_2_YT = 45, ROW_2_YB = 69,
                COL_3_XT = 76, COL_3_XB = 100, ROW_3_YT = 72, ROW_3_YB = 96,
                COL_4_XT = 103, COL_4_XB = 127, ROW_4_YT = 99, ROW_4_YB = 123,
                COL_5_XT = 130, COL_5_XB = 154, ROW_5_YT = 126, ROW_5_YB = 150;

        if (jobId == 0) {
            final ItemStack potion = Items.POTION.getDefaultInstance();
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.EXTRA_STRONG_SWIFTNESS.get());
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_EXTRA_STRONG_SWIFTNESS.get());
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.SUPER_STRONG_SWIFTNESS.get());
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_SUPER_STRONG_SWIFTNESS.get());
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_1_YT, COL_5_XB, ROW_1_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.HASTE.get());
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_2_YT, COL_1_XB, ROW_2_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_HASTE.get());
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_2_YT, COL_2_XB, ROW_2_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.STRONG_HASTE.get());
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_2_YT, COL_3_XB, ROW_2_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_STRONG_HASTE.get());
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_2_YT, COL_4_XB, ROW_2_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.EXTRA_STRONG_STRENGTH.get());
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_2_YT, COL_5_XB, ROW_2_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_EXTRA_STRONG_STRENGTH.get());
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_3_YT, COL_1_XB, ROW_3_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.SUPER_STRONG_STRENGTH.get());
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_3_YT, COL_2_XB, ROW_3_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_SUPER_STRONG_STRENGTH.get());
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_3_YT, COL_3_XB, ROW_3_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_STRONG_REGENERATION.get());
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_3_YT, COL_4_XB, ROW_3_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.EXTRA_LONG_STRONG_REGENERATION.get());
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_3_YT, COL_5_XB, ROW_3_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.EXTRA_STRONG_REGENERATION.get());
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_4_YT, COL_1_XB, ROW_4_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LUCK.get());
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_4_YT, COL_2_XB, ROW_4_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_LUCK.get());
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_4_YT, COL_3_XB, ROW_4_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.STRONG_LUCK.get());
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_4_YT, COL_4_XB, ROW_4_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_STRONG_LUCK.get());
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_4_YT, COL_5_XB, ROW_4_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.JESUS.get());
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_5_YT, COL_1_XB, ROW_5_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_JESUS.get());
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_5_YT, COL_2_XB, ROW_5_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.FLYING.get());
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_5_YT, COL_3_XB, ROW_5_YB)) {
                itemStack = PotionUtils.setPotion(potion, ModPotions.LONG_FLYING.get());
            }
        } else if (jobId == 1) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.SMALL_BACKPACK.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.MEDIUM_BACKPACK.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.LARGE_BACKPACK.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.HUGE_BACKPACK.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_1_YT, COL_5_XB, ROW_1_YB)) {
                itemStack = ModItems.ENDER_BACKPACK.get().getDefaultInstance();
            }
        } else if (jobId == 2) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.DIGGERS_EXCAVATOR_LEVEL_4.get().getDefaultInstance();
            }
        } else if (jobId == 3) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.EXP_JAR.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.EXPERIENCE_BOTTLE.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.CURSE_BREAKER.get().getDefaultInstance();
            }
        } else if (jobId == 4) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.FARMERS_HOE_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.FARMERS_HOE_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.FARMERS_HOE_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.FARMERS_HOE_LEVEL_4.get().getDefaultInstance();
            }
        } else if (jobId == 5) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.FISHERMANS_ROD_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.FISHERMANS_ROD_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.FISHERMANS_ROD_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.FISHERMANS_ROD_LEVEL_4.get().getDefaultInstance();
            }
        } else if (jobId == 6) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = Items.WHITE_WOOL.getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.HUNTERS_SWORD_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.HUNTERS_SWORD_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.HUNTERS_SWORD_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_1_YT, COL_5_XB, ROW_1_YB)) {
                itemStack = ModItems.HUNTERS_SWORD_LEVEL_4.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_2_YT, COL_1_XB, ROW_2_YB)) {
                itemStack = ModItems.HUNTERS_BOW_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_2_YT, COL_2_XB, ROW_2_YB)) {
                itemStack = ModItems.HUNTERS_BOW_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_2_YT, COL_3_XB, ROW_2_YB)) {
                itemStack = ModItems.HUNTERS_BOW_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_2_YT, COL_4_XB, ROW_2_YB)) {
                itemStack = ModItems.HUNTERS_BOW_LEVEL_4.get().getDefaultInstance();
            }
        } else if (jobId == 7) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.LUMBERJACK_AXE_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.LUMBERJACK_AXE_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.LUMBERJACK_AXE_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.LUMBERJACK_AXE_LEVEL_4.get().getDefaultInstance();
            }
        } else if (jobId == 8) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.MINERS_HAMMER_LEVEL_1.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.MINERS_HAMMER_LEVEL_2.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.MINERS_HAMMER_LEVEL_3.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.MINERS_HAMMER_LEVEL_4.get().getDefaultInstance();
            }
        } else if (jobId == 9) {
            if (isBetween(mouseX, mouseY, COL_1_XT, ROW_1_YT, COL_1_XB, ROW_1_YB)) {
                itemStack = ModItems.REINFORCED_IRON_HELMET.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_1_YT, COL_2_XB, ROW_1_YB)) {
                itemStack = ModItems.REINFORCED_IRON_CHESTPLATE.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_1_YT, COL_3_XB, ROW_1_YB)) {
                itemStack = ModItems.REINFORCED_IRON_LEGGINGS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_1_YT, COL_4_XB, ROW_1_YB)) {
                itemStack = ModItems.REINFORCED_IRON_BOOTS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_1_YT, COL_5_XB, ROW_1_YB)) {
                itemStack = ModItems.OBSIDIAN_HELMET.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_2_YT, COL_1_XB, ROW_2_YB)) {
                itemStack = ModItems.OBSIDIAN_CHESTPLATE.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_2_YT, COL_2_XB, ROW_2_YB)) {
                itemStack = ModItems.OBSIDIAN_LEGGINGS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_2_YT, COL_3_XB, ROW_2_YB)) {
                itemStack = ModItems.OBSIDIAN_BOOTS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_2_YT, COL_4_XB, ROW_2_YB)) {
                itemStack = ModItems.REINFORCED_DIAMOND_HELMET.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_2_YT, COL_5_XB, ROW_2_YB)) {
                itemStack = ModItems.REINFORCED_DIAMOND_CHESTPLATE.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_3_YT, COL_1_XB, ROW_3_YB)) {
                itemStack = ModItems.REINFORCED_DIAMOND_LEGGINGS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_2_XT, ROW_3_YT, COL_2_XB, ROW_3_YB)) {
                itemStack = ModItems.REINFORCED_DIAMOND_BOOTS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_3_XT, ROW_3_YT, COL_3_XB, ROW_3_YB)) {
                itemStack = ModItems.REINFORCED_NETHERITE_HELMET.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_4_XT, ROW_3_YT, COL_4_XB, ROW_3_YB)) {
                itemStack = ModItems.REINFORCED_NETHERITE_CHESTPLATE.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_5_XT, ROW_3_YT, COL_5_XB, ROW_3_YB)) {
                itemStack = ModItems.REINFORCED_NETHERITE_LEGGINGS.get().getDefaultInstance();
            } else if (isBetween(mouseX, mouseY, COL_1_XT, ROW_4_YT, COL_1_XB, ROW_4_YB)) {
                itemStack = ModItems.REINFORCED_NETHERITE_BOOTS.get().getDefaultInstance();
            }
        }
        return itemStack;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int clickType, double speedX, double speedY) {
        if (this.scrolling) {
            int i = startY + 14;
            int j = i + 54;
            this.scrollOffs = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 3F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, clickType, speedX, speedY);
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

    public void openConfirmScreen(Component component, String string, int powerUp) {
        Minecraft.getInstance().setScreen(new ConfirmationScreen(component, string, Jobs.getJobFromInt(jobId), powerUp, this));

    }

    private void drawNoJobSelected(PoseStack poseStack, String string) {
        for (int i = 1; i < 6; i++) {
            drawCenteredStringNew(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.no_job_selected." + string + "." + i).getString(), startX + (imageWidth + 150) / 2, startY + 42 + (i * 9), 16777215);
        }
    }

    private void drawJobInfo(PoseStack poseStack) {
        for (int i = 1; i < 10; i++) {
            drawCenteredStringNew(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.info." + Jobs.getEnglishString(jobId).toLowerCase() + "." + i, "" + ChatColor.boldWhite(), "" + ChatColor.darkGray(), "" + ChatColor.boldWhite(), "" + ChatColor.darkGray()).getString(), startX + (imageWidth + 150) / 2, startY + 35 + (i * 9), 16777215);
        }
    }

    private String getTCText(int num) {
        String[] split = (Component.translatable("jobsplus.gui.exp." + Jobs.getEnglishString(jobId).toLowerCase() + "." + num).getString()).split(">text:");
        return split.length > 1 ? split[1] : "";
    }

    private int getTCTextColor(int num) {
        String replace = (Component.translatable("jobsplus.gui.exp." + Jobs.getEnglishString(jobId).toLowerCase() + "." + num).getString()).split(">text:")[0].replace("color:<", "");
        return isNumeric(replace) ? Integer.parseInt(replace) : 16777215;
    }

    public void drawnBigJobTitle(PoseStack poseStack) {
        poseStack.pushPose();
        poseStack.scale(2F, 2F, 2F);
        font.draw(poseStack, getSelectedJobLevel() != 0 ? ChatColor.boldGreen() + Jobs.getString(jobId) : ChatColor.boldRed() + Jobs.getString(jobId), (startX + 156) / 2F, (startY + 5) / 2F, 16777215);
        poseStack.popPose();
        if (getSelectedJobLevel() != 0) {
            font.draw(poseStack, ChatColor.darkGray() + Component.translatable("jobsplus.gui.level", ChatColor.reset(), getSelectedJobLevel()).getString(), startX + 156, startY + 22, 16777215);
            if (getSelectedJobLevel() != 100)
                font.draw(poseStack, ChatColor.darkGray() + Component.translatable("jobsplus.gui.exp", ChatColor.reset(), "[" + getSelectedJobEXP() + "/" + getSelectedJobMaxEXP() + "]").getString(), startX + 216, startY + 22, 16777215);
        } else {
            font.draw(poseStack, ChatColor.darkGray() + Component.translatable("jobsplus.gui.want_this_job").getString(), startX + 156, startY + 22, 16777215);
            if (array[21] < 2) {
                drawRightAlignedString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.cost", ChatColor.reset(), 0).getString(), startX + imageWidth - 10, startY + 22, 16777215);
            } else {
                drawRightAlignedString(poseStack, font, ChatColor.darkGray() + Component.translatable("jobsplus.gui.cost", ChatColor.reset(), 10).getString(), startX + imageWidth - 10, startY + 22, 16777215);
            }
        }
        font.draw(poseStack, ChatFormatting.STRIKETHROUGH + "                                        ", startX + 156, startY + 28, 16777215);
    }

    @Override
    protected void init() {
        startX = (this.width - imageWidth) / 2;
        startY = (this.height - imageHeight) / 2;
        super.init();
    }

    public int getActiveLeftButton() {
        return activeLeftButton;
    }

    public int getActiveRightButton() {
        return activeRightButton;
    }

    public int getSelectedButton() {
        return selectedButton;
    }

    public float getScrollOffs() {
        return scrollOffs;
    }

    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
