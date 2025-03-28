package com.daqem.jobsplus.integration.arc.holder.holders.powerup;

import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.JobsPlusExpectPlatform;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            try {
                JsonObject jsonElement = GsonHelper.parse(entry.getValue().openAsReader());
                map.put(location, jsonElement);
            }
            catch (Exception runtimeException) {
                JobsPlus.LOGGER.error("Parsing error loading powerup {}", location, runtimeException);
            }
        }
        List<IActionHolder> powerups = new ArrayList<>();

        if (!JobsPlus.isDebugEnvironment()) {
            map.entrySet().removeIf(entry -> entry.getKey().getNamespace().equals("debug"));
        }

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
