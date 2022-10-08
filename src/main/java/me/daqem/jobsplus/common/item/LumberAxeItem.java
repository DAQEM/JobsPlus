package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.events.EventWaitTicks;
import me.daqem.jobsplus.handlers.ExpHandler;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.handlers.SoundHandler;
import me.daqem.jobsplus.init.ModItems;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LumberAxeItem extends JobsPlusItem.Axe {

    private static final Jobs JOB = Jobs.LUMBERJACK;

    public LumberAxeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    public void attemptFellTree(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return;
        if (JobGetters.getJobLevel(player, JOB) < getRequiredLevel()) return;
        int maxLogs = getMaxLogs();

        ArrayList<BlockPos> logs = new ArrayList<>();
        ArrayList<BlockPos> candidates = new ArrayList<>();
        candidates.add(pos);

        for (int i = 0; i < candidates.size(); i++) {
            if (logs.size() > maxLogs) {
                ExpHandler.addEXPLow(player, JOB);
                HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.red() + "This tree is too big to cut down with this axe.");
                return;
            }

            BlockPos candidate = candidates.get(i);

            if (level.getBlockState(candidate).is(BlockTags.LOGS)) {
                logs.add(candidate);
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            BlockPos neighbor = candidate.offset(x, y, z);
                            if (candidates.contains(neighbor)) continue;
                            candidates.add(neighbor);
                        }
                    }
                }
            }
        }
        if (logs.size() == 0) return;
        EventWaitTicks.waitTicks(player, EventWaitTicks.Type.TREE_FELLER, null, logs);
    }

    @Override
    public @NotNull
    InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player.isCrouching() && !level.isClientSide) {
            ItemStack stack = player.getMainHandItem();
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("mode")) {
                if (tag.getInt("mode") == 0) tag.putInt("mode", 1);
                else tag.putInt("mode", 0);
            } else {
                tag.putInt("mode", 0);
            }
            HotbarMessageHandler.sendHotbarMessageServer((ServerPlayer) player, ChatColor.boldDarkGreen() + "Mode: " + ChatColor.green() + getModeString(stack));
            SoundHandler.playChangeToolModeSound(player);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            tooltip.addAll(List.of(Component.literal(ChatColor.boldDarkGreen() + "Requirements:"),
                    Component.literal(ChatColor.green() + "Job: " + ChatColor.reset() + "Lumberjack"),
                    Component.literal(ChatColor.green() + "Job Level: " + ChatColor.reset() + getRequiredLevel()),
                    Component.literal(""),
                    Component.literal(ChatColor.boldDarkGreen() + "About:"),
                    Component.literal(ChatColor.green() + "Item Level: " + ChatColor.reset() + Objects.requireNonNull(stack.getItem().getDescriptionId()).replace("item.jobsplus.lumberjack_axe_level_", "")),
                    Component.literal(ChatColor.green() + "Modes: " + ChatColor.reset() + "Tree Feller and Single Block"),
                    Component.literal(ChatColor.green() + "Max Logs: " + ChatColor.reset() + getMaxLogs()),
                    Component.literal(""),
                    Component.literal(ChatColor.boldDarkGreen() + "Controls:"),
                    Component.literal(ChatColor.gray() + "Shift + right-click to change the mode.")));
        } else {
            if (stack.getOrCreateTag().contains("mode")) {
                tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Mode: " + ChatColor.reset() + getModeString(stack)));
            }
            tooltip.add(Component.literal(ChatColor.gray() + "Hold [SHIFT] for more info."));
        }
        if (stack.isEnchanted()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal(ChatColor.boldDarkGreen() + "Enchantments:"));
        }
    }

    public String getModeString(ItemStack stack) {
        return stack.getOrCreateTag().getInt("mode") == 0 ? "Tree Feller" : "Single Block";
    }

    public int getMaxLogs() {
        if (this.equals(ModItems.LUMBERJACK_AXE_LEVEL_1.get())) return 16;
        else if (this.equals(ModItems.LUMBERJACK_AXE_LEVEL_2.get())) return 48;
        else if (this.equals(ModItems.LUMBERJACK_AXE_LEVEL_3.get())) return 128;
        else return 256;
    }
}
