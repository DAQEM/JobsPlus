package com.daqem.jobsplus.integration.arc.holder.holders.job;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.daqem.knot.api.platform.Platform;
import org.jetbrains.annotations.NotNull;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.resources.Identifier;
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
        Map<Identifier, Resource> resourceMap = resourceManager.listResources("jobsplus/jobs", (resourceLocation) ->
                        resourceLocation.getPath().endsWith(".json")).entrySet().stream()
                .collect(Collectors.toMap(entry ->
                                Identifier.fromNamespaceAndPath(
                                        entry.getKey().getNamespace(),
                                        entry.getKey().getPath()
                                                .substring(0, entry.getKey().getPath().length() - ".json".length())
                                                .substring("jobsplus/jobs/".length())),
                        Map.Entry::getValue));

        Map<Identifier, JsonObject> map = new HashMap<>();
        for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
            Identifier location = entry.getKey();
            try (BufferedReader reader = entry.getValue().openAsReader()) {
                JsonObject jsonElement = GsonHelper.parse(reader);
                map.put(location, jsonElement);
            } catch (Exception runtimeException) {
                JobsPlus.API.LOGGER.error("Parsing error loading job {}", location, runtimeException);
            }
        }

        try {
            Path configDir = Platform.INFO.getConfigFolder().resolve(JobsPlus.MOD_ID).resolve("jobs");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            try (Stream<Path> paths = Files.walk(configDir)) {
                paths.filter(path -> path.toString().endsWith(".json"))
                        .forEach(path -> {
                            try (BufferedReader reader = Files.newBufferedReader(path)) {
                                JsonObject jsonElement = GsonHelper.parse(reader);
                                String relativePath = configDir.relativize(path).toString();
                                relativePath = relativePath.replace("\\", "/");
                                relativePath = relativePath.substring(0, relativePath.length() - ".json".length());
                                String namespace;
                                String resourcePath;
                                int firstSlashIndex = relativePath.indexOf('/');
                                if (firstSlashIndex > 0) {
                                    namespace = relativePath.substring(0, firstSlashIndex);
                                    resourcePath = relativePath.substring(firstSlashIndex + 1);
                                } else {
                                    namespace = JobsPlus.MOD_ID;
                                    resourcePath = relativePath;
                                }
                                Identifier location = Identifier.fromNamespaceAndPath(namespace, resourcePath);
                                map.put(location, jsonElement);
                            } catch (Exception e) {
                                JobsPlus.API.LOGGER.error("Parsing error loading job from config {}", path, e);
                            }
                        });
            }
        } catch (Exception e) {
            JobsPlus.API.LOGGER.error("Error loading jobs from config", e);
        }
        List<IActionHolder> jobs = new ArrayList<>();
        List<String> excludedJobs = JobsPlusConfig.excludedJobs.get();

        for (Map.Entry<Identifier, JsonObject> entry : map.entrySet()) {
            Identifier location = entry.getKey();
            if (excludedJobs.contains(location.toString())) {
                continue;
            }
            JsonObject jsonObject = entry.getValue();
            jsonObject.addProperty("location", location.toString());
            try {
                JobInstance job = GSON.fromJson(entry.getValue(), JobInstance.class);
                jobs.add(job);
            } catch (JsonParseException | IllegalArgumentException runtimeException) {
                JobsPlus.API.LOGGER.error("Parsing error loading job {}", location, runtimeException);
            }
        }

        return jobs;
    }

    @Override
    protected void apply(List<IActionHolder> jobs, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ActionHolderManager actionHolderManager = ActionHolderManager.getInstance();
        actionHolderManager.clearAllActionHoldersForType(JobsPlusActionHolderType.JOB_INSTANCE);
        actionHolderManager.registerActionHolders(jobs);
        JobsPlus.API.LOGGER.info("Loaded {} jobs", jobs.size());
    }

    public static JobManager getInstance() {
        return instance != null ? instance : new JobManager();
    }

    public Map<Identifier, JobInstance> getJobs() {
        return ActionHolderManager.getInstance().getActionHolders().stream()
                .filter(actionHolder -> actionHolder instanceof JobInstance)
                .map(actionHolder -> (JobInstance) actionHolder)
                .collect(Collectors.toMap(JobInstance::getIdentifier, jobInstance -> jobInstance));
    }
}
