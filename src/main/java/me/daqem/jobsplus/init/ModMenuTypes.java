package me.daqem.jobsplus.init;

import me.daqem.jobsplus.JobsPlus;
import me.daqem.jobsplus.common.inventory.BackpackMenu;
import me.daqem.jobsplus.common.inventory.ConstructionMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, JobsPlus.MOD_ID);
    public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK = MENU_TYPES.register("backpack_container", () -> IForgeMenuType.create(BackpackMenu::fromNetwork));
    public static final RegistryObject<MenuType<ConstructionMenu>> CONSTRUCTION = MENU_TYPES.register("construction", () -> IForgeMenuType.create(ConstructionMenu::new));


}
