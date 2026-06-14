package com.daqem.jobsplus.integration.arc.holder.holders.powerup;

import com.daqem.arc.api.action.holder.AbstractActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderSerializer;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.integration.arc.holder.type.JobsPlusActionHolderType;
import com.daqem.jobsplus.player.job.powerup.PowerupType;
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
import java.util.List;
import java.util.Locale;

public class PowerupInstance extends AbstractActionHolder {

    private final Identifier jobLocation;
    private final @Nullable Identifier parentLocation;
    private final ItemStackTemplate iconTemplate;
    private ItemStack cachedIcon;
    private final int price;
    private final int requiredLevel;
    private final PowerupType type;

    public PowerupInstance(Identifier location, Identifier jobLocation, @Nullable Identifier parentLocation, ItemStackTemplate iconTemplate, int price, int requiredLevel, PowerupType type) {
        super(location);
        this.jobLocation = jobLocation;
        this.parentLocation = parentLocation;
        this.iconTemplate = iconTemplate;
        this.cachedIcon = null;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.type = type;
    }

    public MutableComponent getName() {
        return JobsPlus.API.translatable("powerup." + location.getNamespace() + "." + location.getPath().replace('/', '.') + ".name");
    }

    public MutableComponent getDescription() {
        return JobsPlus.API.translatable("powerup." + location.getNamespace() + "." + location.getPath().replace('/', '.') + ".description");
    }

    public Identifier getJobLocation() {
        return jobLocation;
    }

    public @Nullable Identifier getParentLocation() {
        return parentLocation;
    }

    public ItemStack getIcon() {
        if (cachedIcon != null) {
            return cachedIcon;
        }
        cachedIcon = iconTemplate.create();
        return cachedIcon;
    }

    public ItemStackTemplate getIconTemplate() {
        return iconTemplate;
    }

    public int getPrice() {
        return Mth.floor(price * JobsPlusConfig.powerupPriceMultiplier.get());
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
    public static PowerupInstance of(Identifier location) {
        return PowerupManager.getInstance().getAllPowerups().get(location);
    }

    public PowerupInstance getParent() {
        return parentLocation == null ? null : PowerupManager.getInstance().getAllPowerups().get(parentLocation);
    }

    public List<PowerupInstance> getChildren() {
        return PowerupManager.getInstance().getAllPowerups().values().stream()
                .filter(powerupInstance -> powerupInstance.getParentLocation() != null && powerupInstance.getParentLocation().equals(this.getIdentifier()))
                .toList();
    }

    public static class Serializer implements JsonDeserializer<PowerupInstance>, IActionHolderSerializer<PowerupInstance> {

        @Override
        public PowerupInstance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return fromJson(jsonObject, getIdentifier(jsonObject, "location"));
        }

        @Override
        public PowerupInstance fromJson(JsonObject jsonObject, Identifier resourceLocation) {
            String parentLocation = GsonHelper.getAsString(jsonObject, "parent", null);
            return new PowerupInstance(
                    resourceLocation,
                    getIdentifier(jsonObject, "job"),
                    parentLocation == null ? null : Identifier.parse(parentLocation),
                    getItemStackTemplate(jsonObject, "icon"),
                    GsonHelper.getAsInt(jsonObject, "price"),
                    GsonHelper.getAsInt(jsonObject, "required_level"),
                    PowerupType.valueOf(GsonHelper.getAsString(jsonObject, "type", "basic").toUpperCase(Locale.ROOT))
            );
        }

        @Override
        public PowerupInstance fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, Identifier resourceLocation) {
            return new PowerupInstance(
                    friendlyByteBuf.readIdentifier(),
                    friendlyByteBuf.readIdentifier(),
                    friendlyByteBuf.readBoolean() ? friendlyByteBuf.readIdentifier() : null,
                    ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readInt(),
                    friendlyByteBuf.readEnum(PowerupType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PowerupInstance powerupInstance) {
            friendlyByteBuf.writeIdentifier(powerupInstance.getIdentifier());
            friendlyByteBuf.writeIdentifier(powerupInstance.getJobLocation());
            friendlyByteBuf.writeBoolean(powerupInstance.getParentLocation() != null);
            if (powerupInstance.getParentLocation() != null) {
                friendlyByteBuf.writeIdentifier(powerupInstance.getParentLocation());
            }
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, powerupInstance.getIconTemplate());
            friendlyByteBuf.writeInt(powerupInstance.getPrice());
            friendlyByteBuf.writeInt(powerupInstance.getRequiredLevel());
            friendlyByteBuf.writeEnum(powerupInstance.getPowerupType());
            IActionHolderSerializer.super.toNetwork(friendlyByteBuf, powerupInstance);
        }
    }
}
