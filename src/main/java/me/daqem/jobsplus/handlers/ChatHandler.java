package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.player.Player;

public class ChatHandler {

    private static final String GREEN_DIVIDER = ChatColor.green() + "=======";
    private static final String GRAY_DIVIDER = ChatColor.gray() + "=======";

    public static String header(String headerText) {
        return GRAY_DIVIDER + GREEN_DIVIDER + ChatColor.boldDarkGreen() + " " +
                headerText + " " + GREEN_DIVIDER + GRAY_DIVIDER + "\n";
    }

    public static String footer(int centerLength) {
        return "\n" + GRAY_DIVIDER + GREEN_DIVIDER + ChatColor.gray() + "==" + "=".repeat(Math.max(0, centerLength)) +
                GREEN_DIVIDER + GRAY_DIVIDER;
    }

    public static void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }

    public static void sendLevelUpMessage(Player player, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.boldWhite()).append("Levels:").append(ChatColor.reset());
        for (Jobs job1 : Jobs.values()) {
            if (JobGetters.getJobLevel(player, job1) > 0) {
                stringBuilder.append("\n").append(ChatHandler.ColorizedJobName(job1)).append(ChatColor.boldDarkGray()).append("> ").append(ChatColor.white()).append(JobGetters.getJobLevel(player, job1));
            }
        }
        player.sendSystemMessage(Component.literal(message).withStyle(Component.literal("").getStyle()
                .withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(stringBuilder.toString())))
                .withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + player.getScoreboardName() + " "))));
    }

    public static String capitalizeWord(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

    public static String ColorizedJobName(Jobs job) {
        switch (job) {
            case ALCHEMIST -> {
                return ChatColor.boldDarkPurple() + Component.translatable("job.alchemist").getString() + " " + ChatColor.reset();
            }
            case BUILDER -> {
                return ChatColor.boldDarkGreen() + Component.translatable("job.builder").getString() + " " + ChatColor.reset();
            }
            case DIGGER -> {
                return ChatColor.boldYellow() + Component.translatable("job.digger").getString() + " " + ChatColor.reset();
            }
            case ENCHANTER -> {
                return ChatColor.boldLightPurple() + Component.translatable("job.enchanter").getString() + " " + ChatColor.reset();
            }
            case FARMER -> {
                return ChatColor.boldGreen() + Component.translatable("job.farmer").getString() + " " + ChatColor.reset();
            }
            case FISHERMAN -> {
                return ChatColor.boldAqua() + Component.translatable("job.fisherman").getString() + " " + ChatColor.reset();
            }
            case HUNTER -> {
                return ChatColor.boldDarkRed() + Component.translatable("job.hunter").getString() + " " + ChatColor.reset();
            }
            case LUMBERJACK -> {
                return ChatColor.boldGold() + Component.translatable("job.lumberjack").getString() + " " + ChatColor.reset();
            }
            case MINER -> {
                return ChatColor.boldGray() + Component.translatable("job.miner").getString() + " " + ChatColor.reset();
            }
            case SMITH -> {
                return ChatColor.boldDarkGray() + Component.translatable("job.smith").getString() + " " + ChatColor.reset();
            }
            default -> {
                return "";
            }
        }
    }

    public static String getAna(Jobs job) {
        return job == Jobs.ALCHEMIST || job == Jobs.ENCHANTER ? "an" : "a";
    }
}
