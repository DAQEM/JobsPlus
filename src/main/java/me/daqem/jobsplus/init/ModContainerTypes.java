package me.daqem.jobsplus.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

//    public static ContainerType<TotemBagContainer> totem_bag;
//
//    public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event) {
//        totem_bag = register("totem_bag", new ContainerType<>(TotemBagContainer::new));
//    }
//    @OnlyIn(Dist.CLIENT)
//    public static void registerScreens(FMLClientSetupEvent event) {
//        ScreenManager.registerFactory(totem_bag, TotemBagContainerScreen::new);
//    }
//
//    private static <T extends Container> ContainerType<T> register(String name, ContainerType<T> type) {
//        ResourceLocation id = Totems.getId(name);
//        type.setRegistryName(id);
//        ForgeRegistries.CONTAINERS.register(type);
//        return type;
//    }
}
