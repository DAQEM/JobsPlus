package me.daqem.jobsplus;

import com.mojang.blaze3d.platform.InputConstants;
import me.daqem.jobsplus.client.renderer.entity.ModFishingHookRenderer;
import me.daqem.jobsplus.common.container.backpack.BackpackGUI;
import me.daqem.jobsplus.data.ModDataGenerator;
import me.daqem.jobsplus.events.*;
import me.daqem.jobsplus.events.item.CurseBreakEvents;
import me.daqem.jobsplus.events.item.FarmersHoeEvents;
import me.daqem.jobsplus.events.jobs.*;
import me.daqem.jobsplus.handlers.ModPacketHandler;
import me.daqem.jobsplus.init.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class SideProxy {

    SideProxy() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.register(this);

        modEventBus.register(new EventRegisterCapabilities());
        modEventBus.register(new EventAttachCapabilities());
        modEventBus.register(new EventRegisterCommands());
        modEventBus.register(new EventServerChat());
        modEventBus.register(new EventNameFormat());
        modEventBus.register(new EventPlayerTick());
        modEventBus.register(new EventPlayerLoggedIn());
        modEventBus.register(new EventPlayerLoggedIn());

        modEventBus.register(new FarmersHoeEvents());
        modEventBus.register(new CurseBreakEvents());

        modEventBus.register(new AlchemistEvents());
        modEventBus.register(new BuilderEvents());
        modEventBus.register(new DiggerEvents());
        modEventBus.register(new FarmerEvents());
        modEventBus.register(new FishermanEvents());
        modEventBus.register(new EnchanterEvents());
        modEventBus.register(new HunterEvents());
        modEventBus.register(new LumberjackEvents());
        modEventBus.register(new MinerEvents());
        modEventBus.register(new SmithEvents());

        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModPotions.POTIONS.register(eventBus);
        ModEffects.EFFECTS.register(eventBus);
        ModContainers.CONTAINERS.register(eventBus);
        ModRecipes.RECIPES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);

        modEventBus.addListener(EventClone::onDeath);
        eventBus.addListener(ModDataGenerator::gatherData);

        ModPacketHandler.init();
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModPotions::addPotionRecipes);
    }

    public static class Server extends SideProxy {
        Server() {

        }

    }

    public static class Client extends SideProxy {

        public static final KeyMapping OPEN_GUI_KEYBIND = new KeyMapping("keys.jobsplus.open_gui", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "keys.jobsplus.category");


        Client() {
            IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
            MinecraftForge.EVENT_BUS.register(new EventKeyInput());
            eventBus.addListener(this::clientStuff);
        }

        private void clientStuff(final FMLClientSetupEvent event) {
            MenuScreens.register(ModContainers.BACKPACK_CONTAINER.get(), BackpackGUI::new);
            EntityRenderers.register(ModEntities.FISHING_BOBBER.get(), ModFishingHookRenderer::new);
            EntityRenderers.register(ModEntities.EXPERIENCE_BOTTLE.get(), ThrownItemRenderer::new);
            ModItemProperties.register();
            ClientRegistry.registerKeyBinding(OPEN_GUI_KEYBIND);
        }
    }
}
