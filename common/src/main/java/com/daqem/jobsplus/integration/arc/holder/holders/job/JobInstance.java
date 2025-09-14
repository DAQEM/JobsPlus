package com.daqem.jobsplus.integration.arc.holder.holders.job;

import com.daqem.arc.api.action.holder.AbstractActionHolder;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.serializer.IActionHolderSerializer;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.daqem.itemrestrictions.data.ItemRestriction;
import com.daqem.itemrestrictions.data.ItemRestrictionManager;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.condition.conditions.job.IJobCondition;
import com.daqem.jobsplus.integration.arc.holder.holders.powerup.PowerupInstance;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.google.gson.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobInstance extends AbstractActionHolder {

    private final int price;
    private final String color;
    private final ItemStack iconItem;
    private final boolean isDefault;

    public JobInstance(ResourceLocation location, int price, String color, ItemStack iconItem, boolean isDefault) {
        super(location);
        this.price = price;
        this.color = color;
        this.iconItem = iconItem;
        this.isDefault = isDefault;
    }

    public int getPrice() {
        return price;
    }

    public MutableComponent getName() {
        return JobsPlus.translatable("job." + location.getNamespace() + "." + location.getPath() + ".name");
    }

    public MutableComponent getDescription() {
        return JobsPlus.translatable("job." + location.getNamespace() + "." + location.getPath() + ".description");
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
        return iconItem;
    }

    public boolean isDefault() {
        return isDefault;
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
                                itemRestriction.getLocation(),
                                itemRestriction.getIcon().copy(),
                                new ArrayList<>(itemRestriction.getRestrictionTypes()),
                                new ArrayList<>(itemRestriction.getConditions())
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
    public static JobInstance of(ResourceLocation location) {
        return JobManager.getInstance().getJobs().get(location);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JobInstance jobInstance && jobInstance.location.equals(location);
    }

    public static class Serializer implements JsonDeserializer<JobInstance>, IActionHolderSerializer<JobInstance> {

        @Override
        public JobInstance deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = element.getAsJsonObject();
            return fromJson(jsonObject, getResourceLocation(jsonObject, "location"));
        }

        @Override
        public JobInstance fromJson(JsonObject jsonObject, ResourceLocation resourceLocation) {
            return new JobInstance(
                    resourceLocation,
                    GsonHelper.getAsInt(jsonObject, "price"),
                    GsonHelper.getAsString(jsonObject, "color"),
                    getItemStack(GsonHelper.getAsJsonObject(jsonObject, "icon")),
                    GsonHelper.getAsBoolean(jsonObject, "is_default", false));
        }

        public JobInstance fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ResourceLocation resourceLocation) {
            return new JobInstance(
                    friendlyByteBuf.readResourceLocation(),
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readUtf(),
                    ItemStack.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readBoolean()
            );
        }

        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, JobInstance jobInstance) {
            friendlyByteBuf.writeResourceLocation(jobInstance.location);
            friendlyByteBuf.writeVarInt(jobInstance.price);
            friendlyByteBuf.writeUtf(jobInstance.color);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, jobInstance.iconItem);
            friendlyByteBuf.writeBoolean(jobInstance.isDefault);
            IActionHolderSerializer.super.toNetwork(friendlyByteBuf, jobInstance);
        }
    }
}
