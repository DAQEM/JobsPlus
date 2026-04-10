package com.daqem.jobsplus.integration.arc.holder.holders.job;

import com.daqem.arc.api.action.holder.AbstractActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderSerializer;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.itemrestrictions.data.ItemRestrictionManager;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.IJobCondition;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.google.gson.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobInstance extends AbstractActionHolder {

    private final double price;
    private final String color;
    private final ItemStackTemplate iconTemplate;
    private ItemStack cachedIcon;

    public JobInstance(Identifier location, double price, String color, ItemStackTemplate iconTemplate) {
        super(location);
        this.price = price;
        this.color = color;
        this.iconTemplate = iconTemplate;
        this.cachedIcon = null;
    }

    public double getPrice() {
        return Mth.floor(price * JobsPlusConfig.jobPriceMultiplier.get());
    }

    public MutableComponent getName() {
        return JobsPlus.API.translatable("job." + location.getNamespace() + "." + location.getPath() + ".name");
    }

    public MutableComponent getDescription() {
        return JobsPlus.API.translatable("job." + location.getNamespace() + "." + location.getPath() + ".description");
    }

    public List<PowerupInstance> getPowerups() {
        return ActionHolderManager.getInstance().getActionHolders().stream()
                .filter(actionHolder -> actionHolder instanceof PowerupInstance powerupInstance && powerupInstance.getJobLocation().equals(location))
                .map(actionHolder -> (PowerupInstance) actionHolder)
                .collect(Collectors.toList());
    }

    public int getColorDecimal() {
        return Integer.parseInt(color.replace("#", ""), 16);
    }

    public ItemStack getIconItem() {
        if (cachedIcon != null) {
            return cachedIcon;
        }
        cachedIcon = iconTemplate.create();
        return cachedIcon;
    }

    public ItemStackTemplate getIconTemplate() {
        return iconTemplate;
    }

    /**
     * @return a map of item restrictions and their corresponding required levels for this job instance
     */
    public Map<ItemRestriction, Integer> getItemRestrictions() {
        return ItemRestrictionManager.getInstance().getItemRestrictions().stream()
                .filter(itemRestriction -> itemRestriction.getConditions().stream()
                        .anyMatch(condition -> condition instanceof IJobCondition jobCondition && jobCondition.getJobLocation().equals(location)))
                .collect(Collectors.toMap(
                        itemRestriction -> new ItemRestriction(
                                itemRestriction.getIdentifier(),
                                ItemStackTemplate.fromNonEmptyStack(itemRestriction.getIcon()),
                                new ArrayList<>(itemRestriction.getRestrictionTypes()),
                                new ArrayList<>(itemRestriction.getConditions()),
                                itemRestriction.isClientSide()
                        ),
                        itemRestriction -> itemRestriction.getConditions().stream()
                                .filter(condition -> condition instanceof IJobCondition)
                                .map(iCondition -> ((IJobCondition) iCondition).getRequiredLevel())
                                .max(Integer::compareTo)
                                .orElse(0)
                ));
    }

    @Override
    public IActionHolderType<?> getType() {
        return JobsPlusActionHolderType.JOB_INSTANCE;
    }

    @Nullable
    public static JobInstance of(Identifier location) {
        return JobManager.getInstance().getJobs().get(location);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JobInstance jobInstance && jobInstance.location.equals(location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }


    public static class Serializer implements JsonDeserializer<JobInstance>, IActionHolderSerializer<JobInstance> {

        @Override
        public JobInstance deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = element.getAsJsonObject();
            return fromJson(jsonObject, getIdentifier(jsonObject, "location"));
        }

        @Override
        public JobInstance fromJson(JsonObject jsonObject, Identifier resourceLocation) {
            return new JobInstance(
                    resourceLocation,
                    GsonHelper.getAsDouble(jsonObject, "price"),
                    GsonHelper.getAsString(jsonObject, "color"),
                    getItemStackTemplate(jsonObject, "icon"));
        }

        public JobInstance fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, Identifier resourceLocation) {
            return new JobInstance(
                    friendlyByteBuf.readIdentifier(),
                    friendlyByteBuf.readDouble(),
                    friendlyByteBuf.readUtf(),
                    ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf)
            );
        }

        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, JobInstance jobInstance) {
            friendlyByteBuf.writeIdentifier(jobInstance.location);
            friendlyByteBuf.writeDouble(jobInstance.price);
            friendlyByteBuf.writeUtf(jobInstance.color);
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, jobInstance.iconTemplate);
            IActionHolderSerializer.super.toNetwork(friendlyByteBuf, jobInstance);
        }
    }
}
