package com.daqem.jobsplus.config;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

public class JobsPlusClientConfig {
    public static final IConfig config;

    public static final IConfigEntry<Boolean> showExpAboveHotbar;
    public static final IConfigEntry<Boolean> showYourLevelUpChatMessages;
    public static final IConfigEntry<Boolean> showYourLevelUpToastMessages;
    public static final IConfigEntry<Boolean> showPlayersLevelUpChatMessages;
    public static final IConfigEntry<Boolean> showRestrictionUnlockToastMessage;
    public static final IConfigEntry<Boolean> showPowerupUnlockToastMessage;

    public static final IConfigEntry<Boolean> playLevelUpSound;
    public static final IConfigEntry<Double> soundVolume;

    public static final IConfigEntry<Boolean> showJobRestrictionTooltip;

    static {
        IConfigBuilder builder = new ConfigBuilder(JobsPlus.MOD_ID, "jobsplus-client", ConfigExtension.YAML, ConfigType.CLIENT);

        builder.push("chat");
        showExpAboveHotbar = builder.defineBoolean("show_exp_above_hotbar", true).withComments("If true, experience gain messages will appear above the hotbar.");
        showYourLevelUpChatMessages = builder.defineBoolean("show_your_level_up_chat_messages", true).withComments("If true, you will see a chat message when you level up.");
        showYourLevelUpToastMessages = builder.defineBoolean("show_your_level_up_toast_messages", true).withComments("If true, you will see a toast message when you level up.");
        showPlayersLevelUpChatMessages = builder.defineBoolean("show_players_level_up_chat_messages", true).withComments("If true, you will see a chat message when other players level up.");
        showRestrictionUnlockToastMessage = builder.defineBoolean("show_restriction_unlock_toast_message", true).withComments("If true, a toast message will appear when you unlock a job restriction.");
        showPowerupUnlockToastMessage = builder.defineBoolean("show_powerup_unlock_toast_message", true).withComments("If true, a toast message will appear when you unlock a powerup.");
        builder.pop();

        builder.push("sound");
        playLevelUpSound = builder.defineBoolean("play_level_up_sound", true).withComments("If true, a sound will play when you level up.");
        soundVolume = builder.defineDouble("sound_volume", 1.0, 0.0, 1.0).withComments("The volume of the level up sound.");
        builder.pop();

        builder.push("tooltips");
        showJobRestrictionTooltip = builder.defineBoolean("show_job_restriction_tooltip", true)
                .withComments("Adds a tooltip to items indicating which Job/Level is required to use them.");
        builder.pop();

        config = builder.build();
    }

    public static void init() {
    }
}