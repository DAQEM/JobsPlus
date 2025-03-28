package com.daqem.jobsplus.integration.arc.holder.holders.job;

import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            try {
                JsonObject jsonElement = GsonHelper.parse(entry.getValue().openAsReader());
                map.put(location, jsonElement);
            } catch (Exception runtimeException) {
                JobsPlus.LOGGER.error("Parsing error loading job {}", location, runtimeException);
            }
        }
        List<IActionHolder> jobs = new ArrayList<>();

        if (!JobsPlus.isDebugEnvironment()) {
            map.entrySet().removeIf(entry -> entry.getKey().getNamespace().equals("debug"));
        }

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
