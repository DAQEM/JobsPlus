package com.daqem.jobsplus.config;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.gui.statusbar.JobStatusBarAlignmentHorizontal;
import com.daqem.jobsplus.client.gui.statusbar.JobStatusBarAlignmentVertical;
import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

import java.util.List;

public class JobsPlusClientConfig {

    public static final IConfig config;

    public static final IConfigEntry<List<String>> jobStatusBarJobs;
    public static final IConfigEntry<Integer> jobStatusBarXOffset;
    public static final IConfigEntry<Integer> jobStatusBarYOffset;
    public static final IConfigEntry<JobStatusBarAlignmentHorizontal> jobStatusBarHorizontalAlignment;
    public static final IConfigEntry<JobStatusBarAlignmentVertical> jobStatusBarVerticalAlignment;
    public static final IConfigEntry<Long> jobStatusBarColor;
    public static final IConfigEntry<Long> jobStatusBarEmptyColor;
    public static final IConfigEntry<Integer> jobStatusBarWidth;
    public static final IConfigEntry<Boolean> jobStatusBarDetailed;

    public static final IConfigEntry<Boolean> jobStatusBarBackgroundEnabled;
    public static final IConfigEntry<Long> jobStatusBarBackgroundColor;
    public static final IConfigEntry<Long> jobStatusBarBackgroundBorderColor;

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
        IConfigBuilder builder = new ConfigBuilder(JobsPlus.MOD_ID, "jobsplus-client", ConfigExtension.YAML, ConfigType.CLIENT, YamlConfigExpectPlatform.getConfigDirectory().resolve("jobsplus"));

        builder.push("status_bar");
        jobStatusBarJobs = builder.defineStringList("jobs", List.of()).withComments("The job status bars to display. Use job IDs.");
        jobStatusBarXOffset = builder.defineInteger("x_offset", 3).withComments("The x offset of the job status bars.");
        jobStatusBarYOffset = builder.defineInteger("y_offset", 3).withComments("The y offset of the job status bars.");
        jobStatusBarHorizontalAlignment = builder.defineEnum("horizontal_alignment", JobStatusBarAlignmentHorizontal.RIGHT, JobStatusBarAlignmentHorizontal.class).withComments("The x alignment of the job status bars. Options: LEFT, CENTER, RIGHT");
        jobStatusBarVerticalAlignment = builder.defineEnum("vertical_alignment", JobStatusBarAlignmentVertical.BOTTOM, JobStatusBarAlignmentVertical.class).withComments("The y alignment of the job status bars. Options: TOP, CENTER, BOTTOM");
        jobStatusBarColor = builder.defineLong("color", 0xaaffffffL, 0x00000000, 0xFFFFFFFFL).withComments("The color of the job status bars in decimal ARGB format.");
        jobStatusBarEmptyColor = builder.defineLong("empty_color", 0xaaffffffL, 0x00000000, 0xFFFFFFFFL).withComments("The empty color of the job status bars in decimal ARGB format.");
        jobStatusBarWidth = builder.defineInteger("width", 120).withComments("The width of the job status bars.");
        jobStatusBarDetailed = builder.defineBoolean("detailed", true).withComments("If true, the job status bars will show detailed information.");
        builder.push("background");
        jobStatusBarBackgroundEnabled = builder.defineBoolean("enabled", true).withComments("If true, the job status bar background will be rendered.");
        jobStatusBarBackgroundColor = builder.defineLong("color", 0xaaaaaaaaL, 0x00000000, 0xFFFFFFFFL).withComments("The color of the job status bar background in decimal ARGB format.");
        jobStatusBarBackgroundBorderColor = builder.defineLong("border_color", 0xaaffffffL, 0x00000000, 0xFFFFFFFFL).withComments("The border color of the job status bar background in decimal ARGB format.");
        builder.pop();
        builder.pop();

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
        // Initialize config
    }
}
