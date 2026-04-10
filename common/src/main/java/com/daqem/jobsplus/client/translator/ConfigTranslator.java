package com.daqem.jobsplus.client.translator;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.knot.api.platform.Platform;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ConfigTranslator {
    private static final Gson GSON = new Gson();

    public static void load(List<String> languages, Map<String, String> translations) {
        Path configDir = Platform.INFO.getConfigFolder().resolve(JobsPlus.MOD_ID).resolve("lang");
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                JobsPlus.API.LOGGER.error("Failed to create jobsplus lang config directory", e);
                return;
            }
        }

        for (String lang : languages) {
            Path langFile = configDir.resolve(lang + ".json");
            if (Files.exists(langFile)) {
                try (Reader reader = Files.newBufferedReader(langFile)) {
                    JsonObject json = GSON.fromJson(reader, JsonObject.class);
                    if (json != null) {
                        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                            if (entry.getValue().isJsonPrimitive()) {
                                translations.put(entry.getKey(), entry.getValue().getAsString());
                            }
                        }
                    }
                } catch (Exception e) {
                    JobsPlus.API.LOGGER.error("Failed to load translation file: {}", langFile, e);
                }
            }
        }
    }
}