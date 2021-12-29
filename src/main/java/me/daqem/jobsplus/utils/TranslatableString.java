package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.utils.enums.ChatColor;

public class TranslatableString extends ChatColor {

    public static String get(String message) {
        if (message.equals("error.magic")) {
            return red() + "You have not learned this type of magic yet.";
        } else if (message.equals("error.magic.tool")) {
            return red() + "Your tool doesn't know this type of magic.";
        }
        return "";
    }

    public static String get(String message, Object object) {
        if (message.equals("success.exp.extract")) {
            return boldDarkGreen() + object + green() + " EXP extracted.";
        } else if (message.equals("success.exp.insert")) {
            return boldDarkGreen() + object + green() + " EXP inserted.";
        }
        return "";
    }
}
