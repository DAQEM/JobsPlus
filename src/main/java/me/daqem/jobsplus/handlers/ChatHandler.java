package me.daqem.jobsplus.handlers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;

public class ChatHandler {

    public static Component header(String headerText) {
        return new KeybindComponent(ChatFormatting.GRAY + "=======" +
                ChatFormatting.GREEN + "=======" + ChatFormatting.DARK_GREEN + ChatFormatting.BOLD + " " + headerText
                + " " + ChatFormatting.RESET + ChatFormatting.GREEN + "=======" + ChatFormatting.GRAY + "=======\n");
    }

    public static Component footer(int centerLength) {
        return new KeybindComponent(ChatFormatting.GRAY + "\n=======" +
                ChatFormatting.GREEN + "=======" + ChatFormatting.GRAY + "==" + "=".repeat(Math.max(0, centerLength)) +
                ChatFormatting.GREEN + "=======" + ChatFormatting.GRAY + "=======");
    }
}
