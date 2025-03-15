package com.daqem.jobsplus.integration.arc.holder.holders.powerup;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.holder.AbstractActionHolder;
import com.daqem.arc.api.action.holder.serializer.IActionHolderSerializer;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.daqem.jobsplus.player.JobsPlayer;
import com.daqem.jobsplus.player.job.Job;
import com.daqem.jobsplus.player.job.powerup.Powerup;
import com.daqem.jobsplus.player.job.powerup.PowerupState;
import com.daqem.jobsplus.player.job.powerup.PowerupType;
import com.google.gson.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;

public class PowerupInstance extends AbstractActionHolder {

    private final ResourceLocation jobLocation;
    private final @Nullable ResourceLocation parentLocation;
    private final ItemStack icon;
    private final int price;
    private final int requiredLevel;
    private final PowerupType type;

    public PowerupInstance(ResourceLocation location, ResourceLocation jobLocation, @Nullable ResourceLocation parentLocation, ItemStack icon, int price, int requiredLevel, PowerupType type) {
        super(location);
        this.jobLocation = jobLocation;
        this.parentLocation = parentLocation;
        this.icon = icon;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.type = type;
    }

    public MutableComponent getName() {
        return JobsPlus.translatable("powerup." + location.getNamespace() + "." + location.getPath().replace('/', '.') + ".name");
    }

    public MutableComponent getDescription() {
        return JobsPlus.translatable("powerup." + location.getNamespace() + "." + location.getPath().replace('/', '.') + ".description");
    }

    public ResourceLocation getJobLocation() {
        return jobLocation;
    }

    public @Nullable ResourceLocation getParentLocation() {
        return parentLocation;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public int getPrice() {
        return price;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public PowerupType getPowerupType() {
        return this.type;
    }

    @Override
    public IActionHolderType<?> getType() {
        return JobsPlusActionHolderType.POWERUP_INSTANCE;
    }

    @Nullable
    public static PowerupInstance of(ResourceLocation location) {
        return PowerupManager.getInstance().getAllPowerups().get(location);
    }

    @Override
    public boolean passedHolderCondition(ActionData actionData) {
        ArcPlayer arcPlayer = actionData.getPlayer();
        if (arcPlayer instanceof JobsPlayer jobsPlayer) {
            Job job = jobsPlayer.jobsplus$getJobs().stream()
                    .filter(job1 -> job1 != null && job1.getJobInstance() != null && job1.getJobInstance().getLocation().equals(this.getJobLocation())).findFirst().orElse(null);
            if (job != null) {
                Powerup powerup = job.getPowerupManager().getAllPowerups().stream().filter(powerup1 -> powerup1.getPowerupInstance().getLocation().equals(this.getLocation())).findFirst().orElse(null);
                if (powerup != null) {
                    return powerup.getState() == PowerupState.ACTIVE;
                }
            }
        }
        return false;
    }

    public PowerupInstance getParent() {
        return parentLocation == null ? null : PowerupManager.getInstance().getAllPowerups().get(parentLocation);
    }

    public List<PowerupInstance> getChildren() {
        return PowerupManager.getInstance().getAllPowerups().values().stream()
                .filter(powerupInstance -> powerupInstance.getParentLocation() != null && powerupInstance.getParentLocation().equals(this.getLocation()))
                .toList();
    }

    public static class Serializer implements JsonDeserializer<PowerupInstance>, IActionHolderSerializer<PowerupInstance> {

        @Override
        public PowerupInstance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return fromJson(jsonObject, getResourceLocation(jsonObject, "location"));
        }

        @Override
        public PowerupInstance fromJson(JsonObject jsonObject, ResourceLocation resourceLocation) {
            String parentLocation = GsonHelper.getAsString(jsonObject, "parent", null);
            return new PowerupInstance(
                    resourceLocation,
                    getResourceLocation(jsonObject, "job"),
                    parentLocation == null ? null : ResourceLocation.parse(parentLocation),
                    getItemStack(GsonHelper.getAsJsonObject(jsonObject, "icon")),
                    GsonHelper.getAsInt(jsonObject, "price"),
                    GsonHelper.getAsInt(jsonObject, "required_level"),
                    PowerupType.valueOf(GsonHelper.getAsString(jsonObject, "type", "basic").toUpperCase())
            );
        }

        @Override
        public PowerupInstance fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ResourceLocation resourceLocation) {
            return new PowerupInstance(
                    friendlyByteBuf.readResourceLocation(),
                    friendlyByteBuf.readResourceLocation(),
                    friendlyByteBuf.readBoolean() ? friendlyByteBuf.readResourceLocation() : null,
                    ItemStack.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readEnum(PowerupType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PowerupInstance powerupInstance) {
            friendlyByteBuf.writeResourceLocation(powerupInstance.getLocation());
            friendlyByteBuf.writeResourceLocation(powerupInstance.getJobLocation());
            friendlyByteBuf.writeBoolean(powerupInstance.getParentLocation() != null);
            if (powerupInstance.getParentLocation() != null) {
                friendlyByteBuf.writeResourceLocation(powerupInstance.getParentLocation());
            }
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, powerupInstance.getIcon());
            friendlyByteBuf.writeInt(powerupInstance.getPrice());
            friendlyByteBuf.writeInt(powerupInstance.getRequiredLevel());
            friendlyByteBuf.writeEnum(powerupInstance.getPowerupType());
            IActionHolderSerializer.super.toNetwork(friendlyByteBuf, powerupInstance);
        }
    }
}
