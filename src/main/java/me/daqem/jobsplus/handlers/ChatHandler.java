package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.enums.ChatColor;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.network.chat.KeybindComponent;
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
        player.sendMessage(new KeybindComponent(message), player.getUUID());
    }

    public static String capitalizeWord(String str){
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord= new StringBuilder();
        for(String w : words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

    public static String ColorizedJobName(Jobs job) {
        switch (job) {
            case ALCHEMIST -> {
                return ChatColor.boldDarkPurple() + "ALCHEMIST " + ChatColor.reset();
            }
            case BUILDER -> {
                return ChatColor.boldDarkGreen() + "BUILDER " +ChatColor.reset();
            }
            case DIGGER -> {
                return ChatColor.boldYellow() + "DIGGER " +ChatColor.reset();
            }
            case ENCHANTER -> {
                return ChatColor.boldLightPurple() + "ENCHANTER " +ChatColor.reset();
            }
            case FARMER -> {
                return ChatColor.boldGreen() + "FARMER " +ChatColor.reset();
            }
            case FISHERMAN -> {
                return ChatColor.boldAqua() + "FISHERMAN " +ChatColor.reset();
            }
            case HUNTER -> {
                return ChatColor.boldDarkRed() + "HUNTER " +ChatColor.reset();
            }
            case LUMBERJACK -> {
                return ChatColor.boldGold() + "LUMBERJACK " +ChatColor.reset();
            }
            case MINER -> {
                return ChatColor.boldGray() + "MINER " +ChatColor.reset();
            }
            case SMITH -> {
                return ChatColor.boldDarkGray() + "SMITH " +ChatColor.reset();
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
