package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.container.backpack.BackpackContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JobsPlus.MOD_ID);

    public static final RegistryObject<MenuType<BackpackContainer>> BACKPACK_CONTAINER = CONTAINERS.register("backpack_container", () -> IForgeMenuType.create(BackpackContainer::fromNetwork));
}
