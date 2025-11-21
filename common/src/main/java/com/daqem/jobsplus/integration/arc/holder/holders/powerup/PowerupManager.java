package com.daqem.jobsplus.integration.arc.holder.holders.powerup;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.JobsPlusExpectPlatform;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

public class PowerupManager extends SimplePreparableReloadListener<List<IActionHolder>> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(PowerupInstance.class, new PowerupInstance.Serializer())
            .create();

    private static PowerupManager instance;

    public PowerupManager() {
        instance = this;
    }

    @Override
    protected @NotNull List<IActionHolder> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, Resource> resourceMap = resourceManager.listResources("jobsplus/powerups", (resourceLocation) ->
                        resourceLocation.getPath().endsWith(".json")).entrySet().stream()
                .collect(Collectors.toMap(entry ->
                                ResourceLocation.fromNamespaceAndPath(
                                        entry.getKey().getNamespace(),
                                        entry.getKey().getPath()
                                                .substring(0, entry.getKey().getPath().length() - ".json".length())
                                                .substring("jobsplus/powerups/".length())),
                        Map.Entry::getValue));

        Map<ResourceLocation, JsonObject> map = new HashMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceMap.entrySet()) {
            ResourceLocation location = entry.getKey();
            try (BufferedReader reader = entry.getValue().openAsReader()) {
                JsonObject jsonElement = GsonHelper.parse(reader);
                map.put(location, jsonElement);
            }
            catch (Exception runtimeException) {
                JobsPlus.LOGGER.error("Parsing error loading powerup {}", location, runtimeException);
            }
        }

        try {
            java.nio.file.Path configDir = dev.architectury.platform.Platform.getConfigFolder().resolve("jobsplus/powerups");
            if (!java.nio.file.Files.exists(configDir)) {
                java.nio.file.Files.createDirectories(configDir);
            }
            try (java.util.stream.Stream<java.nio.file.Path> paths = java.nio.file.Files.walk(configDir)) {
                paths.filter(path -> path.toString().endsWith(".json"))
                        .forEach(path -> {
                            try (BufferedReader reader = java.nio.file.Files.newBufferedReader(path)) {
                                JsonObject jsonElement = GsonHelper.parse(reader);
                                String relativePath = configDir.relativize(path).toString();
                                relativePath = relativePath.replace("\\", "/");
                                relativePath = relativePath.substring(0, relativePath.length() - ".json".length());
                                ResourceLocation location = ResourceLocation.fromNamespaceAndPath(JobsPlus.MOD_ID, relativePath);
                                map.put(location, jsonElement);
                            } catch (Exception e) {
                                JobsPlus.LOGGER.error("Parsing error loading powerup from config {}", path, e);
                            }
                        });
            }
        } catch (Exception e) {
            JobsPlus.LOGGER.error("Error loading powerups from config", e);
        }
        List<IActionHolder> powerups = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonObject> entry : map.entrySet()) {
            ResourceLocation location = entry.getKey();
            JsonObject jsonObject = entry.getValue();
            jsonObject.addProperty("location", location.toString());
            try {
                PowerupInstance powerup = GSON.fromJson(entry.getValue(), PowerupInstance.class);
                powerups.add(powerup);
            }
            catch (JsonParseException | IllegalArgumentException runtimeException) {
                JobsPlus.LOGGER.error("Parsing error loading powerup {}", location, runtimeException);
            }
        }

        return powerups;
    }

    @Override
    protected void apply(List<IActionHolder> powerups, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ActionHolderManager actionHolderManager = ActionHolderManager.getInstance();
        actionHolderManager.clearAllActionHoldersForType(JobsPlusActionHolderType.POWERUP_INSTANCE);
        actionHolderManager.registerActionHolders(powerups);
        JobsPlus.LOGGER.info("Loaded {} powerups", powerups.size());
    }


    public static PowerupManager getInstance() {
        return instance != null ? instance : JobsPlusExpectPlatform.getPowerupManager();
    }

    public ImmutableMap<ResourceLocation, PowerupInstance> getRootPowerups() {
        return getAllPowerups().entrySet().stream()
                .filter(entry -> entry.getValue().getParentLocation() == null)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<ResourceLocation, PowerupInstance> getAllPowerups() {
        return ActionHolderManager.getInstance().getActionHolders().stream()
                .filter(actionHolder -> actionHolder instanceof PowerupInstance)
                .map(actionHolder -> (PowerupInstance) actionHolder)
                .collect(ImmutableMap.toImmutableMap(PowerupInstance::getLocation, powerupInstance -> powerupInstance));
    }
}
