package com.daqem.jobsplus.integration.arc.holder.holders.job;

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

public class JobManager extends SimplePreparableReloadListener<List<IActionHolder>> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(JobInstance.class, new JobInstance.Serializer())
            .create();

    private static JobManager instance;

    public JobManager() {
        instance = this;
    }

    @Override
    protected @NotNull List<IActionHolder> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, Resource> resourceMap = resourceManager.listResources("jobsplus/jobs", (resourceLocation) ->
                        resourceLocation.getPath().endsWith(".json")).entrySet().stream()
                .collect(Collectors.toMap(entry ->
                                ResourceLocation.fromNamespaceAndPath(
                                        entry.getKey().getNamespace(),
                                        entry.getKey().getPath()
                                                .substring(0, entry.getKey().getPath().length() - ".json".length())
                                                .substring("jobsplus/jobs/".length())),
                        Map.Entry::getValue));

        Map<ResourceLocation, JsonObject> map = new HashMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceMap.entrySet()) {
            ResourceLocation location = entry.getKey();
            try (BufferedReader reader = entry.getValue().openAsReader()) {
                JsonObject jsonElement = GsonHelper.parse(reader);
                map.put(location, jsonElement);
            } catch (Exception runtimeException) {
                JobsPlus.LOGGER.error("Parsing error loading job {}", location, runtimeException);
            }
        }

        try {
            java.nio.file.Path configDir = dev.architectury.platform.Platform.getConfigFolder().resolve("jobsplus/jobs");
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
                                JobsPlus.LOGGER.error("Parsing error loading job from config {}", path, e);
                            }
                        });
            }
        } catch (Exception e) {
            JobsPlus.LOGGER.error("Error loading jobs from config", e);
        }
        List<IActionHolder> jobs = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonObject> entry : map.entrySet()) {
            ResourceLocation location = entry.getKey();
            JsonObject jsonObject = entry.getValue();
            jsonObject.addProperty("location", location.toString());
            try {
                JobInstance job = GSON.fromJson(entry.getValue(), JobInstance.class);
                jobs.add(job);
            } catch (JsonParseException | IllegalArgumentException runtimeException) {
                JobsPlus.LOGGER.error("Parsing error loading job {}", location, runtimeException);
            }
        }

        return jobs;
    }

    @Override
    protected void apply(List<IActionHolder> jobs, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ActionHolderManager actionHolderManager = ActionHolderManager.getInstance();
        actionHolderManager.clearAllActionHoldersForType(JobsPlusActionHolderType.JOB_INSTANCE);
        actionHolderManager.registerActionHolders(jobs);
        JobsPlus.LOGGER.info("Loaded {} jobs", jobs.size());
    }

    public static JobManager getInstance() {
        return instance != null ? instance : JobsPlusExpectPlatform.getJobManager();
    }

    public Map<ResourceLocation, JobInstance> getJobs() {
        return ActionHolderManager.getInstance().getActionHolders().stream()
                .filter(actionHolder -> actionHolder instanceof JobInstance)
                .map(actionHolder -> (JobInstance) actionHolder)
                .collect(Collectors.toMap(JobInstance::getLocation, jobInstance -> jobInstance));
    }
}
