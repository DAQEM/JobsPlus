package com.daqem.jobsplus.data.serializer;

import com.google.gson.JsonDeserializer;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface JobsPlusSerializer<T> extends JsonDeserializer<T> {

//    T fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf);

//    void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, T type);

//    default ItemStack getItemStack(JsonObject jsonObject) {
//        Item item = GsonHelper.getAsItem(jsonObject, "item").value();
//        int count = GsonHelper.getAsInt(jsonObject, "count", 1);
//        ItemStack iconStack = new ItemStack(item);
//
//        iconStack.setCount(count);
//
//        if (jsonObject.has("tag")) {
//            String tagName = GsonHelper.getAsString(jsonObject, "tag");
//
//            try {
//                iconStack.setTag(TagParser.parseTag(tagName));
//            } catch (CommandSyntaxException e) {
//                String errorMessage = String.format("Error parsing tag for PowerupInstance icon %s: %s", tagName, e.getMessage());
//                JobsPlus.LOGGER.error(errorMessage);
//            }
//        }
//
//        return iconStack;
//    }
}
