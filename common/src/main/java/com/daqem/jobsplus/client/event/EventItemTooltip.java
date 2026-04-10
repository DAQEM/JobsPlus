package com.daqem.jobsplus.client.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.condition.item.ItemCondition;
import com.daqem.arc.data.condition.item.ItemsCondition;
import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.itemrestrictions.data.ItemRestrictionManager;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.jobs.JobsScreen;
import com.daqem.jobsplus.client.gui.powerups.PowerupsScreen;
import com.daqem.jobsplus.config.JobsPlusClientConfig;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.IJobCondition;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.knot.Knot;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EventItemTooltip {

    public static void registerEvent() {
        Knot.Events.Client.TOOLTIP_GATHER_COMPONENTS.register((stack, tooltipContext, flag, lines) -> {
            if (!JobsPlusClientConfig.showJobRestrictionTooltip.get()) {
                return;
            }
            if (stack.isEmpty()) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.screen instanceof JobsScreen || minecraft.screen instanceof PowerupsScreen) {
                return;
            }

            List<ItemRestriction> restrictions = ItemRestrictionManager.getInstance().getItemRestrictions();

            if (minecraft.level == null) return;

            for (ItemRestriction restriction : restrictions) {
                if (matchesStack(restriction, stack, minecraft)) {
                    addTooltipForRestriction(restriction, lines, minecraft);
                }
            }
        });
    }

    private static boolean matchesStack(ItemRestriction restriction, ItemStack stack, Minecraft minecraft) {
        for (ICondition condition : restriction.getConditions()) {
            if ((condition instanceof ItemCondition || condition instanceof ItemsCondition) && minecraft.player instanceof ArcPlayer arcPlayer) {
                if (condition.isMet(new ActionDataBuilder(arcPlayer, IActionType.USE_ITEM)
                        .withData(IActionDataType.ITEM_STACK, stack).build())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void addTooltipForRestriction(ItemRestriction restriction, List<Component> lines, Minecraft minecraft) {
        List<Component> newLines = new ArrayList<>(List.of(JobsPlus.API.literal("")));
        for (ICondition condition : restriction.getConditions()) {
            if (condition instanceof IJobCondition jobCondition) {
                JobInstance jobInstance = JobInstance.of(jobCondition.getJobLocation());
                if (jobInstance != null && minecraft.player instanceof JobsPlayer jobsPlayer) {
                    Job job = jobsPlayer.jobsplus$getJob(jobInstance);
                    if (job != null && job.getLevel() >= jobCondition.getRequiredLevel()) {
                        continue;
                    }

                    newLines.add(JobsPlus.API.translatable(
                            "tooltip.requirement",
                            jobInstance.getName().withStyle(style ->
                                    style.withColor(jobInstance.getColorDecimal())),
                            jobCondition.getRequiredLevel()
                    ));
                }
            }
        }

        if (newLines.size() > 1) {
            lines.addAll(newLines);
        }
    }
}