package me.daqem.jobsplus.handlers;

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
}
