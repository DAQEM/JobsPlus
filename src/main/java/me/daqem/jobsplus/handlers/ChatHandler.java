package me.daqem.jobsplus.handlers;

import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.world.entity.player.Player;

public class ChatHandler {

    public static String header(String headerText) {
        return ChatFormatting.GRAY + "=======" +
                ChatFormatting.GREEN + "=======" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD + " " + headerText
                + " " + ChatFormatting.RESET + ChatFormatting.GREEN + "=======" + ChatFormatting.GRAY + "=======\n";
    }

    public static String footer(int centerLength) {
        return ChatFormatting.GRAY + "\n=======" +
                ChatFormatting.GREEN + "=======" + ChatFormatting.GRAY + "==" + "=".repeat(Math.max(0, centerLength)) +
                ChatFormatting.GREEN + "=======" + ChatFormatting.GRAY + "=======";
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
                return ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD + "ALCHEMIST " + ChatFormatting.RESET;
            }
            case BUILDER -> {
                return ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "BUILDER " + ChatFormatting.RESET;
            }
            case DIGGER -> {
                return ChatFormatting.YELLOW + "" + ChatFormatting.BOLD + "DIGGER " + ChatFormatting.RESET;
            }
            case ENCHANTER -> {
                return ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.BOLD + "ENCHANTER " + ChatFormatting.RESET;
            }
            case FARMER -> {
                return ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "FARMER " + ChatFormatting.RESET;
            }
            case FISHERMAN -> {
                return ChatFormatting.AQUA + "" + ChatFormatting.BOLD + "FISHERMAN " + ChatFormatting.RESET;
            }
            case HUNTER -> {
                return ChatFormatting.DARK_RED + "" + ChatFormatting.BOLD + "HUNTER " + ChatFormatting.RESET;
            }
            case LUMBERJACK -> {
                return ChatFormatting.GOLD + "" + ChatFormatting.BOLD + "LUMBERJACK " + ChatFormatting.RESET;
            }
            case MINER -> {
                return ChatFormatting.GRAY + "" + ChatFormatting.BOLD + "MINER " + ChatFormatting.RESET;
            }
            case SMITH -> {
                return ChatFormatting.DARK_GRAY + "" + ChatFormatting.BOLD + "SMITH " + ChatFormatting.RESET;
            }
            default -> {
                return "";
            }
        }
    }
}
